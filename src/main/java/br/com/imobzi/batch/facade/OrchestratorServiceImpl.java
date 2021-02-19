package br.com.imobzi.batch.facade;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import br.com.imobzi.batch.domain.CoverPhoto;
import br.com.imobzi.batch.domain.Excel;
import br.com.imobzi.batch.domain.Immobile;
import br.com.imobzi.batch.domain.ImmobileContact;
import br.com.imobzi.batch.domain.ImmobileContacts;
import br.com.imobzi.batch.domain.ImmobileCoverPhoto;
import br.com.imobzi.batch.domain.ImmobilePhotoResponse;
import br.com.imobzi.batch.domain.ImmobileRequest;
import br.com.imobzi.batch.domain.ImmobileResponse;
import br.com.imobzi.batch.domain.Owners;
import br.com.imobzi.batch.domain.Photos;
import br.com.imobzi.batch.domain.Property;
import br.com.imobzi.batch.domain.PropertyCoverPhoto;
import br.com.imobzi.batch.handler.BadRequestException;
import br.com.imobzi.batch.handler.ForbiddenException;
import br.com.imobzi.batch.handler.GenericError;
import br.com.imobzi.batch.service.ExcelService;
import br.com.imobzi.batch.service.ImobziService;
import br.com.imobzi.batch.utils.ImmobileConverter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrchestratorServiceImpl implements OrchestratorService {

	@Autowired
	private ImobziService imobziService;
	@Autowired
	private ExcelService excelService;
	private int callAmount;
	private long start;
	private long finish;

	@Override
	public List<ImmobileResponse> orchestrator(final File inputStream, ImmobileRequest immobileRequest)
			throws Exception {
		
		log.info("Metodo orchestrator chamado.");
		
		List<Excel> excelList = this.excelService.readList(inputStream, immobileRequest);
		
		List<Property> property = excelListToImmobile(excelList);

		List<String> addresses = imobziService.getAllCreatedAddresses();
		// MOCK
//		List<String> addresses = new ArrayList<>();

		List<ImmobileResponse> immobileResponses = new ArrayList<ImmobileResponse>();
		callAmount = 0;
		start = System.currentTimeMillis();
		property.forEach(immobile -> {
			String addressToTest = immobile.getProperty().getAddress() + " "
					+ immobile.getProperty().getAddress_complement();

			ImmobileContacts contacts = this.imobziService.getOwnerByEmail(immobile.getProperty().getAddress(),
					immobile.getProperty().getOwnerEmail());

			if (contacts.getContacts().size() > 0) {
				ImmobileContact contact = contacts.getContacts().get(0);
				List<Owners> owners = new ArrayList<>();
				owners.add(Owners.builder().code(contact.getCode()).id(Long.parseLong(contact.getContact_id()))
						.name(contact.getName()).percentage("100").rate("0").type(contact.getContact_type()).build());

				Immobile propertyWithOwner = immobile.getProperty();
				propertyWithOwner.setOwners(owners);
				immobile.setProperty(propertyWithOwner);
			} else {
				throw new BadRequestException("Contato " + immobile.getProperty().getOwnerEmail() + "não encontrato.");
			}
			if (!addresses.contains(addressToTest)) {
				try {
					ImmobileResponse atualResponse = this.imobziService.postImmobile(immobile);
					callAmount++;
					if (immobile.getProperty().getPhotos().size() > 0) {

						if (!immobile.getProperty().getPhotos().get(0).getUrl().isEmpty()) {
							ImmobilePhotoResponse photoResponse = this.imobziService.postPhoto(immobile, atualResponse.getDb_id());
							callAmount++;

							this.imobziService.postCoverPhotoImmobile(
									this.generateSetCoverPhotoImmobile(photoResponse, immobile.getProperty()),
									atualResponse.getDb_id());
							callAmount++;

							atualResponse.setImmobilePhotoResponse(photoResponse);
						}
						immobileResponses.add(atualResponse);

						finish = System.currentTimeMillis();
						long total = (finish - start) / 1000;

						if (callAmount >= 57 && total < 60) {
							callAmount = 0;
							TimeUnit.SECONDS.sleep(60 - total + 1);
							start = System.currentTimeMillis();
						}

					}
				} catch (IOException | InterruptedException e) {
					log.error(e.getMessage());
					throw new BadRequestException("Ocorreu um erro ao cadastrar o endereço " 
					+ addressToTest + ". Por favor valide o checklist.");
				}
			} else {
				log.error("Endereço " + addressToTest + "já está cadastrado!");
			}
		});
		return immobileResponses;
	}

	private PropertyCoverPhoto generateSetCoverPhotoImmobile(ImmobilePhotoResponse immobilePhotoResponse,
			Immobile immobile) {
		List<Photos> photos = new ArrayList<>();
		photos.add(Photos.builder().action("PUT").category("photos").description("")
				.image_key(immobilePhotoResponse.getImage_key()).position(1).db_id(immobilePhotoResponse.getId())
				.image_name("image.png").url(immobilePhotoResponse.getUrl()).build());

		return PropertyCoverPhoto.builder()
				.property(ImmobileCoverPhoto.builder().address(immobile.getAddress()).city(immobile.getCity())
						.country(immobile.getCountry())
						.cover_photo(CoverPhoto.builder().db_id(immobilePhotoResponse.getId())
								.url(immobilePhotoResponse.getUrl()).build())
						.finality(immobile.getFinality()).neighborhood(immobile.getNeighborhood())
						.owners(immobile.getOwners()).photos(photos).rental_value(immobile.getRental_value())
						.sale_value(immobile.getSale_value()).state(immobile.getState()).status(immobile.getStatus())
						.zipcode(immobile.getZipcode()).build())
				.build();
	}

	private List<Property> excelListToImmobile(List<Excel> execelList) {
		
		log.info("Convertendo para lista de propriedades.");
		
		List<Property> property = execelList.parallelStream()
				.map(excel -> new Property(new Immobile().withActive(Boolean.TRUE).withStatus(excel.getStatus())
						.withAddress(excel.getAddress()).withAddressCompl(excel.getAddress_complement())
						.withAlternativeCode(excel.getAlternative_code()).withBedroom(excel.getBedroom())
						.withSuite(excel.getSuite()).withBathroom(excel.getBathroom()).withGarage(excel.getGarage())
						.withUsefulArea(excel.getUseful_area()).withLotArea(excel.getLot_area())
						.withArea(excel.getArea()).withDescriptions(excel.getDescription())
						.withSaleValue(excel.getSale_value()).withRentalValue(excel.getRental_value())
						.withNeighborhood(excel.getNeighborhood()).withCity(excel.getCity())
						.withState(excel.getState()).withZipcode(excel.getZipcode()).withCountry(excel.getCountry())
						.withFinality(excel.getFinality()).withPropertyType(excel.getProperty_type())
						.withBuildingName(excel.getBuilding_name()).withBuilding(excel.getBuilding())
						.withOwnerEmail(excel.getOwners()).withPhotos(ImmobileConverter.withPhotos(excel.getPhotos()))
						.withMultimidias(ImmobileConverter.withMultimidias(excel.getMultimidias()))
						.withSiteTitle(excel.getTittle())
						.withSiteDescriptions(this.prepareSiteDescription(excel.getDescription()))
						.withSiteUrl(ImmobileConverter.withUrls(excel.getProperty_type(), excel.getBedroom(),
								excel.getNeighborhood(), excel.getCity(), excel.getState()))

						.withLinks(ImmobileConverter.withLinks(excel.getCaptador()))
						.withFieldValues(excel.getBuilt(), excel.getIptu())))
				.collect(Collectors.toList());
		
		log.info("Excell convertido com sucesso para lista de propriedades.");
		
		return property;
	}

	private String prepareSiteDescription(String description) {
		String site_description = description.replaceAll(System.lineSeparator(),"<div><br></div>");
		return site_description;
	}
}

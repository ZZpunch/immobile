package br.com.imobzi.batch.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import br.com.imobzi.batch.domain.Immobile;
import br.com.imobzi.batch.domain.ImmobileContact;
import br.com.imobzi.batch.domain.ImmobileContacts;
import br.com.imobzi.batch.domain.ImmobileGetResponse;
import br.com.imobzi.batch.domain.ImmobilePhotoResponse;
import br.com.imobzi.batch.domain.ImmobileResponse;
import br.com.imobzi.batch.domain.Property;
import br.com.imobzi.batch.domain.PropertyCoverPhoto;
import br.com.imobzi.batch.handler.BadRequestException;
import br.com.imobzi.batch.handler.ForbiddenException;
import br.com.imobzi.batch.handler.GenericError;
import br.com.imobzi.batch.service.ImobziService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImobziServiceImpl implements ImobziService {
	@Value("${url.imobzi.post}")
	private String url;

	@Value("${url.imobzi.photo}")
	private String photoUrl;

	@Value("${url.imobzi.update}")
	private String updateUrl;
	
	@Value("${url.imobzi.get}")
	private String getUrl;

	@Value("${url.imobzi.owner}")
	private String ownerUrl;

	@Value("${secret.imobzi}")
	private String secret;
	
	private final static String SECRET_NAME = "X-Imobzi-Secret";

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public ImmobileContacts getOwnerByEmail(String address, String email) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(this.SECRET_NAME, this.secret);
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

		ImmobileContacts immobileContacts = new ImmobileContacts();
		try {
			HttpEntity httpEntity = new HttpEntity(httpHeaders);
			log.info("===============================================");
			log.info("Making request for the imobzi");

			String urlToGet = String.format(this.ownerUrl, email);
			ResponseEntity<ImmobileContacts> immobileResponseResponseEntity = this.restTemplate.exchange(urlToGet,
					HttpMethod.GET, httpEntity, ImmobileContacts.class);
			
			immobileContacts= immobileResponseResponseEntity.getBody();
		} catch (HttpStatusCodeException e) {
			throw new BadRequestException("Ocorreu um erro ao buscar o Owner com o email " + email + " no endereço " 
					+ address + ". Por favor tente novamente. \nCaso o erro continue, envie a seguinte mensagem para o desenvolvedor: "
							+ e.getMessage() + ". Status code: " + e.getRawStatusCode());		
			}
		return immobileContacts;
	}

	
	@Override
	public List<String> getAllCreatedAddresses() throws InterruptedException{
		
		log.info("Buscando todos os endereços cadastrados.");
		
		int callAmount=0;
		long start= System.currentTimeMillis();
		long finish;
		long total;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(this.SECRET_NAME, this.secret);
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

		ImmobileGetResponse immobileGetResponse = new ImmobileGetResponse();
		
		String cursor="";
		
		List<String> addresses = new ArrayList<>();
		
		try {
			do{
				HttpEntity httpEntity = new HttpEntity(httpHeaders);
				log.info("===============================================");
				log.info("Making request for the imobzi");
				
				String urlToGet = this.getUrl + "?cursor="+cursor;
				ResponseEntity<ImmobileGetResponse> immobileResponseResponseEntity = this.restTemplate.exchange(urlToGet,
						HttpMethod.GET, httpEntity, ImmobileGetResponse.class);
				immobileGetResponse = immobileResponseResponseEntity.getBody();
				List<Immobile> immobiles = immobileGetResponse.getProperties();
				cursor = immobileGetResponse.getCursor();
				callAmount++;
				immobiles.forEach(prop->{
					addresses.add(prop.getAddress() + " " + prop.getAddress_complement());
				});
				
				finish = System.currentTimeMillis();
				total = (finish - start)/1000;
				
				if(callAmount==60 && total<60) {
					TimeUnit.SECONDS.sleep(60-total+1);
					start = System.currentTimeMillis();
					callAmount=0;
				}
			}while(cursor != null);
			log.info("Endereços cadastrados obtidos com sucesso.");
			
			finish = System.currentTimeMillis();
			total = (finish - start)/1000;
			TimeUnit.SECONDS.sleep(60-total+1);
			return addresses;
		} catch (HttpStatusCodeException e) {
			throw new BadRequestException("Ocorreu um erro ao buscar todos os endereços cadastrados. Por favor tente novamente. \nCaso o erro continue, envie a seguinte mensagem para o desenvolvedor: "
							+ e.getMessage() + ". Status code: " + e.getRawStatusCode());	
		}
	}

	@Override
	public ImmobileResponse postCoverPhotoImmobile(PropertyCoverPhoto immobile, String propertyId) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(this.SECRET_NAME, this.secret);
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

		ImmobileResponse immobileResponse = new ImmobileResponse();
		try {
			HttpEntity httpEntity = new HttpEntity(immobile, httpHeaders);
			log.info("===============================================");
			log.info("Making request for the imobzi");

			String urlToUpdate = String.format(this.updateUrl, propertyId);
			ResponseEntity<ImmobileResponse> immobileResponseResponseEntity = this.restTemplate.exchange(urlToUpdate,
					HttpMethod.POST, httpEntity, ImmobileResponse.class);
			immobileResponse = immobileResponseResponseEntity.getBody();
		} catch (HttpStatusCodeException e) {
			log.error("Ocorreu um erro ao cadastrar a foto do endereço " 
					+ immobile.getProperty().getAddress() + ". Por favor tente novamente. \nCaso o erro continue, envie a seguinte mensagem para o desenvolvedor: "
							+ e.getMessage() + ". Status code: " + e.getRawStatusCode());
		}
		return immobileResponse;
	}

	@Override
	public ImmobilePhotoResponse postPhoto(Property immobile, String propertyId) throws IOException {
		
		log.info("Metodo postPhoto chamado.");
		
		ImmobilePhotoResponse immobilePhotoResponse = new ImmobilePhotoResponse();
		try {

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add(this.SECRET_NAME, this.secret);
			httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

			List<MediaType> mediaTypes = new ArrayList();
			mediaTypes.add(MediaType.ALL);
			httpHeaders.setAccept(mediaTypes);

			this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			FileWriter myWriter = new FileWriter("photo");
			myWriter.write(immobile.getProperty().getPhotos().get(0).getUrl());
			myWriter.close();

			File file = new File("photo");

			map.add("uploadFile", new FileSystemResource(file));
			map.add("position", 1);
			map.add("category", "photos");
			map.add("description", "");

			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(map,
					httpHeaders);

			log.info("===============================================");
			log.info("Making request to insert Photos to imobzi");

			String urlToCall = String.format(this.photoUrl, propertyId);

			ResponseEntity<ImmobilePhotoResponse> immobileResponseResponseEntity = this.restTemplate.exchange(urlToCall,
					HttpMethod.POST, httpEntity, ImmobilePhotoResponse.class);
			immobilePhotoResponse = immobileResponseResponseEntity.getBody();

			file.delete();
		} catch (HttpStatusCodeException e) {
			File file = new File("photo");
			file.delete();
			log.error("Ocorreu um erro ao inserir a foto como capa do endereço " 
					+ immobile.getProperty().getAddress() + ". Por favor tente novamente. \nCaso o erro continue, envie a seguinte mensagem para o desenvolvedor: "
							+ e.getMessage() + ". Status code: " + e.getRawStatusCode());
			return null;
		}
		log.info("Foto inserida com sucesso.");
		return immobilePhotoResponse;

	}

	@Override
	public ImmobileResponse postImmobile(Property immobile) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(this.SECRET_NAME, this.secret);
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

		ImmobileResponse immobileResponse = new ImmobileResponse();
		try {
			HttpEntity httpEntity = new HttpEntity(immobile, httpHeaders);
			log.info("===============================================");
			log.info("Making request for the imobzi");
			ResponseEntity<ImmobileResponse> immobileResponseResponseEntity = this.restTemplate.exchange(this.url,
					HttpMethod.POST, httpEntity, ImmobileResponse.class);
			immobileResponse = immobileResponseResponseEntity.getBody();
		} catch (HttpStatusCodeException e) {
			throw new BadRequestException("Ocorreu um erro ao cadastrar o endereço " 
					+ immobile.getProperty().getAddress() + ". Por favor valide o checklist. \nCaso o erro continue, envie a seguinte mensagem para o desenvolvedor: "
							+ e.getMessage() + ". Status code: " + e.getRawStatusCode());
		}
		return immobileResponse;
	}
}

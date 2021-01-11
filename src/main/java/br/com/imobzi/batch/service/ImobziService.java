package br.com.imobzi.batch.service;

import java.io.IOException;
import java.util.List;

import br.com.imobzi.batch.domain.ImmobileContacts;
import br.com.imobzi.batch.domain.ImmobilePhotoResponse;
import br.com.imobzi.batch.domain.ImmobileResponse;
import br.com.imobzi.batch.domain.Property;
import br.com.imobzi.batch.domain.PropertyCoverPhoto;

public interface ImobziService {
	ImmobileResponse postImmobile(Property immobile);

	ImmobilePhotoResponse postPhoto(Property immobile, String string) throws IOException;

	ImmobileResponse postCoverPhotoImmobile(PropertyCoverPhoto immobile, String propertyId);

	List<String> getAllCreatedAddresses() throws InterruptedException;

	ImmobileContacts getOwnerByEmail(String address, String email);

}

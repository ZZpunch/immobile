package br.com.imobzi.batch.utils;

import br.com.imobzi.batch.domain.*;
import org.springframework.boot.actuate.endpoint.web.Link;

import java.text.Normalizer;
import java.util.*;

public class ImmobileConverter {
    public static List<Photos> withPhotos(String photos){
        Photos photosList = new Photos();
        photosList.setUrl(photos);
        return new ArrayList<Photos>(Arrays.asList(photosList));
    }
    public static List<Owners> withOwners(String owners){
        Owners ownersList = new Owners();
        ownersList.setName(owners);
        return new ArrayList<Owners>(Arrays.asList(ownersList));
    }
    public static List<Multimidias> withMultimidias(String multimidias){
        Multimidias multimidiasList = new Multimidias();
        multimidiasList.setUrl(multimidias);
        return new ArrayList<Multimidias>(Arrays.asList(multimidiasList));
    }
    public static String withUrls(String tipo, Integer quartos, String bairro, String cidade, String estado){
    	List<String> parameters = new ArrayList();
    	parameters.add(tipo);
    	parameters.add(quartos.toString());
    	parameters.add(bairro);
    	parameters.add(cidade);
    	parameters.add(estado);
        String urlsBuilder = String.join("-",parameters);
        String url = urlsBuilder.replace(" ", "-");
        url = Normalizer.normalize("corretoraideal.com.br/imovel/" + url, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        return url;
    }
    public static List<Links> withLinks(String captador){
        Links links = new Links();
        Contact contact = new Contact();
        contact.setName(captador);
        links.setContact(contact);
        return new ArrayList<Links>(Arrays.asList(links));
    }
}

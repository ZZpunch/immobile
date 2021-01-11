package br.com.imobzi.batch.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImmobileCoverPhoto {
    private String address;
    private String city;
    private String country;
    private CoverPhoto cover_photo;
    private String finality;
    private String neighborhood;
    private List<Photos> photos;
    private Double rental_value;
    private Double sale_value;
    private String state;
    private String status;
    private String zipcode;
    private List<Owners> owners;
}

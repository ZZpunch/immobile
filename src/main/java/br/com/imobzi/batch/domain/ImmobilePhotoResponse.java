package br.com.imobzi.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImmobilePhotoResponse {
	
	private String url;
	private Integer id;
	private String image_key;

}

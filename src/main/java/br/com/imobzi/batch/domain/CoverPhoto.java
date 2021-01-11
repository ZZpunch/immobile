package br.com.imobzi.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoverPhoto {
	
	private Integer db_id;
	private String url;

}

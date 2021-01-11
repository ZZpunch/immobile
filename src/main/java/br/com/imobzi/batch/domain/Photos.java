package br.com.imobzi.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Photos {
	private String action;
    private String category;
    private String description;
    private String image_key;
    private Integer position;
    private Integer db_id;
    private String image_name;
    private String url;

}

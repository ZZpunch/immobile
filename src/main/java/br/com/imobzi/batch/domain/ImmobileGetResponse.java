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
public class ImmobileGetResponse {
	
	private List<Immobile> properties;
	private String cursor;
}

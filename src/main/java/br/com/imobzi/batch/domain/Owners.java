package br.com.imobzi.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Owners {
    private String code;
    private long id;
    private String name;
    private String type;
    private String percentage;
    private String rate;
}

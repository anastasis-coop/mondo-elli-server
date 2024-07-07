package eu.anastasis.mondoelli.percorso;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PercorsiDto {
	private List<PercorsoDto> content = new ArrayList<PercorsoDto>();
	private int numberOfElements = 0;
	private int totalPages = 0;
	private long totalElements = 0;
}

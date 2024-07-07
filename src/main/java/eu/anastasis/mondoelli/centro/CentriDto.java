package eu.anastasis.mondoelli.centro;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CentriDto {
	private List<CentroDto> content = new ArrayList<CentroDto>();
	private int numberOfElements = 0;
	private int totalPages = 0;
	private long totalElements = 0;
}

package eu.anastasis.mondoelli.operatore;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OperatoriDto {
	private List<OperatoreDto> content = new ArrayList<OperatoreDto>();
	private int numberOfElements = 0;
	private int totalPages = 0;
	private long totalElements = 0;
}

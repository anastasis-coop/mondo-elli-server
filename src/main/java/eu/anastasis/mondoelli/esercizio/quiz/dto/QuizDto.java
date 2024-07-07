package eu.anastasis.mondoelli.esercizio.quiz.dto;

import java.util.List;

import eu.anastasis.mondoelli.enums.Quartiere;
import lombok.Data;

@Data
public class QuizDto {
	private Integer id;
	private List<String> situazioni;
	private String corretta;
	private Quartiere funzioneEsecutiva;
}

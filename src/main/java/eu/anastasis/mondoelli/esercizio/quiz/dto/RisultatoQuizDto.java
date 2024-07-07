package eu.anastasis.mondoelli.esercizio.quiz.dto;

import lombok.Data;

@Data
public class RisultatoQuizDto {
	private Integer idQuiz;
	private Integer tempoImpiegato; // in secs
	private String risposta;
}

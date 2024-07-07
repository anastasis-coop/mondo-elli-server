package eu.anastasis.mondoelli.esercizio.medialiteracy.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class RisultatoMediaLiteracyDto {

	// Inizio dell'esercizio
	private Date inizio;

	// Quanto conosci l'argomento da 1 a 10
	private Integer knowledgeEstimate;

	// Risultato quiz conoscenze pregresse
	private List<Boolean> knowledgeQuizzesAnswers;

	// Scaletta ordinata al primo colpo
	private Boolean outlineNeededHelp;

	// Valutazione rilevanza, risultato per ogni testo
	// (0 sbagliato, 1 vicino alla soluzione, 2 uguale alla soluzione)
	private List<Integer> relevancePoints;

	// Indice risposte ai quiz dove bisogna motivare l'esclusione di un testo
	private List<Integer> relevanceQuizzesAnswers;

	// Stima di tempo (minuti) sulla fase selezione snippet
	private Integer snippetMinutes;

	// Stima di tempo (minuti) sulla fase produzione testo
	private Integer productionMinutes;

	// Stima di tempo (minuti) sulla fase revisione testo
	private Integer revisionMinutes;

	// L'esercizio è stato completato in un intervallo simile alla stima iniziale
	private Boolean timeEstimatesMet;

	// Allocazione numero snippet per ogni testo
	private List<Integer> snippetEstimates;

	// Il numero di snippet estratti dai testi è simile a quello stimato
	private Boolean snippetEstimatesMet;

	// Per ogni testo, lista degli indici degli snippet estratti
	private List<List<Integer>> productionPicks;

	// Somma dei punti totali degli snippet estratti
	private Integer productionPoints;

	// Testo prodotto (snippet + card) senza titolo e tag
	private String productionResult;

	// Numero di tag corretti selezionati
	private Integer tagsCorrectAnswers;

	// Il titolo scelto è quello corretto
	private Boolean titleCorrectAnswer;

	// Indici risposte sondaggio finale
	private List<Integer> submissionQuizzesAnswers;

	// Indici consigli per i compagni selezionati
	private List<Integer> submissionTipsAnswers;

}

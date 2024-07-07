package eu.anastasis.mondoelli.esercizio.quiz;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import eu.anastasis.mondoelli.enums.Quartiere;
import lombok.Data;

@Data
@Entity
@Table(indexes = { @Index(name = "funzioneEsecutiva", columnList = "funzioneEsecutiva", unique = false) })
public class QuizSource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String situazione;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Quartiere funzioneEsecutiva;

}

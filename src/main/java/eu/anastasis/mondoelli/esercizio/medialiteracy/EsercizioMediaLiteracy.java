package eu.anastasis.mondoelli.esercizio.medialiteracy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import eu.anastasis.mondoelli.sessione.Sessione;
import lombok.Data;

@Data
@Entity
public class EsercizioMediaLiteracy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn
	private Sessione sessione;

	@Column(nullable = false)
	private Date inizio;

	@Lob
	@Column(nullable = false)
	private String jsonData;

}

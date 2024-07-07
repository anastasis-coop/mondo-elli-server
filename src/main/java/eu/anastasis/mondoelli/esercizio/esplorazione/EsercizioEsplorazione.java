package eu.anastasis.mondoelli.esercizio.esplorazione;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;

import eu.anastasis.mondoelli.enums.Prospettiva;
import eu.anastasis.mondoelli.sessione.Sessione;
import lombok.Data;

@Data
@Entity
@Table(indexes = { @Index(name = "inizio", columnList = "inizio", unique = false) }, uniqueConstraints = {
		@UniqueConstraint(columnNames = { "sessione_id", "inizio" }) })
public class EsercizioEsplorazione {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn
	private Sessione sessione;

	@Column(nullable = false)
	private Date inizio;

	@Column(nullable = true)
	private Date fine;

	@Column(nullable = false)
	private Integer livello;

	@Column(nullable = false)
	private String compito;

	@Column(nullable = false)
	private String esecuzione;

	@Column(nullable = false)
	private Boolean corretto;

	@Column(nullable = false)
	private Boolean finale;

	@Column(nullable = false)
	private Integer tempoImpiegato; // sec

	@Column(nullable = false)
	private Boolean ombra = false;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Prospettiva prospettiva;

	@Column(name = "created_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@PrePersist
	public void setDatesForInsert() {
		if (this.createdTime == null) {
			this.createdTime = new Date();
		}
	}

}

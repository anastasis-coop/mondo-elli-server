package eu.anastasis.mondoelli.esercizio.stanza;

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

import eu.anastasis.mondoelli.enums.LevelStatus;
import eu.anastasis.mondoelli.enums.RoomChannel;
import eu.anastasis.mondoelli.enums.RoomLevel;
import eu.anastasis.mondoelli.sessione.Sessione;
import lombok.Data;

@Data
@Entity
@Table(indexes = { @Index(name = "inizio", columnList = "inizio", unique = false) }, uniqueConstraints = {
		@UniqueConstraint(columnNames = { "sessione_id", "inizio" }) })
public class EsercizioStanza {

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
	@Enumerated(EnumType.STRING)
	private RoomLevel livello;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private RoomChannel canale;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private LevelStatus statoLivello;

	private Integer accuratezza;

	private Integer numeroStimoliCorretti;

	private Integer numeroStimoliErrati;

	private Integer numeroStimoliSaltati;

	private Integer tempoReazioneMedio; // in ms

	private Integer tempoEsposizioneOggetti; // in ms

	private Integer feedbackAttenzione;

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

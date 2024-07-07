package eu.anastasis.mondoelli.sessione;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.esercizio.esplorazione.EsercizioEsplorazione;
import eu.anastasis.mondoelli.esercizio.medialiteracy.EsercizioMediaLiteracy;
import eu.anastasis.mondoelli.esercizio.quiz.EsercizioQuiz;
import eu.anastasis.mondoelli.esercizio.stanza.EsercizioStanza;
import eu.anastasis.mondoelli.utente.Utente;
import lombok.Data;

@Data
@Entity
@Table(indexes = { @Index(name = "inizio", columnList = "inizio", unique = false) }, uniqueConstraints = {
		@UniqueConstraint(columnNames = { "inizio", "utente_id" }) })
public class Sessione {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonBackReference
	@ManyToOne(optional = false)
	private Utente utente;

	@Column(nullable = false)
	private Date inizio;

	private Date fine;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Quartiere quartiere;

	private Integer accuratezzaMedia;

	private Integer tempoNetto;

	private Integer score;

	private String steps;

	@OneToMany(mappedBy = "sessione", cascade = CascadeType.ALL)
	@OrderBy("inizio ASC")
	private List<EsercizioStanza> eserciziStanza;

	@OneToMany(mappedBy = "sessione", cascade = CascadeType.ALL)
	@OrderBy("inizio ASC")
	private List<EsercizioEsplorazione> eserciziEsplorazione;

	@OneToMany(mappedBy = "sessione", cascade = CascadeType.ALL)
	@OrderBy("inizio ASC")
	private List<EsercizioQuiz> eserciziQuiz;

	@OneToMany(mappedBy = "sessione", cascade = CascadeType.ALL)
	@OrderBy("inizio ASC")
	private List<EsercizioMediaLiteracy> eserciziMediaLiteracy;

	@Column(name = "updated_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = true, updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;

	@Column(name = "created_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@PrePersist
	public void setDatesForInsert() {
		if (this.createdTime == null) {
			this.createdTime = new Date();
		}
		this.updatedTime = new Date();
	}

	@PreUpdate
	public void setDatesForUpdate() {
		this.updatedTime = new Date();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fine == null) ? 0 : fine.hashCode());
		result = prime * result + ((inizio == null) ? 0 : inizio.hashCode());
		result = prime * result + ((utente == null) ? 0 : utente.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sessione other = (Sessione) obj;
		if (fine == null) {
			if (other.fine != null)
				return false;
		} else if (!fine.equals(other.fine))
			return false;
		if (inizio == null) {
			if (other.inizio != null)
				return false;
		} else if (!inizio.equals(other.inizio))
			return false;
		if (utente == null) {
			if (other.utente != null)
				return false;
		} else if (!utente.equals(other.utente))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sessione [id=" + id + ", inizio=" + inizio + ", fine=" + fine + "]";
	}

}

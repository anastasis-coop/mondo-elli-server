package eu.anastasis.mondoelli.percorso;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import eu.anastasis.mondoelli.centro.Centro;
import eu.anastasis.mondoelli.enums.TipoPercorso;
import eu.anastasis.mondoelli.operatore.Operatore;
import eu.anastasis.mondoelli.utente.Utente;
import lombok.Data;

@Data
@Entity
public class Percorso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoPercorso tipo;

	@Column(nullable = false)
	private String nome;

	@JsonBackReference
	@ManyToOne(optional = false)
	private Centro centro;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "operatore_percorsi", joinColumns = { @JoinColumn(name = "percorsi_id") }, inverseJoinColumns = {
			@JoinColumn(name = "operatore_id") })
	private List<Operatore> operatori;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date inizioPercorso;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date inizioPercorsoEffettivo;

	@Column(nullable = false)
	private Integer durataFunzioneEsecutivaGiorni;

	@Column(nullable = false)
	private Boolean periodoIntroduzione;

	@Column(nullable = false)
	private Boolean mediaLiteracy;

	@OneToMany(mappedBy = "percorso", cascade = CascadeType.ALL)
	@OrderBy("username ASC")
	private List<Utente> utenti;

	@Column(nullable = false)
	private Boolean archiviato;

}

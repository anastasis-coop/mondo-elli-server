package eu.anastasis.mondoelli.utente;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import eu.anastasis.mondoelli.account.Account;
import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.percorso.Percorso;
import eu.anastasis.mondoelli.sessione.Sessione;
import eu.anastasis.mondoelli.status.Status;
import eu.anastasis.mondoelli.utente.parametri.Parametri;
import lombok.Data;

@Data
@Entity
@Table(indexes = { @Index(name = "userpass", columnList = "username, plainPassword", unique = true) })
public class Utente {

	@Id
	private Integer id;

	@OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Account account;

	@Column(nullable = false)
	private String username;

	@JsonBackReference
	@ManyToOne(optional = false)
	private Percorso percorso;

	private String plainPassword;

	@Column(nullable = false)
	private Boolean facilitato;

	@Column(nullable = false)
	private Boolean demo;

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	private Parametri parametri;

	@OneToOne(optional = true, cascade = CascadeType.ALL)
	private Status status;

	@OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
	@OrderBy("inizio DESC")
	private List<Sessione> sessioni;

	@Column(nullable = false)
	private Integer livelloEsplorazione;

	@ElementCollection(targetClass = Quartiere.class, fetch = FetchType.EAGER)
	private Set<Quartiere> quartieriCompletati;

}

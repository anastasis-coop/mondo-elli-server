package eu.anastasis.mondoelli.operatore;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonBackReference;

import eu.anastasis.mondoelli.account.Account;
import eu.anastasis.mondoelli.centro.Centro;
import eu.anastasis.mondoelli.percorso.Percorso;
import lombok.Data;

@Data
@Entity
public class Operatore {

	@Id
	private Integer id;

	@OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Account account;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String cognome;

	@ManyToMany
	@OrderBy("nome ASC")
	private List<Percorso> percorsi;

	@JsonBackReference
	@ManyToOne(optional = false)
	private Centro centro;

	@Column(nullable = false)
	Boolean referente;

}

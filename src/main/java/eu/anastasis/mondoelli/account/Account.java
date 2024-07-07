package eu.anastasis.mondoelli.account;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import eu.anastasis.mondoelli.operatore.Operatore;
import eu.anastasis.mondoelli.utente.Utente;
import lombok.Data;

@Data
@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	private Set<Role> ruoli;

	@Column(nullable = false)
	private Boolean enabled;

	@Column(nullable = true)
	private Boolean privacy;

	@Column(nullable = false)
	private String password = "";

	@Column(nullable = true)
	private Date dataCreazionePassword;

	@JsonBackReference
	@OneToOne(optional = true, mappedBy = "account")
	private Operatore operatore;

	public boolean isOperatore() {
		return (operatore != null);
	}

	@JsonBackReference
	@OneToOne(optional = true, mappedBy = "account")
	private Utente utente;

	public boolean isUtente() {
		return (utente != null);
	}

}

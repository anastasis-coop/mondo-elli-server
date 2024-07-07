package eu.anastasis.mondoelli.centro;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import eu.anastasis.mondoelli.comune.Comune;
import eu.anastasis.mondoelli.operatore.Operatore;
import eu.anastasis.mondoelli.percorso.Percorso;
import lombok.Data;

@Data
@Entity
@Table(indexes = { @Index(name = "key", columnList = "codice", unique = true) })
public class Centro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String codice;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Comune comune;

	@OneToMany(mappedBy = "centro", cascade = CascadeType.ALL)
	@OrderBy("nome ASC")
	private List<Percorso> percorsi;

	@OneToMany(mappedBy = "centro", cascade = CascadeType.ALL)
	@OrderBy("cognome ASC")
	private List<Operatore> operatori;

}

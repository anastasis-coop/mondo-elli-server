package eu.anastasis.mondoelli.status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import eu.anastasis.mondoelli.utente.Utente;
import lombok.Data;

@Data
@Entity
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonBackReference
	@OneToOne(optional = false, mappedBy = "status")
	private Utente utente;

	@Lob
	@Column(nullable = false)
	private String jsonData;

}

package eu.anastasis.mondoelli.utente.parametri;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import eu.anastasis.mondoelli.utente.Utente;
import lombok.Data;

@Data
@Entity
public class Parametri {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonBackReference
	@OneToOne(optional = false, mappedBy = "parametri")
	private Utente utente;

	@Column(nullable = false, columnDefinition = "varchar(255) default ''")
	private String nomeEllo;

	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private Integer accessorio1;

	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private Integer accessorio2;

	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private Integer accessorio3;

	// CREATED TIME & UPDATED TIME

	@Column(name = "created_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)

	private Date createdTime;

	@Column(name = "updated_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = true, updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;

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

}

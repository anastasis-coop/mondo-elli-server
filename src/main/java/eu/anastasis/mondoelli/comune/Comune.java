package eu.anastasis.mondoelli.comune;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Comune {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String cityName;

	@Column(nullable = false)
	private String cityProvinceName;

	@Column(nullable = false)
	private String cityProvinceCode;

	@Column(nullable = false)
	private String cityRegion;

}

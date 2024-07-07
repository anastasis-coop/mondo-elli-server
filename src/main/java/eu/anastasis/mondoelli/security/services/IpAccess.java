package eu.anastasis.mondoelli.security.services;

import java.util.Date;

import lombok.Data;

@Data
public class IpAccess {

	private String ip;
	private Date when;

	public IpAccess(String ip) {
		super();
		this.ip = ip;
		this.when = new Date();
	}

}

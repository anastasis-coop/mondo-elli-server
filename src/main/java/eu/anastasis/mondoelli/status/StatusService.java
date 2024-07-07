package eu.anastasis.mondoelli.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.anastasis.mondoelli.utente.Utente;

@Service
public class StatusService {

	@Autowired
	StatusRepository statusRepository;

	public String getStatusUtente(Utente utente) {
		Status status = utente.getStatus();
		if (status == null) {
			return "{}";
		} else {
			return status.getJsonData();
		}
	}

	@Transactional
	public void saveStatusUtente(Utente utente, String jsonData) {
		Status status = utente.getStatus();
		if (status == null) {
			status = new Status();
			status.setUtente(utente);
			utente.setStatus(status);
		}
		status.setJsonData(jsonData);
		statusRepository.save(status);
	}

}

package eu.anastasis.mondoelli.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.utente.Utente;
import eu.anastasis.mondoelli.utente.UtenteService;

@RestController
@RequestMapping("/status")
public class StatusController {

	@Autowired
	UtenteService utenteService;

	@Autowired
	StatusService statusService;

	@GetMapping()
	public ResponseEntity<String> getStatus() throws ForbiddenException {
		Utente utente = utenteService.findLoggedUtente();
		return ResponseEntity.ok(statusService.getStatusUtente(utente));
	}

	@PostMapping()
	public ResponseEntity<Void> saveStatus(@RequestBody String jsonData) throws ForbiddenException {
		Utente utente = utenteService.findLoggedUtente();
		statusService.saveStatusUtente(utente, jsonData);
		return ResponseEntity.ok().build();
	}

}

package eu.anastasis.mondoelli.quartiere;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.utente.UtenteService;

@RestController
@RequestMapping("/quartieri")
public class QuartiereController {

	@Autowired
	UtenteService utenteService;

	@PostMapping(value = "/{quartiere}/completed")
	public ResponseEntity<Void> quartiereCompletato(@PathVariable Quartiere quartiere) throws ForbiddenException {
		utenteService.quartiereCompletato(utenteService.findLoggedUtente(), quartiere);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/reset")
	public ResponseEntity<Void> resetQuartieri() throws ForbiddenException {
		utenteService.resetQuartieri(utenteService.findLoggedUtente());
		return ResponseEntity.ok().build();
	}

}

package eu.anastasis.mondoelli.esercizio.medialiteracy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.esercizio.medialiteracy.dto.EsercizioMediaLiteracyDto;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.utente.Utente;
import eu.anastasis.mondoelli.utente.UtenteService;

@RestController
@RequestMapping("/medialiteracy")
public class EsercizioMediaLiteracyController {

	@Autowired
	UtenteService utenteService;

	@Autowired
	EsercizioMediaLiteracyService esercizioMediaLiteracyService;

	@GetMapping("/init")
	public EsercizioMediaLiteracyDto inizioEsplorazione() throws ForbiddenException {
		Utente utente = utenteService.findLoggedUtente();
		return esercizioMediaLiteracyService.inizioMediaLiteracy(utente);
	}

}

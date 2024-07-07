package eu.anastasis.mondoelli.esercizio.esplorazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.esercizio.esplorazione.dto.EsercizioEsplorazioneDto;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.utente.Utente;
import eu.anastasis.mondoelli.utente.UtenteService;

@RestController
@RequestMapping("/esplorazioni")
public class EsercizioEsplorazioneController {

	@Autowired
	UtenteService utenteService;

	@Autowired
	EsercizioEsplorazioneService esercizioEsplorazioneService;

	@GetMapping("/init")
	public EsercizioEsplorazioneDto inizioEsplorazione() throws ForbiddenException {
		Utente utente = utenteService.findLoggedUtente();
		return esercizioEsplorazioneService.inizioEsplorazione(utente);
	}

}

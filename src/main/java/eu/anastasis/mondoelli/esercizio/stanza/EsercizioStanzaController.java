package eu.anastasis.mondoelli.esercizio.stanza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.esercizio.stanza.dto.BooleanDto;
import eu.anastasis.mondoelli.esercizio.stanza.dto.EsitoEsercizioStanzaDto;
import eu.anastasis.mondoelli.esercizio.stanza.dto.RisultatoEsercizioStanzaDto;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.sessione.SessioneService;
import eu.anastasis.mondoelli.utente.Utente;
import eu.anastasis.mondoelli.utente.UtenteService;

@RestController
@RequestMapping("stanze")
public class EsercizioStanzaController {

	@Autowired
	UtenteService utenteService;

	@Autowired
	SessioneService sessioneService;

	@Autowired
	EsercizioStanzaService esercizioStanzaService;

	@GetMapping("{fe}/video")
	public BooleanDto getMostraVideo(@PathVariable Quartiere fe)
			throws NotFoundException, ForbiddenException {
		BooleanDto result = new BooleanDto();
		if (fe == Quartiere.INTRODUZIONE) {
			result.setValue(true);
		} else {
			Utente utente = utenteService.findLoggedUtente();
			result.setValue(esercizioStanzaService.getMostraVideo(utente, fe));
		}
		return result;
	}

	@PostMapping("{idEsercizio}")
	public EsitoEsercizioStanzaDto salvaEsercizio(@PathVariable Integer idEsercizio, @RequestBody RisultatoEsercizioStanzaDto dto)
			throws NotFoundException {
		return sessioneService.salvaEsercizioStanza(idEsercizio, dto);
	}

}

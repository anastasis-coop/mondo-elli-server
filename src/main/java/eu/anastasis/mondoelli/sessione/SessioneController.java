package eu.anastasis.mondoelli.sessione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import eu.anastasis.mondoelli.esercizio.esplorazione.dto.RisultatoEsercizioEsplorazioneDto;
import eu.anastasis.mondoelli.esercizio.medialiteracy.dto.RisultatoMediaLiteracyDto;
import eu.anastasis.mondoelli.esercizio.quiz.dto.RisultatoQuizDto;
import eu.anastasis.mondoelli.esercizio.quiz.dto.ScoreDto;
import eu.anastasis.mondoelli.esercizio.stanza.EsercizioStanzaService;
import eu.anastasis.mondoelli.esercizio.stanza.dto.EsercizioStanzaDto;
import eu.anastasis.mondoelli.esercizio.stanza.dto.InitEsercizioDto;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.sessione.dto.DatiSessioneDto;
import eu.anastasis.mondoelli.sessione.dto.FineSessioneDto;
import eu.anastasis.mondoelli.utente.UtenteService;

@RestController
@RequestMapping("sessioni")
public class SessioneController {

	@Autowired
	UtenteService utenteService;

	@Autowired
	SessioneService sessioneService;

	@Autowired
	EsercizioStanzaService esercizioStanzaService;

	@PostMapping(value = "init")
	public DatiSessioneDto initSessione() throws ForbiddenException {
		return sessioneService.initSessione(utenteService.findLoggedUtente());
	}

	@PostMapping(value = "{idSessione}/end")
	public FineSessioneDto endSessione(@PathVariable Integer idSessione) {
		return sessioneService.endSessione(idSessione);
	}

	@PostMapping("{idSessione}/updateScore/{score}")
	public ResponseEntity<Void> updateScore(@PathVariable Integer idSessione, @PathVariable String scoreStep) {
		sessioneService.updateScore(idSessione, scoreStep);
		return ResponseEntity.ok().build();
	}

	@PostMapping("{idSessione}/esplorazioni")
	public ResponseEntity<Void> salvaEsplorazione(@PathVariable Integer idSessione, @RequestBody RisultatoEsercizioEsplorazioneDto dto)
			throws NotFoundException {
		sessioneService.salvaEsercizioEsplorazione(idSessione, dto);
		return ResponseEntity.ok().build();
	}

	@PostMapping("{idSessione}/stanze/init")
	public EsercizioStanzaDto getStanza(@PathVariable Integer idSessione, @RequestBody InitEsercizioDto dto) throws NotFoundException {
		Sessione s = sessioneService.findSessioneById(idSessione);
		return esercizioStanzaService.getEsercizioStanza(s, dto.getFunzioneEsecutiva(), dto.getCanale());
	}

	@PostMapping("{idSessione}/quiz")
	public ScoreDto saveQuiz(@PathVariable Integer idSessione, @RequestBody RisultatoQuizDto dto) {
		return sessioneService.saveQuiz(idSessione, dto);
	}

	@PostMapping("{idSessione}/medialiteracy")
	public ResponseEntity<Void> salvaMediaLiteracy(@PathVariable Integer idSessione, @RequestBody RisultatoMediaLiteracyDto dto)
			throws NotFoundException, JsonProcessingException {
		sessioneService.salvaEsercizioMediaLiteracy(idSessione, dto);
		return ResponseEntity.ok().build();
	}

}

package eu.anastasis.mondoelli.utente.parametri;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.quartiere.QuartiereDto;
import eu.anastasis.mondoelli.quartiere.QuartiereService;
import eu.anastasis.mondoelli.utente.Utente;
import eu.anastasis.mondoelli.utente.UtenteService;

@RestController
@RequestMapping("/parametri")
public class ParametriController {

	@Autowired
	UtenteService utenteService;

	@Autowired
	ParametriService parametriService;

	@Autowired
	QuartiereService quartiereService;

	@Autowired
	ParametriRepository parametriRepository;

	@GetMapping
	public ParametriDto getParams() throws ForbiddenException {
		Utente utente = utenteService.findLoggedUtente();
		ParametriDto result = parametriService.createParametriDto(utente.getParametri());
		result.setDemo(utente.getDemo());
		QuartiereDto quartiereDto = quartiereService.getQuartiereCorrente(utente);
		result.setQuartiereCorrente(quartiereDto.getQuartiere());
		result.setPercorsoNonIniziato(quartiereDto.getPercorsoNonIniziato());
		result.setPercorsoCompletato(quartiereDto.getPercorsoCompletato());
		result.setQuartieriCompletati(utente.getQuartieriCompletati());
		return result;
	}

	@PostMapping()
	public ResponseEntity<Void> saveParams(@RequestBody SetParametriDto dto) throws ForbiddenException {
		Utente utente = utenteService.findLoggedUtente();
		if (utente.getDemo() != true) {
			Parametri parametri = utente.getParametri();
			parametriService.fillParametriFromDto(parametri, dto);
			parametriRepository.save(parametri);
		}
		return ResponseEntity.ok().build();
	}

}

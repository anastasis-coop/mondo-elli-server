package eu.anastasis.mondoelli.monitor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.monitor.dto.MonitorEsplorazioneDto;
import eu.anastasis.mondoelli.monitor.dto.MonitorFunzioneDto;
import eu.anastasis.mondoelli.monitor.dto.MonitorPercorsoDto;
import eu.anastasis.mondoelli.monitor.dto.MonitorQuizDto;
import eu.anastasis.mondoelli.monitor.dto.MonitorSessioneDto;
import eu.anastasis.mondoelli.utente.Utente;
import eu.anastasis.mondoelli.utente.UtenteService;

@RestController
@RequestMapping("/utenti/{id}/monitor")
public class MonitorController {

	@Autowired
	MonitorService monitorService;

	@Autowired
	UtenteService utenteService;

	@GetMapping("/percorso")
	public List<MonitorPercorsoDto> getPercorso(@PathVariable Integer id) throws ForbiddenException {
		Utente utente = utenteService.findUtenteById(id);
		return monitorService.getPercorsoUtente(utente);
	}

	@GetMapping("/sessioni")
	public List<MonitorSessioneDto> getSessioni(@PathVariable Integer id) throws ForbiddenException {
		Utente utente = utenteService.findUtenteById(id);
		return monitorService.getSessioniUtente(utente);
	}

	@GetMapping("/esplorazioni")
	public List<MonitorEsplorazioneDto> getEsplorazione(@PathVariable Integer id) throws ForbiddenException {
		Utente utente = utenteService.findUtenteById(id);
		return monitorService.getEsplorazioneUtente(utente);
	}

	@GetMapping("/inibizioneRisposta")
	public MonitorFunzioneDto getInibizioneRisposta(@PathVariable Integer id) throws ForbiddenException {
		Utente utente = utenteService.findUtenteById(id);
		return monitorService.getEserciziUtenteFunzioneEsecutiva(utente, Quartiere.INIBIZIONE_RISPOSTA);
	}

	@GetMapping("/controlloInterferenza")
	public MonitorFunzioneDto getControlloInterferenza(@PathVariable Integer id) throws ForbiddenException {
		Utente utente = utenteService.findUtenteById(id);
		return monitorService.getEserciziUtenteFunzioneEsecutiva(utente, Quartiere.CONTROLLO_INTERFERENZA);
	}

	@GetMapping("/memoriaLavoro")
	public MonitorFunzioneDto getMemoriaLavoro(@PathVariable Integer id) throws ForbiddenException {
		Utente utente = utenteService.findUtenteById(id);
		return monitorService.getEserciziUtenteFunzioneEsecutiva(utente, Quartiere.MEMORIA_LAVORO);
	}

	@GetMapping("/flessibilitaCognitiva")
	public MonitorFunzioneDto getFlessibilitaCognitiva(@PathVariable Integer id) throws ForbiddenException {
		Utente utente = utenteService.findUtenteById(id);
		return monitorService.getEserciziUtenteFunzioneEsecutiva(utente, Quartiere.FLESSIBILITA_COGNITIVA);
	}

	@GetMapping("/quiz")
	public List<MonitorQuizDto> getQuiz(@PathVariable Integer id) throws ForbiddenException {
		Utente utente = utenteService.findUtenteById(id);
		return monitorService.getQuizUtente(utente);
	}

}

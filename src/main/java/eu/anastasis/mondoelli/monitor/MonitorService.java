package eu.anastasis.mondoelli.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.enums.RoomChannel;
import eu.anastasis.mondoelli.enums.RoomLevel;
import eu.anastasis.mondoelli.esercizio.esplorazione.EsercizioEsplorazione;
import eu.anastasis.mondoelli.esercizio.esplorazione.EsercizioEsplorazioneRepository;
import eu.anastasis.mondoelli.esercizio.esplorazione.EsercizioEsplorazioneService;
import eu.anastasis.mondoelli.esercizio.quiz.EsercizioQuiz;
import eu.anastasis.mondoelli.esercizio.quiz.EsercizioQuizRepository;
import eu.anastasis.mondoelli.esercizio.quiz.QuizSource;
import eu.anastasis.mondoelli.esercizio.stanza.EsercizioStanza;
import eu.anastasis.mondoelli.esercizio.stanza.EsercizioStanzaRepository;
import eu.anastasis.mondoelli.esercizio.stanza.EsercizioStanzaService;
import eu.anastasis.mondoelli.monitor.dto.MonitorEsercizioStanzaDto;
import eu.anastasis.mondoelli.monitor.dto.MonitorEsplorazioneDto;
import eu.anastasis.mondoelli.monitor.dto.MonitorFunzioneCanaleDto;
import eu.anastasis.mondoelli.monitor.dto.MonitorFunzioneDto;
import eu.anastasis.mondoelli.monitor.dto.MonitorPercorsoDto;
import eu.anastasis.mondoelli.monitor.dto.MonitorQuizDto;
import eu.anastasis.mondoelli.monitor.dto.MonitorSessioneDto;
import eu.anastasis.mondoelli.quartiere.QuartiereService;
import eu.anastasis.mondoelli.sessione.Sessione;
import eu.anastasis.mondoelli.sessione.SessioneRepository;
import eu.anastasis.mondoelli.sessione.SessioneService;
import eu.anastasis.mondoelli.utente.Utente;

@Service
public class MonitorService {
	@Autowired
	SessioneService sessioneService;

	@Autowired
	EsercizioStanzaService esercizioStanzaService;

	@Autowired
	QuartiereService quartiereService;

	@Autowired
	EsercizioEsplorazioneService esercizioEsplorazioneService;

	@Autowired
	SessioneRepository sessioneRepository;

	@Autowired
	EsercizioEsplorazioneRepository esercizioEsplorazioneRepository;

	@Autowired
	EsercizioStanzaRepository esercizioStanzaRepository;

	@Autowired
	EsercizioQuizRepository esercizioQuizRepository;

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(SessioneService.class);

	public List<MonitorPercorsoDto> getPercorsoUtente(Utente utente) {
		List<MonitorPercorsoDto> res = new ArrayList<MonitorPercorsoDto>();
		List<Sessione> sessioni = utente.getSessioni();
		Quartiere corrente = quartiereService.getQuartiereCorrente(utente).getQuartiere();
		if (sessioni != null && !sessioni.isEmpty()) {
			for (Quartiere quartiere : Quartiere.values()) {
				MonitorPercorsoDto dto = getPercorsoQuartiere(utente, quartiere);
				if (utente.getQuartieriCompletati().contains(quartiere)) {
					dto.setCompletato(true);
				}
				if (dto != null) {
					dto.setCorrente(quartiere == corrente);
					res.add(dto);
				}
			}
		}
		return res;
	}

	private MonitorPercorsoDto getPercorsoQuartiere(Utente utente, Quartiere quartiere) {
		MonitorPercorsoDto dto = new MonitorPercorsoDto();
		dto.setQuartiere(quartiere);
		if (quartiere == Quartiere.INTRODUZIONE) {
			dto.setNumeroSessioni(esercizioEsplorazioneService.computeNumeroSessioniUtenteQuartiere(utente, quartiere));
		} else {
			MonitorFunzioneDto esStanzaQuartiere = getEserciziUtenteFunzioneEsecutiva(utente, quartiere);
			MonitorFunzioneCanaleDto visivo = esStanzaQuartiere.getCanaleVisivo();
			MonitorFunzioneCanaleDto verbale = esStanzaQuartiere.getCanaleUditivoVerbale();
			if (visivo.getLivelloRaggiunto() != null) {
				dto.setVisivo(visivo.getLivelloRaggiunto().name());
			}
			if (verbale.getLivelloRaggiunto() != null) {
				dto.setVerbale(verbale.getLivelloRaggiunto().name());
			}
			dto.setNumeroSessioni(esercizioStanzaService.computeNumeroSessioniUtenteQuartiere(utente, quartiere));
		}
		return dto;
	}

	public List<MonitorSessioneDto> getSessioniUtente(Utente utente) {
		List<Sessione> sessioni = sessioneRepository.findAllByUtente(utente);
		return sessioni.stream().map(this::sessioneToDto).collect(Collectors.toList());
	}

	private MonitorSessioneDto sessioneToDto(Sessione sessione) {
		MonitorSessioneDto dto = new MonitorSessioneDto();
		dto.setAccuratezzaMedia(sessione.getAccuratezzaMedia());
		dto.setCompletata(sessione.getFine() != null);
		dto.setData(sessione.getInizio());
		dto.setId(sessione.getId());
		dto.setQuartiere(sessione.getQuartiere());
		dto.setTempoNetto(sessione.getTempoNetto());
		dto.setScore(sessione.getScore());
		dto.setSteps(sessione.getSteps());
		return dto;
	}

	public List<MonitorEsplorazioneDto> getEsplorazioneUtente(Utente utente) {
		List<EsercizioEsplorazione> esercizi = esercizioEsplorazioneRepository.findAllByUtenteAsc(utente);
		return esercizi.stream().map(this::esercizioToDto).collect(Collectors.toList());
	}

	private MonitorEsplorazioneDto esercizioToDto(EsercizioEsplorazione esercizioEsplorazione) {
		MonitorEsplorazioneDto dto = new MonitorEsplorazioneDto();
		dto.setInizio(esercizioEsplorazione.getInizio());
		dto.setCompito(esercizioEsplorazione.getCompito());
		dto.setEsecuzione(esercizioEsplorazione.getEsecuzione());
		dto.setCorretto(esercizioEsplorazione.getCorretto());
		dto.setFinale(esercizioEsplorazione.getFinale());
		dto.setOmbra(esercizioEsplorazione.getOmbra());
		dto.setProspettiva(esercizioEsplorazione.getProspettiva());
		dto.setQuartiere(esercizioEsplorazione.getSessione().getQuartiere());
		dto.setTempoImpiegato(esercizioEsplorazione.getTempoImpiegato());
		return dto;
	}

	public MonitorFunzioneDto getEserciziUtenteFunzioneEsecutiva(Utente utente, Quartiere quartiere) {
		MonitorFunzioneDto dto = new MonitorFunzioneDto();
		dto.setFunzione(quartiere);
		MonitorFunzioneCanaleDto visivo = new MonitorFunzioneCanaleDto();
		MonitorFunzioneCanaleDto verbale = new MonitorFunzioneCanaleDto();
		visivo.setCanale(RoomChannel.VISIVO);
		visivo.setLivelloRaggiunto(this.getCurrentRoomLevel(utente, quartiere, RoomChannel.VISIVO));
		verbale.setCanale(RoomChannel.VERBALE);
		verbale.setLivelloRaggiunto(this.getCurrentRoomLevel(utente, quartiere, RoomChannel.VERBALE));
		List<EsercizioStanza> eserciziVisivi = esercizioStanzaRepository.findAllByUtenteAndQuartiereAndCanaleAsc(utente, quartiere,
				RoomChannel.VISIVO);
		List<EsercizioStanza> eserciziVerbali = esercizioStanzaRepository.findAllByUtenteAndQuartiereAndCanaleAsc(utente, quartiere,
				RoomChannel.VERBALE);
		List<MonitorEsercizioStanzaDto> eserciziVisiviDto = listaEserciziStanzaToDto(eserciziVisivi);
		List<MonitorEsercizioStanzaDto> eserciziVerbaliDto = listaEserciziStanzaToDto(eserciziVerbali);
		visivo.setEsercizi(eserciziVisiviDto);
		verbale.setEsercizi(eserciziVerbaliDto);
		dto.setCanaleVisivo(visivo);
		dto.setCanaleUditivoVerbale(verbale);
		return dto;
	}

	private List<MonitorEsercizioStanzaDto> listaEserciziStanzaToDto(List<EsercizioStanza> esercizi) {
		return esercizi.stream().map(this::esercizioStanzaToDto).collect(Collectors.toList());
	}

	private MonitorEsercizioStanzaDto esercizioStanzaToDto(EsercizioStanza esercizioStanza) {
		MonitorEsercizioStanzaDto dto = new MonitorEsercizioStanzaDto();
		dto.setLivello(esercizioStanza.getLivello());
		dto.setAccuratezza(esercizioStanza.getAccuratezza());
		dto.setFine(esercizioStanza.getFine());
		dto.setFeedbackAttenzione(esercizioStanza.getFeedbackAttenzione());
		dto.setNumeroStimoliCorretti(esercizioStanza.getNumeroStimoliCorretti());
		dto.setNumeroStimoliErrati(esercizioStanza.getNumeroStimoliErrati());
		dto.setNumeroStimoliSaltati(esercizioStanza.getNumeroStimoliSaltati());
		dto.setTempoEsposizioneOggetti(esercizioStanza.getTempoEsposizioneOggetti());
		dto.setTempoReazioneMedio(esercizioStanza.getTempoReazioneMedio());
		dto.setStatoLivello(esercizioStanza.getStatoLivello());
		return dto;
	}

	private RoomLevel getCurrentRoomLevel(Utente utente, Quartiere quartiere, RoomChannel canale) {
		List<EsercizioStanza> eserciziSvolti = esercizioStanzaRepository.findAllByUtenteAndQuartiereAndCanaleDesc(utente, quartiere,
				canale);
		if (eserciziSvolti == null || eserciziSvolti.isEmpty()) {
			return null;
		}
		EsercizioStanza ultimo = eserciziSvolti.get(0);
		return ultimo.getLivello();
	}

	public List<MonitorQuizDto> getQuizUtente(Utente utente) {
		List<EsercizioQuiz> quiz = esercizioQuizRepository.findAllByUtenteAsc(utente);
		return quiz.stream().map(this::quizToDto).collect(Collectors.toList());
	}

	private MonitorQuizDto quizToDto(EsercizioQuiz esercizioQuiz) {
		MonitorQuizDto dto = new MonitorQuizDto();
		QuizSource quiz = esercizioQuiz.getQuiz();
		dto.setData(esercizioQuiz.getInizio());
		dto.setQuartiere(quiz.getFunzioneEsecutiva());
		dto.setDomanda(quiz.getSituazione());
		dto.setRisposta(esercizioQuiz.getRisposta().getSituazione());
		dto.setCorretto(esercizioQuiz.getCorretto());
		dto.setTempoImpiegato(esercizioQuiz.getTempoImpiegato());
		return dto;
	}

}

package eu.anastasis.mondoelli.sessione;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import eu.anastasis.mondoelli.configuration.ConstantsConfiguration;
import eu.anastasis.mondoelli.enums.LevelStatus;
import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.enums.RoomChannel;
import eu.anastasis.mondoelli.enums.Score;
import eu.anastasis.mondoelli.enums.StepsSessione;
import eu.anastasis.mondoelli.esercizio.esplorazione.EsercizioEsplorazione;
import eu.anastasis.mondoelli.esercizio.esplorazione.EsercizioEsplorazioneService;
import eu.anastasis.mondoelli.esercizio.esplorazione.dto.RisultatoEsercizioEsplorazioneDto;
import eu.anastasis.mondoelli.esercizio.medialiteracy.EsercizioMediaLiteracy;
import eu.anastasis.mondoelli.esercizio.medialiteracy.EsercizioMediaLiteracyService;
import eu.anastasis.mondoelli.esercizio.medialiteracy.dto.RisultatoMediaLiteracyDto;
import eu.anastasis.mondoelli.esercizio.quiz.EsercizioQuiz;
import eu.anastasis.mondoelli.esercizio.quiz.QuizService;
import eu.anastasis.mondoelli.esercizio.quiz.dto.RisultatoQuizDto;
import eu.anastasis.mondoelli.esercizio.quiz.dto.ScoreDto;
import eu.anastasis.mondoelli.esercizio.stanza.EsercizioStanza;
import eu.anastasis.mondoelli.esercizio.stanza.EsercizioStanzaService;
import eu.anastasis.mondoelli.esercizio.stanza.dto.EsitoEsercizioStanzaDto;
import eu.anastasis.mondoelli.esercizio.stanza.dto.RisultatoEsercizioStanzaDto;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.quartiere.QuartiereDto;
import eu.anastasis.mondoelli.quartiere.QuartiereService;
import eu.anastasis.mondoelli.sessione.dto.DatiSessioneDto;
import eu.anastasis.mondoelli.sessione.dto.FineSessioneDto;
import eu.anastasis.mondoelli.utente.Utente;
import eu.anastasis.mondoelli.utente.UtenteService;

@Service
public class SessioneService {

	@Autowired
	UtenteService utenteService;

	@Autowired
	QuartiereService quartiereService;

	@Autowired
	ConstantsConfiguration constants;

	@Autowired
	SessioneRepository sessioneRepository;

	@Autowired
	EsercizioEsplorazioneService esercizioEsplorazioneService;

	@Autowired
	EsercizioStanzaService esercizioStanzaService;

	@Autowired
	QuizService quizService;

	@Autowired
	EsercizioMediaLiteracyService esercizioMediaLiteracyService;

	private static final Logger logger = LoggerFactory.getLogger(SessioneService.class);

	public Sessione findSessioneById(Integer id) throws NotFoundException {
		Optional<Sessione> optionalSessione = sessioneRepository.findOneById(id);
		return optionalSessione.orElseThrow(() -> new NotFoundException("Sessione " + id + " non trovata."));
	}

	public Integer computeAccuratezza(Sessione sessione) {
		int accuratezza = 0;
		int counter = 0;
		if (sessione.getEserciziStanza() != null && !sessione.getEserciziStanza().isEmpty()) {
			for (EsercizioStanza es : sessione.getEserciziStanza()) {
				if (es.getAccuratezza() != null && es.getAccuratezza() > 0) {
					accuratezza += es.getAccuratezza();
					counter++;
				}
			}
		}
		if (accuratezza > 0) {
			accuratezza = Math.round(accuratezza / counter);
		}
		return accuratezza;
	}

	public void updateScore(Sessione sessione, Score tipo) {
		int score = 0;
		if (sessione.getScore() != null && sessione.getScore() > 0) {
			score += sessione.getScore();
		}
		sessione.setScore(score + tipo.getScore());
	}

	public void updateTempoNetto(Sessione sessione) {
		int tempoNettoMinuti = 0;
		long diff = new Date().getTime() - sessione.getInizio().getTime();
		tempoNettoMinuti = (int) diff / (60 * 1000);
		sessione.setTempoNetto(tempoNettoMinuti);
	}

	public void updateSteps(Sessione sessione, StepsSessione p) {
		String steps = sessione.getSteps();
		if (steps == null) {
			steps = "";
		}
		steps += p.name();
		sessione.setSteps(steps);
	}

	private Sessione createNewSessione(Utente utente, Quartiere quartiere) {
		Sessione sessione = new Sessione();
		sessione.setInizio(new Date());
		sessione.setScore(Score.INIT_SESSIONE.getScore());
		sessione.setUtente(utente);
		sessione.setQuartiere(quartiere);
		return sessione;
	}

	@Transactional
	public DatiSessioneDto initSessione(Utente utente) {
		logger.info("Init sessione per " + utente.getUsername());
		QuartiereDto quartiereDto = quartiereService.getQuartiereCorrente(utente);
		DatiSessioneDto res = new DatiSessioneDto();
		Set<Quartiere> quartieriCompletati = utente.getQuartieriCompletati();
		Quartiere quartiere = quartiereDto.getQuartiere();
		if (quartiere == null) {
			res.setPercorsoNonIniziato(quartiereDto.getPercorsoNonIniziato());
			res.setPercorsoCompletato(quartiereDto.getPercorsoCompletato());
			return res;
		} else if (quartiere == Quartiere.INTRODUZIONE) {
			res.setTempoMassimoEsplorazione(constants.getTempoMassimoIntroduzione());
		} else {
			res.setTempoMassimoEsplorazione(constants.getTempoMassimoEsplorazione());
		}
		res.setDemo(utente.getDemo());
		res.setMediaLiteracy(utente.getPercorso().getMediaLiteracy());
		res.setQuartiere(quartiere);
		res.setQuartiereCompletato(quartieriCompletati.contains(quartiere));
		if (utente.getDemo()) {
			// Il demo non salva dati
			res.setInizio(new Date());
		} else {
			// Crea nuova sessione sul db
			Sessione sessione = createNewSessione(utente, quartiere);
			sessione = sessioneRepository.save(sessione);
			res.setId(sessione.getId());
			res.setInizio(sessione.getInizio());
		}
		res.setLivello(utente.getLivelloEsplorazione());
		res.setTessere(esercizioEsplorazioneService.computeTessere(utente));
		return res;
	}

	@Transactional
	public FineSessioneDto endSessione(Integer idSessione) {
		Sessione sessione = findSessioneById(idSessione);
		if (sessione.getFine() == null) {
			sessione.setFine(new Date());
			sessione.setAccuratezzaMedia(computeAccuratezza(sessione));
			utenteService.changeLivelloEsplorazione(sessione.getUtente(),
					computeDeltaLivelloEsplorazione(sessione));
			updateScore(sessione, Score.END_SESSION);
			sessione = sessioneRepository.save(sessione);
		} else {
			logger.warn("Tentativo di chiudere la sessione " +
					sessione.getId() + " che era già chiusa!");
		}
		FineSessioneDto res = new FineSessioneDto();
		res.setScore(sessione.getScore());
		return res;
	}

	protected int computeDeltaLivelloEsplorazione(Sessione sessione) {
		List<Boolean> esiti = sessione.getEserciziEsplorazione().stream().map(e -> {
			return e.getCorretto();
		}).collect(Collectors.toList());
		boolean allTrue = true;
		boolean allFalse = true;
		for (Boolean esito : esiti) {
			if (esito) {
				allFalse = false;
			} else {
				allTrue = false;
			}
		}
		int delta = 0;
		if (allFalse) {
			// Tutte le esplorazioni sbagliate: si scende di livello
			delta--;
		}
		if (allTrue) {
			// Tutte le esplorazioni corrette: si sale di livello
			delta++;
		}
		return delta;
	}

	@Transactional
	public void updateScore(Integer idSessione, String scoreStep) {
		Sessione sessione = findSessioneById(idSessione);
		updateScore(sessione, Score.valueOf(scoreStep));
		sessione = sessioneRepository.save(sessione);
	}

	@Transactional
	public void salvaEsercizioEsplorazione(Integer idSessione, RisultatoEsercizioEsplorazioneDto dto) {
		Sessione sessione = findSessioneById(idSessione);
		EsercizioEsplorazione esercizio = esercizioEsplorazioneService.salvaEsercizio(sessione, dto);
		sessione = esercizio.getSessione();
		updateTempoNetto(sessione);
		updateScore(sessione, Score.STEP_PERCORSO);
		updateSteps(sessione, StepsSessione.P);
		if (dto.getCorretto()) {
			updateScore(sessione, Score.STEP_PERCORSO_OK);
		}
		sessioneRepository.save(sessione);
	}

	@Transactional
	public EsitoEsercizioStanzaDto salvaEsercizioStanza(Integer idEsercizio, RisultatoEsercizioStanzaDto dto) {
		EsercizioStanza esercizio = esercizioStanzaService.salvaEsercizio(idEsercizio, dto);
		Sessione sessione = esercizio.getSessione();
		updateTempoNetto(sessione);
		updateSteps(sessione, esercizio.getCanale() == RoomChannel.VISIVO ? StepsSessione.V : StepsSessione.U);
		updateScore(sessione, Score.LIVELLO);
		if (esercizio.getStatoLivello() == LevelStatus.PASSATO) {
			updateScore(sessione, Score.LIVELLO_OK);
		}
		sessioneRepository.save(sessione);
		EsitoEsercizioStanzaDto result = new EsitoEsercizioStanzaDto();
		result.setEsito(esercizio.getStatoLivello());
		return result;
	}

	@Transactional
	public ScoreDto saveQuiz(Integer idSessione, RisultatoQuizDto dto) {
		logger.info("saveQuiz " + dto.getIdQuiz());
		Sessione sessione = findSessioneById(idSessione);
		EsercizioQuiz e = quizService.esercizioQuizFromDto(dto, sessione);
		Score score = e.getCorretto() ? Score.QUIZ_OK : Score.QUIZ;
		updateScore(sessione, score);
		updateSteps(sessione, StepsSessione.Q);
		if (sessione.getEserciziQuiz() == null) {
			sessione.setEserciziQuiz(new ArrayList<EsercizioQuiz>());
		}
		sessione.getEserciziQuiz().add(e);
		sessioneRepository.save(sessione);
		ScoreDto res = new ScoreDto();
		res.setScore(score.getScore());
		logger.info("saveQuiz returns score " + score);
		return res;
	}

	@Transactional
	public void salvaEsercizioMediaLiteracy(Integer idSessione, RisultatoMediaLiteracyDto dto) throws JsonProcessingException {
		Sessione sessione = findSessioneById(idSessione);
		EsercizioMediaLiteracy esercizio = esercizioMediaLiteracyService.salvaEsercizio(sessione, dto);
		sessione = esercizio.getSessione();
		updateTempoNetto(sessione);
		sessioneRepository.save(sessione);
	}

}

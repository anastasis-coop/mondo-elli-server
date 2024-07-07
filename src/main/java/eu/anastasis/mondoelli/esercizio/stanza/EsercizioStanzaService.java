package eu.anastasis.mondoelli.esercizio.stanza;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.anastasis.mondoelli.configuration.ConstantsConfiguration;
import eu.anastasis.mondoelli.enums.LevelStatus;
import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.enums.RoomChannel;
import eu.anastasis.mondoelli.enums.RoomLevel;
import eu.anastasis.mondoelli.esercizio.stanza.dto.EsercizioStanzaDto;
import eu.anastasis.mondoelli.esercizio.stanza.dto.RisultatoEsercizioStanzaDto;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.sessione.Sessione;
import eu.anastasis.mondoelli.utente.Utente;

@Service
public class EsercizioStanzaService {

	@Autowired
	ConstantsConfiguration config;

	@Autowired
	EsercizioStanzaRepository esercizioStanzaRepository;

	private static final Logger logger = LoggerFactory.getLogger(EsercizioStanzaService.class);

	@Transactional
	public EsercizioStanzaDto getEsercizioStanza(Sessione s, Quartiere funzioneEsecutiva, RoomChannel canale) throws NotFoundException {
		logger.info("getEsercizio");
		logger.info("funzione esecutiva " + funzioneEsecutiva);
		logger.info("canale " + canale);
		Utente u = s.getUtente();
		RoomLevel l = getNextRoomLevel(u, funzioneEsecutiva, canale, u.getFacilitato());
		EsercizioStanza e = new EsercizioStanza();
		e.setSessione(s);
		e.setInizio(new Date());
		e.setCanale(canale);
		e.setLivello(l);
		e.setStatoLivello(LevelStatus.IN_CORSO);
		e = esercizioStanzaRepository.save(e);
		EsercizioStanzaDto res = new EsercizioStanzaDto();
		res.setInizio(e.getInizio());
		res.setCanale(canale);
		res.setQuartiere(s.getQuartiere());
		res.setIdEsercizio(e.getId());
		res.setLivello(l);
		res.setFinito(getStanzaFinita(l, u, funzioneEsecutiva, canale));
		logger.info("id esercizio = " + e.getId());
		logger.info("livello = " + l);
		return res;
	}

	private Boolean getStanzaFinita(RoomLevel livello, Utente utente, Quartiere quartiere, RoomChannel canale) {
		if (livello != RoomLevel.LEVEL_32) {
			return false;
		}
		List<EsercizioStanza> eserciziSvolti = esercizioStanzaRepository.findAllByUtenteAndQuartiereAndCanaleAndLivelloDesc(utente,
				quartiere, canale, RoomLevel.LEVEL_32);
		if (!eserciziSvolti.isEmpty()) {
			EsercizioStanza ultimo = eserciziSvolti.get(0);
			return ultimo.getStatoLivello() == LevelStatus.PASSATO;
		} else {
			return false;
		}
	}

	private RoomLevel getNextRoomLevel(Utente u, Quartiere quartiere, RoomChannel canale, Boolean facilitato) {
		List<EsercizioStanza> eserciziSvolti = esercizioStanzaRepository.findAllByUtenteAndQuartiereAndCanaleDesc(u, quartiere, canale);
		if (eserciziSvolti == null || eserciziSvolti.isEmpty()) {
			return facilitato ? RoomLevel.LEVEL_01 : RoomLevel.LEVEL_11;
		}
		EsercizioStanza ultimo = eserciziSvolti.get(0);
		RoomLevel lUltimo = ultimo.getLivello();
		switch (ultimo.getStatoLivello()) {
		case PASSATO:
			return RoomLevel.getNext(lUltimo);
		case IN_CORSO:
			return lUltimo;
		case FALLITO: // supponiamo si torni indietro....
			return RoomLevel.getPrevious(lUltimo);
		default:
			return lUltimo;
		}
	}

	@Transactional
	public EsercizioStanza salvaEsercizio(Integer idEsercizio, RisultatoEsercizioStanzaDto dto) throws NotFoundException {
		logger.info("salvataggio esercizio " + idEsercizio);
		logger.info("stimoli corretti " + dto.getNumeroStimoliCorretti());
		logger.info("stimoli errati " + dto.getNumeroStimoliErrati());
		logger.info("stimoli saltati " + dto.getNumeroStimoliSaltati());
		Optional<EsercizioStanza> e = esercizioStanzaRepository.findById(idEsercizio);
		if (!e.isPresent()) {
			String msg = "Esercizio + " + idEsercizio + " non trovato.";
			logger.error(msg);
			throw new NotFoundException(msg);
		}
		Integer accuratezza = calcolaAccuratezzaLivello(dto.getNumeroStimoliCorretti(), dto.getNumeroStimoliErrati(),
				dto.getNumeroStimoliSaltati());
		EsercizioStanza esercizio = e.get();
		esercizio.setAccuratezza(accuratezza);
		esercizio.setFine(new Date());
		esercizio.setFeedbackAttenzione(dto.getFeedbackAttenzione());
		esercizio.setNumeroStimoliCorretti(dto.getNumeroStimoliCorretti());
		esercizio.setNumeroStimoliErrati(dto.getNumeroStimoliErrati());
		esercizio.setNumeroStimoliSaltati(dto.getNumeroStimoliSaltati());
		esercizio.setTempoEsposizioneOggetti(dto.getTempoEsposizioneOggetti());
		esercizio.setTempoReazioneMedio(dto.getTempoReazioneMedio());
		esercizio.setStatoLivello(computeStatoLivello(accuratezza));
		logger.info("accuratezza = " + accuratezza);
		esercizioStanzaRepository.save(esercizio);
		return esercizio;
	}

	public LevelStatus computeStatoLivello(Integer accuratezza) {
		if (accuratezza != null) {
			if (accuratezza >= config.getSogliaAccuratezzaPassato()) {
				return LevelStatus.PASSATO;
			} else if (accuratezza < config.getSogliaAccuratezzaFallito()) {
				return LevelStatus.FALLITO;
			}
			return LevelStatus.IN_CORSO;
		} else {
			return null;
		}
	}

	private Integer calcolaAccuratezzaLivello(Integer numeroStimoliCorretti, Integer numeroStimoliErrati, Integer numeroStimoliSaltati) {
		int tot = numeroStimoliCorretti + numeroStimoliErrati + numeroStimoliSaltati;
		return (int) Math.round(100d * numeroStimoliCorretti / tot);
	}

	public List<EsercizioStanza> getEserciziUtente(Utente utente) {
		List<EsercizioStanza> eserciziSvolti = esercizioStanzaRepository.findAllByUtenteDesc(utente);
		return eserciziSvolti;
	}

	public Boolean getMostraVideo(Utente utente, Quartiere quartiere) throws NotFoundException {
		List<EsercizioStanza> eserciziSvolti = esercizioStanzaRepository.findAllByUtenteAndQuartiereAsc(utente, quartiere);
		return (eserciziSvolti == null || eserciziSvolti.isEmpty());
	}

	public Integer computeNumeroSessioniUtenteQuartiere(Utente utente, Quartiere quartiere) {
		List<EsercizioStanza> eserciziVisivi = esercizioStanzaRepository.findAllByUtenteAndQuartiereAndCanaleAsc(utente, quartiere,
				RoomChannel.VISIVO);
		if (eserciziVisivi == null || eserciziVisivi.isEmpty()) {
			return 0;
		}
		int numeroSessioni = 0;
		int sessioneCorrente = 0;
		for (EsercizioStanza e : eserciziVisivi) {
			if (e.getSessione().getId() != sessioneCorrente) {
				numeroSessioni++;
				sessioneCorrente = e.getSessione().getId();
			}
		}
		return numeroSessioni;
	}

}

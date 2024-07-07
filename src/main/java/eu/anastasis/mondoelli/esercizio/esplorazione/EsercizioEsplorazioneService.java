package eu.anastasis.mondoelli.esercizio.esplorazione;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.esercizio.esplorazione.dto.EsercizioEsplorazioneDto;
import eu.anastasis.mondoelli.esercizio.esplorazione.dto.RisultatoEsercizioEsplorazioneDto;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.sessione.Sessione;
import eu.anastasis.mondoelli.utente.Utente;

@Service
public class EsercizioEsplorazioneService {

	@Autowired
	EsercizioEsplorazioneRepository esercizioEsplorazioneRepository;

	private static final Logger logger = LoggerFactory.getLogger(EsercizioEsplorazioneService.class);

	public EsercizioEsplorazioneDto inizioEsplorazione(Utente utente) {
		EsercizioEsplorazioneDto result = new EsercizioEsplorazioneDto();
		result.setInizio(new Date());
		return result;
	}

	protected Integer computeTessere(int numeroEsplorazioniCorrette) {
		if (numeroEsplorazioniCorrette < 4) {
			return 0;
		} else if (numeroEsplorazioniCorrette < 8) {
			return 1;
		} else if (numeroEsplorazioniCorrette < 15) {
			return 2;
		} else if (numeroEsplorazioniCorrette < 20) {
			return 3;
		} else if (numeroEsplorazioniCorrette < 25) {
			return 4;
		} else {
			return 5;
		}
	}

	public Integer computeTessere(Utente utente) {
		int numeroEsplorazioniCorrette = esercizioEsplorazioneRepository.countEsplorazioniCorrette(utente);
		return computeTessere(numeroEsplorazioniCorrette);
	}

	@Transactional
	public EsercizioEsplorazione salvaEsercizio(Sessione s, RisultatoEsercizioEsplorazioneDto dto) throws NotFoundException {
		logger.info("compito " + dto.getCompito());
		logger.info("esecuzione " + dto.getEsecuzione());
		logger.info("tempo " + dto.getTempoImpiegato());
		EsercizioEsplorazione esercizio = new EsercizioEsplorazione();
		if (s.getEserciziEsplorazione() == null) {
			s.setEserciziEsplorazione(new ArrayList<EsercizioEsplorazione>());
		}
		s.getEserciziEsplorazione().add(esercizio);
		esercizio.setSessione(s);
		esercizio.setInizio(dto.getInizio());
		esercizio.setCompito(dto.getCompito());
		esercizio.setEsecuzione(dto.getEsecuzione());
		esercizio.setCorretto(dto.getCorretto());
		esercizio.setFinale(dto.getFinale());
		esercizio.setOmbra(dto.getOmbra());
		esercizio.setProspettiva(dto.getProspettiva());
		esercizio.setTempoImpiegato(dto.getTempoImpiegato());
		esercizio.setFine(new Date());
		esercizio.setLivello(s.getUtente().getLivelloEsplorazione());
		return esercizioEsplorazioneRepository.save(esercizio);
	}

	public Integer computeNumeroSessioniUtenteQuartiere(Utente utente, Quartiere quartiere) {
		List<EsercizioEsplorazione> esercizi = esercizioEsplorazioneRepository.findAllByUtenteAndQuartiereAsc(utente, quartiere);
		if (esercizi == null || esercizi.isEmpty()) {
			return 0;
		}
		int numeroSessioni = 0;
		int sessioneCorrente = 0;
		for (EsercizioEsplorazione e : esercizi) {
			if (e.getSessione().getId() != sessioneCorrente) {
				numeroSessioni++;
				sessioneCorrente = e.getSessione().getId();
			}
		}
		return numeroSessioni;
	}

}

package eu.anastasis.mondoelli.esercizio.medialiteracy;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.anastasis.mondoelli.esercizio.medialiteracy.dto.EsercizioMediaLiteracyDto;
import eu.anastasis.mondoelli.esercizio.medialiteracy.dto.RisultatoMediaLiteracyDto;
import eu.anastasis.mondoelli.sessione.Sessione;
import eu.anastasis.mondoelli.utente.Utente;

@Service
public class EsercizioMediaLiteracyService {

	@Autowired
	EsercizioMediaLiteracyRepository esercizioMediaLiteracyRepository;

	public EsercizioMediaLiteracyDto inizioMediaLiteracy(Utente utente) {
		EsercizioMediaLiteracyDto result = new EsercizioMediaLiteracyDto();
		result.setInizio(new Date());
		return result;
	}

	@Transactional
	public EsercizioMediaLiteracy salvaEsercizio(Sessione s, RisultatoMediaLiteracyDto dto) throws JsonProcessingException {
		EsercizioMediaLiteracy esercizio = new EsercizioMediaLiteracy();
		if (s.getEserciziMediaLiteracy() == null) {
			s.setEserciziMediaLiteracy(new ArrayList<EsercizioMediaLiteracy>());
		}
		s.getEserciziMediaLiteracy().add(esercizio);
		esercizio.setSessione(s);
		esercizio.setInizio(dto.getInizio());
		ObjectMapper objectMapper = new ObjectMapper();
		esercizio.setJsonData(objectMapper.writeValueAsString(dto));
		return esercizioMediaLiteracyRepository.save(esercizio);
	}

}

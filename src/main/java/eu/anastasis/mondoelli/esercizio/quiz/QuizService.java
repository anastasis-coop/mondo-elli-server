package eu.anastasis.mondoelli.esercizio.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.esercizio.quiz.dto.QuizDto;
import eu.anastasis.mondoelli.esercizio.quiz.dto.RisultatoQuizDto;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.sessione.Sessione;

@Service
public class QuizService {

	@Autowired
	QuizSourceRepository quizSourceRepository;

	private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

	public QuizDto getQuiz(Quartiere fe) {
		Random ran = new Random();
		QuizDto res = new QuizDto();
		res.setFunzioneEsecutiva(fe);
		res.setSituazioni(new ArrayList<String>());
		List<QuizSource> candidates4Correct = quizSourceRepository.findAllByFunzioneEsecutiva(fe);
		// Quartieri come distrattori (tutti eccetto INTRODUZIONE e quello della risposta corretta)
		List<Quartiere> candidates4DistractorsTmp = new ArrayList<Quartiere>();
		for (Quartiere q : Quartiere.values()) {
			if (q != fe && q != Quartiere.INTRODUZIONE && q != Quartiere.MEDIA_LITERACY) {
				candidates4DistractorsTmp.add(q);
			}
		}
		logger.info("distractors collection: " + candidates4DistractorsTmp.size());
		// sono 4 -> ne dobbiamo scegliere 2
		Quartiere q1 = null;
		Quartiere q2 = null;
		while (q1 == q2) {
			Collections.shuffle(candidates4DistractorsTmp);
			q1 = candidates4DistractorsTmp.get(0);
			q2 = candidates4DistractorsTmp.get(1);
		}
		logger.info("q1: " + q1.name());
		logger.info("q2: " + q2.name());
		logger.info("size: " + candidates4Correct.size());
		int indexOfCorrect = ran.nextInt(candidates4Correct.size());
		logger.info("indexOfCorrect: " + indexOfCorrect);
		int counter = 0;
		for (QuizSource q : candidates4Correct) {
			if (counter++ == indexOfCorrect) {
				res.setId(q.getId());
				res.setCorretta(q.getSituazione());
			}
		}
		// distrattore 1: estraggo casualmente domanda
		List<QuizSource> candidates4Distractor1Quiz = quizSourceRepository.findAllByFunzioneEsecutiva(q1);
		indexOfCorrect = ran.nextInt(candidates4Distractor1Quiz.size());
		logger.info("q1: " + indexOfCorrect + " on " + candidates4Distractor1Quiz.size());
		counter = 0;
		for (QuizSource q : candidates4Distractor1Quiz) {
			if (counter++ == indexOfCorrect) {
				res.getSituazioni().add(q.getSituazione());
			}
		}
		// distrattore 2: estraggo casualmente domanda
		List<QuizSource> candidates4Distractor2Quiz = quizSourceRepository.findAllByFunzioneEsecutiva(q2);
		indexOfCorrect = ran.nextInt(candidates4Distractor2Quiz.size());
		logger.info("q2: " + indexOfCorrect + " on " + candidates4Distractor2Quiz.size());
		counter = 0;
		for (QuizSource q : candidates4Distractor2Quiz) {
			if (counter++ == indexOfCorrect) {
				res.getSituazioni().add(q.getSituazione());
			}
		}
		return res;
	}

	public EsercizioQuiz esercizioQuizFromDto(RisultatoQuizDto dto, Sessione sessione) throws NotFoundException {
		Optional<QuizSource> source = quizSourceRepository.findById(dto.getIdQuiz());
		Optional<QuizSource> answer = quizSourceRepository.findBySituazione(dto.getRisposta());
		if (source.isPresent() && answer.isPresent()) {
			QuizSource theQuiz = source.get();
			QuizSource theAnswer = answer.get();
			EsercizioQuiz e = new EsercizioQuiz();
			e.setInizio(new Date());
			e.setFine(new Date());
			e.setQuiz(theQuiz);
			e.setSessione(sessione);
			e.setRisposta(theAnswer);
			e.setTempoImpiegato(dto.getTempoImpiegato());
			e.setCorretto(dto.getRisposta().equals(theQuiz.getSituazione()));
			return e;
		} else {
			String msg = "Quiz " + dto.getIdQuiz() + " non trovato";
			logger.error(msg);
			throw new NotFoundException(msg);
		}
	}

}

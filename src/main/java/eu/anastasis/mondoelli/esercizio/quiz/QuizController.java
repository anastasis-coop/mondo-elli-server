package eu.anastasis.mondoelli.esercizio.quiz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.esercizio.quiz.dto.QuizDto;

@RestController
@RequestMapping("quiz")
public class QuizController {

	private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

	@Autowired
	QuizService quizService;

	@GetMapping("{fe}")
	public QuizDto getQuiz(@PathVariable Quartiere fe) {
		logger.info("getQuiz " + fe);
		return quizService.getQuiz(fe);
	}

}

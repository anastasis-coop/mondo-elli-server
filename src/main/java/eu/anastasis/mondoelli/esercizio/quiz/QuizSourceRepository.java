package eu.anastasis.mondoelli.esercizio.quiz;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.anastasis.mondoelli.enums.Quartiere;

public interface QuizSourceRepository extends JpaRepository<QuizSource, Integer> {

	Optional<QuizSource> findBySituazione(String situazione);

	List<QuizSource> findAllByFunzioneEsecutiva(Quartiere funzioneEsecutiva);

}

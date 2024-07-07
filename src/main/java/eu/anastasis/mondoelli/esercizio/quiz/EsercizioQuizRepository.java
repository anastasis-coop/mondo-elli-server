package eu.anastasis.mondoelli.esercizio.quiz;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eu.anastasis.mondoelli.utente.Utente;

public interface EsercizioQuizRepository extends JpaRepository<EsercizioQuiz, Integer> {

	@Query("select e from EsercizioQuiz e join e.sessione s join s.utente u where u = ?1 order by e.inizio ASC")
	List<EsercizioQuiz> findAllByUtenteAsc(Utente utente);

}

package eu.anastasis.mondoelli.sessione;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.anastasis.mondoelli.utente.Utente;

public interface SessioneRepository extends JpaRepository<Sessione, Integer> {

	Optional<Sessione> findOneById(Integer id);

	List<Sessione> findAllByUtente(Utente utente);

}

package eu.anastasis.mondoelli.utente;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<Utente, Integer> {

	Optional<Utente> findOneById(Integer id);

	Optional<Utente> findByUsernameAndPlainPassword(String username, String password);

	Page<Utente> findById(Pageable pageable, Integer id);

	Page<Utente> findAll(Specification<Utente> specification, Pageable pageable);

}

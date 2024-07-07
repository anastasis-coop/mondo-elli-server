package eu.anastasis.mondoelli.percorso;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PercorsoRepository extends JpaRepository<Percorso, Integer> {

	Optional<Percorso> findOneById(Integer id);

}

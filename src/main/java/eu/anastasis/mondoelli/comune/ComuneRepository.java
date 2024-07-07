package eu.anastasis.mondoelli.comune;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComuneRepository extends JpaRepository<Comune, Integer> {

	Optional<Comune> findById(Integer id);

	@Query("SELECT c FROM Comune c WHERE c.cityName LIKE ?1%")
	List<Comune> findAllByQuicksearch(String filter);

}

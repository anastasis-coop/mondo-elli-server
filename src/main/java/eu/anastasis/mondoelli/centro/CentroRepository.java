package eu.anastasis.mondoelli.centro;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CentroRepository extends JpaRepository<Centro, Integer> {

	Optional<Centro> findOneById(Integer id);

	Optional<Centro> findOneByCodice(String codice);

	Page<Centro> findById(Pageable pageable, Integer id);

	Page<Centro> findAll(Specification<Centro> specification, Pageable pageable);

}

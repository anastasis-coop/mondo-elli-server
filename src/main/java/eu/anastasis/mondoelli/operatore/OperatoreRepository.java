package eu.anastasis.mondoelli.operatore;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperatoreRepository extends JpaRepository<Operatore, Integer> {

	Optional<Operatore> findOneById(Integer id);

	Optional<Operatore> findByUsername(String username);

	Page<Operatore> findById(Pageable pageable, Integer id);

	Page<Operatore> findAll(Specification<Operatore> specification, Pageable pageable);

}

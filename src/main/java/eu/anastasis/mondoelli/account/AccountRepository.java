package eu.anastasis.mondoelli.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	Optional<Account> findById(Integer id);

}

package eu.anastasis.mondoelli.esercizio.esplorazione;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.utente.Utente;

public interface EsercizioEsplorazioneRepository extends JpaRepository<EsercizioEsplorazione, Integer> {

	@Query("select e from EsercizioEsplorazione e join e.sessione s join s.utente u where u = ?1 order by e.inizio ASC")
	List<EsercizioEsplorazione> findAllByUtenteAsc(Utente utente);

	@Query("select count(e.id) from EsercizioEsplorazione e join e.sessione s join s.utente u where u = ?1 and e.corretto = 1 and e.finale = 1")
	int countEsplorazioniCorrette(Utente utente);

	@Query("select e from EsercizioEsplorazione e join e.sessione s join s.utente u where u = ?1 and s.quartiere=?2 order by e.inizio ASC")
	List<EsercizioEsplorazione> findAllByUtenteAndQuartiereAsc(Utente utente, Quartiere quartiere);

}

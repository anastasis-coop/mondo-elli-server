package eu.anastasis.mondoelli.esercizio.stanza;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.enums.RoomChannel;
import eu.anastasis.mondoelli.enums.RoomLevel;
import eu.anastasis.mondoelli.utente.Utente;

public interface EsercizioStanzaRepository extends JpaRepository<EsercizioStanza, Integer> {
	@Query("select e from EsercizioStanza e join e.sessione s join s.utente u where u = ?1 order by e.inizio DESC")
	List<EsercizioStanza> findAllByUtenteDesc(Utente utente);

	@Query("select e from EsercizioStanza e join e.sessione s join s.utente u where u = ?1 and s.quartiere=?2 and e.canale =?3 order by e.inizio DESC")
	List<EsercizioStanza> findAllByUtenteAndQuartiereAndCanaleDesc(Utente utente, Quartiere quartiere, RoomChannel canale);

	@Query("select e from EsercizioStanza e join e.sessione s join s.utente u where u = ?1 and s.quartiere=?2 and e.canale =?3 and e.livello = ?4 order by e.inizio DESC")
	List<EsercizioStanza> findAllByUtenteAndQuartiereAndCanaleAndLivelloDesc(Utente utente, Quartiere quartiere, RoomChannel canale,
			RoomLevel level);

	@Query("select e from EsercizioStanza e join e.sessione s join s.utente u where u = ?1 and s.quartiere=?2 order by e.inizio ASC")
	List<EsercizioStanza> findAllByUtenteAndQuartiereAsc(Utente utente, Quartiere quartiere);

	@Query("select e from EsercizioStanza e join e.sessione s join s.utente u where u = ?1 and s.quartiere=?2 and e.canale = ?3 order by e.inizio ASC")
	List<EsercizioStanza> findAllByUtenteAndQuartiereAndCanaleAsc(Utente utente, Quartiere quartiere, RoomChannel canale);
}

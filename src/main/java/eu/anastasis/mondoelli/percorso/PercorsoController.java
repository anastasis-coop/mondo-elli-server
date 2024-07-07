package eu.anastasis.mondoelli.percorso;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.centro.CentroDto;
import eu.anastasis.mondoelli.exceptions.AlreadyExistsException;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.operatore.OperatoreDto;
import eu.anastasis.mondoelli.utente.UtenteDto;

@RestController
@RequestMapping("/percorsi")
public class PercorsoController {

	@Autowired
	PercorsoService percorsoService;

	@GetMapping("/{id}")
	public PercorsoDto getPercorso(@PathVariable Integer id) {
		return percorsoService.getPercorso(id);
	}

	@GetMapping("/{id}/centro")
	public CentroDto getCentroPercorso(@PathVariable Integer id) {
		return percorsoService.getCentroPercorso(id);
	}

	@GetMapping("/{id}/operatori")
	public List<OperatoreDto> getOperatoriPercorso(@PathVariable Integer id) {
		return percorsoService.getOperatoriPercorso(id);
	}

	@PostMapping("/{gid}/operatori/{oid}")
	public ResponseEntity<Void> addOperatoreToPercorso(@PathVariable Integer gid, @PathVariable Integer oid) {
		percorsoService.addOperatoreToPercorso(oid, gid);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{gid}/operatori/{oid}")
	public ResponseEntity<Void> removeOperatoreFromPercorso(@PathVariable Integer gid, @PathVariable Integer oid) {
		percorsoService.removeOperatoreFromPercorso(oid, gid);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}/utenti")
	public List<UtenteDto> getUtentiPercorso(@PathVariable Integer id) {
		return percorsoService.getUtentiPercorso(id);
	}

	@GetMapping("/{id}/utenti/full")
	public List<UtentePercorsoDto> getUtentiPercorsoFull(@PathVariable Integer id) {
		return percorsoService.getUtentiPercorsoFull(id);
	}

	@PostMapping("/{id}/utenti")
	public UtenteDto createUtente(@PathVariable Integer id, @RequestBody UtenteDto utente)
			throws NotFoundException, ForbiddenException, AlreadyExistsException {
		return percorsoService.createUtente(id, utente);
	}

	@PostMapping("/{id}/utenti/serie")
	public ResponseEntity<Void> createSerieUtenti(@PathVariable Integer id, @RequestBody SerieUtentiDto serieUtenti)
			throws NotFoundException, ForbiddenException, AlreadyExistsException {
		percorsoService.createSerieUtenti(id, serieUtenti);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}")
	public PercorsoDto updatePercorso(@PathVariable Integer id, @RequestBody PercorsoDto percorso) {
		return percorsoService.updatePercorso(id, percorso);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePercorso(@PathVariable Integer id) {
		percorsoService.deletePercorso(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/media-literacy/{mediaLiteracy}")
	public ResponseEntity<Void> setMediaLiteracy(@PathVariable Integer id, @PathVariable Boolean mediaLiteracy) throws NotFoundException {
		percorsoService.setMediaLiteracy(id, mediaLiteracy);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/archiviato/{archiviato}")
	public ResponseEntity<Void> setArchiviato(@PathVariable Integer id, @PathVariable Boolean archiviato) throws NotFoundException {
		percorsoService.setArchiviato(id, archiviato);
		return ResponseEntity.ok().build();
	}

}

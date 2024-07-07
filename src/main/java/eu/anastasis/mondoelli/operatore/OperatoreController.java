package eu.anastasis.mondoelli.operatore;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.centro.CentroDto;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.percorso.PercorsoDto;

@RestController
@RequestMapping("/operatori")
public class OperatoreController {

	@Autowired
	OperatoreService operatoreService;

	@GetMapping()
	public OperatoriDto getOperatori(
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String nome,
			@RequestParam(required = false) String cognome,
			@RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize) {
		if (id != null || username != null || nome != null || cognome != null) {
			return operatoreService.getOperatori(id, username, nome, cognome,
					sortOrder, pageNumber, pageSize);
		} else {
			return operatoreService.getOperatori(sortOrder, pageNumber, pageSize);
		}
	}

	@GetMapping("/{id}")
	public OperatoreDto getOperatore(@PathVariable Integer id) {
		return operatoreService.getOperatore(id);
	}

	@GetMapping("/{id}/centro")
	public CentroDto getCentroOperatore(@PathVariable Integer id) {
		return operatoreService.getCentroOperatore(id);
	}

	@GetMapping("/{id}/percorsi")
	public List<PercorsoDto> getPercorsiOperatore(@PathVariable Integer id) {
		return operatoreService.getPercorsiOperatore(id, false);
	}

	@GetMapping("/{id}/percorsi/archiviati")
	public List<PercorsoDto> getPercorsiOperatoreArchiviati(@PathVariable Integer id) {
		return operatoreService.getPercorsiOperatore(id, true);
	}

	@PostMapping("/{oid}/percorsi/{gid}")
	public ResponseEntity<Void> addOperatoreToPercorso(@PathVariable Integer oid, @PathVariable Integer gid) {
		operatoreService.addPercorsoToOperatore(gid, oid);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{oid}/percorsi/{gid}")
	public ResponseEntity<Void> removeOperatoreFromPercorso(@PathVariable Integer oid, @PathVariable Integer gid) {
		operatoreService.removePercorsoFromOperatore(gid, oid);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}")
	public OperatoreDto updateOperatore(@PathVariable Integer id, @RequestBody OperatoreDto operatore) {
		return operatoreService.updateOperatore(id, operatore);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOperatore(@PathVariable Integer id) {
		operatoreService.deleteOperatore(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/invia-mail")
	public ResponseEntity<Void> inviaMailPrimoAccesso(@PathVariable Integer id) throws ForbiddenException {
		operatoreService.inviaMailPrimoAccessoOperatore(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/{id}/assistenza")
	public ResponseEntity<Void> inviaRichiestaAssistenza(@PathVariable Integer id,
			@RequestBody RichiestaAssistenzaDto dto,
			@RequestHeader(value = "User-Agent") String userAgent)
			throws NotFoundException, IOException {
		operatoreService.inviaRichiestaAssistenza(id, dto.getMessaggio(), userAgent);
		return ResponseEntity.ok().build();
	}

}

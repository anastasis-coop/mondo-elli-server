package eu.anastasis.mondoelli.centro;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.operatore.OperatoreDto;
import eu.anastasis.mondoelli.percorso.PercorsoDto;

@RestController
@RequestMapping("/centri")
public class CentroController {

	@Autowired
	CentroService centroService;

	@GetMapping()
	public CentriDto getCentri(
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String nome,
			@RequestParam(required = false) String codice,
			@RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize) {
		if (id != null || nome != null || codice != null) {
			return centroService.getCentri(id, nome, codice, sortOrder, pageNumber, pageSize);
		} else {
			return centroService.getCentri(sortOrder, pageNumber, pageSize);
		}
	}

	@PostMapping("/exists")
	public Boolean codiceExists(@RequestBody CodiceCheckDto dto) {
		return centroService.alreadyExists(dto.getCodice());
	}

	@GetMapping("/{id}")
	public CentroDto getCentro(@PathVariable Integer id) {
		return centroService.getCentro(id);
	}

	@GetMapping("/{id}/percorsi")
	public List<PercorsoDto> getPercorsiCentro(@PathVariable Integer id) {
		return centroService.getPercorsiCentro(id);
	}

	@GetMapping("/{id}/operatori")
	public List<OperatoreDto> getOperatoriCentro(@PathVariable Integer id) {
		return centroService.getOperatoriCentro(id);
	}

	@PostMapping()
	public CentroDto createCentro(@RequestBody CentroDto centro) {
		return centroService.createCentroDto(centro);
	}

	@PostMapping("/{id}/percorsi")
	public PercorsoDto createPercorso(@PathVariable Integer id, @RequestBody PercorsoDto percorso)
			throws NotFoundException, ForbiddenException {
		return centroService.createPercorso(id, percorso);
	}

	@PostMapping("{id}/operatori")
	public OperatoreDto createOperatore(@PathVariable Integer id, @RequestBody OperatoreDto operatore) {
		return centroService.createOperatore(id, operatore);
	}

	@PutMapping("/{id}")
	public CentroDto updateCentro(@PathVariable Integer id, @RequestBody CentroDto centro) {
		return centroService.updateCentro(id, centro);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCentro(@PathVariable Integer id) {
		centroService.deleteCentro(id);
		return ResponseEntity.ok().build();
	}

}

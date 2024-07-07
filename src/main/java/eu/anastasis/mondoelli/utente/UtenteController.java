package eu.anastasis.mondoelli.utente;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.account.Account;
import eu.anastasis.mondoelli.account.AccountService;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.UnauthorizedException;
import eu.anastasis.mondoelli.percorso.PercorsoDto;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

	@Autowired
	AccountService accountService;

	@Autowired
	UtenteService utenteService;

	@GetMapping("/current")
	public UtenteDto getCurrentAccount() throws UnauthorizedException, ForbiddenException {
		Optional<Account> currentUserOpt = accountService.findLoggedAccount();
		if (currentUserOpt.isPresent()) {
			Account account = currentUserOpt.get();
			if (account.isUtente()) {
				return utenteService.getUtente(account.getId());
			} else {
				// Usato il token di un operatore
				throw new ForbiddenException();
			}
		} else {
			// Chiamata non autenticata o token scaduto
			throw new UnauthorizedException();
		}
	}

	@GetMapping()
	public UtentiDto getUtenti(
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String nickname,
			@RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize) {
		if (id != null || username != null || nickname != null) {
			return utenteService.getUtenti(id, username, nickname, sortOrder, pageNumber, pageSize);
		} else {
			return utenteService.getUtenti(sortOrder, pageNumber, pageSize);
		}
	}

	@GetMapping("/{id}")
	public UtenteDto getUtente(@PathVariable Integer id) {
		return utenteService.getUtente(id);
	}

	@GetMapping("/{id}/percorso")
	public PercorsoDto getPercorsoUtente(@PathVariable Integer id) {
		return utenteService.getPercorsoUtente(id);
	}

	@PutMapping("/{id}")
	public UtenteDto updateUtente(@PathVariable Integer id, @RequestBody UtenteDto utente) {
		return utenteService.updateUtente(id, utente);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUtente(@PathVariable Integer id) {
		utenteService.deleteUtente(id);
		return ResponseEntity.ok().build();
	}

}

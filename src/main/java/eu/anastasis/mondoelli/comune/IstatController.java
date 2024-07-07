package eu.anastasis.mondoelli.comune;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/istat")
public class IstatController {

	@Autowired
	IstatService comuneService;

	@GetMapping("/comuni")
	public List<ComuneDto> getComuni() {
		return comuneService.findAllComuni();
	}

	@GetMapping("/comuni/quicksearch/{startswith}")
	public List<ComuneDto> getComuniCheInizianoPer(@PathVariable String startswith) {
		return comuneService.findAllComuniStartingBy(startswith);
	}

}

package eu.anastasis.mondoelli.utente.parametri;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParametriService {

	@Autowired
	ParametriRepository parametriRepository;

	public ParametriDto defaultParametriDto() {
		ParametriDto result = new ParametriDto();
		result.setNomeEllo("");
		result.setAccessorio1(0);
		result.setAccessorio2(0);
		result.setAccessorio3(0);
		return result;
	}

	public ParametriDto createParametriDto(Parametri parametri) {
		ParametriDto result = new ParametriDto();
		result.setNomeEllo(parametri.getNomeEllo());
		result.setAccessorio1(parametri.getAccessorio1());
		result.setAccessorio2(parametri.getAccessorio2());
		result.setAccessorio3(parametri.getAccessorio3());
		return result;
	}

	public void fillParametriFromDto(Parametri parametri, SetParametriDto dto) {
		parametri.setNomeEllo(dto.getNomeEllo());
		parametri.setAccessorio1(dto.getAccessorio1());
		parametri.setAccessorio2(dto.getAccessorio2());
		parametri.setAccessorio3(dto.getAccessorio3());
	}

	public void fillParametriWithDefaults(Parametri parametri) {
		parametri.setNomeEllo("");
		parametri.setAccessorio1(0);
		parametri.setAccessorio2(0);
		parametri.setAccessorio3(0);
	}

}

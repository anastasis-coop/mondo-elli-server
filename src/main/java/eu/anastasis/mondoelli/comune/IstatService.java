package eu.anastasis.mondoelli.comune;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IstatService {

	@Autowired
	ComuneRepository comuneRepository;

	private static final Logger logger = LoggerFactory.getLogger(IstatService.class);

	public Optional<Comune> findComuneById(Integer id) {
		return comuneRepository.findById(id);
	}

	Comparator<ComuneDto> compareByNome = new Comparator<ComuneDto>() {
		@Override
		public int compare(ComuneDto c1, ComuneDto c2) {
			return c1.getNome().compareTo(c2.getNome());
		}
	};

	public List<ComuneDto> findAllComuni() {
		logger.debug("Get tutti comuni");
		List<Comune> comuni = comuneRepository.findAll();
		List<ComuneDto> comuniDto = new ArrayList<ComuneDto>();
		for (Comune c : comuni) {
			ComuneDto cDto = createComuneDto(c);
			comuniDto.add(cDto);
		}
		Collections.sort(comuniDto, compareByNome);
		return comuniDto;
	}

	public List<ComuneDto> findAllComuniStartingBy(String filter) {
		logger.debug("Get tutti comuni che inizianoPer");
		List<Comune> comuni = comuneRepository.findAllByQuicksearch(filter);
		List<ComuneDto> comuniDto = new ArrayList<ComuneDto>();
		for (Comune c : comuni) {
			ComuneDto cDto = createComuneDto(c);
			comuniDto.add(cDto);
		}
		Collections.sort(comuniDto, compareByNome);
		return comuniDto;
	}

	public Optional<Comune> findComuneByDto(ComuneDto comuneDto) {
		if (comuneDto == null) {
			return Optional.empty();
		} else {
			return findComuneById(comuneDto.getId());
		}
	}

	public ComuneDto createComuneDto(Comune c) {
		if (c != null) {
			ComuneDto dto = new ComuneDto();
			dto.setId(c.getId());
			dto.setNome(c.getCityName());
			dto.setTarga(c.getCityProvinceCode());
			return dto;
		} else {
			return null;
		}
	}

}

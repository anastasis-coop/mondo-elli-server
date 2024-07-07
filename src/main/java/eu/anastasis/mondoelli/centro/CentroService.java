package eu.anastasis.mondoelli.centro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.anastasis.mondoelli.comune.Comune;
import eu.anastasis.mondoelli.comune.IstatService;
import eu.anastasis.mondoelli.configuration.ConstantsConfiguration;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.operatore.OperatoreDto;
import eu.anastasis.mondoelli.operatore.OperatoreRepository;
import eu.anastasis.mondoelli.operatore.OperatoreService;
import eu.anastasis.mondoelli.percorso.PercorsoDto;
import eu.anastasis.mondoelli.percorso.PercorsoRepository;
import eu.anastasis.mondoelli.percorso.PercorsoService;
import eu.anastasis.mondoelli.security.services.AccessValidatorService;

@Service
public class CentroService {

	// private static final Logger logger = LoggerFactory.getLogger(CentroService.class);

	private OperatoreService operatoreService;
	private PercorsoService percorsoService;

	@Autowired
	public CentroService(@Lazy OperatoreService operatoreService,
			@Lazy PercorsoService percorsoService) {
		this.operatoreService = operatoreService;
		this.percorsoService = percorsoService;
	}

	@Autowired
	ConstantsConfiguration constantsConfiguration;

	@Autowired
	CentroRepository centroRepository;

	@Autowired
	PercorsoRepository percorsoRepository;

	@Autowired
	OperatoreRepository operatoreRepository;

	@Autowired
	IstatService istatService;

	@Autowired
	AccessValidatorService accessValidatorService;

	public Centro findCentroById(Integer id) throws NotFoundException {
		Optional<Centro> optionalCentro = centroRepository.findOneById(id);
		return optionalCentro.orElseThrow(() -> new NotFoundException("Centro " + id + " non trovato."));
	}

	public Centro findCentroByCodice(String codice) {
		return centroRepository.findOneByCodice(codice).orElse(null);
	}

	public boolean alreadyExists(String codice) {
		Optional<Centro> centro = centroRepository.findOneByCodice(codice);
		return centro.isPresent();
	}

	public CentriDto getCentri(String sortOrder, Integer pageNumber, Integer pageSize) {
		Pageable pageable = buildPageable(sortOrder, pageNumber, pageSize);
		Page<Centro> resultsPage = centroRepository.findAll(pageable);
		return convertToCentriDto(resultsPage);
	}

	public CentriDto getCentri(Integer id, String nome, String codice,
			String sortOrder, Integer pageNumber, Integer pageSize) {
		Pageable pageable = buildPageable(sortOrder, pageNumber, pageSize);
		Page<Centro> resultsPage = null;
		if (id != null) {
			resultsPage = centroRepository.findById(pageable, id);
		} else {
			resultsPage = centroRepository.findAll(new Specification<Centro>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Centro> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (nome != null && nome != "") {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("nome"), "%" + nome + "%")));
					}
					if (codice != null && codice != "") {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("codice"), "%" + codice + "%")));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			}, pageable);
		}
		if (resultsPage != null) {
			return convertToCentriDto(resultsPage);
		} else {
			return createEmptyCentriDto();
		}
	}

	private CentriDto createEmptyCentriDto() {
		CentriDto res = new CentriDto();
		res.setNumberOfElements(0);
		res.setTotalElements(0);
		res.setTotalPages(0);
		res.setContent(new ArrayList<CentroDto>());
		return res;
	}

	private Pageable buildPageable(String sortOrder, Integer pageNumber, Integer pageSize) {
		Integer pageNumberParam = (pageNumber == null) ? 0 : pageNumber;
		Integer pageSizeParam = (pageSize == null) ? 50 : pageSize;

		Sort sortParam = Sort.by("id");
		if ("asc".equalsIgnoreCase(sortOrder)) {
			sortParam = sortParam.ascending();
		} else if ("desc".equals(sortOrder)) {
			sortParam = sortParam.descending();
		}

		Pageable pageable = PageRequest.of(pageNumberParam, pageSizeParam, sortParam);
		return pageable;
	}

	private CentriDto convertToCentriDto(Page<Centro> resultsPage) {
		CentriDto result = new CentriDto();
		result.setNumberOfElements(resultsPage.getNumberOfElements());
		result.setTotalPages(resultsPage.getTotalPages());
		result.setTotalElements(resultsPage.getTotalElements());
		result.setContent(resultsPage.getContent().stream()
				.map(this::convertToCentroDto)
				.collect(Collectors.toList()));
		return result;
	}

	public CentroDto convertToCentroDto(Centro centro) {
		CentroDto dto = new CentroDto();
		dto.setId(centro.getId());
		dto.setNome(centro.getNome());
		dto.setCodice(centro.getCodice());
		Comune comune = centro.getComune();
		if (comune != null) {
			dto.setComune(istatService.createComuneDto(comune));
		}
		return dto;
	}

	private void fillFromCentroDto(Centro centro, CentroDto dto) {
		centro.setNome(dto.getNome());
		centro.setCodice(dto.getCodice());
		Optional<Comune> comune = istatService.findComuneByDto(dto.getComune());
		if (comune.isPresent()) {
			centro.setComune(comune.get());
		}
	}

	public CentroDto getCentro(Integer id) {
		Centro centro = findCentroById(id);
		return convertToCentroDto(centro);
	}

	@Transactional
	public Centro createCentro(CentroDto dto) {
		Centro centro = new Centro();
		fillFromCentroDto(centro, dto);
		centro = centroRepository.save(centro);
		return centro;
	}

	public CentroDto createCentroDto(CentroDto dto) {
		return convertToCentroDto(createCentro(dto));
	}

	@Transactional
	public CentroDto updateCentro(Centro centro, CentroDto dto) {
		fillFromCentroDto(centro, dto);
		centro = centroRepository.save(centro);
		return convertToCentroDto(centro);
	}

	public CentroDto updateCentro(Integer id, CentroDto dto) {
		return updateCentro(findCentroById(id), dto);
	}

	public void deleteCentro(Integer id) {
		centroRepository.deleteById(id);
	}

	public List<PercorsoDto> getPercorsiCentro(Centro centro) {
		return centro.getPercorsi().stream()
				.map(percorsoService::convertToPercorsoDto)
				.collect(Collectors.toList());
	}

	public List<PercorsoDto> getPercorsiCentro(Integer id) {
		return getPercorsiCentro(findCentroById(id));
	}

	public List<OperatoreDto> getOperatoriCentro(Centro centro) {
		return centro.getOperatori().stream()
				.map(operatoreService::convertToOperatoreDto)
				.collect(Collectors.toList());
	}

	public List<OperatoreDto> getOperatoriCentro(Integer id) {
		return getOperatoriCentro(findCentroById(id));
	}

	@Transactional
	public PercorsoDto createPercorso(Centro centro, PercorsoDto dto) {
		return percorsoService.createPercorso(centro, dto);
	}

	public PercorsoDto createPercorso(Integer id, PercorsoDto percorso) throws NotFoundException, ForbiddenException {
		return createPercorso(findCentroById(id), percorso);
	}

	public OperatoreDto createOperatore(Centro centro, OperatoreDto dto) {
		return operatoreService.createOperatore(centro, dto);
	}

	public OperatoreDto createOperatore(Integer id, OperatoreDto dto) {
		return createOperatore(findCentroById(id), dto);
	}

}

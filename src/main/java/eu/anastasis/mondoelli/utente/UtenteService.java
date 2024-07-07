package eu.anastasis.mondoelli.utente;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.anastasis.mondoelli.account.Account;
import eu.anastasis.mondoelli.account.AccountService;
import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.exceptions.AlreadyExistsException;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.percorso.Percorso;
import eu.anastasis.mondoelli.percorso.PercorsoDto;
import eu.anastasis.mondoelli.percorso.PercorsoService;
import eu.anastasis.mondoelli.utente.parametri.Parametri;
import eu.anastasis.mondoelli.utente.parametri.ParametriService;

@Service
public class UtenteService {

	@Autowired
	AccountService accountService;

	@Autowired
	ParametriService parametriService;

	@Autowired
	PercorsoService percorsoService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UtenteRepository utenteRepository;

	public Utente findUtenteById(Integer id) throws NotFoundException {
		Optional<Utente> optionalUtente = utenteRepository.findOneById(id);
		return optionalUtente.orElseThrow(() -> new NotFoundException("Utente " + id + " non trovato."));
	}

	public Utente findLoggedUtente() throws ForbiddenException {
		Optional<Account> optionalAccount = accountService.findLoggedAccount();
		if (optionalAccount.isPresent()) {
			return findUtenteById(optionalAccount.get().getId());
		} else {
			throw new ForbiddenException();
		}
	}

	private boolean utenteAlreadyExists(String username, String password) {
		return utenteRepository.findByUsernameAndPlainPassword(username, password).isPresent();
	}

	public UtentiDto getUtenti(String sortOrder, Integer pageNumber, Integer pageSize) {
		Pageable pageable = buildPageable(sortOrder, pageNumber, pageSize);
		Page<Utente> resultsPage = utenteRepository.findAll(pageable);
		return convertToUtentiDto(resultsPage);
	}

	public UtentiDto getUtenti(Integer id, String username, String nickname,
			String sortOrder, Integer pageNumber, Integer pageSize) {
		Pageable pageable = buildPageable(sortOrder, pageNumber, pageSize);
		Page<Utente> resultsPage = null;
		if (id != null) {
			resultsPage = utenteRepository.findById(pageable, id);
		} else {
			resultsPage = utenteRepository.findAll(new Specification<Utente>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Utente> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (username != null && username != "") {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("username"), "%" + username + "%")));
					}
					if (nickname != null && nickname != "") {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("nickname"), "%" + nickname + "%")));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			}, pageable);
		}
		if (resultsPage != null) {
			return convertToUtentiDto(resultsPage);
		} else {
			return createEmptyUtentiDto();
		}
	}

	private UtentiDto createEmptyUtentiDto() {
		UtentiDto res = new UtentiDto();
		res.setNumberOfElements(0);
		res.setTotalElements(0);
		res.setTotalPages(0);
		res.setContent(new ArrayList<UtenteDto>());
		return res;
	}

	private Pageable buildPageable(String sortOrder, Integer pageNumber, Integer pageSize) {
		Integer pageNumberParam = (pageNumber == null) ? 0 : pageNumber;
		Integer pageSizeParam = (pageSize == null) ? 50 : pageSize;

		Sort sortParam = Sort.by("username");
		if ("asc".equalsIgnoreCase(sortOrder)) {
			sortParam = sortParam.ascending();
		} else if ("desc".equals(sortOrder)) {
			sortParam = sortParam.descending();
		}

		Pageable pageable = PageRequest.of(pageNumberParam, pageSizeParam, sortParam);
		return pageable;
	}

	private UtentiDto convertToUtentiDto(Page<Utente> resultsPage) {
		UtentiDto result = new UtentiDto();
		result.setNumberOfElements(resultsPage.getNumberOfElements());
		result.setTotalPages(resultsPage.getTotalPages());
		result.setTotalElements(resultsPage.getTotalElements());
		result.setContent(resultsPage.getContent().stream()
				.map(this::convertToUtenteDto)
				.collect(Collectors.toList()));
		return result;
	}

	public UtenteDto convertToUtenteDto(Utente utente) {
		UtenteDto dto = new UtenteDto();
		dto.setId(utente.getId());
		dto.setUsername(utente.getUsername());
		dto.setPassword(utente.getPlainPassword());
		dto.setFacilitato(utente.getFacilitato());
		dto.setNomeEllo(utente.getParametri().getNomeEllo());
		return dto;
	}

	private void fillFromUtenteDto(Utente utente, UtenteDto dto) {
		utente.setUsername(dto.getUsername());
		utente.setFacilitato(dto.getFacilitato());
	}

	public UtenteDto getUtente(Integer id) {
		Utente utente = findUtenteById(id);
		return convertToUtenteDto(utente);
	}

	private void setPasswordUtente(Utente utente, String password) {
		utente.setPlainPassword(password);
		utente.getAccount().setPassword(passwordEncoder.encode(password));
		utente.getAccount().setDataCreazionePassword(new Date());
	}

	@Transactional
	public UtenteDto createUtente(Percorso percorso, UtenteDto dto) throws AlreadyExistsException {
		if (utenteAlreadyExists(dto.getUsername(), dto.getPassword())) {
			throw new AlreadyExistsException();
		} else {
			Account account = accountService.createNewAccount();
			Utente utente = new Utente();
			Parametri parametri = new Parametri();
			parametriService.fillParametriWithDefaults(parametri);
			account.setUtente(utente);
			utente.setId(account.getId());
			utente.setAccount(account);
			utente.setParametri(parametri);
			utente.setPercorso(percorso);
			utente.setDemo(false);
			utente.setLivelloEsplorazione(0);
			setPasswordUtente(utente, dto.getPassword());
			fillFromUtenteDto(utente, dto);
			utente = utenteRepository.save(utente);
			return convertToUtenteDto(utente);
		}
	}

	@Transactional
	public UtenteDto updateUtente(Utente utente, UtenteDto dto) {
		if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
			setPasswordUtente(utente, dto.getPassword());
		}
		fillFromUtenteDto(utente, dto);
		utente = utenteRepository.save(utente);
		return convertToUtenteDto(utente);
	}

	public UtenteDto updateUtente(Integer id, UtenteDto dto) {
		return updateUtente(findUtenteById(id), dto);
	}

	public void deleteUtente(Integer id) {
		utenteRepository.deleteById(id);
	}

	public PercorsoDto getPercorsoUtente(Utente utente) {
		return percorsoService.convertToPercorsoDto(utente.getPercorso());
	}

	public PercorsoDto getPercorsoUtente(Integer id) {
		return getPercorsoUtente(findUtenteById(id));
	}

	@Transactional
	public void quartiereCompletato(Utente utente, Quartiere quartiere) {
		Set<Quartiere> quartieriCompletati = utente.getQuartieriCompletati();
		if (!quartieriCompletati.contains(quartiere)) {
			quartieriCompletati.add(quartiere);
			utente.setQuartieriCompletati(quartieriCompletati);
			utenteRepository.save(utente);
		}
	}

	@Transactional
	public void resetQuartieri(Utente utente) {
		utente.setQuartieriCompletati(new HashSet<Quartiere>());
		utenteRepository.save(utente);
	}

	@Transactional
	public void changeLivelloEsplorazione(Utente utente, int delta) {
		if (delta != 0) {
			utente.setLivelloEsplorazione(Math.max(0, Math.min(3, utente.getLivelloEsplorazione() + delta)));
			utenteRepository.save(utente);
		}
	}

}

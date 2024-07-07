package eu.anastasis.mondoelli.operatore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.anastasis.mondoelli.account.Account;
import eu.anastasis.mondoelli.account.AccountService;
import eu.anastasis.mondoelli.account.Role;
import eu.anastasis.mondoelli.centro.Centro;
import eu.anastasis.mondoelli.centro.CentroDto;
import eu.anastasis.mondoelli.centro.CentroService;
import eu.anastasis.mondoelli.configuration.AppConfiguration;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.percorso.Percorso;
import eu.anastasis.mondoelli.percorso.PercorsoDto;
import eu.anastasis.mondoelli.percorso.PercorsoService;
import eu.anastasis.mondoelli.security.services.PasswordTokenService;
import eu.anastasis.mondoelli.utils.EmailDto;
import eu.anastasis.mondoelli.utils.EmailGeneratorService;
import eu.anastasis.mondoelli.utils.MailService;

@Service
public class OperatoreService {

	private static final Logger logger = LoggerFactory.getLogger(OperatoreService.class);

	private static final String EMAIL_PRIMO_ACCESSO_REFERENTE = "email_primo_accesso_referente";
	private static final String EMAIL_PRIMO_ACCESSO_OPERATORE = "email_primo_accesso_operatore";
	private static final String EMAIL_MODIFICA_PASSWORD_OPERATORE = "email_modifica_password_operatore";
	private static final String EMAIL_ASSISTENZA = "email_assistenza";

	@Autowired
	AccountService accountService;

	@Autowired
	CentroService centroService;

	@Autowired
	PercorsoService percorsoService;

	@Autowired
	AppConfiguration appConfiguration;

	@Autowired
	PasswordTokenService passwordTokenService;

	@Autowired
	EmailGeneratorService emailGeneratorService;

	@Autowired
	MailService mailService;

	@Autowired
	OperatoreRepository operatoreRepository;

	public Operatore findOperatoreById(Integer id) throws NotFoundException {
		Optional<Operatore> optionalOperatore = operatoreRepository.findOneById(id);
		return optionalOperatore.orElseThrow(() -> new NotFoundException("Operatore " + id + " non trovato."));
	}

	public Optional<Operatore> findLoggedOperatore() {
		Optional<Account> optionalAccount = accountService.findLoggedAccount();
		if (optionalAccount.isPresent() && optionalAccount.get().isOperatore()) {
			return Optional.of(optionalAccount.get().getOperatore());
		} else {
			return Optional.empty();
		}
	}

	public OperatoriDto getOperatori(String sortOrder, Integer pageNumber, Integer pageSize) {
		Pageable pageable = buildPageable(sortOrder, pageNumber, pageSize);
		Page<Operatore> resultsPage = operatoreRepository.findAll(pageable);
		return convertToOperatoriDto(resultsPage);
	}

	public OperatoriDto getOperatori(Integer id, String username, String nome, String cognome,
			String sortOrder, Integer pageNumber, Integer pageSize) {
		Pageable pageable = buildPageable(sortOrder, pageNumber, pageSize);
		Page<Operatore> resultsPage = null;
		if (id != null) {
			resultsPage = operatoreRepository.findById(pageable, id);
		} else {
			resultsPage = operatoreRepository.findAll(new Specification<Operatore>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Operatore> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (username != null && username != "") {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("username"), "%" + username + "%")));
					}
					if (nome != null && nome != "") {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("nome"), "%" + nome + "%")));
					}
					if (cognome != null && cognome != "") {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("cognome"), "%" + cognome + "%")));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			}, pageable);
		}
		if (resultsPage != null) {
			return convertToOperatoriDto(resultsPage);
		} else {
			return createEmptyOperatoriDto();
		}
	}

	private OperatoriDto createEmptyOperatoriDto() {
		OperatoriDto res = new OperatoriDto();
		res.setNumberOfElements(0);
		res.setTotalElements(0);
		res.setTotalPages(0);
		res.setContent(new ArrayList<OperatoreDto>());
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

	private OperatoriDto convertToOperatoriDto(Page<Operatore> resultsPage) {
		OperatoriDto result = new OperatoriDto();
		result.setNumberOfElements(resultsPage.getNumberOfElements());
		result.setTotalPages(resultsPage.getTotalPages());
		result.setTotalElements(resultsPage.getTotalElements());
		result.setContent(resultsPage.getContent().stream()
				.map(this::convertToOperatoreDto)
				.collect(Collectors.toList()));
		return result;
	}

	public OperatoreDto convertToOperatoreDto(Operatore operatore) {
		OperatoreDto dto = new OperatoreDto();
		dto.setId(operatore.getId());
		dto.setUsername(operatore.getUsername());
		dto.setNome(operatore.getNome());
		dto.setCognome(operatore.getCognome());
		dto.setReferente(operatore.getReferente());
		dto.setDataCreazionePassword(operatore.getAccount().getDataCreazionePassword());
		return dto;
	}

	private void fillFromOperatoreDto(Operatore operatore, OperatoreDto dto) {
		operatore.setUsername(dto.getUsername());
		operatore.setNome(dto.getNome());
		operatore.setCognome(dto.getCognome());
		operatore.setReferente(dto.getReferente());
	}

	public OperatoreDto getOperatore(Integer id) {
		Operatore operatore = findOperatoreById(id);
		return convertToOperatoreDto(operatore);
	}

	@Transactional
	public OperatoreDto createOperatore(Centro centro, OperatoreDto dto) {
		Set<Role> ruoli = new HashSet<Role>();
		ruoli.add(Role.ROLE_OPERATORE);
		Account account = accountService.createNewAccount(ruoli);
		Operatore operatore = new Operatore();
		account.setOperatore(operatore);
		operatore.setId(account.getId());
		operatore.setAccount(account);
		operatore.setCentro(centro);
		fillFromOperatoreDto(operatore, dto);
		operatore = operatoreRepository.save(operatore);
		return convertToOperatoreDto(operatore);
	}

	@Transactional
	public OperatoreDto updateOperatore(Operatore operatore, OperatoreDto dto) {
		fillFromOperatoreDto(operatore, dto);
		operatore = operatoreRepository.save(operatore);
		return convertToOperatoreDto(operatore);
	}

	public OperatoreDto updateOperatore(Integer id, OperatoreDto dto) {
		return updateOperatore(findOperatoreById(id), dto);
	}

	public void deleteOperatore(Integer id) {
		operatoreRepository.deleteById(id);
	}

	public CentroDto getCentroOperatore(Operatore operatore) {
		return centroService.convertToCentroDto(operatore.getCentro());
	}

	public CentroDto getCentroOperatore(Integer id) {
		return getCentroOperatore(findOperatoreById(id));
	}

	public List<PercorsoDto> getPercorsiOperatore(Operatore operatore, boolean archivio) {
		return operatore.getPercorsi().stream()
				.filter(p -> p.getArchiviato() == archivio)
				.map(percorsoService::convertToPercorsoDto)
				.collect(Collectors.toList());
	}

	public List<PercorsoDto> getPercorsiOperatore(Integer id, boolean archivio) {
		return getPercorsiOperatore(findOperatoreById(id), archivio);
	}

	@Transactional
	public void addPercorsoToOperatore(Percorso percorso, Operatore operatore) {
		if (!operatore.getPercorsi().contains(percorso)) {
			operatore.getPercorsi().add(percorso);
			operatoreRepository.save(operatore);
		}
	}

	public void addPercorsoToOperatore(Integer gid, Integer oid) {
		addPercorsoToOperatore(percorsoService.findPercorsoById(gid), findOperatoreById(oid));
	}

	@Transactional
	public void removePercorsoFromOperatore(Percorso percorso, Operatore operatore) {
		if (operatore.getPercorsi().contains(percorso)) {
			operatore.getPercorsi().remove(percorso);
			operatoreRepository.save(operatore);
		}
	}

	public void removePercorsoFromOperatore(Integer gid, Integer oid) {
		removePercorsoFromOperatore(percorsoService.findPercorsoById(gid), findOperatoreById(oid));
	}

	private void inviaMailAdOperatore(Operatore operatore, String template) {
		inviaMailAdOperatore(operatore, null, template);
	}

	private void inviaMailAdOperatore(Operatore operatore, Operatore referente, String template) {
		Integer idOperatore = operatore.getId();
		String token = passwordTokenService.getTokenForIdAndDate(idOperatore, operatore.getAccount().getDataCreazionePassword());
		String tokenAccessLink = appConfiguration.getClientAppUrl() + "/auth/token-login/" + token;

		Map<String, Object> model = new HashMap<>();
		model.put("operatore", operatore.getNome() + " " + operatore.getCognome());
		model.put("ruolo", operatore.getReferente() ? "referente" : "operatore");
		if (referente != null) {
			model.put("referente", referente.getNome() + " " + referente.getCognome());
		}
		model.put("tokenAccessLink", tokenAccessLink);
		model.put("linkClientApp", appConfiguration.getClientAppUrl());

		String emailSubject = emailGeneratorService.createSubject(template, model);
		String emailBody = emailGeneratorService.createBody(template, model);

		EmailDto emailDto = new EmailDto();
		emailDto.setEmailSubject(emailSubject);
		emailDto.setEmailBody(emailBody);
		emailDto.addEmailTo(operatore.getUsername());
		emailDto.setPrivateContent(false);

		boolean mailInviata = mailService.send(emailDto);

		if (mailInviata) {
			logger.info("Inviata mail " + template + " a (" + operatore.getUsername() + ")");
		} else {
			logger.error("Impossibile inviare la mail " + template + " a (" + operatore.getUsername() + ")");
		}
	}

	public void inviaMailPrimoAccessoDopoOrdine(Integer id) {
		inviaMailAdOperatore(findOperatoreById(id), null, EMAIL_PRIMO_ACCESSO_REFERENTE);
	}

	public void inviaMailPrimoAccessoReferente(Integer id) {
		inviaMailAdOperatore(findOperatoreById(id), null, EMAIL_PRIMO_ACCESSO_REFERENTE);
	}

	public void inviaMailPrimoAccessoOperatore(Integer id) throws ForbiddenException {
		Account logged = accountService.findLoggedAccount().orElseThrow(() -> new ForbiddenException());
		if (logged.isOperatore()) {
			if (logged.getRuoli().contains(Role.ROLE_ADMIN)) {
				// Se creato da admin manda la mail senza riferimenti all'ordine e a chi ha creato l'account
				inviaMailPrimoAccessoReferente(id);
			} else {
				inviaMailAdOperatore(findOperatoreById(id), logged.getOperatore(), EMAIL_PRIMO_ACCESSO_OPERATORE);
			}
		} else {
			throw new ForbiddenException();
		}
	}

	public void inviaMailModificaPassword(Operatore operatore) {
		inviaMailAdOperatore(operatore, EMAIL_MODIFICA_PASSWORD_OPERATORE);
	}

	public void inviaRichiestaAssistenza(Integer id, String messaggio, String userAgent) throws NotFoundException, IOException {
		inviaMailAssistenza(findOperatoreById(id), messaggio, userAgent);
	}

	public void inviaMailAssistenza(Operatore operatore, String contenuto, String userAgent) throws IOException {
		Map<String, Object> model = new HashMap<>();

		model.put("oggetto", "Richiesta di assistenza");
		model.put("nomeUtente", operatore.getNome() + " " + operatore.getCognome());
		model.put("emailUtente", operatore.getUsername());
		model.put("nomeCentro", operatore.getCentro().getNome());
		model.put("contenuto", contenuto);
		model.put("userAgent", userAgent);

		String emailSubject = emailGeneratorService.createSubject(EMAIL_ASSISTENZA, model);
		String emailBody = emailGeneratorService.createBody(EMAIL_ASSISTENZA, model);

		EmailDto emailDto = new EmailDto();
		emailDto.setEmailSubject(emailSubject);
		emailDto.setEmailBody(emailBody);
		emailDto.addEmailTo(appConfiguration.getEmailAssistenza());
		emailDto.setReplyTo(operatore.getUsername());

		boolean mailInviata = mailService.send(emailDto);

		if (!mailInviata) {
			logger.error("Impossibile inviare la mail da parte dello specialista a ("
					+ appConfiguration.getEmailAssistenza() + ")");
		}
	}

	public void recoverPassword(String username) {
		Operatore operatore = operatoreRepository.findByUsername(username)
				.orElseThrow(NotFoundException::new);
		inviaMailModificaPassword(operatore);
	}

}

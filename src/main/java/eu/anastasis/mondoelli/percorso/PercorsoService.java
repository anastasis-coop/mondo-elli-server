package eu.anastasis.mondoelli.percorso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.anastasis.mondoelli.centro.Centro;
import eu.anastasis.mondoelli.centro.CentroDto;
import eu.anastasis.mondoelli.centro.CentroService;
import eu.anastasis.mondoelli.configuration.ConstantsConfiguration;
import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.enums.StatoPercorso;
import eu.anastasis.mondoelli.enums.TipoPercorso;
import eu.anastasis.mondoelli.exceptions.AlreadyExistsException;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.operatore.Operatore;
import eu.anastasis.mondoelli.operatore.OperatoreDto;
import eu.anastasis.mondoelli.operatore.OperatoreService;
import eu.anastasis.mondoelli.quartiere.QuartiereService;
import eu.anastasis.mondoelli.sessione.Sessione;
import eu.anastasis.mondoelli.utente.Utente;
import eu.anastasis.mondoelli.utente.UtenteDto;
import eu.anastasis.mondoelli.utente.UtenteRepository;
import eu.anastasis.mondoelli.utente.UtenteService;

@Service
public class PercorsoService {

	@Autowired
	CentroService centroService;

	private OperatoreService operatoreService;
	private UtenteService utenteService;

	public PercorsoService(@Lazy OperatoreService operatoreService,
			@Lazy UtenteService utenteService) {
		this.operatoreService = operatoreService;
		this.utenteService = utenteService;
	}

	@Autowired
	ConstantsConfiguration constants;

	@Autowired
	QuartiereService quartiereService;

	@Autowired
	PercorsoRepository percorsoRepository;

	@Autowired
	UtenteRepository utenteRepository;

	public Percorso findPercorsoById(Integer id) throws NotFoundException {
		Optional<Percorso> optionalPercorso = percorsoRepository.findOneById(id);
		return optionalPercorso.orElseThrow(() -> new NotFoundException("Percorso " + id + " non trovato."));
	}

	private void fillFromPercorso(PercorsoDto dto, Percorso percorso) {
		dto.setId(percorso.getId());
		dto.setTipo(percorso.getTipo());
		dto.setNome(percorso.getNome());
		dto.setInizioPercorso(percorso.getInizioPercorso());
		dto.setInizioPercorsoEffettivo(percorso.getInizioPercorsoEffettivo());
		dto.setDurataFunzioneEsecutivaGiorni(percorso.getDurataFunzioneEsecutivaGiorni());
		dto.setPeriodoIntroduzione(percorso.getPeriodoIntroduzione());
		dto.setMediaLiteracy(percorso.getMediaLiteracy());
		dto.setArchiviato(percorso.getArchiviato());
	}

	private void fillFromPercorsoDto(Percorso percorso, PercorsoDto dto) {
		percorso.setTipo(dto.getTipo());
		percorso.setNome(dto.getNome());
		percorso.setInizioPercorso(dto.getInizioPercorso());
		percorso.setInizioPercorsoEffettivo(dto.getInizioPercorsoEffettivo());
		percorso.setDurataFunzioneEsecutivaGiorni(dto.getDurataFunzioneEsecutivaGiorni());
		percorso.setPeriodoIntroduzione(dto.getPeriodoIntroduzione());
		percorso.setMediaLiteracy(dto.getMediaLiteracy());
	}

	public PercorsoDto convertToPercorsoDto(Percorso percorso) {
		PercorsoDto dto = new PercorsoDto();
		fillFromPercorso(dto, percorso);
		if (percorso.getOperatori() != null) {
			dto.setNumeroOperatori(percorso.getOperatori().size());
		} else {
			dto.setNumeroOperatori(0);
		}
		if (percorso.getUtenti() != null) {
			dto.setNumeroUtenti(percorso.getUtenti().size());
		} else {
			dto.setNumeroUtenti(0);
		}
		dto.setFinePercorso(computeFinePercorso(percorso));
		dto.setQuartiere(quartiereService.getQuartiereCorrente(percorso).getQuartiere());
		dto.setStato(computeStatoPercorso(percorso.getInizioPercorsoEffettivo(),
				dto.getFinePercorso()));
		return dto;
	}

	public PercorsoDto getPercorso(Integer id) {
		Percorso percorso = findPercorsoById(id);
		return convertToPercorsoDto(percorso);
	}

	@Transactional
	public PercorsoDto createPercorso(Centro centro, PercorsoDto dto) {
		Percorso percorso = new Percorso();
		percorso.setCentro(centro);
		percorso.setArchiviato(false);
		fillFromPercorsoDto(percorso, dto);
		percorso = percorsoRepository.save(percorso);
		return convertToPercorsoDto(percorso);
	}

	@Transactional
	public PercorsoDto updatePercorso(Percorso percorso, PercorsoDto dto) {
		fillFromPercorsoDto(percorso, dto);
		percorso = percorsoRepository.save(percorso);
		return convertToPercorsoDto(percorso);
	}

	public PercorsoDto updatePercorso(Integer id, PercorsoDto dto) {
		return updatePercorso(findPercorsoById(id), dto);
	}

	public void deletePercorso(Integer id) {
		percorsoRepository.deleteById(id);
	}

	public CentroDto getCentroPercorso(Percorso percorso) {
		return centroService.convertToCentroDto(percorso.getCentro());
	}

	public CentroDto getCentroPercorso(Integer id) {
		return getCentroPercorso(findPercorsoById(id));
	}

	public List<OperatoreDto> getOperatoriPercorso(Percorso percorso) {
		return percorso.getOperatori().stream()
				.map(operatoreService::convertToOperatoreDto)
				.collect(Collectors.toList());
	}

	public List<OperatoreDto> getOperatoriPercorso(Integer id) {
		return getOperatoriPercorso(findPercorsoById(id));
	}

	public List<UtenteDto> getUtentiPercorso(Percorso percorso) {
		List<UtenteDto> utenti = percorso.getUtenti().stream()
				.map(utenteService::convertToUtenteDto)
				.collect(Collectors.toList());
		if (percorso.getTipo() == TipoPercorso.GRUPPO) {
			marcaUtenteOperatore(utenti);
		}
		return utenti;
	}

	private void marcaUtenteOperatore(List<UtenteDto> utenti) {
		// L'utente operatore è quello creato per primo, e quindi con id più basso
		UtenteDto utenteOperatore = utenti.stream()
				.min((c1, c2) -> c1.getId().compareTo(c2.getId()))
				.orElse(null);
		if (utenteOperatore != null) {
			utenteOperatore.setUtenteOperatore(true);
		}
	}

	public List<UtenteDto> getUtentiPercorso(Integer id) {
		return getUtentiPercorso(findPercorsoById(id));
	}

	public List<UtentePercorsoDto> getUtentiPercorsoFull(Percorso percorso) {
		List<UtentePercorsoDto> utenti = percorso.getUtenti().stream()
				.map(this::convertToUtentePercorsoDto)
				.collect(Collectors.toList());
		if (percorso.getTipo() == TipoPercorso.GRUPPO) {
			marcaUtenteOperatoreFull(utenti);
		}
		return utenti;
	}

	private void marcaUtenteOperatoreFull(List<UtentePercorsoDto> utenti) {
		// L'utente operatore è quello creato per primo, e quindi con id più basso
		UtenteDto utenteOperatore = utenti.stream()
				.min((c1, c2) -> c1.getId().compareTo(c2.getId()))
				.orElse(null);
		if (utenteOperatore != null) {
			utenteOperatore.setUtenteOperatore(true);
		}
	}

	private UtentePercorsoDto convertToUtentePercorsoDto(Utente utente) {
		UtentePercorsoDto dto = new UtentePercorsoDto();
		dto.setId(utente.getId());
		dto.setUsername(utente.getUsername());
		dto.setPassword(utente.getPlainPassword());
		dto.setFacilitato(utente.getFacilitato());
		dto.setNomeEllo(utente.getParametri().getNomeEllo());
		List<Sessione> sessioni = utente.getSessioni();
		if (sessioni != null && sessioni.size() > 0) {
			dto.setPrimaSessione(sessioni.get(sessioni.size() - 1).getInizio());
		}
		dto.setQuartiere(quartiereService.getQuartiereCorrente(utente).getQuartiere());
		return dto;
	}

	public List<UtentePercorsoDto> getUtentiPercorsoFull(Integer id) {
		return getUtentiPercorsoFull(findPercorsoById(id));
	}

	@Transactional
	public UtenteDto createUtente(Percorso percorso, UtenteDto dto) throws AlreadyExistsException {
		return utenteService.createUtente(percorso, dto);
	}

	public UtenteDto createUtente(Integer id, UtenteDto dto) throws NotFoundException, AlreadyExistsException {
		return createUtente(findPercorsoById(id), dto);
	}

	@Transactional
	public void addOperatoreToPercorso(Operatore operatore, Percorso percorso) {
		if (!percorso.getOperatori().contains(operatore)) {
			percorso.getOperatori().add(operatore);
			percorsoRepository.save(percorso);
		}
	}

	public void addOperatoreToPercorso(Integer oid, Integer gid) {
		addOperatoreToPercorso(operatoreService.findOperatoreById(oid), findPercorsoById(gid));
	}

	@Transactional
	public void removeOperatoreFromPercorso(Operatore operatore, Percorso percorso) {
		if (percorso.getOperatori().contains(operatore)) {
			percorso.getOperatori().remove(operatore);
			percorsoRepository.save(percorso);
		}
	}

	public void removeOperatoreFromPercorso(Integer oid, Integer gid) {
		removeOperatoreFromPercorso(operatoreService.findOperatoreById(oid), findPercorsoById(gid));
	}

	@Transactional
	public void createSerieUtenti(Percorso percorso, SerieUtentiDto serie) throws AlreadyExistsException {
		UtenteDto dto = new UtenteDto();
		dto.setPassword(serie.getPassword());
		dto.setFacilitato(false);
		int start = serie.getIndicePrimoUtente();
		// Se il percorso è di gruppo e non ci sono ancora utenti
		// allora viene creato anche l'indice 0 per l'insegnante
		if (percorso.getTipo() == TipoPercorso.GRUPPO && percorso.getUtenti().size() == 0) {
			dto.setUsername(serie.getPrefissoUsername() + getSuffissoUsername(0));
			utenteService.createUtente(percorso, dto);
		}
		for (int index = 0; index < serie.getNumeroUtenti(); index++) {
			dto.setUsername(serie.getPrefissoUsername() + getSuffissoUsername(start + index));
			utenteService.createUtente(percorso, dto);
		}
	}

	private String getSuffissoUsername(int index) {
		return (index < 10 ? "0" : "") + index;
	}

	public void createSerieUtenti(Integer id, SerieUtentiDto dto) throws NotFoundException, ForbiddenException, AlreadyExistsException {
		createSerieUtenti(findPercorsoById(id), dto);
	}

	public Date computeFinePercorso(Percorso percorso) {
		Date inizio = percorso.getInizioPercorsoEffettivo();
		int numeroFunzioniEsecutive = (Quartiere.values().length - 2);
		int durataGiorni = percorso.getDurataFunzioneEsecutivaGiorni() * numeroFunzioniEsecutive;
		if (percorso.getPeriodoIntroduzione()) {
			durataGiorni += constants.getDurataIntroduzioneGiorni();
		}
		if (percorso.getMediaLiteracy()) {
			durataGiorni += constants.getDurataMediaLiteracyGiorni();
		}
		return DateUtils.addDays(inizio, durataGiorni - 1);
	}

	public StatoPercorso computeStatoPercorso(Date inizio, Date fine) {
		Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		if (today.before(inizio)) {
			return StatoPercorso.NON_INIZIATO;
		} else {
			if (today.after(fine)) {
				return StatoPercorso.TERMINATO;
			} else {
				return StatoPercorso.IN_CORSO;
			}
		}
	}

	@Transactional
	public void setMediaLiteracy(Percorso percorso, Boolean mediaLiteracy) {
		percorso.setMediaLiteracy(mediaLiteracy);
		percorsoRepository.save(percorso);
	}

	public void setMediaLiteracy(Integer id, Boolean mediaLiteracy) {
		setMediaLiteracy(findPercorsoById(id), mediaLiteracy);
	}

	@Transactional
	public void setArchiviato(Percorso percorso, Boolean archiviato) {
		percorso.setArchiviato(true);
		percorsoRepository.save(percorso);
	}

	public void setArchiviato(Integer id, Boolean archiviato) {
		setArchiviato(findPercorsoById(id), archiviato);
	}

}

package eu.anastasis.mondoelli.quartiere;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.anastasis.mondoelli.configuration.ConstantsConfiguration;
import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.percorso.Percorso;
import eu.anastasis.mondoelli.utente.Utente;

@Service
public class QuartiereService {

	// private static final Logger logger = LoggerFactory.getLogger(QuartiereService.class);

	@Autowired
	ConstantsConfiguration constants;

	public QuartiereDto getQuartiereCorrente(Percorso percorso) {
		Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		if (percorso.getPeriodoIntroduzione()) {
			return getQuartiereCorrenteConIntroduzione(today, percorso);
		} else {
			return getQuartiereCorrenteSenzaIntroduzione(today, percorso);
		}
	}

	public QuartiereDto getQuartiereCorrente(Utente utente) {
		QuartiereDto res;
		if (utente.getDemo()) {
			// Il demo non tiene conto del percorso
			res = new QuartiereDto();
			res.setQuartiere(Quartiere.INTRODUZIONE);
		} else {
			Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
			Percorso percorso = utente.getPercorso();
			if (percorso.getPeriodoIntroduzione()) {
				res = getQuartiereCorrenteConIntroduzione(today, percorso);
			} else {
				res = getQuartiereCorrenteSenzaIntroduzione(today, percorso);
				if (res.getQuartiere() != null) {
					if (!utente.getQuartieriCompletati().contains(Quartiere.INTRODUZIONE)) {
						res.setQuartiere(Quartiere.INTRODUZIONE);
					}
				}
			}
		}
		return res;
	}

	private int daysBetween(Date date1, Date date2) {
		return (int) TimeUnit.DAYS.convert(date1.getTime() - date2.getTime(), TimeUnit.MILLISECONDS);
	}

	protected QuartiereDto getQuartiereCorrenteSenzaIntroduzione(Date today, Percorso percorso) {
		QuartiereDto res = new QuartiereDto();
		if (today.before(percorso.getInizioPercorsoEffettivo())) {
			res.setPercorsoNonIniziato(true);
		} else {
			int giorni = daysBetween(today, percorso.getInizioPercorsoEffettivo());
			int indice = giorni / percorso.getDurataFunzioneEsecutivaGiorni() + 1;
			int numeroQuartieriSenzaMediaLiteracy = Quartiere.values().length - 1;
			int numeroFuzioniEsecutive = numeroQuartieriSenzaMediaLiteracy - 1;
			if (indice < numeroQuartieriSenzaMediaLiteracy) {
				res.setQuartiere(Quartiere.values()[indice]);
			} else {
				if (percorso.getMediaLiteracy()) {
					Date inizioMediaLiteracy = DateUtils.addDays(percorso.getInizioPercorsoEffettivo(),
							numeroFuzioniEsecutive * percorso.getDurataFunzioneEsecutivaGiorni());
					giorni = daysBetween(today, inizioMediaLiteracy);
					if (giorni < constants.getDurataMediaLiteracyGiorni()) {
						res.setQuartiere(Quartiere.MEDIA_LITERACY);
					} else {
						res.setPercorsoCompletato(true);
					}
				} else {
					res.setPercorsoCompletato(true);
				}
			}
		}
		return res;
	}

	protected QuartiereDto getQuartiereCorrenteConIntroduzione(Date today, Percorso percorso) {
		QuartiereDto res = new QuartiereDto();
		if (today.before(percorso.getInizioPercorsoEffettivo())) {
			res.setPercorsoNonIniziato(true);
		} else {
			Date inizioPrimaFunzioneEsecutiva = DateUtils.addDays(percorso.getInizioPercorsoEffettivo(),
					constants.getDurataIntroduzioneGiorni());
			if (today.before(inizioPrimaFunzioneEsecutiva)) {
				res.setQuartiere(Quartiere.INTRODUZIONE);
			} else {
				int giorni = daysBetween(today, inizioPrimaFunzioneEsecutiva);
				int indice = giorni / percorso.getDurataFunzioneEsecutivaGiorni() + 1;
				int numeroQuartieriSenzaMediaLiteracy = Quartiere.values().length - 1;
				int numeroFuzioniEsecutive = numeroQuartieriSenzaMediaLiteracy - 1;
				if (indice < numeroQuartieriSenzaMediaLiteracy) {
					res.setQuartiere(Quartiere.values()[indice]);
				} else {
					if (percorso.getMediaLiteracy()) {
						Date inizioMediaLiteracy = DateUtils.addDays(inizioPrimaFunzioneEsecutiva,
								numeroFuzioniEsecutive * percorso.getDurataFunzioneEsecutivaGiorni());
						giorni = daysBetween(today, inizioMediaLiteracy);
						if (giorni < constants.getDurataMediaLiteracyGiorni()) {
							res.setQuartiere(Quartiere.MEDIA_LITERACY);
						} else {
							res.setPercorsoCompletato(true);
						}
					} else {
						res.setPercorsoCompletato(true);
					}
				}
			}
		}
		return res;
	}

}

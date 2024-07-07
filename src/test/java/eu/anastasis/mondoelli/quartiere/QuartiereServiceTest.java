package eu.anastasis.mondoelli.quartiere;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import eu.anastasis.mondoelli.configuration.ConstantsConfiguration;
import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.percorso.Percorso;

public class QuartiereServiceTest extends QuartiereService {

	@Test
	public void testGetQuartiereCorrenteSenzaIntroduzione_NoMediaLiteacy() throws ParseException {
		Percorso percorso = new Percorso();
		percorso.setInizioPercorsoEffettivo(DateUtils.parseDate("22/05/2023", "dd/MM/yyyy"));
		percorso.setDurataFunzioneEsecutivaGiorni(14);
		percorso.setMediaLiteracy(false);
		// Prima dell'inizio del percorso
		Date today = DateUtils.parseDate("18/05/2023", "dd/MM/yyyy");
		assertNull(getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// Il primo giorno del percorso
		today = percorso.getInizioPercorsoEffettivo();
		assertEquals(Quartiere.CONTROLLO_INTERFERENZA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 13 giorni dopo
		today = DateUtils.addDays(today, 13);
		assertEquals(Quartiere.CONTROLLO_INTERFERENZA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 14 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.INIBIZIONE_RISPOSTA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 27 giorni dopo
		today = DateUtils.addDays(today, 13);
		assertEquals(Quartiere.INIBIZIONE_RISPOSTA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 28 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.MEMORIA_LAVORO, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 41 giorni dopo
		today = DateUtils.addDays(today, 13);
		assertEquals(Quartiere.MEMORIA_LAVORO, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 42 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.FLESSIBILITA_COGNITIVA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 55 giorni dopo
		today = DateUtils.addDays(today, 13);
		assertEquals(Quartiere.FLESSIBILITA_COGNITIVA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 56 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertNull(getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
	}

	@Test
	public void testGetQuartiereCorrenteConIntroduzione_NoMediaLiteracy() throws ParseException {
		constants = new ConstantsConfiguration();
		constants.setDurataIntroduzioneGiorni(7);
		Percorso percorso = new Percorso();
		percorso.setInizioPercorsoEffettivo(DateUtils.parseDate("22/05/2023", "dd/MM/yyyy"));
		percorso.setDurataFunzioneEsecutivaGiorni(14);
		percorso.setMediaLiteracy(false);
		// Prima dell'inizio del percorso
		Date today = DateUtils.parseDate("18/05/2023", "dd/MM/yyyy");
		assertNull(getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// Il primo giorno del percorso
		today = percorso.getInizioPercorsoEffettivo();
		assertEquals(Quartiere.INTRODUZIONE, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 6 giorni dopo
		today = DateUtils.addDays(today, 6);
		assertEquals(Quartiere.INTRODUZIONE, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 7 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.CONTROLLO_INTERFERENZA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 20 giorni dopo
		today = DateUtils.addDays(today, 13);
		assertEquals(Quartiere.CONTROLLO_INTERFERENZA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 21 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.INIBIZIONE_RISPOSTA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 34 giorni dopo
		today = DateUtils.addDays(today, 13);
		assertEquals(Quartiere.INIBIZIONE_RISPOSTA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 35 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.MEMORIA_LAVORO, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 48 giorni dopo
		today = DateUtils.addDays(today, 13);
		assertEquals(Quartiere.MEMORIA_LAVORO, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 49 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.FLESSIBILITA_COGNITIVA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 62 giorni dopo
		today = DateUtils.addDays(today, 13);
		assertEquals(Quartiere.FLESSIBILITA_COGNITIVA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 63 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertNull(getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
	}

	@Test
	public void testGetQuartiereCorrenteSenzaIntroduzione_MediaLiteacy() throws ParseException {
		constants = new ConstantsConfiguration();
		constants.setDurataMediaLiteracyGiorni(14);
		Percorso percorso = new Percorso();
		percorso.setInizioPercorsoEffettivo(DateUtils.parseDate("22/05/2023", "dd/MM/yyyy"));
		percorso.setDurataFunzioneEsecutivaGiorni(7);
		percorso.setMediaLiteracy(true);
		// Prima dell'inizio del percorso
		Date today = DateUtils.parseDate("18/05/2023", "dd/MM/yyyy");
		assertNull(getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// Il primo giorno del percorso
		today = percorso.getInizioPercorsoEffettivo();
		assertEquals(Quartiere.CONTROLLO_INTERFERENZA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 6 giorni dopo
		today = DateUtils.addDays(today, 6);
		assertEquals(Quartiere.CONTROLLO_INTERFERENZA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 7 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.INIBIZIONE_RISPOSTA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 13 giorni dopo
		today = DateUtils.addDays(today, 6);
		assertEquals(Quartiere.INIBIZIONE_RISPOSTA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 14 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.MEMORIA_LAVORO, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 20 giorni dopo
		today = DateUtils.addDays(today, 6);
		assertEquals(Quartiere.MEMORIA_LAVORO, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 21 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.FLESSIBILITA_COGNITIVA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 27 giorni dopo
		today = DateUtils.addDays(today, 6);
		assertEquals(Quartiere.FLESSIBILITA_COGNITIVA, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 28 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.MEDIA_LITERACY, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 41 giorni dopo
		today = DateUtils.addDays(today, 13);
		assertEquals(Quartiere.MEDIA_LITERACY, getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
		// 42 giorni dopo
		today = DateUtils.addDays(today, 13);
		assertNull(getQuartiereCorrenteSenzaIntroduzione(today, percorso).getQuartiere());
	}

	@Test
	public void testGetQuartiereCorrenteConIntroduzione_MediaLiteracy() throws ParseException {
		constants = new ConstantsConfiguration();
		constants.setDurataIntroduzioneGiorni(7);
		constants.setDurataMediaLiteracyGiorni(14);
		Percorso percorso = new Percorso();
		percorso.setInizioPercorsoEffettivo(DateUtils.parseDate("22/05/2023", "dd/MM/yyyy"));
		percorso.setDurataFunzioneEsecutivaGiorni(7);
		percorso.setMediaLiteracy(true);
		// Prima dell'inizio del percorso
		Date today = DateUtils.parseDate("18/05/2023", "dd/MM/yyyy");
		assertNull(getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// Il primo giorno del percorso
		today = percorso.getInizioPercorsoEffettivo();
		assertEquals(Quartiere.INTRODUZIONE, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 6 giorni dopo
		today = DateUtils.addDays(today, 6);
		assertEquals(Quartiere.INTRODUZIONE, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 7 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.CONTROLLO_INTERFERENZA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 13 giorni dopo
		today = DateUtils.addDays(today, 6);
		assertEquals(Quartiere.CONTROLLO_INTERFERENZA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 14 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.INIBIZIONE_RISPOSTA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 20 giorni dopo
		today = DateUtils.addDays(today, 6);
		assertEquals(Quartiere.INIBIZIONE_RISPOSTA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 21 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.MEMORIA_LAVORO, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 27 giorni dopo
		today = DateUtils.addDays(today, 6);
		assertEquals(Quartiere.MEMORIA_LAVORO, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 28 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.FLESSIBILITA_COGNITIVA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 34 giorni dopo
		today = DateUtils.addDays(today, 6);
		assertEquals(Quartiere.FLESSIBILITA_COGNITIVA, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 35 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertEquals(Quartiere.MEDIA_LITERACY, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 48 giorni dopo
		today = DateUtils.addDays(today, 13);
		assertEquals(Quartiere.MEDIA_LITERACY, getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
		// 49 giorni dopo
		today = DateUtils.addDays(today, 1);
		assertNull(getQuartiereCorrenteConIntroduzione(today, percorso).getQuartiere());
	}

}

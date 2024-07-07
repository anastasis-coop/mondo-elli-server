package eu.anastasis.mondoelli.sessione;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import eu.anastasis.mondoelli.esercizio.esplorazione.EsercizioEsplorazione;

public class SessioneServiceTest extends SessioneService {

	@Test
	public void testComputeDeltaLivelloEsplorazione() {
		Sessione sessione = new Sessione();
		List<EsercizioEsplorazione> esercizi = new ArrayList<EsercizioEsplorazione>();
		sessione.setEserciziEsplorazione(esercizi);
		esercizi.add(new EsercizioEsplorazione());
		esercizi.add(new EsercizioEsplorazione());
		esercizi.add(new EsercizioEsplorazione());
		esercizi.get(0).setCorretto(false);
		esercizi.get(1).setCorretto(false);
		esercizi.get(2).setCorretto(false);
		assertEquals(-1, computeDeltaLivelloEsplorazione(sessione));
		esercizi.get(0).setCorretto(true);
		assertEquals(0, computeDeltaLivelloEsplorazione(sessione));
		esercizi.get(1).setCorretto(true);
		assertEquals(0, computeDeltaLivelloEsplorazione(sessione));
		esercizi.get(2).setCorretto(true);
		assertEquals(1, computeDeltaLivelloEsplorazione(sessione));
	}

}

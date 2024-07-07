package eu.anastasis.mondoelli.esercizio.esplorazione;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EsercizioEsplorazioneServiceTest extends EsercizioEsplorazioneService {

	@Test
	public void testComputeTessere() {
		assertEquals(0, computeTessere(0));
		assertEquals(0, computeTessere(1));
		assertEquals(0, computeTessere(2));
		assertEquals(0, computeTessere(3));
		assertEquals(1, computeTessere(4));
		assertEquals(1, computeTessere(7));
		assertEquals(2, computeTessere(8));
		assertEquals(2, computeTessere(14));
		assertEquals(3, computeTessere(15));
		assertEquals(3, computeTessere(19));
		assertEquals(4, computeTessere(20));
		assertEquals(4, computeTessere(24));
		assertEquals(5, computeTessere(25));
		assertEquals(5, computeTessere(100));
		assertEquals(5, computeTessere(1000));
	}

}

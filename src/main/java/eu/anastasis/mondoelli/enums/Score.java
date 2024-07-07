package eu.anastasis.mondoelli.enums;

public enum Score {
	INIT_SESSIONE(100),
	STEP_PERCORSO(50),
	STEP_PERCORSO_OK(50),
	LIVELLO(200),
	LIVELLO_OK(100),
	QUIZ(100),
	QUIZ_OK(300),
	END_SESSION(500);
	
	private Integer score;

	Score(Integer score) {
		this.score = score;
	}

	public Integer getScore() {
		return score;
	}

}

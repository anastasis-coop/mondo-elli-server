package eu.anastasis.mondoelli.security.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eu.anastasis.mondoelli.security.dto.CheckPasswordDto;

@Service
public class PasswordService {

	private static final int REQUIRED_LENGTH = 10;
	private static final int RANDOM_PASSWORD_LENGTH = 64;

	private static final String UPPERCASE_LETTERS = "A-Z";
	private static final String LOWERCASE_LETTERS = "a-z";
	private static final String LETTERS = UPPERCASE_LETTERS + LOWERCASE_LETTERS;
	private static final String NUMBERS = "0-9";
	private static final String SYMBOLS = "-+_!@#$%^&*.,?";
	private static final String ALLOWED_CHARS = LETTERS + NUMBERS + SYMBOLS;

	private Random random = new Random();

	@Autowired
	PasswordEncoder passwordEncoder;

	public String createRandomPassword() {
		String res = "";
		for (int i = 0; i < RANDOM_PASSWORD_LENGTH; i++) {
			res += (char) (random.nextInt(96) + 32);
		}
		return res;
	}

	public String createEncodedRandomPassword() {
		return passwordEncoder.encode(createRandomPassword());
	}

	public CheckPasswordDto checkPasswordComplexity(String password) {
		CheckPasswordDto result = new CheckPasswordDto();
		result.setIsValid(true);
		if (password == null || password.isEmpty()) {
			result.setIsValid(false);
			result.setIsNullOrEmpty(true);
		} else {
			if (password.length() < REQUIRED_LENGTH) {
				result.setIsValid(false);
				result.setIsTooShort(true);
			}
			if (!password.matches("^[" + ALLOWED_CHARS + "]+$")) {
				result.setIsValid(false);
				result.setContainsInvalidChars(true);
			}
			if (!password.matches(".*[" + UPPERCASE_LETTERS + "].*")) {
				result.setIsValid(false);
				result.setMissingUppercaseLetters(true);
			}
			if (!password.matches(".*[" + LOWERCASE_LETTERS + "].*")) {
				result.setIsValid(false);
				result.setMissingLowercaseLetters(true);
			}
			if (!password.matches(".*[" + NUMBERS + "].*")) {
				result.setIsValid(false);
				result.setMissingNumbers(true);
			}
			if (!password.matches(".*[" + SYMBOLS + "].*")) {
				result.setIsValid(false);
				result.setMissingSymbols(true);
			}
		}
		return result;
	}

	public String getPasswordCheckMessage(CheckPasswordDto dto) {
		if (dto.getIsValid()) {
			return "La password è valida.";
		}
		if (dto.getIsNullOrEmpty()) {
			return "La password è vuota.";
		}
		if (dto.getIsTooShort()) {
			return "La password è troppo corta.";
		}
		if (dto.getContainsInvalidChars()) {
			return "La password contiene caratteri non ammessi.";
		}
		if (dto.getMissingUppercaseLetters()) {
			return "La password deve contenere almeno una maiuscola.";
		}
		if (dto.getMissingLowercaseLetters()) {
			return "La password deve contenere almeno una minuscola.";
		}
		if (dto.getMissingNumbers()) {
			return "La password deve contenere almeno un numero.";
		}
		if (dto.getMissingSymbols()) {
			return "La password deve contenere almeno un simbolo.";
		}
		return null;
	}

}

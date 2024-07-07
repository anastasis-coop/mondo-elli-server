package eu.anastasis.mondoelli.security.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PasswordTokenService {

	private static final String HASH_PREFIX = "mondoelli_hash_";

	private static final Logger logger = LoggerFactory.getLogger(PasswordTokenService.class);

	private static final String DIGEST_ALGORITHM = "SHA-1";
	private static final String DATE_PATTERN = "dd/MM/yyyy hh:mm:ss";

	public String getTokenForIdAndDate(Integer id, Date date) {
		String hash = getHashFromDate(date);
		Integer cryptingValue = getCryptingValue(hash);
		String info = Long.toHexString(id ^ cryptingValue);
		info = StringUtils.leftPad(info, 16, '0');
		return hash + info;
	}

	public Integer getIdFromToken(String token) {
		if (checkTokenSyntax(token)) {
			String hash = token.substring(0, 40);
			Integer cryptingValue = getCryptingValue(hash);
			String info = token.substring(40);
			Integer cryptedLong = new BigInteger(info, 16).intValue();
			return (cryptedLong ^ cryptingValue);
		} else {
			return null;
		}
	}

	public boolean validateToken(String actualToken, Integer id, Date date) {
		String expectedToken = getTokenForIdAndDate(id, date);
		boolean isValid = expectedToken.equals(actualToken);
		if (!isValid) {
			logger.warn("Login token rifiutato: expected=" + expectedToken + " actual=" + actualToken + " id=" + id
					+ " date=" + date.getTime());
		}
		return isValid;
	}

	String getSHA1Hash(String text) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
			byte[] digest = messageDigest.digest(text.getBytes());
			String ret = new BigInteger(1, digest).toString(16);
			ret = StringUtils.leftPad(ret, 40, '0');
			return ret;
		} catch (NoSuchAlgorithmException e) {
			logger.error("Errore inizializzando l'algoritmo di hashing", e);
			return "";
		}
	}

	String getHashFromDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
		String text = HASH_PREFIX;
		if (date != null) {
			text += dateFormat.format(date);
		}
		return getSHA1Hash(text);
	}

	Integer getCryptingValue(String hash) {
		String cryptingHexString = getSHA1Hash(hash).substring(0, 16);
		Integer cryptingValue = new BigInteger(cryptingHexString, 16).intValue();
		return cryptingValue;
	}

	boolean checkTokenSyntax(String token) {
		if (token != null && token.length() == 56) {
			for (char it : token.toCharArray()) {
				if (!(((it >= '0') && (it <= '9')) || ((it >= 'A') && (it <= 'F')) || ((it >= 'a') && (it <= 'f')))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}

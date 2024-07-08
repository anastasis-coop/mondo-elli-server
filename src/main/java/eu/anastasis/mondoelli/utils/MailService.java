package eu.anastasis.mondoelli.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import eu.anastasis.mondoelli.configuration.AppConfiguration;

@Service
public class MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private AppConfiguration appConfig;

	@Autowired
	private MailSender mailSender;

	public boolean send(EmailDto emailDto) {

		if (appConfig.getMailCommunicationEnabled()) {

			if (emailDto.getEmailFrom() == null) {
				emailDto.setEmailFrom(appConfig.getMailAddressFrom());
			}

			if (emailDto.getReplyTo() == null) {
				emailDto.setReplyTo(appConfig.getMailAddressReplyTo());
			}

			if (appConfig.getMailOverride()) {
				String overrideEmail = appConfig.getMailOverrideAddress();
				logger.info("Override destinatari email: " + overrideEmail);
				List<String> emailTo = new ArrayList<String>();
				emailTo.add(overrideEmail);
				emailDto.setEmailTo(emailTo);
			}

			return sendMail(emailDto);
		} else {
			logger.info("Invio mail disabilitato da configurazione. Avrei dovuto inviare " + emailDto.getEmailSubject() + " a "
					+ String.join(",", emailDto.getEmailTo()));
			if (logger.isDebugEnabled()) {
				logger.debug("Email body: " + emailDto.getEmailBody());
			}
			return true;
		}
	}

	boolean sendMail(EmailDto emailDto) {
		JavaMailSender emailSender = mailSender.getJavaMailSender();
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailDto.getEmailFrom());
		message.setTo(emailDto.getEmailTo().get(0));
		message.setSubject(emailDto.getEmailSubject());
		message.setText(emailDto.getEmailBody());
		emailSender.send(message);
		return true;
	}

}

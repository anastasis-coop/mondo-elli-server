package eu.anastasis.mondoelli.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Genera oggetti e corpi di email partendo dai template Thymeleaf in templates/emails.
 * 
 * @see ThymeleafConfiguration
 * @see MailService
 * @author mcarnazzo
 */
@Service
public class EmailGeneratorService {
	@Autowired
	protected TemplateEngine templateEngine;

	@Autowired
	private ResourceLoader resourceLoader;

	public String createSubject(String templateName) {
		return createSubject(templateName, new HashMap<String, Object>());
	}

	public String createSubject(String templateName, Map<String, Object> model) {
		Context ctx = new Context();
		ctx.setVariables(model);

		String subject = templateEngine.process(templateName + "_subject", ctx);
		return subject;
	}

	public String createBody(String templateName) {
		return createBody(templateName, new HashMap<String, Object>());
	}

	public String createBody(String templateName, Map<String, Object> map) {
		Context ctx = new Context();
		ctx.setVariables(map);

		String body = templateEngine.process(templateName + "_body", ctx);
		return body;
	}

	// usato per override nei test
	public HtmlTemplate getHtmlTemplate(String name) {
		return new HtmlTemplate(name, resourceLoader);
	}

}

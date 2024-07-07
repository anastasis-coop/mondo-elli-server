package eu.anastasis.mondoelli.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import eu.anastasis.mondoelli.configuration.AppConfiguration;

public class HtmlTemplate {

	static final String TEMPLATE_FOLDER = "templates/emails/";
	static final String OPEN_VAR = "{{";
	static final String CLOSE_VAR = "}}";
	static final String EXTENSION = ".html";
	static final String START_IMPORT = "[[";
	static final String END_IMPORT = "]]";
	static final String IMPORT_FOLDER = "/imports/";

	@Autowired
	AppConfiguration appConfiguration;

	private String name;
	private Hashtable<String, String> substitutions;
	private ResourceLoader resourceLoader;

	public HtmlTemplate(String fileName, ResourceLoader resourceLoader) {
		this.name = fileName;
		this.resourceLoader = resourceLoader;
		this.substitutions = new Hashtable<String, String>();
	}

	/**
	 * Aggiunge una sostituzione, poi ritorna l'oggetto stesso per poter fare chain di chiamate.
	 * 
	 * @param placeholder
	 * @param value
	 * @return
	 */
	public HtmlTemplate addSubstitution(String placeholder, String value) {
		this.substitutions.put(placeholder, value);
		return this;
	}

	public String resolve() throws IOException {
		String content = getFileContent(this.name, false);

		content = this.resolveImports(content);

		for (String key : this.substitutions.keySet()) {
			content = content.replace(OPEN_VAR + key + CLOSE_VAR, this.substitutions.get(key));
		}

		return content;
	}

	private String resolveImports(String content) throws IOException {

		String ret = content;

		Pattern pattern = Pattern.compile("\\[\\[(.+?)\\]\\]");
		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			String name = matcher.group(1);
			String importContent = this.getFileContent(name, true);
			ret = ret.replace(START_IMPORT + name + END_IMPORT, importContent);
		}

		return ret;
	}

	String getFileContent(String name, boolean isImport) throws IOException {
		if (isImport) {
			name = IMPORT_FOLDER + name;
		}
		String path = this.getFilePath(name);
		return new String(Files.readAllBytes(Paths.get(path)));
	}

	String getFilePath(String name) throws IOException {
		String completeName = "classpath:" + TEMPLATE_FOLDER + name + EXTENSION;
		String path = resourceLoader.getResource(completeName).getFile().getAbsolutePath();
		return path;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		String ret = this.name + " (";
		for (String key : this.substitutions.keySet()) {
			ret += "{" + key + "," + this.substitutions.get(key) + "} ";
		}
		ret += ")";
		return ret;
	}

}

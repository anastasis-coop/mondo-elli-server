package eu.anastasis.mondoelli.utils;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Data;

@Data
public class EmailDto {

	private String emailFrom;
	private List<String> emailTo;
	private List<String> emailCcn;
	private String emailSubject;
	private String emailBody;
	private String replyTo;
	private List<String> staticAttachments;
	private Boolean privateContent;

	public void addEmailTo(String email) {
		if (this.emailTo == null) {
			this.emailTo = new ArrayList<String>();
		}
		this.emailTo.add(email);
	}

	public void addEmailCcn(String email) {
		if (this.emailCcn == null) {
			this.emailCcn = new ArrayList<String>();
		}
		this.emailCcn.add(email);
	}

	public void addStaticAttachment(String file) {
		if (this.staticAttachments == null) {
			this.staticAttachments = new ArrayList<String>();
		}
		this.staticAttachments.add(file);
	}

	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.writer();
		String jsonInString = this.emailFrom;
		try {
			jsonInString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonInString;
	}
}

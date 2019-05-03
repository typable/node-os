package os.server.classes;

import java.util.HashMap;


public class Email {

	private String from;
	private String to;
	private String subject;
	private String body;
	private HashMap<String, String> props = new HashMap<String, String>();

	public Email(String from, String to) {

		this.from = from;
		this.to = to;
	}
	
	public void put(String key, String value) {
		
		props.put(key, value);
	}

	public String getFrom() {

		return from;
	}

	public void setFrom(String from) {

		this.from = from;
	}

	public String getTo() {

		return to;
	}

	public void setTo(String to) {

		this.to = to;
	}

	public String getSubject() {

		return subject;
	}

	public void setSubject(String subject) {

		this.subject = subject;
	}

	public String getBody() {

		return body;
	}

	public void setBody(String body) {

		this.body = body;
	}

	public HashMap<String, String> getProps() {

		return props;
	}

	public void setProps(HashMap<String, String> props) {

		this.props = props;
	}
}

package os.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import os.core.Core;
import os.type.Cookie;
import os.type.Header;
import os.type.Logger.Messages;
import os.type.MediaType;
import os.type.RequestMethod;
import os.type.Status;
import os.util.Property;


public class HttpResponse {

	private String url;
	private RequestMethod requestMethod;
	private Status status;
	private Property<String> headers;
	private Property<String> parameters;
	private Property<String> attributes;
	private byte[] body;

	public HttpResponse() {

		headers = new Property<String>();
		parameters = new Property<String>();
		attributes = new Property<String>();
	}

	public void ok() {

		status = Status.OK;
	}

	public void notFound() {

		status = Status.NOT_FOUND;
	}

	public void forbidden() {

		status = Status.FORBIDDEN;
	}

	public void badRequest() {

		status = Status.BAD_REQUEST;
	}

	public void redirect(String url) {

		status = Status.FOUND;
		headers.set(Header.LOCATION.getCode(), url);
	}

	public void view(String code) {

		status = Status.OK;
		headers.set(Header.CONTENT_TYPE.getCode(), MediaType.TEXT_PLAIN.getType());
		body = code.getBytes();
	}

	public void viewPage(String path) {

		viewPage(path, MediaType.TEXT_HTML);
	}

	public void viewPage(String path, MediaType type) {

		byte[] data = Core.LOADER.loadFile(path);

		if(data != null) {

			body = data;
			headers.set(Header.CONTENT_TYPE.getCode(), type.getType());
			status = Status.OK;
		}
		else {

			status = Status.NOT_FOUND;
		}
	}

	public void provideDownload(File file) {

		try {

			headers.set(Header.CONTENT_DISPOSITION.getCode(), "attachment; filename=\"" + file.getName() + "\"");
			body = Files.readAllBytes(file.toPath());
			status = Status.OK;
		}
		catch(IOException e) {

			Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(file.getName()));
		}
	}

	public void addAttribute(String key, String value) {

		attributes.set(key, value);
	}

	public void addHeader(Header key, String value) {

		headers.set(key.getCode(), value);
	}

	public void addCookie(Cookie cookie) {

		headers.set(Header.SET_COOKIE.getCode(), cookie.getKey() + "=" + cookie.getValue() + "; Max-Age=" + cookie.getAge() + "; Expires=" + cookie.getAge());
	}

	public String getUrl() {

		return url;
	}

	public void setUrl(String url) {

		this.url = url;
	}

	public RequestMethod getRequestMethod() {

		return requestMethod;
	}

	public void setRequestMethod(RequestMethod requestMethod) {

		this.requestMethod = requestMethod;
	}

	public Status getStatus() {

		return status;
	}

	public void setStatus(Status status) {

		this.status = status;
	}

	public Property<String> getHeaders() {

		return headers;
	}

	public void setHeaders(Property<String> headers) {

		this.headers = headers;
	}

	public Property<String> getParameters() {

		return parameters;
	}

	public void setParameters(Property<String> parameters) {

		this.parameters = parameters;
	}

	public Property<String> getAttributes() {

		return attributes;
	}

	public void setAttributes(Property<String> attributes) {

		this.attributes = attributes;
	}

	public byte[] getBody() {

		return body;
	}

	public void setBody(byte[] body) {

		this.body = body;
	}
}

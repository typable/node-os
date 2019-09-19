package com.prototype.http;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.core.base.Environment;
import com.core.lang.Property;
import com.core.reflect.Inject;
import com.core.reflect.Injectable;
import com.prototype.Prototype;
import com.prototype.constants.Constants;
import com.prototype.core.Core;
import com.prototype.http.constants.Header;
import com.prototype.http.constants.MediaType;
import com.prototype.http.constants.RequestMethod;
import com.prototype.http.constants.Status;
import com.prototype.loader.Loader;
import com.prototype.type.Cookie;


public class HTTPResponse implements Injectable {

	@Inject(code = Loader.CODE)
	private Loader loader;

	private HTTPRequest request;
	private Status status;
	private Property<String> attributes;
	private Property<Cookie> cookies;
	private byte[] body;
	private boolean committed;

	// TODO Give HTTPResponse separate Fields without HTTPRequest
	public HTTPResponse(HTTPRequest request) {

		this.request = request;
		attributes = new Property<>();
		cookies = new Property<>();

		inject(this, Core.environment);
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

	public void noContent() {

		status = Status.NO_CONTENT;
	}

	public void created() {

		status = Status.CREATED;
	}

	public void redirect(String url) {

		request.setMethod(RequestMethod.GET);
		request.getHeaders().put(Header.LOCATION.getCode(), url);
		status = Status.FOUND;
	}

	public void view(String body, MediaType type) {

		this.body = body.getBytes(Environment.CHARSET);
		request.setType(type);
		status = Status.OK;
	}

	public void viewNotFoundPage() {

		Path path = Paths.get(Prototype.PATH + Constants.PATHS.WEB_PATH + "/404.html");

		viewPage(path, MediaType.TEXT_HTML);
	}

	public void viewPage(Path path, MediaType type) {

		File file = path.toFile();

		if(file.exists() && file.isFile()) {

			try {

				byte[] data = loader.read(path);

				body = data;
				request.setType(type);
				status = Status.OK;
			}
			catch(Exception e) {

				status = Status.NOT_FOUND;
			}
		}
		else {

			status = Status.NOT_FOUND;
		}
	}

	public void download(Path path) {

		File file = path.toFile();

		if(file.exists() && file.isFile()) {

			try {

				byte[] data = loader.read(path);

				download(data, file.getName());
			}
			catch(Exception e) {

				status = Status.NOT_FOUND;
			}
		}
		else {

			status = Status.NOT_FOUND;
		}
	}

	public void download(byte[] data, String name) {

		request.getHeaders().put(Header.CONTENT_DISPOSITION.getCode(), "attachment; filename=\"" + name + "\"");
		request.getHeaders().put(Header.CONTENT_LENGTH.getCode(), String.valueOf(data.length));
		request.setType(MediaType.ofFile(name));
		body = data;
		status = Status.OK;
	}

	public void addAttribute(String key, String value) {

		attributes.put(key, value);
	}

	public void addHeader(Header header, String value) {

		addHeader(header.getCode(), value);
	}

	public void addHeader(String key, String value) {

		request.getHeaders().put(key, value);
	}

	public void addCookie(Cookie cookie) {

		cookies.put(cookie.getKey(), cookie);
	}

	public void clearCookie(String key) {

		cookies.put(key, new Cookie(key, null));
	}

	public HTTPRequest getRequest() {

		return request;
	}

	public void setRequest(HTTPRequest request) {

		this.request = request;
	}

	public Status getStatus() {

		return status;
	}

	public void setStatus(Status status) {

		this.status = status;
	}

	public Double getVersion() {

		return request.getVersion();
	}

	public void setVersion(Double version) {

		request.setVersion(version);
	}

	public Property<String> getAttributes() {

		return attributes;
	}

	public void setAttributes(Property<String> attributes) {

		this.attributes = attributes;
	}

	public Property<String> getHeaders() {

		return request.getHeaders();
	}

	public void setHeaders(Property<String> headers) {

		request.setHeaders(headers);
	}

	public MediaType getType() {

		return request.getType();
	}

	public void setType(MediaType type) {

		request.setType(type);
	}

	public Property<Cookie> getCookies() {

		return cookies;
	}

	public void setCookies(Property<Cookie> cookies) {

		this.cookies = cookies;
	}

	public byte[] getBody() {

		return body;
	}

	public void setBody(byte[] body) {

		this.body = body;
	}

	public boolean isCommitted() {

		return committed;
	}
}

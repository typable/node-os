package os.handler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import com.prototype.http.constants.Header;
import com.prototype.http.constants.MediaType;
import com.prototype.http.constants.RequestMethod;
import com.prototype.http.constants.Status;
import com.prototype.logger.Logger.Messages;
import com.prototype.type.Property;

import os.core.Core;
import os.type.Cookie;


public class HttpResponse {

	private HttpRequest request;
	private Status status;
	private Property<String> attributes;

	public HttpResponse(HttpRequest request) {

		this.request = request;
		attributes = new Property<String>();
	}

	public void ok() {

		status = Status.OK;
	}

	public void notFound() {

		try {

			File file = new File(Core.getResourcePath("/404.html"));

			if(file.exists()) {

				viewPage("*/404.html");

				return;
			}
		}
		catch(IOException e) {

			e.printStackTrace();
		}

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
		request.getHeaders().put(Header.LOCATION.getCode(), url);
	}

	public void view(String code) {

		status = Status.OK;
		request.getHeaders().put(Header.CONTENT_TYPE.getCode(), MediaType.TEXT_PLAIN.getType());
		request.setBody(code.getBytes());
	}

	public void viewPage(String path) {

		viewPage(path, MediaType.TEXT_HTML, Core.DEFAULT_CHARSET);
	}

	public void viewPage(String path, MediaType type) {

		viewPage(path, type, Core.DEFAULT_CHARSET);
	}

	public void viewPage(String path, MediaType type, Charset charset) {

		byte[] data = Core.LOADER.loadFile(path);

		if(data != null) {

			request.setBody(data);
			request.getHeaders().put(Header.CONTENT_TYPE.getCode(), type.getType() + "; charset=" + charset.displayName().toLowerCase());
			status = Status.OK;
		}
		else {

			notFound();
		}
	}

	public void provideDownload(String path) {

		try {

			File file = new File(Core.getResourcePath("/src/downloads/" + path));

			if(file.exists()) {

				request.getHeaders().put(Header.CONTENT_DISPOSITION.getCode(), "attachment; filename=\"" + file.getName() + "\"");
				request.setBody(Files.readAllBytes(file.toPath()));
				status = Status.OK;
			}
			else {

				Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(path));
				notFound();
			}
		}
		catch(IOException e) {

			Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(path));
			notFound();
		}
	}

	public void addAttribute(String key, String value) {

		attributes.put(key, value);
	}

	public void addHeader(Header key, String value) {

		request.getHeaders().put(key.getCode(), value);
	}

	public void addCookie(Cookie cookie) {

		request.getHeaders().put(Header.SET_COOKIE.getCode(), cookie.getKey() + "=" + cookie.getValue() + "; Max-Age=" + cookie.getAge() + "; Expires=" + cookie.getAge());
	}

	public String getUrl() {

		return request.getUrl();
	}

	public void setUrl(String url) {

		request.setUrl(url);
	}

	public RequestMethod getRequestMethod() {

		return request.getRequestMethod();
	}

	public void setRequestMethod(RequestMethod requestMethod) {

		request.setRequestMethod(requestMethod);
	}

	public Status getStatus() {

		return status;
	}

	public void setStatus(Status status) {

		this.status = status;
	}

	public Property<String> getHeaders() {

		return request.getHeaders();
	}

	public void setHeaders(Property<String> headers) {

		request.setHeaders(headers);
	}

	public Property<String> getParameters() {

		return request.getParameters();
	}

	public void setParameters(Property<String> parameters) {

		request.setParameters(parameters);
	}

	public Property<String> getAttributes() {

		return attributes;
	}

	public void setAttributes(Property<String> attributes) {

		this.attributes = attributes;
	}

	public byte[] getBody() {

		return request.getBody();
	}

	public void setBody(byte[] body) {

		request.setBody(body);
	}
}

package com.prototype.http;

import com.prototype.http.constants.MediaType;
import com.prototype.http.constants.Status;
import com.prototype.type.Property;


public class HTTPResponse {

	private HTTPRequest request;
	private Status status;
	private Property<String> attributes;

	public HTTPResponse(HTTPRequest request) {

		this.request = request;
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

	public byte[] getBody() {

		return request.getBody();
	}

	public void setBody(byte[] body) {

		request.setBody(body);
	}
}

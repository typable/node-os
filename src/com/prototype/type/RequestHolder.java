package com.prototype.type;

import com.prototype.http.constants.RequestMethod;
import com.prototype.reflect.Callback;


public class RequestHolder {

	private String url;
	private RequestMethod method;
	private Callback callback;

	public RequestHolder(Request request, Callback callback) {

		this(request.url(), request.method(), callback);
	}

	public RequestHolder(String url, RequestMethod method, Callback callback) {

		this.url = url;
		this.method = method;
		this.callback = callback;
	}

	public String getUrl() {

		return url;
	}

	public void setUrl(String url) {

		this.url = url;
	}

	public RequestMethod getMethod() {

		return method;
	}

	public void setMethod(RequestMethod method) {

		this.method = method;
	}

	public Callback getCallback() {

		return callback;
	}

	public void setCallback(Callback callback) {

		this.callback = callback;
	}
}

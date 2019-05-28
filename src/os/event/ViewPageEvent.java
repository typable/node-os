package os.event;

import os.handler.HttpRequest;
import os.handler.HttpResponse;


public class ViewPageEvent extends Event {

	private String url;
	private HttpRequest request;
	private HttpResponse response;

	public ViewPageEvent(String url, HttpRequest request, HttpResponse response) {

		this.url = url;
		this.request = request;
		this.response = response;
	}

	public String getUrl() {

		return url;
	}

	public void setUrl(String url) {

		this.url = url;
	}

	public HttpRequest getRequest() {

		return request;
	}

	public void setRequest(HttpRequest request) {

		this.request = request;
	}

	public HttpResponse getResponse() {

		return response;
	}

	public void setResponse(HttpResponse response) {

		this.response = response;
	}
}

package os.type.holder;

import java.lang.reflect.Method;

import os.type.Request;


public class RequestHolder extends MethodHolder {

	private Request request;

	public RequestHolder(Request request, Object instance, Method method) {

		super(instance, method);

		this.request = request;
	}

	public Request getRequest() {

		return request;
	}

	public void setRequest(Request request) {

		this.request = request;
	}
}

package com.prototype.reflect;

import java.lang.reflect.Method;


public class Callback {

	private Instance instance;
	private Method method;

	public Callback(Instance instance, Method method) {

		this.instance = instance;
		this.method = method;
	}

	public void call(Object... args) throws Exception {

		method.setAccessible(true);
		method.invoke(instance.getInstance(), args);
	}

	public Instance getInstance() {

		return instance;
	}

	public void setInstance(Instance instance) {

		this.instance = instance;
	}

	public Method getMethod() {

		return method;
	}

	public void setMethod(Method method) {

		this.method = method;
	}
}

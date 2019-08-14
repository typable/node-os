package com.prototype.reflect;

import java.lang.reflect.Method;


public class Caller<T> {

	private T t;
	private Method method;
	private Object instance;

	public Caller(T t, Method method, Object instance) {

		this.t = t;
		this.method = method;
		this.instance = instance;
	}

	public void call(Object... args) throws Exception {

		method.setAccessible(true);
		method.invoke(instance, args);
	}

	public T get() {

		return t;
	}

	public Method getMethod() {

		return method;
	}

	public Object getInstance() {

		return instance;
	}
}

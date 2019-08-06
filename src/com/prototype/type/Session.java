package com.prototype.type;

import java.util.Set;


public class Session {

	private Property<Object> props;

	public Session() {

		props = new Property<>();
	}

	public void put(String key, Object value) {

		props.put(key, value);
	}

	public void remove(String key) {

		props.remove(key);
	}

	public Object get(String key) {

		return props.get(key);
	}

	public boolean has(String key) {

		return props.hasKey(key);
	}

	public Set<String> keys() {

		return props.keys();
	}
}

package com.prototype.type;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;


public class Property<T extends Object> {

	private LinkedHashMap<String, T> map;

	public Property() {

		map = new LinkedHashMap<String, T>();
	}

	public boolean has(String key) {

		return map.containsKey(key);
	}

	public int size() {

		return map.size();
	}

	public boolean isEmpty() {

		return map.isEmpty();
	}

	public void put(String key, T value) {

		map.put(key, value);
	}

	public Optional<T> getNullable(String key) {

		return Optional.ofNullable(map.get(key));
	}

	public T get(String key) {

		return map.get(key);
	}

	public void remove(String key) {

		map.remove(key);
	}

	public Set<String> keys() {

		return map.keySet();
	}

	public Collection<T> values() {

		return map.values();
	}
}

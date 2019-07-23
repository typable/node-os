package com.prototype.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;


public class Property<T extends Object> {

	private LinkedHashMap<String, T> map;

	public Property() {

		map = new LinkedHashMap<String, T>();
	}

	public Property(LinkedHashMap<String, T> map) {

		this.map = map;
	}

	public Property(HashMap<String, T> map) {

		this.map = new LinkedHashMap<>(map);
	}

	public boolean hasKey(String key) {

		return map.containsKey(key);
	}

	public boolean hasValue(T value) {

		return map.containsValue(value);
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

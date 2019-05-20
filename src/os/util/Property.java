package os.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;


public class Property<T extends Object> {

	private HashMap<String, T> map;

	public Property() {

		map = new HashMap<String, T>();
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

	public void set(String key, T value) {

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

	public HashMap<String, T> getMap() {

		return map;
	}
}

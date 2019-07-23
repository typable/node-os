package com.prototype.type;

import java.util.ArrayList;
import java.util.List;


public class CSVObject {

	private Property<List<String>> map;

	public CSVObject(String... keys) {

		map = new Property<List<String>>();

		for(String key : keys) {

			map.put(key, new ArrayList<String>());
		}
	}

	public void add(Object... value) {

		int index = 0;

		for(String key : map.keys()) {

			if(index < value.length) {

				map.get(key).add(String.valueOf(value[index]));
			}

			index++;
		}
	}

	public String[] get(int i) {

		String[] obj = new String[map.size()];

		int index = 0;

		for(String key : map.keys()) {

			if(i < map.get(key).size()) {

				obj[index] = map.get(key).get(i);
			}

			index++;
		}

		return obj;
	}

	public int size() {

		for(String key : map.keys()) {

			return map.get(key).size();
		}

		return 0;
	}

	public Property<List<String>> getMap() {

		return map;
	}

	public void setMap(Property<List<String>> map) {

		this.map = map;
	}
}

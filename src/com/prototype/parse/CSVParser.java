package com.prototype.parse;

import com.prototype.type.CSVObject;


public class CSVParser implements Parser<CSVObject, String> {

	@Override
	public CSVObject parse(String source) {

		CSVObject target = null;

		String[] args = source.split("\r\n");

		String[] keys = args[0].replaceAll("\\s", "").split(";");

		target = new CSVObject(keys);

		for(int i = 1; i < args.length; i++) {

			String[] values = args[i].replaceAll("\\s", "").split(";");

			target.add((Object[]) values);
		}

		return target;
	}

	@Override
	public String compose(CSVObject source) {

		String target = "";

		for(String key : source.getMap().keys()) {

			target += key + ";";
		}

		target += "\r\n";

		for(int i = 0; i < source.size(); i++) {

			Object[] array = source.get(i);

			for(Object obj : array) {

				if(obj == null) {

					target += ";";
				}
				else {

					target += String.valueOf(obj) + ";";
				}
			}

			target += "\r\n";
		}

		return target;
	}
}

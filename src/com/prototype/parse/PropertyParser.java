package com.prototype.parse;

import com.prototype.type.Property;


public class PropertyParser implements Parser<Property<String>, String> {

	@Override
	public Property<String> parse(String source) {

		if(source != null) {

			Property<String> target = new Property<String>();

			for(String s : source.split("\r\n")) {

				if(!s.startsWith("#") && s.contains("=")) {

					String[] s_ = s.split("=", 2);

					if(s_.length == 2) {

						String key = s_[0];
						String value = s_[1];

						target.put(key, value);
					}
				}
			}

			return target;
		}

		return null;
	}

	@Override
	public String compose(Property<String> source) {

		if(source != null) {

			String target = "";

			for(String key : source.keys()) {

				String value = source.get(key);

				target += key + "=" + value + "\r\n";
			}

			return target;
		}

		return null;
	}
}

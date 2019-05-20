package os.util.parser;

import os.util.Property;


public class PropertyParser implements Parser<Property<String>, String> {

	@Override
	public Property<String> parse(String source) {

		Property<String> target = new Property<String>();

		for(String s : source.split("\r\n")) {

			if(!s.startsWith("#") && s.contains("=")) {

				String[] s_ = s.split("=");

				if(s_.length == 2) {

					String key = s.split("=")[0];
					String value = s.split("=")[1];

					target.set(key, value);
				}
			}
		}

		return target;
	}

	@Override
	public String compose(Property<String> source) {

		String target = "";

		for(String key : source.keys()) {

			String value = (String) source.get(key);

			target += key + "=" + value + "\r\n";
		}

		return target;
	}
}

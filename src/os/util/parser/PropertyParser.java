package os.util.parser;

import os.util.Parser;
import os.util.Property;


public class PropertyParser implements Parser<Property<String>, String> {

	@Override
	public void parse(Property<String> target, String source) {
		
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
	}

	@Override
	public void compose(String target, Property<String> source) {
		
		for(String key : source.keys()) {
			
			String value = (String) source.get(key);
			
			target += key + "=" + value + "\r\n";
		}
	}
}

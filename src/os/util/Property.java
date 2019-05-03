package os.util;

import java.util.HashMap;
import java.util.Set;

public class Property {
	
	private HashMap<String, String> map;

	public Property() {
		
		map = new HashMap<String, String>();
	}
	
	public static Property parse(String props) {
		
		Property property = new Property();
		
		for(String s : props.split("\r\n")) {
			
			if(!s.startsWith("#") && s.contains("=")) {
				
				String[] s_ = s.split("=");
				
				if(s_.length == 2) {
					
					String key = s.split("=")[0];
					String value = s.split("=")[1];
					
					property.set(key, value);
				}
			}
		}
		
		return property;
	}
	
	public String stringify() {
		
		String props = "";
		
		for(String key : keys()) {
			
			String value = get(key);
			
			props += key + "=" + value + "\r\n";
		}
		
		return props;
	}
	
	public boolean hasKey(String key) {
		
		return map.containsKey(key);
	}
	
	public boolean hasValue(String key) {
		
		return map.containsValue(key);
	}
	
	public boolean isEmpty() {
		
		return map.isEmpty();
	}
	
	public void set(String key, String value) {
		
		map.put(key, value);
	}
	
	public String get(String key) {
		
		return map.get(key);
	}
	
	public Set<String> keys() {
		
		return map.keySet();
	}
	
	public String[] values() {
		
		return (String[]) map.values().toArray();
	}
	
	public HashMap<String, String> getMap() {
		
		return map;
	}
}

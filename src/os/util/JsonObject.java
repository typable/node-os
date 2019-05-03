package os.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class JsonObject {

	private HashMap<String, Object> objects;
	
	public JsonObject() {
		
		objects = new HashMap<String, Object>();
	}
	
	public static JsonObject parse(String json) {
		
		JsonObject obj = new JsonObject();
		
		json = json.substring(1, json.length() - 1);
		
		String[] splits = split(json, ',', new char[] {'{', '['}, new char[] {'}', ']'});
		
		for(String s : splits) {
			
			int index_ = s.indexOf(":");
			String key = s.substring(1, index_ - 1);
			String value = s.substring(index_ + 2, s.length());
			
			char start = value.charAt(0);
			char end = value.charAt(value.length() - 1);
			
			Object obj_ = null;
			
			if(start == '{' && end == '}') {
				
				value = value.substring(1, value.length() - 1);
				
				obj_ = parse(value);
			}
			else if(start == '[' && end == ']') {
				
				value = value.substring(1, value.length() - 1);
				
				String[] splits_ = split(value, ',', new char[] {'{', '['}, new char[] {'}', ']'});
				
				JsonObject[] obj__ = new JsonObject[splits_.length];
				
				for(int i = 0; i < splits_.length; i++) {
					
					obj__[i] = parse(splits_[i]);
				}
				
				obj_ = obj__;
			}
			else if(start == '"' && end == '"') {
				
				value = value.substring(1, value.length() - 1);
				
				obj_ = value;
			}
			else {
				
				if(value.equals("true") || value.equals("false")) {
					
					obj_ = Boolean.valueOf(value);
				}
				else if(value.contains(".")) {
					
					obj_ = Double.parseDouble(value);
				}
				else {
					
					obj_ = Integer.parseInt(value);
				}
			}
			
			obj.set(key, obj_);
		}
		
		return obj;
	}
	
	public String stringify() {
		
		String json = "";
		
		json += "{";
		
		int i = 0;
		int len = this.keys().size() - 1;
		
		for(String key : keys()) {
			
			Object obj_ = values().get(key);
			
			json += "\"" + key + "\": ";
			
			if(obj_ instanceof JsonObject) {
				
				json += ((JsonObject) obj_).stringify();
			}
			else if(obj_ instanceof JsonObject[]) {
				
				json += "[";
				
				int i_ = 0;
				int len_ = ((JsonObject[]) obj_).length - 1;
				
				for(JsonObject obj__ : ((JsonObject[]) obj_)) {
					
					if(obj__ != null) {
						
						json += obj__.stringify();
						
						if(i_ < len_) {
							
							json += ", ";
						}
						
						i_++;
					}
				}
				
				json += "]";
			}
			else if(obj_ instanceof String) {
				
				json += "\"" + (String) obj_ + "\"";
			}
			else if(obj_ instanceof Integer || obj_ instanceof Double || obj_ instanceof Boolean) {
				
				json += String.valueOf(obj_);
			}
			else {
				
				json += "null";
			}
			
			if(i < len) {
				
				json += ", ";
			}
			
			i++;
		}
		
		json += "}";
		
		return json;
	}
	
	public void set(String key, JsonObject value) {
		
		objects.put(key, value);
	}
	
	public JsonObject getObject(String key) {
		
		return get(key, JsonObject.class);
	}
	
	public void set(String key, JsonObject[] value) {
		
		objects.put(key, value);
	}
	
	public JsonObject[] getArray(String key) {
		
		return get(key, JsonObject[].class);
	}
	
	public void set(String key, String value) {
		
		objects.put(key, value);
	}
	
	public String getString(String key) {
		
		return get(key, String.class);
	}
	
	public void set(String key, int value) {
		
		objects.put(key, value);
	}
	
	public int getInt(String key) {
		
		return get(key, Integer.class);
	}
	
	public void set(String key, double value) {
		
		objects.put(key, value);
	}
	
	public double getDouble(String key) {
		
		return get(key, Double.class);
	}
	
	public void set(String key, boolean value) {
		
		objects.put(key, value);
	}
	
	public boolean getBoolean(String key) {
		
		return get(key, Boolean.class);
	}
	
	public void set(String key, Object value) {
		
		objects.put(key, value);
	}
	
	private <T extends Object> T get(String key, Class<T> type) {
		
		try {
			
			return type.cast(objects.get(key));
		}
		catch(ClassCastException ex) {
			
			throw new ClassCastException("Cannot cast " + objects.get(key).getClass().getName() + " to " + type.getName());
		}
	}
	
	public Set<String> keys() {
		
		return objects.keySet();
	}
	
	public HashMap<String, Object> values() {
		
		return objects;
	}
	
	private static String[] split(String line, char splitter, char[] start, char[] end) {
		
		List<Integer> splits = new ArrayList<Integer>();
		int i = 0;
		int scope = 0;
		
		for(char c : line.toCharArray()) {
			
			for(char cs : start) {
				
				if(c == cs) {
					
					scope++;
				}
			}
			
			for(char ce : end) {
				
				if(c == ce) {
					
					scope--;
				}
			}
			
			if(c == splitter && scope == 0) {
				
				splits.add(i);
			}
			
			i++;
		}
		
		int last = 0;
		int index = 0;
		
		String[] json_ = new String[splits.size() + 1];
		
		for(int i_ = 0; i_ < splits.size() + 1; i_++) {
			
			if(i_ < splits.size()) {
				
				index = splits.get(i_);
			}
			else {
				
				index = line.length();
			}
			
			json_[i_] = line.substring(last, index);
			
			last = index + 2;
		}
		
		return json_;
	}
}

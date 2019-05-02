package os.util;

import java.util.HashMap;

public class JsonObject {

	private HashMap<String, Object> objectMap = new HashMap<String, Object>();
	
	public JsonObject() {
		
		
	}
	
	public void set(String key, Object object) {
		
		objectMap.put(key, object);
	}
	
	public void json(String json) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		json = json.substring(1, json.length() - 2);
		
		String[] jsonParts = json.split("(?![^{]*(}|])),");
		
		for(String part : jsonParts) {
			
			String[] mapParts = part.split("(?![^{]*(}|])):");
			
			if(mapParts.length == 2) {
				
				map.put(mapParts[0].replaceAll("\"", ""), mapParts[1]);
			}
		}
		
		for(String key : map.keySet()) {
			
			String value = map.get(key);
			
			if(value.startsWith("\"")) {
				
				set(key, value.replaceAll("\"", ""));
			}
			else if(value.startsWith("{")) {
				
				JsonObject json_ = new JsonObject();
				
				json_.json(value);
				
				set(key, json_);
			}
			else if(value.startsWith("[")) {
				
				value = value.replaceAll("\\[", "");
				value = value.replaceAll("]", "");
				
				String[] parts = value.split("(?![^{]*(}|])),");
				
				JsonObject[] jsonArray = new JsonObject[parts.length];
				
				for(int i = 0; i < parts.length; i++) {
					
					JsonObject json_ = new JsonObject();
					json_.json(parts[i]);
					
					jsonArray[i] = json_;
				}
				
				set(key, jsonArray);
			}
			else {
				
				if(value.contains("true") || value.contains("false")) {
					
					value = value.replaceAll(",", "");
					set(key, Boolean.parseBoolean(value));
				}
				else if(value.contains(".")) {
					
					value = value.replaceAll(",", "");
					set(key, Double.parseDouble(value));
				}
				else {
					
					value = value.replaceAll(",", "");
					set(key, Integer.parseInt(value));
				}
			}
		}
	}
	
	public String stringify() {
		
		String code = "{";
		
		for(String key : objectMap.keySet()) {
			
			code += "\"" + key + "\":";
			
			Object obj = objectMap.get(key);
			
			if(obj instanceof String) {
				
				code += "\"" + (String) obj + "\",";
			}
			else if(obj instanceof Integer || obj instanceof Double || obj instanceof Boolean) {
				
				code += obj.toString() + ",";
			}
			else if(obj instanceof JsonObject) {
				
				code += ((JsonObject) obj).stringify();
			}
			else if(obj instanceof JsonObject[]) {
				
				code += "[";
				
				for(JsonObject obj_ : ((JsonObject[]) obj)) {
					
					code += obj_.stringify();
				}
				
				code += "],";
			}
			else {
				
				code += obj.toString() + ",";
			}
		}
		
		code += "},";
		
		return code;
	}
	
	public JsonObject getObject(String key) {
		
		return get(key, JsonObject.class);
	}
	
	public JsonObject[] getArray(String key) {
		
		return get(key, JsonObject[].class);
	}
	
	public String getString(String key) {
		
		return get(key, String.class);
	}
	
	public int getInt(String key) {
		
		return get(key, Integer.class);
	}
	
	public double getDouble(String key) {
		
		return get(key, Double.class);
	}
	
	public boolean getBoolean(String key) {
		
		return get(key, Boolean.class);
	}
	
	public <T extends Object> T get(String key, Class<T> type) {
		
		try {
			
			return type.cast(objectMap.get(key));
		}
		catch(ClassCastException ex) {
			
			throw new ClassCastException("Cannot cast " + objectMap.get(key).getClass().getName() + " to " + type.getName());
		}
	}
}

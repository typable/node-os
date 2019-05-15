package os.util;

import java.util.Set;

public class JsonObject {

	private Property<Object> objects;
	
	public JsonObject() {
		
		objects = new Property<Object>();
	}
	
	public void set(String key, JsonObject value) {
		
		objects.set(key, value);
	}
	
	public JsonObject getObject(String key) {
		
		return get(key, JsonObject.class);
	}
	
	public void set(String key, JsonObject[] value) {
		
		objects.set(key, value);
	}
	
	public JsonObject[] getArray(String key) {
		
		return get(key, JsonObject[].class);
	}
	
	public void set(String key, String value) {
		
		objects.set(key, value);
	}
	
	public String getString(String key) {
		
		return get(key, String.class);
	}
	
	public void set(String key, int value) {
		
		objects.set(key, value);
	}
	
	public int getInt(String key) {
		
		return get(key, Integer.class);
	}
	
	public void set(String key, double value) {
		
		objects.set(key, value);
	}
	
	public double getDouble(String key) {
		
		return get(key, Double.class);
	}
	
	public void set(String key, boolean value) {
		
		objects.set(key, value);
	}
	
	public boolean getBoolean(String key) {
		
		return get(key, Boolean.class);
	}
	
	public void set(String key, Object value) {
		
		objects.set(key, value);
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
		
		return objects.keys();
	}
	
	public Property<Object> values() {
		
		return objects;
	}
}

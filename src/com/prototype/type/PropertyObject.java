package com.prototype.type;

public class PropertyObject extends Property<Object> {

	public PropertyObject() {

		super();
	}

	public <T extends Object> T get(String key, Class<T> type) {

		Object obj = get(key);

		try {

			if(obj != null) {

				return type.cast(obj);
			}
		}
		catch(ClassCastException ex) {

			System.err.println("Cannot cast " + obj.getClass().getName() + " to " + type.getName());
		}

		return null;
	}

	public String getString(String key) {

		return get(key, String.class);
	}

	public Integer getInt(String key) {

		return get(key, Integer.class);
	}

	public Double getDouble(String key) {

		return get(key, Double.class);
	}

	public Float getFloat(String key) {

		return get(key, Float.class);
	}

	public Short getShort(String key) {

		return get(key, Short.class);
	}

	public Boolean getBoolean(String key) {

		return get(key, Boolean.class);
	}

	public Object getObject(String key) {

		return get(key);
	}
}

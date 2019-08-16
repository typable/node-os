package com.prototype.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.prototype.type.Property;


public class Instance {

	private Class<?> type;
	private Object instance;

	public static Object of(Class<?> type) throws Exception {

		return type.getConstructor().newInstance();
	}

	public static <T> void inject(T type, Property<Object> args) throws Exception {

		inject(type.getClass(), type, args);
	}

	public static void inject(Class<?> type, Object instance, Property<Object> args) throws Exception {

		for(Field field : type.getDeclaredFields()) {

			for(Annotation annotation : field.getAnnotations()) {

				if(annotation instanceof Inject) {

					for(String key : args.keys()) {

						if(((Inject) annotation).name().equals(key)) {

							field.setAccessible(true);
							field.set(instance, args.get(key));
						}
					}
				}
			}
		}
	}

	public Instance(Class<?> type) throws Exception {

		this.setType(type);
		instance = type.getConstructor().newInstance();
	}

	public Class<?> getType() {

		return type;
	}

	public void setType(Class<?> type) {

		this.type = type;
	}

	public Object getInstance() {

		return instance;
	}

	public void setInstance(Object instance) {

		this.instance = instance;
	}
}

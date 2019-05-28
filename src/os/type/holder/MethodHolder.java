package os.type.holder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import os.type.Inject;
import util.type.Property;


public class MethodHolder {

	private Object instance;
	private Method method;

	public MethodHolder(Object instance, Method method) {

		try {

			this.instance = instance;
			this.method = method;
		}
		catch(Exception e) {

			e.printStackTrace();
		}
	}

	public void call(Object... args) throws Exception {

		method.setAccessible(true);
		method.invoke(instance, args);
	}

	public void inject(Class<?> type, Property<Object> fields) throws Exception {

		for(Field field : type.getDeclaredFields()) {

			for(Annotation annotation : field.getAnnotations()) {

				if(annotation instanceof Inject) {

					Inject inject = (Inject) annotation;

					for(String key : fields.keys()) {

						if(inject.code().equals(key)) {

							field.setAccessible(true);
							field.set(instance, fields.get(key));
						}
					}
				}
			}
		}
	}

	public Object getInstance() {

		return instance;
	}

	public void setInstance(Object instance) {

		this.instance = instance;
	}

	public Method getMethod() {

		return method;
	}

	public void setMethod(Method method) {

		this.method = method;
	}
}

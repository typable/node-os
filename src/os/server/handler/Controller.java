package os.server.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import os.server.type.Inject;
import os.server.type.Request;

public class Controller {

	private Class<?> type;
	private Object instance;
	private List<RequestHandler> requestHandlerList;
	
	public Controller(Class<?> type) {
		
		try {
			
			this.type = type;
			instance = type.getConstructor().newInstance();
			
			requestHandlerList = new ArrayList<RequestHandler>();
			
			for(Method method : type.getDeclaredMethods()) {
				
				for(Annotation annotation : method.getAnnotations()) {

					if(annotation instanceof Request) {

						requestHandlerList.add(new RequestHandler(this, (Request) annotation, method));
					}
				}
			}
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void inject(HashMap<String, Object> fields) throws Exception {
		
		for(Field field : type.getDeclaredFields()) {
			
			for(Annotation annotation : field.getAnnotations()) {

				if(annotation instanceof Inject) {

					Inject inject = (Inject) annotation;
					
					for(String key : fields.keySet()) {
						
						if(inject.src().equals(key)) {
							
							field.setAccessible(true);
							field.set(instance, fields.get(key));
						}
					}
				}
			}
		}
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

	public List<RequestHandler> getRequestHandlerList() {
		
		return requestHandlerList;
	}

	public void setRequestHandlerList(List<RequestHandler> requestHandlerList) {
		
		this.requestHandlerList = requestHandlerList;
	}
}

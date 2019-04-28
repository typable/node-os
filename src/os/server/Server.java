package os.server;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import os.server.handler.Handler;
import os.server.note.Request;
import os.server.type.ContentType;

public class Server {

	private ServerSocket serverSocket;
	
	private HashMap<Request, Method> requestList = new HashMap<Request, Method>();
	
	public Server() {
		
		for(Method method : this.getClass().getMethods())
			for(Annotation annotation : method.getAnnotations()) {

			if(annotation instanceof Request) {

				requestList.put((Request) annotation, method);
			}
		}
	}
	
	public void launch(int port) {
		
		try {
			
			/** Starting Server **/
			
			serverSocket = new ServerSocket(port);
			
			while(!serverSocket.isClosed()) {
				
				Socket socket = serverSocket.accept();
				
				new Thread(() -> {
					
					try {
						
						/** Handling Socket **/
						
						Handler requestHandler = new Handler(socket);
						requestHandler.request();
						
						Handler responseHandler = new Handler(socket);
						
						if(requestHandler.getUrl() != null) {
							
							String url = requestHandler.getUrl();							
							Method method = null;
							
							for(Request request : requestList.keySet()) {
								
								if(request.url().equals(url) && request.method() == requestHandler.getMethod()) {
									
									method = requestList.get(request);
								}
							}
							
							if(method != null) {
								
								method.invoke(this, requestHandler, responseHandler);
							}
							else if(url.startsWith("/src/")) {
								
								responseHandler.showPage(url, ContentType.getByFile(url));
							}
							else {
								
								responseHandler.notFound();
							}
						}
						
						responseHandler.respond();
						
						socket.close();
					}
					catch(IOException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						
						e.printStackTrace();
					}
					
				}).start();
			}
		}
		catch(IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void close() {
		
		System.exit(0);
	}
}

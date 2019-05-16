package os.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import os.core.Core;
import os.handler.Controller;
import os.handler.HttpRequest;
import os.handler.HttpRequestConnection;
import os.handler.HttpResponse;
import os.handler.RequestEvent;
import os.type.Logger.Messages;
import os.type.Request;
import os.type.RequestMethod;
import os.type.Status;

public class HttpServer {

	private ServerSocket serverSocket;
	
	public HttpServer() {
		
		
	}
	
	public void launch() {
		
		try {
			
			serverSocket = new ServerSocket(Core.PORT);
			
			Core.LOGGER.info(Messages.SERVER_STARTED.getMessage(String.valueOf(Core.PORT)));
			
			while(!serverSocket.isClosed()) {
				
				Socket socket = serverSocket.accept();
				
				new Thread(() -> {
					
					HttpRequestConnection requestConnection = new HttpRequestConnection(socket);
					requestConnection.connect(() -> {

						try {
							
							HttpRequest requestHandler = requestConnection.request();
							HttpResponse responseHandler = new HttpResponse();
							
							String url = requestHandler.getUrl();
							RequestMethod requestMethod = requestHandler.getRequestMethod();
							RequestEvent currentEvent = null;
							
							for(Controller controller : Core.CONTROLLERS) {
								
								for(RequestEvent event : controller.getRequestHandlerList()) {
									
									Request request = event.getRequest();
									
									if(request.url().equals(url) && request.method() == requestMethod) {
										
										currentEvent = event;
									}
								}
							}
							
							if(currentEvent != null) {
								
								currentEvent.call(requestHandler, responseHandler);
							}
							else if(url.startsWith("/src/")) {
								
								// TODO source request
								// responseHandler.showPage(url, ContentType.getByFile(url));
							}
							else {
								
								Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(url));
								responseHandler.setStatus(Status.NOT_FOUND);
							}
							
							requestConnection.commit(responseHandler);
						}
						catch(Exception ex) {
							
							Core.LOGGER.error(Messages.FATAL_ERROR.getMessage(), ex);
							
							Core.stop();
						}
					});
					
				}).start();
			}
		}
		catch(IOException e) {
			
			Core.LOGGER.error(Messages.FATAL_ERROR.getMessage(), e);
			e.printStackTrace();
			
			Core.stop();
		}
	}

	public ServerSocket getServerSocket() {
	
		return serverSocket;
	}
}

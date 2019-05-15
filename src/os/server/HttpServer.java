package os.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import os.core.Core;
import os.server.handler.Controller;
import os.server.handler.HttpRequest;
import os.server.handler.HttpResponse;
import os.server.handler.RequestHandler;
import os.type.ContentType;
import os.type.Request;
import os.type.Logger.Messages;

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
					
					try {
						
						HttpRequest requestHandler = new HttpRequest(this, socket);
						requestHandler.request();
						
						HttpResponse responseHandler = new HttpResponse(this, socket);
						responseHandler.setLanguage(requestHandler.getLanguage());
						
						if(requestHandler.getUrl() != null) {
							
							String url = requestHandler.getUrl();					
							RequestHandler currentHandler = null;
							
							for(Controller controller : Core.CONTROLLERS) {
								
								for(RequestHandler handler : controller.getRequestHandlerList()) {
									
									Request request = handler.getRequest();
									
									if(request.url().equals(url) && request.method() == requestHandler.getMethod()) {
										
										currentHandler = handler;
									}
								}
							}
							
							if(currentHandler != null) {
								
								currentHandler.call(requestHandler, responseHandler);
							}
							else if(url.startsWith("/src/")) {
								
								responseHandler.showPage(url, ContentType.getByFile(url));
							}
							else {
								
								Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(url));
								responseHandler.notFound();
							}
						}
						
						responseHandler.commit();
					}
					catch(Exception ex) {
						
						Core.LOGGER.error(Messages.FATAL_ERROR.getMessage(), ex);
						
						Core.stop();
					}
					
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

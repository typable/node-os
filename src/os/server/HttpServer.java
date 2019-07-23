package os.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.prototype.http.constants.MediaType;
import com.prototype.http.constants.RequestMethod;
import com.prototype.logger.Logger.Messages;

import os.core.Core;
import os.handler.HttpConnection;
import os.handler.HttpRequest;
import os.handler.HttpResponse;
import os.type.holder.RequestHolder;


public class HttpServer {

	private ServerSocket serverSocket;

	public void launch() {

		try {

			serverSocket = new ServerSocket(Core.PORT);

			Core.LOGGER.info(Messages.SERVER_STARTED.getMessage(String.valueOf(Core.PORT)));

			while(!serverSocket.isClosed()) {

				Socket socket = serverSocket.accept();

				new Thread(() -> {

					/*
					HttpConnection httpConnection = new HttpConnection(socket);
					httpConnection.connect(new Runnable() {
					
						@Override
						public void run() {
					
							try {
					
								HttpRequest httpRequest = httpConnection.request();
								HttpResponse httpResponse = new HttpResponse();
					
								String url = httpRequest.getUrl();
								RequestMethod requestMethod = httpRequest.getRequestMethod();
								RequestHolder requestHolder = null;
					
								if(url != null) {
					
									for(RequestHolder holder : Core.REQUESTS) {
					
										if(holder.getUrl().equals(url) && holder.getMethod() == requestMethod) {
					
											requestHolder = holder;
					
											break;
										}
									}
					
									if(requestHolder != null) {
					
										Cookie cookie = httpRequest.getCookie("_uuid");
					
										if(cookie != null) {
					
										}
										else {
					
										}
					
										//
									}
					
									httpConnection.commit(httpResponse);
								}
							}
							catch(Exception e) {
					
								e.printStackTrace();
							}
						}
					});
					*/

					HttpConnection requestConnection = new HttpConnection(socket);
					requestConnection.connect(() -> {

						try {

							HttpRequest requestHandler = requestConnection.request();
							HttpResponse responseHandler = new HttpResponse(requestHandler);

							String url = requestHandler.getUrl();
							RequestMethod requestMethod = requestHandler.getRequestMethod();
							RequestHolder currentRequestHolder = null;

							if(url != null) {

								for(RequestHolder holder : Core.REQUESTS) {

									if(holder.getUrl().equals(url) && holder.getMethod() == requestMethod) {

										currentRequestHolder = holder;
									}
								}

								if(currentRequestHolder != null) {

									//									SessionService sessionService = Core.sessionService;
									//
									//									Cookie cookie = requestHandler.getCookie("_uuid");
									//
									//									if(cookie == null) {
									//
									//										sessionService.createSession(responseHandler);
									//									}
									//									else {
									//
									//										Session session = sessionService.getCurrentSession(cookie);
									//
									//										if(session == null) {
									//
									//											sessionService.createSession(responseHandler);
									//										}
									//									}

									currentRequestHolder.getCallback().call(requestHandler, responseHandler);
								}
								else if(url.startsWith("/res/")) {

									String path = Core.getResourcePath(url);

									responseHandler.viewPage(path, MediaType.getByFileType(path.substring(path.lastIndexOf("/"), path.length())));
								}
								else {

									Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(url));
									responseHandler.viewPage("*/404.html", MediaType.TEXT_HTML);
								}

								requestConnection.commit(responseHandler);
							}
						}
						catch(Exception ex) {

							Core.LOGGER.error(Messages.FATAL_ERROR.getMessage(), ex);

							ex.printStackTrace();
							// Core.stop();
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

package os.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import os.core.Core;
import os.event.Event;
import os.event.NotFoundEvent;
import os.handler.HttpConnection;
import os.handler.HttpRequest;
import os.handler.HttpResponse;
import os.service.SessionService;
import os.type.Cookie;
import os.type.Logger.Messages;
import os.type.Request;
import os.type.Session;
import os.type.constants.MediaType;
import os.type.constants.RequestMethod;
import os.type.holder.EventHolder;
import os.type.holder.RequestHolder;


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

					HttpConnection requestConnection = new HttpConnection(socket);
					requestConnection.connect(() -> {

						try {

							HttpRequest requestHandler = requestConnection.request();
							HttpResponse responseHandler = new HttpResponse();

							String url = requestHandler.getUrl();
							RequestMethod requestMethod = requestHandler.getRequestMethod();
							RequestHolder currentRequestHolder = null;

							if(url != null) {

								for(RequestHolder holder : Core.REQUESTS) {

									Request request = holder.getRequest();

									if(request.url().equals(url) && request.method() == requestMethod) {

										currentRequestHolder = holder;
									}
								}

								if(currentRequestHolder != null) {

									SessionService sessionService = Core.sessionService;

									Cookie cookie = requestHandler.getCookie("_uuid");

									if(cookie == null) {

										sessionService.createSession(responseHandler);
									}
									else {

										Session session = sessionService.getCurrentSession(cookie);

										if(session == null) {

											sessionService.createSession(responseHandler);
										}
									}

									currentRequestHolder.call(requestHandler, responseHandler);
								}
								else if(url.startsWith("/src/")) {

									String path = Core.getResourcePath(url);

									responseHandler.viewPage(path, MediaType.getByFileType(path.substring(path.lastIndexOf("/"), path.length())));
								}
								else {

									Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(url));
									responseHandler.viewPage("*/404.html", MediaType.TEXT_HTML);

									for(EventHolder holder : Core.EVENTS) {

										Event event = holder.getEvent();

										if(event instanceof NotFoundEvent) {

											holder.call(requestHandler, responseHandler);
										}
									}
								}

								requestConnection.commit(responseHandler);
							}
						}
						catch(Exception ex) {

							Core.LOGGER.error(Messages.FATAL_ERROR.getMessage(), ex);

							ex.printStackTrace();

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

package os.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import os.core.Core;
import os.handler.Controller;
import os.handler.HttpConnection;
import os.handler.HttpRequest;
import os.handler.HttpResponse;
import os.handler.RequestEvent;
import os.type.Cookie;
import os.type.Logger.Messages;
import os.type.MediaType;
import os.type.Request;
import os.type.RequestMethod;
import os.type.Session;


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
							RequestEvent currentEvent = null;

							if(url != null) {

								for(Controller controller : Core.CONTROLLERS) {

									for(RequestEvent event : controller.getRequestHandlerList()) {

										Request request = event.getRequest();

										if(request.url().equals(url) && request.method() == requestMethod) {

											currentEvent = event;
										}
									}
								}

								if(currentEvent != null) {

									Cookie cookie = requestHandler.getCookie("_uuid");

									if(cookie == null) {

										createSessionCookie(requestHandler, responseHandler);
									}
									else {

										Session session = Core.getSession(cookie.getValue());

										if(session == null) {

											createSessionCookie(requestHandler, responseHandler);
										}
										else {

											requestHandler.setSession(session);
										}
									}

									currentEvent.call(requestHandler, responseHandler);
								}
								else if(url.startsWith("/src/")) {

									String path = Core.getResourcePath(url);

									responseHandler.viewPage(path, MediaType.TEXT_CSS);
								}
								else {

									Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(url));
									responseHandler.viewPage("*/404.html", MediaType.TEXT_HTML);

									/*
									responseHandler.setStatus(Status.NOT_FOUND);
									*/
								}

								requestConnection.commit(responseHandler);
							}
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

			Core.stop();
		}
	}

	private void createSessionCookie(HttpRequest request, HttpResponse response) {

		String uuid = UUID.randomUUID().toString();
		Session session = new Session();

		Cookie cookie = new Cookie("_uuid", uuid);
		cookie.setAge(60 * 60 * 24);

		Core.SESSIONS.set(uuid, session);

		request.setSession(session);

		response.addCookie(cookie);
	}

	public ServerSocket getServerSocket() {

		return serverSocket;
	}
}

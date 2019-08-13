package com.prototype.http;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

import com.prototype.Prototype;
import com.prototype.http.constants.MediaType;
import com.prototype.http.constants.RequestMethod;
import com.prototype.http.constants.Status;
import com.prototype.http.error.HTTPError;
import com.prototype.http.error.HTTPException;
import com.prototype.logger.Logger;
import com.prototype.logger.Logger.Messages;
import com.prototype.type.RequestHolder;


public class HTTPServer {

	public static final String LOG_PREFIX = "[Server] ";

	private Logger LOGGER;

	private ServerSocket serverSocket;

	public HTTPServer() {

		LOGGER = Prototype.logger();
	}

	public void start(int port) {

		try {

			// TODO SSLServerSocket
			serverSocket = new ServerSocket(port);

			LOGGER.info(LOG_PREFIX + Messages.SERVER_STARTED.getMessage(String.valueOf(port)));

			while(!serverSocket.isClosed()) {

				Socket socket = serverSocket.accept();

				HTTPConnection connection = new HTTPConnection(socket);
				connection.connect(new Runnable() {

					@Override
					public void run() {

						try {

							HTTPRequest request = null;
							HTTPResponse response = null;

							try {

								request = connection.request();
								response = new HTTPResponse(request);

								String url = request.getUrl();
								RequestMethod method = request.getMethod();
								RequestHolder requestHolder = null;

								if(url != null) {

									for(RequestHolder holder : Prototype.request()) {

										if(url.equals(holder.getUrl()) && method == holder.getMethod()) {

											requestHolder = holder;

											break;
										}
									}

									if(requestHolder != null) {

										requestHolder.getCallback().call(request, response);
									}
									else if(url.startsWith("/res/")) {

										File file = new File(Prototype.PATH + url);

										if(file.exists() && file.isFile()) {

											response.viewPage(file.toPath(), MediaType.ofFile(file.getName()));
										}
										else {

											response.notFound();
										}
									}
									else {

										response.viewNotFoundPage();
									}

									connection.commit(response);
								}
							}
							catch(HTTPException ex) {

								HTTPError error = ex.getError();

								response = new HTTPResponse(new HTTPRequest());

								if(error == HTTPError.UNSUPPORTED_HTTP_VERSION) {

									response.setStatus(Status.HTTP_VERSION_NOT_SUPPORTED);
								}

								if(error == HTTPError.UNSUPPORTED_REQUEST_METHOD || error == HTTPError.MALFORMED_URL) {

									response.setStatus(Status.BAD_REQUEST);
								}

								connection.commit(response);
							}
						}
						catch(Exception e) {

							LOGGER.error(LOG_PREFIX + Messages.FATAL_ERROR.getMessage(), e);
						}
					}
				});
			}
		}
		catch(Exception e) {

			LOGGER.error(LOG_PREFIX + Messages.FATAL_ERROR.getMessage(), e);
		}
	}

	public void stop() throws Exception {

		serverSocket.close();

		LOGGER.info(LOG_PREFIX + Messages.SERVER_STOPPED.getMessage());

		System.exit(0);
	}
}

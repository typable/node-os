package com.prototype.http;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;

import com.core.reflect.Caller;
import com.core.reflect.Inject;
import com.core.reflect.Injectable;
import com.core.service.Service;
import com.prototype.Prototype;
import com.prototype.constants.Constants;
import com.prototype.core.Core;
import com.prototype.http.constants.MediaType;
import com.prototype.http.constants.RequestMethod;
import com.prototype.http.constants.Status;
import com.prototype.http.error.HTTPError;
import com.prototype.http.error.HTTPException;
import com.prototype.logger.Logger;
import com.prototype.logger.Logger.Messages;
import com.prototype.service.SessionService;
import com.prototype.type.Request;


public class HTTPServer extends Service implements Injectable {

	public static final String CODE = "server";
	public static final String PREFIX = "[Server] ";

	@Inject(code = Logger.CODE)
	private Logger logger;

	@Inject(code = "requests")
	private List<Caller<Request>> requests;

	@Inject(code = "sessionService")
	private SessionService sessionService;

	private ServerSocket serverSocket;
	private boolean ssl;

	public HTTPServer(ServerSocket serverSocket) {

		this.serverSocket = serverSocket;

		if(serverSocket.getLocalPort() == 443) {

			ssl = true;
		}

		inject(this, Core.environment);
	}

	@Override
	public void run() {

		logger.info(PREFIX + Messages.SERVER_STARTED.getMessage(String.valueOf(serverSocket.getLocalPort())));

		while(!serverSocket.isClosed()) {

			try {

				Socket socket = serverSocket.accept();

				try {

					if(ssl) {

						((SSLSocket) socket).startHandshake();
					}

					HTTPConnection connection = new HTTPConnection(socket);
					connection.connect(new Runnable() {

						@Override
						public void run() {

							try {

								HTTPRequest request = null;
								HTTPResponse response = null;

								try {

									/** Get request **/
									request = connection.request();
									response = new HTTPResponse(request);

									String url = request.getUrl();
									RequestMethod method = request.getMethod();

									Caller<Request> caller = null;

									for(Caller<Request> c : requests) {

										Request r = c.get();

										/** Checks if request has no ignore tag **/
										if(!r.ignore()) {

											if(r.url().equals(url) && r.method() == method) {

												caller = c;

												break;
											}
										}
									}

									if(caller != null) {

										/** Prepare Session **/
										sessionService.prepareSession(request, response);

										/** Calls method of current request **/
										caller.call(request, response);
									}
									else if(url.startsWith(Constants.PATHS.RESOURCE_PATH)) {

										/** Handle '/res' requests **/
										File file = Prototype.path(url).toFile();

										if(file.exists() && file.isFile()) {

											response.viewPage(file.toPath(), MediaType.ofFile(file.getName()));
										}
										else {

											response.notFound();
										}
									}
									else {

										/** Views 404 page if exists **/
										response.viewNotFoundPage();
									}

									/** Send response **/
									connection.commit(response);
								}
								catch(HTTPException ex) {

									/** Handle HTTPException **/
									HTTPError error = ex.getError();

									if(error == HTTPError.UNSUPPORTED_HTTP_VERSION) {

										response.setStatus(Status.HTTP_VERSION_NOT_SUPPORTED);
									}

									if(error == HTTPError.UNSUPPORTED_REQUEST_METHOD || error == HTTPError.MALFORMED_URL) {

										response.badRequest();
									}

									/** Send response **/
									connection.commit(response);
								}
							}
							catch(Exception ex) {

								/** SSLException: socket write error **/
								// ex.printStackTrace();
							}
						}
					});
				}
				catch(SSLException ex) {

					/** Certificate not trusted! **/
				}
			}
			catch(Exception ex) {

				ex.printStackTrace();
			}
		}
	}

	@Override
	public void close() throws IOException {

		serverSocket.close();

		logger.info(PREFIX + Messages.SERVER_STOPPED.getMessage());
	}

	public boolean isSSL() {

		return ssl;
	}
}

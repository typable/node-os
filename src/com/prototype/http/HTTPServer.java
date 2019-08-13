package com.prototype.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

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

	private Logger logger;

	private ServerSocket serverSocket;
	private boolean isSSL;

	public HTTPServer() {

		logger = Prototype.logger();
	}

	public void start(int port) {

		start(port, false, null, null);
	}

	public void start(int port, boolean ssl, String key, String password) {

		boolean isSSL = false;

		try {

			if(ssl && key != null && !key.isBlank() && password != null && !password.isBlank()) {

				isSSL = ssl;

				File file = Prototype.path(key).toFile();

				if(!file.exists()) {

					logger.warn(Messages.NOT_FOUND.getMessage(file.getName()));

					isSSL = false;
				}
				else {

					try {

						serverSocket = createSSLServerSocket(file, password);

						port = 443;
					}
					catch(IOException e) {

						logger.warn("Invalid SSL-Key password!");

						isSSL = false;
					}
				}
			}

			if(!isSSL) {

				serverSocket = new ServerSocket(port);
			}

			this.isSSL = isSSL;

			logger.info(LOG_PREFIX + Messages.SERVER_STARTED.getMessage(String.valueOf(port)));

			while(!serverSocket.isClosed()) {

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

								logger.error(LOG_PREFIX + Messages.FATAL_ERROR.getMessage(), e);
							}
						}
					});
				}
				catch(SSLException e) {

					// TODO Certificate not verified!
				}
			}
		}
		catch(Exception e) {

			logger.error(LOG_PREFIX + Messages.FATAL_ERROR.getMessage(), e);
		}
	}

	public void stop() throws Exception {

		serverSocket.close();

		logger.info(LOG_PREFIX + Messages.SERVER_STOPPED.getMessage());

		System.exit(0);
	}

	private ServerSocket createSSLServerSocket(File file, String password) throws Exception {

		KeyStore keyStore = generateKey(file, password);

		SSLContext context = generateSSLContext(keyStore, password);

		SSLServerSocketFactory sslServerSocketFactory = context.getServerSocketFactory();
		SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(443);
		sslServerSocket.setEnabledProtocols(new String[] { "TLSv1.1", "TLSv1.2" });
		sslServerSocket.setEnabledCipherSuites(sslServerSocket.getSupportedCipherSuites());
		sslServerSocket.setUseClientMode(false);
		sslServerSocket.setNeedClientAuth(true);
		sslServerSocket.setWantClientAuth(true);
		sslServerSocket.setEnableSessionCreation(true);

		return sslServerSocket;
	}

	private SSLContext generateSSLContext(KeyStore keyStore, String password) throws Exception {

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, password.toCharArray());

		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(keyStore);

		SSLContext context = SSLContext.getInstance("SSL");
		context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

		return context;
	}

	private KeyStore generateKey(File file, String password) throws Exception {

		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

		keyStore.load(new FileInputStream(file), password.toCharArray());

		return keyStore;
	}

	public boolean isSSL() {

		return isSSL;
	}
}

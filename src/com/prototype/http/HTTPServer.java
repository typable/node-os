package com.prototype.http;

import java.net.ServerSocket;
import java.net.Socket;

import com.prototype.Prototype;
import com.prototype.http.constants.RequestMethod;

import os.type.holder.RequestHolder;


public class HTTPServer {

	private ServerSocket serverSocket;

	public HTTPServer() {

		//
	}

	public void start(int port) {

		try {

			serverSocket = new ServerSocket(port);

			while(!serverSocket.isClosed()) {

				Socket socket = serverSocket.accept();

				HTTPConnection connection = new HTTPConnection(socket);
				connection.connect(new Runnable() {

					@Override
					public void run() {

						try {

							HTTPRequest request = connection.request();
							HTTPResponse response = new HTTPResponse(request);

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
								else {

									//
								}

								connection.commit(response);
							}
						}
						catch(Exception e) {

							e.printStackTrace();
						}
					}
				});
			}
		}
		catch(Exception e) {

			e.printStackTrace();
		}
	}
}

package com.prototype.http;

import java.net.Socket;
import java.nio.charset.Charset;

import com.prototype.Prototype;
import com.prototype.format.Formatter;

import net.Connection;
import os.type.constants.MediaType;
import os.type.constants.RequestMethod;
import os.type.constants.Status;


public class HTTPConnection extends Connection {

	private Charset CHARSET;

	public HTTPConnection(Socket socket) {

		super(socket);

		CHARSET = Prototype.constant().CHARSET;
	}

	public HTTPRequest request() throws Exception {

		HTTPRequest request = new HTTPRequest();

		String line;

		while((line = readLine()) != null && !line.isBlank()) {

			String[] args = line.split(" ");

			if(args.length == 3 && args[2].startsWith("HTTP/")) {

				request.setMethod(RequestMethod.valueOf(args[0]));
				request.setUrl(Formatter.parseURL(args[1]));
				request.setVersion(Double.valueOf(args[2].split("/")[1]));
			}

			// System.out.println(line);
		}

		return request;
	}

	public void commit(HTTPResponse response) throws Exception {

		if(response.getStatus() != null) {

			emit("HTTP/1.1 " + response.getStatus().getMessage());

			if(!response.getHeaders().isEmpty()) {

				for(String key : response.getHeaders().keys()) {

					emit(key + ": " + response.getHeaders().get(key));
				}
			}

			if(response.getBody() != null) {

				String body = new String(response.getBody(), CHARSET);

				if(response.getType() == MediaType.TEXT_HTML) {

					Prototype.loader().loadMessages(Prototype.message());

					body = Formatter.parse(body, response.getAttributes());
				}

				emit("");
				emit(body);
			}
		}
		else {

			emit("HTTP/1.1 " + Status.BAD_REQUEST.getMessage());
		}

		quit();
	}
}

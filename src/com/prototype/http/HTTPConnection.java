package com.prototype.http;

import java.net.Socket;
import java.nio.charset.Charset;

import com.prototype.Prototype;
import com.prototype.format.Formatter;
import com.prototype.http.constants.Header;
import com.prototype.http.constants.MediaType;
import com.prototype.http.constants.RequestMethod;
import com.prototype.http.constants.Status;
import com.prototype.net.Connection;
import com.prototype.util.Utils;


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

				String[] query = Formatter.parseURL(args[1]).split("\\?");

				if(query.length == 2) {

					request.setParameters(Formatter.parseQuery(query[1]));
				}

				request.setUrl(query[0]);
				request.setMethod(RequestMethod.valueOf(args[0]));
				request.setVersion(Double.valueOf(args[2].split("/")[1]));
			}
			else {

				Utils.addAttribute(request.getHeaders(), ": ", line);
			}
		}

		if(request.getMethod() == RequestMethod.POST) {

			String contentLength = request.getHeaders().get(Header.CONTENT_LENGTH.getCode());

			if(contentLength != null) {

				Integer length = Integer.parseInt(contentLength);

				String body = new String(readLength(length), CHARSET);

				body = Formatter.parseURL(body);

				request.setParameters(Formatter.parseQuery(body));

				request.setBody(body.getBytes(CHARSET));
			}
		}

		return request;
	}

	public void commit(HTTPResponse response) throws Exception {

		if(response.getStatus() != null) {

			emit("HTTP/" + response.getVersion() + " " + response.getStatus().getMessage());

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

			emit("HTTP/" + response.getVersion() + " " + Status.BAD_REQUEST.getMessage());
		}

		quit();
	}
}

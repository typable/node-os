package os.handler;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import os.type.constants.Header;
import os.type.constants.MediaType;
import os.type.constants.RequestMethod;
import os.type.constants.Status;
import os.util.Connection;
import os.util.Formatter;
import os.util.Utils;


public class HttpConnection extends Connection {

	public HttpConnection(Socket socket) {

		super(socket);
	}

	public HttpRequest request() throws IOException {

		HttpRequest request = new HttpRequest();

		String line;
		int i = 0;

		while(Utils.notEmpty(line = readLine())) {

			if(i == 0) {

				String[] args = line.split(" ");

				if(args[1].contains("?")) {

					String[] args_ = args[1].split("\\?");

					request.setUrl(args_[0]);

					if(args_[1].contains("&")) {

						for(String arg : args_[1].split("&")) {

							Utils.addAttribute(request.getParameters(), "=", arg);
						}
					}
					else {

						Utils.addAttribute(request.getParameters(), "=", args_[1]);
					}
				}
				else {

					request.setUrl(args[1]);
				}

				request.setRequestMethod(RequestMethod.valueOf(args[0]));
			}
			else {

				Utils.addAttribute(request.getHeaders(), ": ", line);
			}

			i++;
		}

		if(request.getRequestMethod() == RequestMethod.POST) {

			String contentLength = request.getHeaders().get(Header.CONTENT_LENGTH.getCode());

			if(contentLength != null) {

				int length = Integer.parseInt(contentLength);

				if(length > 0) {

					String body = new String(readLength(length), StandardCharsets.UTF_8);

					body = Formatter.parseURL(body);

					if(body.contains("&")) {

						for(String arg : body.split("&")) {

							Utils.addAttribute(request.getParameters(), "=", arg);
						}
					}
					else {

						Utils.addAttribute(request.getParameters(), "=", body);
					}

					request.setBody(body.getBytes());
				}
			}
		}

		return request;
	}

	public void commit(HttpResponse response) throws IOException {

		if(response.getStatus() != null) {

			emit("HTTP/1.1 " + response.getStatus().getMessage());
		}
		else {

			emit("HTTP/1.1 " + Status.BAD_REQUEST.getMessage());

			return;
		}

		if(!response.getHeaders().isEmpty()) {

			for(String key : response.getHeaders().keys()) {

				emit(key + ": " + response.getHeaders().get(key));
			}
		}

		if(response.getBody() != null) {

			byte[] body = response.getBody();

			if(response.getHeaders().hasKey(Header.CONTENT_TYPE.getCode())) {

				if(response.getHeaders().get(Header.CONTENT_TYPE.getCode()).split("; ")[0].equals(MediaType.TEXT_HTML.getType())) {

					String code = new String(body, StandardCharsets.UTF_8);

					// TODO Formatter.parse()
					// code = Formatter.parseHTML(code, getTemplates());
					code = Formatter.parseHTML(code, response.getAttributes());
					// code = Formatter.parseLang(code, getLanguage(), Core.LANGUAGES);

					body = code.getBytes(StandardCharsets.UTF_8);
				}
			}

			emit("");
			emit(body);
		}

		quit();
	}
}

package os.handler;

import java.io.IOException;
import java.net.Socket;

import net.Connection;
import os.core.Core;
import os.format.Formatter;
import os.type.constants.Header;
import os.type.constants.MediaType;
import os.type.constants.RequestMethod;
import os.type.constants.Status;
import util.Utils;


public class HttpConnection extends Connection {

	public HttpConnection(Socket socket) {

		super(socket);
	}

	public HttpRequest request() throws IOException {

		HttpRequest request = new HttpRequest();

		String line;
		int i = 0;

		while((line = readLine()) != null && !line.isBlank()) {

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

					String body = new String(readLength(length), Core.DEFAULT_CHARSET);

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

	public void commit(HttpResponse response) throws Exception {

		if(response.getStatus() != null) {

			emit("HTTP/1.1 " + response.getStatus().getMessage());

			if(!response.getHeaders().isEmpty()) {

				for(String key : response.getHeaders().keys()) {

					emit(key + ": " + response.getHeaders().get(key));
				}
			}

			if(response.getBody() != null) {

				byte[] body = response.getBody();

				if(response.getHeaders().hasKey(Header.CONTENT_TYPE.getCode())) {

					if(response.getHeaders().get(Header.CONTENT_TYPE.getCode()).split("; ")[0].equals(MediaType.TEXT_HTML.getType())) {

						String code = new String(body, Core.DEFAULT_CHARSET);

						try {

							Core.loadTexts(Core.DEFAULT_LANG);
						}
						catch(Exception e) {

							e.printStackTrace();
						}

						code = Formatter.parse(code, response.getAttributes());

						body = code.getBytes(Core.DEFAULT_CHARSET);
					}
				}

				emit("");
				emit(body);
			}
		}
		else {

			Core.LOGGER.warn("Bad Request!");

			emit("HTTP/1.1 " + Status.BAD_REQUEST.getMessage());
		}

		quit();
	}
}

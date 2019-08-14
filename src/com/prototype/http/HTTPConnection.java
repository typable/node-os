package com.prototype.http;

import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.prototype.constants.Constants;
import com.prototype.format.Formatter;
import com.prototype.http.constants.Header;
import com.prototype.http.constants.MediaType;
import com.prototype.http.constants.RequestMethod;
import com.prototype.http.constants.Status;
import com.prototype.http.error.HTTPError;
import com.prototype.http.error.HTTPException;
import com.prototype.net.Connection;
import com.prototype.type.Cookie;
import com.prototype.type.FormData;
import com.prototype.type.Parameter;
import com.prototype.type.Property;
import com.prototype.util.Utils;


public class HTTPConnection extends Connection {

	private Charset CHARSET;

	public HTTPConnection(Socket socket) {

		super(socket);

		CHARSET = Constants.CHARSET;
	}

	public HTTPRequest request() throws HTTPException, Exception {

		HTTPRequest request = new HTTPRequest();

		String line;

		while((line = readLine()) != null && !line.isBlank()) {

			String[] args = line.split(" ");

			if(args.length == 3 && args[2].startsWith("HTTP/")) {

				String[] query = Formatter.parseURL(args[1]).split("\\?");

				if(query.length == 2) {

					request.setParameters(Formatter.parseQuery(query[1]));
				}
				else {

					request.setParameters(new Property<>());
				}

				String url = query[0];
				RequestMethod method = RequestMethod.valueOf(args[0]);
				Double version = Double.valueOf(args[2].split("/")[1]);

				if(version != 1.1) {

					throw new HTTPException(HTTPError.UNSUPPORTED_HTTP_VERSION);
				}

				if(!(method == RequestMethod.GET || method == RequestMethod.POST)) {

					throw new HTTPException(HTTPError.UNSUPPORTED_REQUEST_METHOD);
				}

				request.setUrl(url);
				request.setMethod(method);
				request.setVersion(version);
			}
			else {

				Utils.addAttribute(request.getHeaders(), ": ", line);
			}
		}

		if(request.getMethod() == RequestMethod.POST) {

			String contentLength = request.getHeaders().get(Header.CONTENT_LENGTH.getCode());
			String contentType = request.getHeaders().get(Header.CONTENT_TYPE.getCode());
			MediaType type = null;
			String body = null;

			if(contentType != null) {

				String[] typeArgs = contentType.split("; ");

				type = MediaType.ofType(typeArgs[0]);

				if(contentLength != null) {

					Integer length = Integer.parseInt(contentLength);

					if(type == MediaType.MULTIPART_FORM_DATA) {

						String boundary = typeArgs[1].split("=")[1];

						byte[] data = read(length);

						String formattedData = new String(data, CHARSET);
						String[] args = formattedData.split("--" + boundary + "\\r\\n");

						List<FormData> formDataList = new ArrayList<>();

						for(int i = 0; i < args.length; i++) {

							String arg = args[i];

							if(i > 0) {

								FormData formData = null;

								String[] sect = arg.split("\\r\\n\\r\\n", 2);

								String formDataHeader = sect[0];
								String formDataDisposition = formDataHeader.split("\\r\\n")[0].split(": ")[1];
								String formDataContentType = null;

								if(formDataHeader.split("\\r\\n").length >= 2) {

									formDataContentType = formDataHeader.split("\\r\\n")[1].split(": ")[1];
								}

								String formDataData = sect[1];

								formData = new FormData(MediaType.ofType(formDataContentType), formDataDisposition);

								if(i < args.length - 1) {

									String _data = formDataData;

									formData.setData(_data.substring(0, _data.length() - "\r\n".length()).getBytes(CHARSET));
								}
								else {

									String _data = formDataData.replace("--" + boundary + "--" + CRLF, "");

									formData.setData(_data.substring(0, _data.length() - "\r\n".length()).getBytes(CHARSET));
								}

								formDataList.add(formData);
							}
						}

						Property<Parameter> params = new Property<>();

						for(FormData form : formDataList) {

							String key = form.getDisposition().split("; ")[1].split("=")[1].replaceAll("\"", "");

							if(!form.getDisposition().contains("filename=") && form.getData() != null) {

								String value = new String(form.getData(), CHARSET);

								value = Formatter.parseURL(value);

								Parameter param = new Parameter(key);
								param.setValue(value);

								params.put(key, param);
							}
							else if(form.getDisposition().contains("filename=")) {

								String fileName = form.getDisposition().split("; ")[2].split("=")[1].replaceAll("\"", "");

								Property<byte[]> files = new Property<>();

								files.put(fileName, form.getData());

								Parameter param = new Parameter(key);

								if(params.hasKey(key)) {

									for(String fileKey : params.get(key).getFiles().keys()) {

										files.put(fileKey, params.get(key).getFiles().get(fileKey));
									}
								}

								param.setFiles(files);
								params.put(key, param);
							}
						}

						request.setParameters(params);
						request.setBody(data);
					}
					else {

						body = new String(read(length), CHARSET);

						body = Formatter.parseURL(body);

						request.setParameters(Formatter.parseQuery(body));
						request.setBody(body.getBytes(CHARSET));
					}
				}
			}
		}

		if(request.getHeaders().hasKey(Header.COOKIE.getCode())) {

			String cookies = request.getHeader(Header.COOKIE);

			if(cookies.contains("; ")) {

				for(String arg : cookies.split("; ")) {

					String[] cookie_args = arg.split("=");

					if(cookie_args.length == 2) {

						Cookie cookie = new Cookie(cookie_args[0], cookie_args[1]);
						request.getCookies().put(cookie_args[0], cookie);
					}
				}
			}
			else {

				String[] cookie_args = cookies.split("=");

				if(cookie_args.length == 2) {

					Cookie cookie = new Cookie(cookie_args[0], cookie_args[1]);
					request.getCookies().put(cookie_args[0], cookie);
				}
			}
		}

		return request;
	}

	public void commit(HTTPResponse response) throws Exception {

		// FIXME HTTPS/SSL ERR_RESPONSE_HEADERS_TRUNCATED

		if(response.getStatus() != null) {

			emit("HTTP/" + response.getVersion() + " " + response.getStatus().getMessage());

			if(response.getType() != null) {

				response.getHeaders().put(Header.CONTENT_TYPE.getCode(), response.getType().getType());
			}
			else {

				response.getHeaders().put(Header.CONTENT_TYPE.getCode(), MediaType.TEXT_PLAIN.getType());
			}

			byte[] body = null;

			if(response.getBody() != null) {

				if(response.getType() == MediaType.TEXT_HTML) {

					String formattedBody = new String(response.getBody(), CHARSET);

					formattedBody = Formatter.parse(formattedBody, response.getAttributes());

					body = formattedBody.getBytes(CHARSET);
				}
				else {

					body = response.getBody();
				}

				if(!response.getHeaders().hasKey(Header.CONTENT_LENGTH.getCode())) {

					response.getHeaders().put(Header.CONTENT_LENGTH.getCode(), String.valueOf(body.length));
				}
			}

			if(!response.getCookies().isEmpty()) {

				String cookies = "";

				for(String key : response.getCookies().keys()) {

					Cookie cookie = response.getCookies().get(key);

					if(cookie.getAge() != -1) {

						cookies += cookie.getKey() + "=" + cookie.getValue() + "; Expires=" + cookie.getAge() + "; Max-Age=" + cookie.getAge();
					}
					else {

						cookies += cookie.getKey() + "=" + cookie.getValue() + "; Expires=0; Max-Age=0";
					}
				}

				emit(Header.SET_COOKIE.getCode() + ": " + cookies);
			}

			if(!response.getHeaders().isEmpty()) {

				for(String key : response.getHeaders().keys()) {

					emit(key + ": " + response.getHeaders().get(key));
				}
			}

			if(response.getBody() != null) {

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

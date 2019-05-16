package os.handler;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import os.type.Header;
import os.type.MediaType;
import os.type.RequestMethod;
import os.type.Status;
import os.util.Connection;
import os.util.Formatter;
import os.util.Utils;

public class HttpRequestConnection extends Connection {

	public HttpRequestConnection(Socket socket) {

		super(socket);
	}
	
	public HttpRequest request() throws IOException {
		
		HttpRequest request = new HttpRequest();
		
		String line;
		int i = 0;
		
		while(Utils.notEmpty(line = read())) {
			
			if(i == 0) {
				
				String[] args = line.split(" ");
				
				if(!args[2].equals("HTTP/1.1")) {
					
					// TODO HttpRequestConnection.request() - Status.HTTP_VERSION_NOT_SUPPORTED
				}
				
				if(args[1].contains("?")) {
					
					String[] args_ = args[1].split("\\?");
					
					request.setUrl(args_[0]);
					
					if(args_[1].contains("&")) {
						
						for(String arg : args_[1].split("&")) {
							
							Utils.addAttribute(request.getParameters(), "=", arg);
						}
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
			
			String contentLength = request.getHeaders().get(Header.CONTENT_LENGTH);
			
			if(contentLength != null) {
				
				int length = Integer.parseInt(contentLength);
				
				if(length > 0) {
					
					byte[] buffer = new byte[length];
					
					getSocket().getInputStream().read(buffer, 0, length);
					
					String body = new String(buffer, StandardCharsets.UTF_8);

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
			
			if(response.getHeaders().hasKey(Header.CONTENT_TYPE)) {
				
				if(response.getHeaders().get(Header.CONTENT_TYPE).equals(MediaType.TEXT_HTML.getType())) {
					
					String code = new String(body, StandardCharsets.UTF_8);
					
					// TODO Formatter.parse()
					// code = Formatter.parseHTML(code, getTemplates());
					code = Formatter.parseHTML(code, response.getAttributes());
					// code = Formatter.parseLang(code, getLanguage(), Core.LANGUAGES);
					// code = Formatter.parseHref(code, getLanguage());
					
					body = code.getBytes();
				}
			}
			
			emit("");
			emit(body);
		}
		
		quit();
	}
}

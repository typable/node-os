package os.server.handler;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import os.server.Server;
import os.server.classes.Cookie;
import os.server.constants.Keys;
import os.server.type.RequestMethod;
import os.util.Parser;
import os.util.Utils;

public class Request extends Handler {
	
	public Request(Server server, Socket socket) throws IOException {

		super(server, socket);
	}
	
	public void request() throws IOException {
		
		String line;
		
		while(Utils.notEmpty(line = getIn().readLine())) {
			
			if(line.contains("HTTP")) {
				
				String method = line.split(" ")[0];
				
				setMethod(RequestMethod.valueOf(method));
				setUrl(line.split(" ")[1]);
			}
			else {
				
				Utils.keySet(getHeader(), ": ", line);
			}
		}
		
		if(getMethod() == RequestMethod.POST) {
			
			int contentLength = getContentLength();
			
			if(contentLength > 0) {
				
				char[] buffer = new char[contentLength];
				
				getIn().read(buffer, 0, contentLength);
				
				String body = String.valueOf(buffer);

				body = Parser.parseURL(body);
				
				if(body.contains("&")) {
					
					for(String bodyParts : body.split("&")) {
						
						Utils.keySet(getParameter(), "=", bodyParts);
					}
				}
				else {
					
					Utils.keySet(getParameter(), "=", body);
				}
				
				setBody(body);
			}
		}
	}
	
	public int getContentLength() {
		
		if(getHeader().containsKey(Keys.CONTENT_LENGTH)) {
			
			try {
				
				return Integer.parseInt(getHeader().get(Keys.CONTENT_LENGTH));
			}
			catch(NumberFormatException ex) {
				
				return 0;
			}
		}
		
		return 0;
	}
	
	public Cookie[] getCookies() {
		
		Cookie[] cookies;
		HashMap<String, String> cookiesMap = new HashMap<String, String>();
		
		if(getHeader().containsKey(Keys.COOKIE)) {
			
			String line = getHeader().get(Keys.COOKIE);
			
			if(line.contains("; ")) {
				
				for(String lineParts : line.split("; ")) {
					
					Utils.keySet(cookiesMap, "=", lineParts);
				}
			}
			else {
				
				Utils.keySet(cookiesMap, "=", line);
			}
		}
		
		cookies = new Cookie[cookiesMap.size()];
		
		int i = 0;
		
		for(String key : cookiesMap.keySet()) {
			
			cookies[i] = new Cookie(key, cookiesMap.get(key));
			
			i++;
		}
		
		return cookies;
	}
	
	public Cookie getCookie(String key) {
		
		for(Cookie cookie : getCookies()) {
			
			if(cookie.getKey().equals(key)) {
				
				return cookie;
			}
		}
		
		return null;
	}
}

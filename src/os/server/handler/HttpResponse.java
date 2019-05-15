package os.server.handler;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;

import os.core.Core;
import os.server.HttpServer;
import os.type.ContentType;
import os.type.Cookie;
import os.type.Header;
import os.type.Status;
import os.type.Logger.Messages;
import os.util.Formatter;

public class HttpResponse extends Handler {
	
	public HttpResponse(HttpServer server, Socket socket) throws IOException {

		super.setSocket(socket);
	}
	
	public void commit() throws IOException {
		
		if(getStatus() != null) {
			
			emit("HTTP/1.1 " + getStatus().getMessage());
		}
		else {
			
			emit("HTTP/1.1 " + Status.BAD_REQUEST.getMessage());
		}
		
		if(!getHeader().isEmpty()) {
			
			for(String key : getHeader().keys()) {
				
				emit(key + ": " + getHeader().get(key));
			}
		}
		
		if(getBody() != null) {
			
			if(getHeader().hasKey(Header.CONTENT_TYPE)) {
				
				ContentType contentType = ContentType.getByType(getHeader().get(Header.CONTENT_TYPE));
				
				if(contentType != null && contentType == ContentType.HTML) {
					
					addAttribute("lang", getLanguage());
					
					String body = new String(getBody());
					
					body = Formatter.parseHTML(body, getTemplates());
					body = Formatter.parseHTML(body, getAttributes());
					body = Formatter.parseLang(body, getLanguage(), Core.LANGUAGES);
					body = Formatter.parseHref(body, getLanguage());
					
					setBody(body);
				}
			}
			
			emit("");
			emit(getBody());
		}
		
		setCommitted(true);
		
		getSocket().close();
	}
	
	public void ok() {
		
		setStatus(Status.OK);
	}
	
	public void notFound() {
		
		setStatus(Status.NOT_FOUND);
	}
	
	public void forbidden() {
		
		setStatus(Status.FORBIDDEN);
	}
	
	public void redirect(String location) {
		
		setLocation(location);
		setStatus(Status.FOUND);
	}
	
	public void badRequest() {
		
		setStatus(Status.BAD_REQUEST);
	}
	
	public void showPage(String path) {
		
		try {
			
			File file = new File(Core.ROOT + path);
			setBody(Files.readAllBytes(file.toPath()));
			setContentType(ContentType.getByFile(file.toPath().toString()));
			ok();
		}
		catch(IOException e) {
			
			Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(path));
			notFound();
		}
	}
	
	public void showPage(String path, ContentType contentType) {
		
		try {
			
			File file = new File(Core.ROOT + path);
			setBody(Files.readAllBytes(file.toPath()));
			setContentType(contentType);
			ok();
		}
		catch(IOException e) {
			
			Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(path));
			notFound();
		}
	}
	
	public void showBody(String body, ContentType contentType) {
		
		setBody(body);
		setContentType(contentType);
		ok();
	}
	
	public void addAttribute(String key, String value) {
		
		getAttributes().set(key, value);
	}
	
	public void addTemplate(String key, String path) {
		
		try {
			
			File file = new File(Core.ROOT + Core.RESOURCE_PATH + path);
			getTemplates().set(key, String.join("", Files.readAllLines(file.toPath())));
		}
		catch(IOException e) {
			
			Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(path));
		}
	}
	
	public void setCookie(Cookie cookie) {
		
		getHeader().set(Header.SET_COOKIE, cookie.getKey() + "=" + cookie.getValue() + "; Max-Age=" + cookie.getAge() + "; Expires=" + cookie.getAge());
	}
	
	public void setLocation(String location) {
		
		getHeader().set(Header.LOCATION, location);
	}
	
	public void setContentLength(int length) {
		
		getHeader().set(Header.CONTENT_LENGTH, String.valueOf(length));
	}
	
	public void setContentEncoding(String contentEncoding) {
		
		getHeader().set(Header.CONTENT_ENCODING, contentEncoding);
	}
	
	public void setContentType(ContentType contentType) {
		
		getHeader().set(Header.CONTENT_TYPE, contentType.getType() + " charset=UTF-8");
	}
}

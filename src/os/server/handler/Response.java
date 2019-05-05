package os.server.handler;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;

import os.server.Logger.Messages;
import os.server.Server;
import os.server.classes.Cookie;
import os.server.constants.Keys;
import os.server.type.ContentEncoding;
import os.server.type.ContentType;
import os.server.type.Status;
import os.util.Parser;

public class Response extends Handler {
	
	public Response(Server server, Socket socket) throws IOException {

		super(server);
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
			
			for(String key : getHeader().keySet()) {
				
				emit(key + ": " + getHeader().get(key));
			}
		}
		
		if(getBody() != null) {
			
			if(getHeader().containsKey(Keys.CONTENT_TYPE)) {
				
				ContentType contentType = ContentType.getByType(getHeader().get(Keys.CONTENT_TYPE));
				
				if(contentType != null && contentType == ContentType.HTML) {
					
					addAttribute("lang", getLanguage());
					
					String body = new String(getBody());
					
					body = Parser.parseHTML(body, getTemplates());
					body = Parser.parseHTML(body, getAttributes());
					body = Parser.parseLang(body, getLanguage(), Server.languages);
					body = Parser.parseHref(body, getLanguage());
					
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
			
			File file = new File(getServer().getRootPath() + path);
			setBody(Files.readAllBytes(file.toPath()));
			setContentType(ContentType.getByFile(file.toPath().toString()));
			ok();
		}
		catch(IOException e) {
			
			getServer().getLogger().warn(Messages.NOT_FOUND.getMessage(path));
			notFound();
		}
	}
	
	public void showPage(String path, ContentType contentType) {
		
		try {
			
			File file = new File(getServer().getRootPath() + path);
			setBody(Files.readAllBytes(file.toPath()));
			setContentType(contentType);
			ok();
		}
		catch(IOException e) {
			
			getServer().getLogger().warn(Messages.NOT_FOUND.getMessage(path));
			notFound();
		}
	}
	
	public void showBody(String body, ContentType contentType) {
		
		setBody(body);
		setContentType(contentType);
		ok();
	}
	
	public void addAttribute(String key, String value) {
		
		getAttributes().put(key, value);
	}
	
	public void addTemplate(String key, String path) {
		
		try {
			
			File file = new File(getServer().getRootPath() + path);
			getTemplates().put(key, String.join("", Files.readAllLines(file.toPath())));
		}
		catch(IOException e) {
			
			getServer().getLogger().warn(Messages.NOT_FOUND.getMessage(path));
		}
	}
	
	public void setCookie(Cookie cookie) {
		
		getHeader().put(Keys.SET_COOKIE, cookie.getKey() + "=" + cookie.getValue() + "; Max-Age=" + cookie.getAge() + "; Expires=" + cookie.getAge());
	}
	
	public void setLocation(String location) {
		
		getHeader().put(Keys.LOCATION, location);
	}
	
	public void setContentLength(int length) {
		
		getHeader().put(Keys.CONTENT_LENGTH, String.valueOf(length));
	}
	
	public void setContentEncoding(ContentEncoding contentEncoding) {
		
		getHeader().put(Keys.CONTENT_ENCODING, contentEncoding.getType());
	}
	
	public void setContentType(ContentType contentType) {
		
		getHeader().put(Keys.CONTENT_TYPE, contentType.getType() + " charset=UTF-8");
	}
}

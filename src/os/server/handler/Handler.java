package os.server.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;

import os.server.classes.Header;
import os.server.type.ContentType;
import os.server.type.RequestMethod;
import os.server.type.Status;
import os.util.Utils;

public class Handler {

	private RequestMethod method;
	private Status status;
	private Header header;
	private HashMap<String, String> parameter;
	private HashMap<String, String> attributes;
	private String url;
	private byte[] body;
	
	private Socket socket;
	
	private BufferedReader in;
	private OutputStream out;
	
	public Handler(Socket socket) throws IOException {
		
		this.socket = socket;
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = socket.getOutputStream();
		
		header = new Header();
		parameter = new HashMap<String, String>();
		attributes = new HashMap<String, String>();
	}
	
	public void request() throws IOException {
		
		String line;
		
		while(Utils.notEmpty(line = in.readLine())) {
			
			if(line.contains("HTTP")) {
				
				setMethod(RequestMethod.valueOf(line.split(" ")[0]));
				setUrl(line.split(" ")[1]);
			}
			else {
				
				header.getHeader().put(line.split(": ")[0], line.split(": ")[1]);
			}
		}
		
		if(method == RequestMethod.POST) {
			
			if(header.getContentLength() != null) {
				
				char[] chars = new char[header.getContentLength()];
				
				in.read(chars, 0, header.getContentLength());
				
				String body = String.valueOf(chars);

				body = body.replace("+", " ");
				body = body.replace("%40", "@");
				body = body.replace("%21", "!");
				
				if(body.contains("\n")) {
					
					for(String bodyLine : body.split("\n")) {
						
						if(bodyLine.contains("=")) {
							
							parameter.put(bodyLine.split("=")[0], bodyLine.split("=")[1]);
						}
					}
				}
				else {
					
					if(body.contains("=")) {
						
						parameter.put(body.split("=")[0], body.split("=")[1]);
					}
				}
				
				setBody(body);
			}
		}
	}
	
	public void respond() throws IOException {
		
		if(status != null) {
			
			emit("HTTP/1.1 " + status.getMessage());
		}
		
		if(!header.getHeader().isEmpty()) {
			
			for(String key : header.getHeader().keySet()) {
				
				emit(key + ": " + header.getHeader().get(key));
			}
		}
		
		if(body != null) {
			
			if(header.getContentType() != null) {
				
				ContentType contentType = header.getContentType();
				
				if(contentType == ContentType.HTML) {
					
					String html = new String(body);
					
					for(String key : attributes.keySet()) {
						
						html = html.replaceAll("\\$\\{" + key + "\\}", attributes.get(key));
					}
					
					setBody(html);
				}
			}
			
			emit("");
			emit(body);
		}
	}
	
	public void emit(String line) throws IOException {
		
		out.write((line + "\n").getBytes());
	}
	
	public void emit(byte[] data) throws IOException {
		
		out.write(data);
	}
	
	public void redirect(String url) {
		
		setStatus(Status.FOUND);
		header.setLocation(url);
	}
	
	public void notFound() {
		
		setStatus(Status.NOT_FOUND);
	}
	
	public void ok() {
		
		setStatus(Status.OK);
	}
	
	public void addAttribute(String key, String value) {
		
		attributes.put(key, value);
	}
	
	public void showPage(String path, ContentType type) {
		
		try {
			
			File file = new File(Utils.getCurrentPath() + "/public" + path);
			setBody(Files.readAllBytes(file.toPath()));
			header.setContentType(type);
			ok();
		}
		catch(IOException e) {
			
			notFound();
		}
	}
	
	public void showBody(String body, ContentType type) {
		
		setBody(body);
		header.setContentType(type);
		ok();
	}

	public RequestMethod getMethod() {
		
		return method;
	}

	public void setMethod(RequestMethod method) {
		
		this.method = method;
	}
	
	public Status getStatus() {
		
		return status;
	}
	
	public void setStatus(Status status) {
		
		this.status = status;
	}
	
	public Header getHeader() {
		
		return header;
	}
	
	public void setHeader(Header header) {
		
		this.header = header;
	}
	
	public HashMap<String, String> getParameter() {
		
		return parameter;
	}
	
	public void setParameter(HashMap<String, String> parameter) {
		
		this.parameter = parameter;
	}
	
	public HashMap<String, String> getAttributes() {
		
		return attributes;
	}
	
	public void setAttributes(HashMap<String, String> attributes) {
		
		this.attributes = attributes;
	}
	
	public String getUrl() {
		
		return url;
	}
	
	public void setUrl(String url) {
		
		this.url = url;
	}

	public byte[] getBody() {
		
		return body;
	}

	public void setBody(String body) {
		
		setBody(body.getBytes());
	}
	
	public void setBody(byte[] body) {
		
		this.body = body;
	}
	
	public Socket getSocket() {
		
		return socket;
	}
	
	public BufferedReader getInput() {
		
		return in;
	}
	
	public OutputStream getOutput() {
		
		return out;
	}
}

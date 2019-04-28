package os.server.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

import main.NodeOS;
import os.server.classes.Header;
import os.server.type.ContentType;
import os.server.type.RequestMethod;
import os.server.type.Status;
import os.util.URLParser;
import os.util.Utils;

public class Handler {

	private RequestMethod method;
	private Status status;
	private Header header;
	private HashMap<String, String> parameter;
	private HashMap<String, String> attributes;
	private HashMap<String, String> templates;
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
		templates = new HashMap<String, String>();
	}
	
	/** request() **/
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

				body = URLParser.parseURL(body);
				
				if(body.contains("&")) {
					
					for(String bodyLine : body.split("&")) {
						
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
	
	/** respond() **/
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
					
					for(String key : templates.keySet()) {
						
						html = html.replaceAll("\\@\\{" + key + "\\}", templates.get(key));
					}
					
					for(String key : attributes.keySet()) {
						
						html = html.replaceAll("\\@\\{" + key + "\\}", attributes.get(key));
					}
					
					setBody(html);
				}
			}
			
			emit("");
			emit(body);
		}
	}
	
	/** emit() **/
	public void emit(String line) throws IOException {
		
		out.write((line + "\n").getBytes());
	}
	
	/** emit() **/
	public void emit(byte[] data) throws IOException {
		
		out.write(data);
	}
	
	/** redirect() **/
	public void redirect(String url) {
		
		setStatus(Status.FOUND);
		header.setLocation(url);
	}
	
	/** notFound() **/
	public void notFound() {
		
		setStatus(Status.NOT_FOUND);
	}
	
	/** ok() **/
	public void ok() {
		
		setStatus(Status.OK);
	}
	
	/** addAttribute() **/
	public void addAttribute(String key, String value) {
		
		attributes.put(key, value);
	}
	
	/** showPage() **/
	public void showPage(String path, ContentType type) {
		
		try {
			
			File file = new File(NodeOS.getCurrentPath() + "/public" + path);
			setBody(Files.readAllBytes(file.toPath()));
			header.setContentType(type);
			ok();
		}
		catch(IOException e) {
			
			notFound();
		}
	}
	
	/** showBody() **/
	public void showBody(String body, ContentType type) {
		
		setBody(body);
		header.setContentType(type);
		ok();
	}
	
	/** addTemplate() **/
	public void addTemplate(String key, String path) {
		
		try {
			
			File file = new File(NodeOS.getCurrentPath() + "/public" + path);
			templates.put(key, String.join("", Files.readAllLines(file.toPath())));
		}
		catch(IOException e) {
			
			e.printStackTrace();
			
			// TODO LOG
		}
	}
	
	/** requestKey **/
	public String requestKey(String username, String password) {
		
		// TODO
		return UUID.randomUUID().toString();
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
	
	public HashMap<String, String> getTemplates() {
		
		return templates;
	}
	
	public void setTemplate(HashMap<String, String> templates) {
		
		this.templates = templates;
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

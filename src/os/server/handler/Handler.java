package os.server.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import os.type.RequestMethod;
import os.type.Status;
import os.util.Property;

public class Handler {

	private Socket socket;
	private BufferedReader in;
	private OutputStream out;
	
	private RequestMethod method;
	private Status status;
	private String url;
	private Property<String> header;
	private Property<String> parameter;
	private Property<String> attributes;
	private Property<String> templates;
	private byte[] body;
	private boolean committed = false;
	private String lang;
	
	public Handler() {
		
		header = new Property<String>();
		parameter = new Property<String>();
		attributes = new Property<String>();
		templates = new Property<String>();
	}
	
	public void emit(String line) throws IOException {
		
		out.write((line + "\n").getBytes());
	}
	
	public void emit(byte[] data) throws IOException {
		
		out.write(data);
	}

	public Socket getSocket() {
	
		return socket;
	}
	
	public void setSocket(Socket socket) throws IOException {
		
		this.socket = socket;
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = socket.getOutputStream();
	}

	public BufferedReader getIn() {
	
		return in;
	}

	public OutputStream getOut() {
	
		return out;
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
	
	public String getUrl() {
	
		return url;
	}
	
	public void setUrl(String url) {
	
		this.url = url;
	}

	public Property<String> getHeader() {
		
		return header;
	}
	
	public Property<String> getParameter() {
	
		return parameter;
	}
	
	public Property<String> getAttributes() {
	
		return attributes;
	}
	
	public Property<String> getTemplates() {
	
		return templates;
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
	
	public boolean isCommitted() {
		
		return committed;
	}
	
	public void setCommitted(boolean committed) {
		
		this.committed = committed;
	}
	
	public void setLanguage(String lang) {
		
		this.lang = lang;
	}
	
	public String getLanguage() {
		
		return lang;
	}
}

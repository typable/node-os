package os.server.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

import os.server.Server;
import os.server.type.RequestMethod;
import os.server.type.Status;

public class Handler {

	private Server server;
	private Socket socket;
	private BufferedReader in;
	private OutputStream out;
	
	private RequestMethod method;
	private Status status;
	private String url;
	private HashMap<String, String> header;
	private HashMap<String, String> parameter;
	private HashMap<String, String> attributes;
	private HashMap<String, String> templates;
	private byte[] body;
	private boolean committed = false;
	
	public Handler(Server server, Socket socket) throws IOException {
		
		this.server = server;
		this.socket = socket;
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = socket.getOutputStream();
		
		header = new HashMap<String, String>();
		parameter = new HashMap<String, String>();
		attributes = new HashMap<String, String>();
		templates = new HashMap<String, String>();
	}
	
	public void emit(String line) throws IOException {
		
		out.write((line + "\n").getBytes());
	}
	
	public void emit(byte[] data) throws IOException {
		
		out.write(data);
	}
	
	public Server getServer() {
		
		return server;
	}

	public Socket getSocket() {
	
		return socket;
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

	public HashMap<String, String> getHeader() {
		
		return header;
	}
	
	public void setHeader(HashMap<String, String> header) {
	
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
	
	public void setTemplates(HashMap<String, String> templates) {
	
		this.templates = templates;
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
}

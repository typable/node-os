package os.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.net.ssl.SSLSocketFactory;

/**
 * @version 1.0.0
 * @author Prototype Studio
 */
public class Connection {

	private Socket socket;
	private BufferedReader in;
	private OutputStream out;
	
	private String host;
	private int port;
	private boolean isSSL;
	private final byte[] CRLF = { (byte) '\r', (byte) '\n' };
	
	/** 
	 * Creates a new Connection
	 * @param host Is the IP address to connect on
	 * @param port Is the Channel to bind
	**/
	public Connection(String host, int port) {
		
		this.host = host;
		this.port = port;
	}
	
	/** 
	 * Creates a new Connection
	 * @param host Is the IP address to connect on
	 * @param port Is the Channel to bind
	 * @param isSSL If a SSL connection should be used
	**/
	public Connection(String host, int port, boolean isSSL) {
		
		this.host = host;
		this.port = port;
		this.isSSL = isSSL;
	}
	
	/** 
	 * Creates a new Connection
	 * @param socket Is the socket
	**/
	public Connection(Socket socket) {
		
		this.socket = socket;
	}
	
	/** 
	 * Connects to the Server
	 * @param runnable Is the method who handle the connection
	**/
	public void connect(Runnable runnable) {
		
		try {
			
			if(socket == null) {
				
				if(isSSL) {
					
					SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
					socket = factory.createSocket(InetAddress.getByName(host), port);
				}
				else {
					
					socket = new Socket(host, port);
				}
			}
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			out = socket.getOutputStream();
			
			runnable.run();
		}
		catch(Exception ex) {
			
			ex.printStackTrace();
		}
	}
	
	/**
	 * Closes the connection
	**/
	public void quit() throws IOException {
		
		socket.close();
	}
	
	/**
	 * Sends the data
	 * @param data Is the value who would be send
	**/
	public void emit(String data) throws IOException {
		
		emit(data.getBytes());
	}
	
	/** 
	 * Sends the data encoded in Base64
	 * @param data Is the value who would be send
	 * @param base64 Defines if it would be converted to Base64
	**/
	public void emit(String data, boolean base64) throws IOException {
		
		emit(data.getBytes(), base64);
	}
	
	/**
	 * Sends the data
	 * @param data Is the value who would be send
	**/
	public void emit(byte[] data) throws IOException {
		
		out.write(data);
		out.write(CRLF);
		out.flush();
	}
	
	/** 
	 * Sends the data encoded in Base64
	 * @param data Is the value who would be send
	 * @param base64 Defines if it would be converted to Base64
	**/
	public void emit(byte[] data, boolean base64) throws IOException {
		
		emit(Base64.getEncoder().encode(data));
	}
	
	/** 
	 * Reads incoming data
	**/
	public String read() throws IOException {
		
		return in.readLine();
	}

	/** 
	 * Returns socket
	**/
	public Socket getSocket() {
		
		return socket;
	}

	/** 
	 * Returns in
	**/
	public BufferedReader getIn() {
		
		return in;
	}

	/** 
	 * Returns out
	**/
	public OutputStream getOut() {
		
		return out;
	}

	/** 
	 * Returns host
	**/
	public String getHost() {
		
		return host;
	}

	/** 
	 * Returns port
	**/
	public int getPort() {
		
		return port;
	}
	
	/** 
	 * Returns isSSL
	**/
	public boolean isSSL() {
		
		return isSSL;
	}
}

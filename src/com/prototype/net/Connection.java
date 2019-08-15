package com.prototype.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.net.ssl.SSLSocketFactory;


/**
 * @version 1.3.4
 * @author Prototype Studio
 */
public class Connection {

	public final String CRLF = "\r\n";
	public final String LF = "\n";
	public final Charset CHARSET = StandardCharsets.ISO_8859_1;

	private Socket socket;
	private InputStream in;
	private BufferedReader reader;
	private OutputStream out;

	private String host;
	private int port;
	private boolean isSSL;
	private byte[] LINE_BREAK;

	/**
	 * Creates a new Connection
	 * 
	 * @param host Is the IP address to connect on
	 * @param port Is the Channel to bind
	 **/
	public Connection(String host, int port) {

		this(host, port, false);
	}

	/**
	 * Creates a new Connection
	 * 
	 * @param socket Is the socket
	 **/
	public Connection(Socket socket) {

		this(socket.getInetAddress().getHostAddress(), socket.getPort(), socket.getPort() == 443);
		this.socket = socket;
	}

	/**
	 * Creates a new Connection
	 * 
	 * @param host  Is the IP address to connect on
	 * @param port  Is the Channel to bind
	 * @param isSSL If a SSL connection should be used
	 **/
	public Connection(String host, int port, boolean isSSL) {

		this.host = host;
		this.port = port;
		this.isSSL = isSSL;

		LINE_BREAK = (isUNIX() ? LF : CRLF).getBytes(CHARSET);
	}

	/**
	 * Connects to the Server
	 * 
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

			in = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET));
			out = socket.getOutputStream();

			new Thread(runnable).start();
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
	 * 
	 * @param data Is the value who would be send
	 **/
	public void emit(String data) throws IOException {

		emit(data.getBytes(CHARSET));
	}

	/**
	 * Sends the data encoded in Base64
	 * 
	 * @param data   Is the value who would be send
	 * @param base64 Defines if it would be converted to Base64
	 **/
	public void emit(String data, boolean base64) throws IOException {

		emit(data.getBytes(CHARSET), base64);
	}

	/**
	 * Sends the data
	 * 
	 * @param data Is the value who would be send
	 **/
	public void emit(byte[] data) throws IOException {

		out.write(data);
		out.write(LINE_BREAK);
		out.flush();
	}

	/**
	 * Sends the data encoded in Base64
	 * 
	 * @param data   Is the value who would be send
	 * @param base64 Defines if it would be converted to Base64
	 **/
	public void emit(byte[] data, boolean base64) throws IOException {

		emit(Base64.getEncoder().encode(data));
	}

	/**
	 * Reads incoming data as String
	 **/
	public String readLine() throws IOException {

		return reader.readLine();
	}

	/**
	 * Reads incoming data as String by a specific length
	 */
	public byte[] read(int length) throws IOException {

		char[] buffer = new char[length];

		reader.read(buffer, 0, length);

		return String.valueOf(buffer).getBytes(CHARSET);
	}

	/**
	 * Check if it is UNIX
	 */
	private boolean isUNIX() {

		final String osName = System.getProperty("os.name");

		return !osName.startsWith("Windows");
	}

	/**
	 * Returns socket
	 **/
	public Socket getSocket() {

		return socket;
	}

	/**
	 * Returns reader
	 */
	public BufferedReader getReader() {

		return reader;
	}

	/**
	 * Returns in
	 **/
	public InputStream getIn() {

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

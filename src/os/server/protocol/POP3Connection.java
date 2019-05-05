package os.server.protocol;

import java.io.IOException;

import os.server.Connection;

public class POP3Connection extends Connection {

	public POP3Connection(String host, int port) {
		
		super(host, port);
	}
	
	public POP3Connection(String host, int port, boolean isSSL) {
		
		super(host, port, isSSL);
	}
	
	public void send(String username, String password) throws IOException {
		
		connect(() -> {
			
			try {
				
				System.out.println(read());
				emit("USER " + username);
				System.out.println(read());
				emit("PASS " + password);
				System.out.println(read());
				emit("STAT");
				System.out.println(read());
				emit("RETR 2");
				System.out.println(read());
				String line;
				while((line = read()) != null) {
					
					if(line.equals(".")) {
						
						break;
					}
					
					System.out.println(line);
				}
				quit();
			}
			catch(IOException e) {
				
				e.printStackTrace();
			}
		});
	}
}

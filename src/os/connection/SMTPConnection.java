package os.connection;

import java.io.IOException;

import os.server.classes.Email;

public class SMTPConnection extends Connection {

	public SMTPConnection(String host, int port) {
		
		super(host, port);
	}
	
	public SMTPConnection(String host, int port, boolean isSSL) {
		
		super(host, port, isSSL);
	}
	
	public void send(Email email, String username, String password) throws IOException {
		
		if(email != null) {
			
			connect(() -> {
				
				try {
					
					emit("EHLO localhost");
					System.out.println(read());
					emit("AUTH LOGIN");
					System.out.println(read());
					System.out.println(read());
					System.out.println(read());
					System.out.println(read());
					System.out.println(read());
					System.out.println(read());
					System.out.println(read());
					System.out.println(read());
					System.out.println(read());
					emit(username, true);
					System.out.println(read());
					emit(password, true);
					System.out.println(read());
					emit("MAIL FROM:<" + email.getFrom() + ">");
					System.out.println(read());
					emit("RCPT TO:<" + email.getTo() + ">");
					System.out.println(read());
					emit("DATA");
					System.out.println(read());
					if(!isSSL()) {
						System.out.println(read());
					}
					emit("From: " + email.getFrom());
					emit("To: " + email.getTo());
					emit("Subject: " + email.getSubject());
					emit("Content-Type: text/html; charset=\"utf-8\";");
					emit("");
					emit(email.getBody());
					emit(".");
					System.out.println(read());
					emit("QUIT");
					System.out.println(read());
					
					quit();
				}
				catch(IOException e) {
					
					e.printStackTrace();
				}
			});
		}
	}
}

package os.handler;

import net.Connection;
import os.type.Email;


public class SmtpConnection extends Connection {

	public SmtpConnection(String host, int port) {

		this(host, port, false);
	}

	public SmtpConnection(String host, int port, boolean ssl) {

		super(host, port, ssl);
	}

	public void send(Email email, String username, String password) throws Exception {

		if(email != null) {

			connect(new Runnable() {

				@Override
				public void run() {

					try {

						emit("EHLO localhost");
						System.out.println(readLine());
						emit("AUTH LOGIN");
						System.out.println(readLine());
						System.out.println(readLine());
						System.out.println(readLine());
						System.out.println(readLine());
						System.out.println(readLine());
						System.out.println(readLine());
						System.out.println(readLine());
						System.out.println(readLine());
						System.out.println(readLine());
						emit(username, true);
						System.out.println(readLine());
						emit(password, true);
						System.out.println(readLine());
						emit("MAIL FROM:<" + email.getFrom() + ">");
						System.out.println(readLine());
						emit("RCPT TO:<" + email.getTo() + ">");
						System.out.println(readLine());
						emit("DATA");
						System.out.println(readLine());
						if(!isSSL()) {
							System.out.println(readLine());
						}
						emit("From: " + email.getFrom());
						emit("To: " + email.getTo());
						emit("Subject: " + email.getSubject());
						// emit("Content-Type: text/html; charset=\"utf-8\";");
						emit("");
						emit(email.getBody());
						emit(".");
						System.out.println(readLine());
						emit("QUIT");
						System.out.println(readLine());

						quit();

					}
					catch(Exception e) {

						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void main(String[] args) throws Exception {

		SmtpConnection bind = new SmtpConnection("smtp.gmail.com", 465, true);

		Email email = new Email("byblockhd@gmail.com", "andy.sitzler@gmail.com");
		email.setSubject("Hello World!");
		email.setBody("This is a short introduction!");

		bind.send(email, "byblockhd@gmail.com", "VcR98uE4");
	}
}

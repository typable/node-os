package main;

import java.io.File;
import java.io.IOException;

import main.controller.EmailController;
import main.controller.LoginController;
import main.controller.MainController;
import os.server.Server;
import os.server.protocol.FTPConnection;

public class Main {

	public static void main(String[] args) throws IOException {
		
		Server server = new Server();
		server.registerController(MainController.class);
		server.registerController(LoginController.class);
		server.registerController(EmailController.class);
		// server.launch();
		
		// File file = new File("C:/DATA/git/NodeOS/public/src/database/users.json");
		File file = new File("C:/DATA/freetime/Projects/Hub/src/sap.png");
		
		FTPConnection ftp = new FTPConnection("localhost", 21);
		ftp.send(file, "C:/DATA/FTP");
	}
}

package main;

import java.io.IOException;

import main.controller.EmailController;
import main.controller.LoginController;
import main.controller.MainController;
import os.server.Server;

public class Main {

	public static void main(String[] args) throws IOException {
		
		Server server = new Server();
		server.registerController(MainController.class);
		server.registerController(LoginController.class);
		server.registerController(EmailController.class);
		server.launch();
	}
}

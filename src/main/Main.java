package main;


import main.controller.EmailController;
import main.controller.LoginController;
import main.controller.MainController;
import os.core.Core;

public class Main {

	public static void main(String[] args) throws Exception {
		
		Core core = new Core();
		core.addController(MainController.class);
		core.addController(LoginController.class);
		core.addController(EmailController.class);
		
		core.launch();
	}
}

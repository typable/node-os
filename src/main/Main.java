package main;

import java.io.IOException;

import main.controller.LoginController;
import main.controller.MainController;
import os.server.Server;
import os.util.JsonFile;
import os.util.JsonObject;

public class Main {

	public static void main(String[] args) {
		
		Server server = new Server();
		server.registerController(MainController.class);
		server.registerController(LoginController.class);
		// server.launch();
		
		JsonFile jsonfile = new JsonFile("C:/DATA/git/NodeOS/public/src/database/users.json");
		try {
			
			JsonObject root = new JsonObject();
			
			JsonObject[] array = new JsonObject[2];
			
			JsonObject array0 = new JsonObject();
			array0.set("name", "Andreas Sitzler");
			array0.set("email", "andy.sitzler@gmail.com");
			array0.set("password", "123");
			
			array[0] = array0;
			
			root.set("users", array);
			
			jsonfile.setJson(root);
			jsonfile.save();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

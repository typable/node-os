package main;

import os.server.Server;
import os.server.handler.Handler;
import os.server.note.Request;
import os.server.type.ContentType;
import os.server.type.RequestMethod;

public class Main extends Server {

	public Main() {
		
		launch(80);
	}

	public static void main(String[] args) {
		
		new Main();
	}
	
	@Request(url = "/")
	public void getRoot(Handler request, Handler response) {
		
		response.addAttribute("hello", "Hello World!");
		response.showPage("/index.html", ContentType.HTML);
	}
	
	@Request(url = "/image")
	public void getLogin(Handler request, Handler response) {
		
		response.showPage("/src/image.jpg", ContentType.JPG);
	}
	
	@Request(url = "/login", method = RequestMethod.POST)
	public void postLogin(Handler request, Handler response) {
		
		String param = request.getParameter().get("password");
		response.showBody(param, ContentType.HTML);
	}
}

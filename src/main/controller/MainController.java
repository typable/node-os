package main.controller;

import os.server.Server;
import os.server.classes.Cookie;
import os.server.handler.Request;
import os.server.handler.Response;
import os.server.note.Controller;
import os.server.note.RequestHandler;

@Controller
public class MainController {

	private final String COOKIE_NAME = "uuid";
	
	private final String TEMPLATE_PATH = Server.RESOURCE_PATH + "/template";
	
	@RequestHandler(url = "/")
	public void getRoot(Request request, Response response) {
		
		Cookie cookie = request.getCookie(COOKIE_NAME);
		String code = "";
		
		if(cookie != null) {
			
			String userProfile = "https://lh3.googleusercontent.com/-a8ulId-i9cY/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rcieSoPf-8Y_6Sch5sY7H7PTUttJw/s96-c-mo/photo.jpg";
			
			code = "<div class='icon m-1 tooltip'><div class='material-icons'>notifications_none</div><div class='tooltip-text tooltip-icon'>Notifications</div></div>"
					+ "<div class='account m-1 tooltip'><div class='image image-round'><img src='" + userProfile + "'></div><div class='tooltip-text tooltip-icon' style='transform: translateX(calc(-50% + 22px));'>Account</div></div>" +
					"<form action='/logout' method='post'><button class='signin m-1'>Log Out</button></form>";
		}
		else {
			
			code = "<a href='/login' class='text-none'><div class='signin m-1'>Log In</div></a>";
		}
		
		response.addTemplate("nav", TEMPLATE_PATH + "/nav.html");
		response.addAttribute("login", code);
		response.showPage("/index.html");
	}
}

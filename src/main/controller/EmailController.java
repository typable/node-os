package main.controller;

import java.io.IOException;

import os.server.Server;
import os.server.classes.Cookie;
import os.server.classes.Email;
import os.server.handler.Request;
import os.server.handler.Response;
import os.server.note.Controller;
import os.server.note.RequestHandler;
import os.server.protocol.SMTPConnection;
import os.server.type.RequestMethod;

@Controller
public class EmailController {

	private final String COOKIE_NAME = "uuid";
	
	private final String TEMPLATE_PATH = Server.RESOURCE_PATH + "/template";
	
	@RequestHandler(url = "/email")
	public void getEmail(Request request, Response response) {
		
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
		response.showPage("/email.html");
	}
	
	@RequestHandler(url = "/email", method = RequestMethod.POST)
	public void postEmail(Request request, Response response) throws IOException {
		
		String subject = request.getParameter().get("subject");
		String address = request.getParameter().get("email");
		String message = request.getParameter().get("message");
		
		new Thread(() -> {
			
			Email email = new Email("byblockhd@gmail.com", "andy.sitzler@gmail.com");
			email.setSubject(subject);
			email.setBody("<h2>" + address + "</h2><div>" + message + "</div>");
			
			SMTPConnection smtp = new SMTPConnection("smtp.gmail.com", 465, true);
			
			try {
				
				smtp.send(email, "byblockhd@gmail.com", "VcR98uE4");
			}
			catch(IOException e) {
				
				e.printStackTrace();
			}
			
		}).start();
		
		response.redirect("/");
	}
}

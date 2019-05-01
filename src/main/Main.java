package main;

import os.server.Server;
import os.server.classes.Cookie;
import os.server.handler.Request;
import os.server.handler.Response;
import os.server.note.RequestHandler;
import os.server.type.RequestMethod;

public class Main extends Server {

	private final String COOKIE_NAME = "uuid";
	
	private final String TEMPLATE_PATH = Server.RESOURCE_PATH + "/template";
	
	public Main() {
		
		launch();
	}

	public static void main(String[] args) {
		
		new Main();
	}
	
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
	
	@RequestHandler(url = "/login")
	public void getLogin(Request request, Response response) {
		
		response.addTemplate("nav", TEMPLATE_PATH + "/nav.html");
		response.addAttribute("login", "");
		response.showPage("/login.html");
	}
	
	@RequestHandler(url = "/login", method = RequestMethod.POST)
	public void postLogin(Request request, Response response) {
		
		String username = request.getParameter().get("username");
		String password = request.getParameter().get("password");
		
		if(username.equals("Andy") && password.equals("123")) {
			
			Cookie cookie = new Cookie(COOKIE_NAME, "56GD-5R7N-PW3X-O30IU");
			cookie.setAge(60 * 60 * 24);
			
			response.setCookie(cookie);
			response.redirect("/");
		}
		else {
			
			response.redirect("/login");
		}
	}
	
	@RequestHandler(url = "/logout", method = RequestMethod.POST)
	public void postLogout(Request request, Response response) {
		
		Cookie cookie = new Cookie(COOKIE_NAME, null);
		cookie.setAge(0);
		
		response.setCookie(cookie);
		response.redirect("/");
	}
}

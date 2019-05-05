package main.controller;

import os.server.Server;
import os.server.classes.Cookie;
import os.server.handler.Request;
import os.server.handler.Response;
import os.server.note.Controller;
import os.server.note.RequestHandler;
import os.server.type.RequestMethod;

@Controller
public class LoginController {

private final String COOKIE_NAME = "uuid";
	
	private final String TEMPLATE_PATH = Server.RESOURCE_PATH + "/template";
	
	@RequestHandler(url = "/login")
	public void getLogin(Request request, Response response) {
		
		response.addTemplate("header", TEMPLATE_PATH + "/header.html");
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
		
		String lang = request.getLanguage();
		
		if(lang.equals("en")) {
			
			response.redirect("/");
		}
		else {
			
			response.redirect("/?lang=" + lang);
		}
	}
}

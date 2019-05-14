package main.controller;

import os.server.classes.Cookie;
import os.server.classes.User;
import os.server.handler.HttpRequest;
import os.server.handler.HttpResponse;
import os.server.type.Inject;
import os.server.type.Request;
import os.server.type.RequestMethod;
import os.service.UserService;

public class LoginController {

	private final String COOKIE_NAME = "uuid";
	
	@Inject(src = "userService")
	private UserService userService;

	@Request(url = "/login")
	public void getLogin(HttpRequest request, HttpResponse response) {
		
		response.addTemplate("header", "/template/header.html");
		response.addTemplate("nav", "/template/nav.html");
		response.addAttribute("login", "");
		response.showPage("/login.html");
	}
	
	@Request(url = "/login", method = RequestMethod.POST)
	public void postLogin(HttpRequest request, HttpResponse response) {
		
		String username = request.getParameter().get("username");
		String password = request.getParameter().get("password");
		
		for(User user : userService.getAllUsers()) {
			
			if((username.equals(user.getName()) || username.equals(user.getEmail())) && password.equals(user.getPassword())) {
				
				Cookie cookie = new Cookie(COOKIE_NAME, user.getEmail()); // "56GD-5R7N-PW3X-O30IU"
				cookie.setAge(60 * 60 * 24);
				
				response.setCookie(cookie);
				response.redirect("/");
				
				return;
			}
		}
		
		response.redirect("/login");
	}
	
	@Request(url = "/logout", method = RequestMethod.POST)
	public void postLogout(HttpRequest request, HttpResponse response) {
		
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

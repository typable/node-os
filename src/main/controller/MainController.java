package main.controller;

import java.io.IOException;

import os.core.Core;
import os.server.classes.Cookie;
import os.server.handler.HttpRequest;
import os.server.handler.HttpResponse;
import os.server.type.Request;
import os.server.type.RequestMethod;
import os.util.File;
import os.util.JsonObject;

public class MainController {

	private final String COOKIE_NAME = "uuid";
	
	@Request(url = "/")
	public void getRoot(HttpRequest request, HttpResponse response) {
		
		requestPage(request, response);
		
		response.showPage("/index.html");
	}
	
	@Request(url = "/search", method = RequestMethod.POST)
	public void postSearch(HttpRequest request, HttpResponse response) {
		
		String query = request.getParameter().get("q");
		
		String lang = request.getLanguage();
		
		if(lang.equals("en")) {
			
			response.redirect("/" + query);
		}
		else {
			
			response.redirect("/" + query + "?lang=" + lang);
		}
	}
	
	public void requestPage(HttpRequest request, HttpResponse response) {
		
		Cookie cookie = request.getCookie(COOKIE_NAME);
		String code = "";
		
		if(cookie != null) {
			
			File file = new File(Core.ROOT + Core.RESOURCE_PATH + "/database/users.json");
			JsonObject json = null;
			String userProfile = "";
			
			try {
				
				file.load();
				json = file.getJson();
				userProfile = json.getString("profile");
				
			}
			catch(IOException e) {
				
				e.printStackTrace();
			}
			
			code = "<div class='icon m-1 tooltip'><div class='material-icons'>notifications_none</div><div class='tooltip-text tooltip-icon'>@{lang:nav.notification.tooltip}</div></div>"
					+ "<div class='account m-1 tooltip'><div class='image image-round'><img src='" + userProfile + "'></div><div class='tooltip-text tooltip-icon' style='transform: translateX(calc(-50% + 22px));'>@{lang:nav.account.tooltip}</div></div>" +
					"<form action='/logout' method='post'><button class='signin m-1'>@{lang:nav.logout}</button></form>";
		}
		else {
			
			code = "<a href='/login' class='text-none'><div class='signin m-1'>@{lang:nav.login}</div></a>";
		}
		
		response.addTemplate("header", "/template/header.html");
		response.addTemplate("nav", "/template/nav.html");
		response.addAttribute("login", code);
	}
}

package main;

import java.util.ArrayList;
import java.util.List;

import os.core.Core;
import os.handler.HttpRequest;
import os.handler.HttpResponse;
import os.type.MediaType;
import os.type.Request;
import os.type.RequestMethod;
import os.type.Session;
import os.type.User;
import util.file.JSONFile;
import util.type.JSONObject;


public class PageController {

	@Request(url = "/")
	private void getRoot(HttpRequest request, HttpResponse response) {

		response.addAttribute("google.apikey", Core.get("google.apikey"));
		response.addAttribute("meta.author", Core.get("meta.author"));
		response.addAttribute("meta.keywords", Core.get("meta.keywords"));
		response.addAttribute("meta.description", Core.get("meta.description"));
		response.viewPage("*/index.html", MediaType.TEXT_HTML);
	}

	@Request(url = "/impressum")
	private void getImpressum(HttpRequest request, HttpResponse response) {

		response.viewPage("*/impressum.html", MediaType.TEXT_HTML);
	}

	@Request(url = "/404")
	private void get404(HttpRequest request, HttpResponse response) {

		response.viewPage("*/404.html", MediaType.TEXT_HTML);
	}

	@Request(url = "/login")
	private void getLogin(HttpRequest request, HttpResponse response) {

		response.viewPage("*/login.html", MediaType.TEXT_HTML);
	}

	@Request(url = "/login", method = RequestMethod.POST)
	private void postLogin(HttpRequest request, HttpResponse response) {

		List<User> USERS = new ArrayList<User>();

		try {

			JSONFile file = new JSONFile(Core.ROOT + "/src/database/users.json");

			file.load();

			JSONObject obj = file.getJSONObject();

			for(JSONObject user : obj.getJSONObjectArray("users")) {

				USERS.add(new User(user.getString("name"), user.getString("email"), user.getString("password")));
			}

			String url = request.getParameter("url");
			String email = request.getParameter("email");
			String password = request.getParameter("password");

			if(email != null && password != null) {

				User currentUser = null;

				for(User user : USERS) {

					if(email.equals(user.getEmail()) && password.equals(user.getPassword())) {

						currentUser = user;
					}
				}

				if(currentUser != null) {

					Session session = request.getSession();
					session.setAttribute("user", currentUser);

					if(url != null && !url.equals("@{url}")) {

						response.redirect(url);
					}
					else {

						response.redirect("/");
					}

					return;
				}
			}
		}
		catch(Exception e) {

			e.printStackTrace();
		}

		response.redirect("/login");
	}

	@Request(url = "/styleguide")
	private void getStyleguide(HttpRequest request, HttpResponse response) {

		Session session = request.getSession();

		User user = (User) session.getAttribute("user");

		if(user != null) {

			response.viewPage("*/styleguide.html", MediaType.TEXT_HTML);
		}
		else {

			response.addAttribute("url", "/styleguide");
			response.viewPage("*/login.html", MediaType.TEXT_HTML);
		}
	}
}

package main;

import java.io.File;

import os.core.Core;
import os.handler.HttpRequest;
import os.handler.HttpResponse;
import os.type.Request;
import os.type.RequestMethod;
import os.type.Session;


public class PageController {

	@Request(url = "/")
	private void getIndex(HttpRequest request, HttpResponse response) {

		Session session = request.getSession();

		Boolean logged = (Boolean) session.getAttribute("logged");

		if(logged != null) {

			response.viewPage("*/index.html");
		}
		else {

			response.viewPage("*/login.html");
		}
	}

	@Request(url = "/login", method = RequestMethod.POST)
	private void postLogin(HttpRequest request, HttpResponse response) {

		Session session = request.getSession();

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if(username != null && password != null) {

			if(username.equals("andy") && password.equals("123")) {

				session.setAttribute("logged", true);
			}
		}

		response.redirect("/");
	}

	@Request(url = "/download")
	private void getDownload(HttpRequest request, HttpResponse response) {

		response.provideDownload(new File(Core.ROOT + "/index.html"));
	}
}

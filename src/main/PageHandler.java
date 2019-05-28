package main;

import os.core.Core;
import os.event.ViewPageEvent;
import os.handler.HttpRequest;
import os.handler.HttpResponse;
import os.service.AuthenticationService;
import os.service.SessionService;
import os.service.UserService;
import os.type.Event;
import os.type.Inject;
import os.type.Listener;
import os.type.Request;
import os.type.Session;
import os.type.User;
import os.type.constants.RequestMethod;
import os.util.Utils;


public class PageHandler implements Listener {

	@Inject(code = "userService")
	private UserService userService;

	@Inject(code = "authenticationService")
	private AuthenticationService authenticationService;

	@Inject(code = "sessionService")
	private SessionService sessionService;

	@Event
	private void onViewPage(ViewPageEvent event) {

		//
	}

	@Request(url = "/")
	private void getRoot(HttpRequest request, HttpResponse response) {

		response.addAttribute("google.apikey", Core.get("google.apikey"));
		response.viewPage("*/index.html");
	}

	@Request(url = "/impressum")
	private void getImpressum(HttpRequest request, HttpResponse response) {

		response.viewPage("*/impressum.html");
	}

	@Request(url = "/404")
	private void get404(HttpRequest request, HttpResponse response) {

		response.viewPage("*/404.html");
	}

	@Request(url = "/login")
	private void getLogin(HttpRequest request, HttpResponse response) {

		response.viewPage("*/login.html");
	}

	@Request(url = "/login", method = RequestMethod.POST)
	private void postLogin(HttpRequest request, HttpResponse response) {

		String url = request.getParameter("url");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		User user = authenticationService.login(email, password);

		if(user != null) {

			Session session = sessionService.getCurrentSession(request);

			if(session != null) {

				session.setAttribute("user", user);
				sessionService.getSessions().remove(session);
				sessionService.getSessions().add(session);

				if(Utils.notEmpty(url)) {

					response.redirect(url);
				}
				else {

					response.redirect("/");
				}
			}
		}
		else {

			response.redirect("/login");
		}
	}

	@Request(url = "/styleguide")
	private void getStyleguide(HttpRequest request, HttpResponse response) {

		User user = userService.getCurrentUser(request);

		if(user != null) {

			response.viewPage("*/styleguide.html");
		}
		else {

			response.addAttribute("url", "/styleguide");
			response.viewPage("*/login.html");
		}
	}
}

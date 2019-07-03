package main;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import core.reflect.Inject;
import os.core.Core;
import os.experimental.ContentService;
import os.handler.HttpRequest;
import os.handler.HttpResponse;
import os.service.AuthenticationService;
import os.service.SessionService;
import os.service.UserService;
import os.type.Request;
import os.type.Session;
import os.type.User;
import os.type.constants.MediaType;
import os.type.constants.RequestMethod;
import util.Utils;
import util.file.JSONFile;
import util.type.JSONObject;


public class PageHandler {

	@Inject
	private UserService userService;

	@Inject
	private AuthenticationService authenticationService;

	@Inject
	private SessionService sessionService;

	@Request(url = "/")
	private void getRoot(HttpRequest request, HttpResponse response) {

		response.addAttribute("google.apikey", Core.get("google.apikey"));
		response.viewPage("*/index.html");
	}

	@Request(url = "/impressum")
	private void getImpressum(HttpRequest request, HttpResponse response) {

		response.viewPage("*/impressum.html");
	}

	@Request(url = "/login")
	private void getLogin(HttpRequest request, HttpResponse response) {

		response.viewPage("*/login.html");
	}

	@Request(url = "/login", method = RequestMethod.POST)
	private void postLogin(HttpRequest request, HttpResponse response) {

		String url = request.getParameter("url");
		String email = request.getParameter("email");
		String password = Utils.encode(request.getParameter("password"));

		User user = authenticationService.login(email, password);

		if(user != null) {

			Session session = sessionService.getCurrentSession(request);

			if(session != null) {

				session.setAttribute("user", user);
				sessionService.getSessions().remove(session);
				sessionService.getSessions().add(session);

				if(url != null && !url.isBlank()) {

					response.redirect(url);
				}
				else {

					response.redirect("/");
				}
			}
			else {

				response.redirect("/login");
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

	@Request(url = "/download")
	private void getDownload(HttpRequest request, HttpResponse response) {

		User user = userService.getCurrentUser(request);

		if(user != null) {

			response.viewPage("*/download.html");
		}
		else {

			response.addAttribute("url", "/download");
			response.viewPage("*/login.html");
		}
	}

	@Request(url = "/download", method = RequestMethod.POST)
	private void postDownload(HttpRequest request, HttpResponse response) {

		String file = request.getParameter("file");

		if(file != null && !file.isBlank()) {

			response.provideDownload(file);
		}
		else {

			response.notFound();
		}
	}

	@Request(url = "/favicon.ico")
	private void getFavicon(HttpRequest request, HttpResponse response) {

		response.viewPage("*/src/assets/favicon.png", MediaType.IMAGE_PNG);

		response.ok();
	}

	/* ### PROTOTYPE-STUDIO ### */

	@Request(url = "/ps")
	private void getPs(HttpRequest request, HttpResponse response) {

		response.viewPage("*/ps/index.html");
	}

	@Request(url = "/ps/viewer")
	private void getPsViewer(HttpRequest request, HttpResponse response) {

		response.viewPage("*/ps/viewer.html");
	}

	@Request(url = "/ps/update-image", method = RequestMethod.POST)
	private void postPsUpdateImage(HttpRequest request, HttpResponse response) {

		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String tags = request.getParameter("tags");
		String description = request.getParameter("description");

		ContentService contentService = new ContentService();

		JSONObject obj = contentService.prepareImage(id, name, tags.trim(), description);

		try {

			JSONFile file = new JSONFile(Core.getResourcePath("/src/database/content.json"));

			if(file.exists()) {

				file.load();

				JSONObject root = file.getJSONObject();

				List<JSONObject> list = new LinkedList<JSONObject>(Arrays.asList(root.getJSONObjectArray("images")));

				list.add(obj);

				JSONObject[] array = new JSONObject[list.size()];

				for(int i = 0; i < list.size(); i++) {

					array[i] = list.get(i);
				}

				root.set("images", array);

				file.setJSONObject(root);

				file.save();
			}
			else {

				System.out.println("File not exists: " + Core.getResourcePath("/src/database/content.json"));
			}
		}
		catch(Exception e) {

			e.printStackTrace();
		}

		response.redirect("/ps");
	}
}

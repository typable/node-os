package main;

import os.handler.HttpRequest;
import os.handler.HttpResponse;
import os.type.Inject;
import os.type.Loader;
import os.type.Logger;
import os.type.Request;


public class PageController {

	@Inject(code = "logger")
	private Logger logger;

	@Inject(code = "loader")
	private Loader loader;

	@Request(url = "/")
	private void getIndex(HttpRequest request, HttpResponse response) {

		logger.info("Hello World!");

		response.viewPage("*/index.html");
	}
}

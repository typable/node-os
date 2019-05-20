package main;

import os.handler.HttpRequest;
import os.handler.HttpResponse;
import os.type.Request;


public class PageController {

	@Request(url = "/")
	private void getIndex(HttpRequest request, HttpResponse response) {

		response.viewPage("*/index.html");
	}
}

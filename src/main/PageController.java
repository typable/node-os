package main;

import os.server.handler.HttpRequest;
import os.server.handler.HttpResponse;
import os.type.ContentType;
import os.type.Inject;
import os.type.Logger;
import os.type.Request;

public class PageController {

	@Inject(code = "logger")
	private Logger logger;
	
	@Request(url = "/")
	private void getIndex(HttpRequest request, HttpResponse response) {
		
		logger.info("Hello World!");
		
		response.showBody("It works!", ContentType.PLAIN);
	}
}

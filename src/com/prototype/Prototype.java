package com.prototype;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.prototype.condition.Condition;
import com.prototype.constants.Constants;
import com.prototype.environment.Environment;
import com.prototype.http.HTTPServer;
import com.prototype.loader.Loader;
import com.prototype.logger.Logger;
import com.prototype.type.Property;

import os.type.holder.RequestHolder;


public class Prototype {

	private final String PORT = "port";

	private static Constants constants;
	private static Environment environment;
	private static Property<String> messages;

	private static List<File> templates;
	private static List<RequestHolder> requests;

	private static Logger logger;
	private static Loader loader;

	private HTTPServer server;

	public Prototype() {

		constants = new Constants();
		environment = new Environment();

		messages = new Property<>();

		templates = new ArrayList<>();
		requests = new ArrayList<>();

		logger = new Logger();
		loader = new Loader();

		server = new HTTPServer();
	}

	public static void main(String[] args) throws Exception {

		Prototype prototype = new Prototype();

		prototype.launch();
	}

	private void init() {

		//
	}

	private void launch() throws Exception {

		loader.loadConfigurations(environment);
		loader.loadRequests(requests);
		loader.loadTemplates(templates);
		loader.loadMessages(messages);

		String port = (String) environment.get(PORT);

		if(Condition.notNull(port, PORT)) {

			server.start(Integer.valueOf(port));
		}
	}

	private void updateDomain() {

		//
	}

	public static String path() {

		try {

			return new File(".").getCanonicalPath();
		}
		catch(Exception e) {

			e.printStackTrace();

			return null;
		}
	}

	public static Constants constant() {

		return constants;
	}

	public static Environment env() {

		return environment;
	}

	public static Property<String> message() {

		return messages;
	}

	public static List<File> template() {

		return templates;
	}

	public static List<RequestHolder> request() {

		return requests;
	}

	public static Logger logger() {

		return logger;
	}

	public static Loader loader() {

		return loader;
	}
}

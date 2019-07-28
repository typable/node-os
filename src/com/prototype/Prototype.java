package com.prototype;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.prototype.condition.Condition;
import com.prototype.constants.Constants;
import com.prototype.environment.Environment;
import com.prototype.http.HTTPServer;
import com.prototype.loader.Loader;
import com.prototype.logger.Logger;
import com.prototype.logger.Logger.Messages;
import com.prototype.parse.PropertyParser;
import com.prototype.type.Property;
import com.prototype.type.RequestHolder;


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

		environment.put("logger", logger);
		environment.put("loader", loader);
		environment.put("server", server);
	}

	public static void main(String[] args) throws Exception {

		Prototype prototype = new Prototype();

		if(args.length >= 1) {

			if(args[0].equals("-init")) {

				prototype.init(args);
			}
			else {

				logger.warn("Invalid command!");
			}

			//			if(args[0].equals("-update")) {
			//
			//				prototype.update();
			//			}
		}
		else {

			prototype.launch();
		}
	}

	private void init(String[] args) throws Exception {

		final String PATH = path();

		File SOURCE_PATH = new File(PATH + constants.SOURCE_PATH);
		SOURCE_PATH.mkdir();

		File RESOURCE_PATH = new File(PATH + constants.RESOURCE_PATH);
		RESOURCE_PATH.mkdir();

		File LIBRARY_PATH = new File(PATH + constants.LIBRARY_PATH);
		LIBRARY_PATH.mkdir();

		File LOG_PATH = new File(PATH + constants.LOG_PATH);
		LOG_PATH.mkdir();

		File WEB_PATH = new File(PATH + constants.WEB_PATH);
		WEB_PATH.mkdir();

		File CONFIG_FILE = new File(PATH + "/" + constants.CONFIG_FILE);
		CONFIG_FILE.createNewFile();

		PropertyParser propertyParser = new PropertyParser();

		Property<String> props = new Property<>();
		props.put(PORT, "80");

		loader.writeText(CONFIG_FILE.toPath(), propertyParser.compose(props));

		File LAUNCH_BAT = new File(PATH + "/" + constants.LAUNCH_BAT);
		LAUNCH_BAT.createNewFile();

		loader.writeText(LAUNCH_BAT.toPath(), "@echo off" + constants.DOS_LINE_BREAK + "java -jar NodeOS.jar");

		File LAUNCH_BASH = new File(PATH + "/" + constants.LAUNCH_BASH);
		LAUNCH_BASH.createNewFile();

		loader.writeText(LAUNCH_BASH.toPath(), "java -jar NodeOS.jar");

		if(args.length == 2) {

			if(args[1].startsWith("eclipse=") && args[1].split("=").length == 2) {

				String name = args[1].split("=")[1];

				File PROJECT_FILE = new File(PATH + "/" + constants.PROJECT_FILE);
				PROJECT_FILE.createNewFile();

				String data = constants.PROJECT_TEMPLATE.replaceAll("\\@\\{name\\}", name);

				loader.writeText(PROJECT_FILE.toPath(), data);

				File CLASSPATH_FILE = new File(PATH + "/" + constants.CLASSPATH_FILE);
				CLASSPATH_FILE.createNewFile();

				loader.writeText(CLASSPATH_FILE.toPath(), constants.CLASSPATH_TEMPLATE);

				logger.info("Project successfully initialized");
			}
			else {

				logger.warn("Invalid command!");
			}
		}
		else {

			logger.info("Project successfully initialized");
		}
	}

	private void launch() throws Exception {

		logger.logFile(Paths.get(path() + constants.LOG_PATH));

		File CONFIG_FILE = new File(path() + "/" + constants.CONFIG_FILE);

		if(CONFIG_FILE.exists()) {

			loader.loadConfigurations(environment);
			loader.loadRequests(requests);
			loader.loadTemplates(templates);
			loader.loadMessages(messages);

			String port = (String) environment.get(PORT);

			if(Condition.notNull(port, PORT)) {

				server.start(Integer.valueOf(port));
			}
		}
		else {

			logger.warn("Project must be initialized!");

			System.exit(0);
		}
	}

	//	private void update() {
	//
	//		// TODO update()
	//	}

	public static String path() {

		try {

			return new File(".").getCanonicalPath();
		}
		catch(Exception e) {

			logger.error(Messages.FATAL_ERROR.getMessage(), e);

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

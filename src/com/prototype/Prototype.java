package com.prototype;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.prototype.constants.Constants;
import com.prototype.http.HTTPServer;
import com.prototype.loader.Loader;
import com.prototype.logger.Logger;
import com.prototype.logger.Logger.Messages;
import com.prototype.parse.PropertyParser;
import com.prototype.type.Property;
import com.prototype.type.RequestHolder;
import com.prototype.update.Updater;
import com.prototype.util.Utils;


public class Prototype {

	public static final String VERSION = "1.3.2";

	public static String PATH;

	private static Property<String> environment;
	private static Property<String> messages;

	private static List<File> templates;
	private static List<RequestHolder> requests;

	private static Logger logger;
	private static Loader loader;
	private static Updater updater;

	private HTTPServer server;

	public Prototype() {

		environment = new Property<>();
		messages = new Property<>();

		templates = new ArrayList<>();
		requests = new ArrayList<>();

		logger = new Logger();
		loader = new Loader();
		updater = new Updater();

		server = new HTTPServer();
	}

	public static void main(String[] args) throws Exception {

		Prototype prototype = new Prototype();

		Prototype.PATH = new File(".").getCanonicalPath();

		if(args.length >= 1) {

			if(args[0].equals("-init")) {

				prototype.init(args);
			}
			else if(args[0].equals("-reload")) {

				prototype.reload();
			}
			else {

				logger.warn("Invalid command!");
			}
		}
		else {

			prototype.launch();
		}
	}

	private void init(String[] args) throws Exception {

		final String PATH = Prototype.PATH;

		File SOURCE_PATH = new File(PATH + Constants.PATHS.SOURCE_PATH);
		SOURCE_PATH.mkdir();

		File RESOURCE_PATH = new File(PATH + Constants.PATHS.RESOURCE_PATH);
		RESOURCE_PATH.mkdir();

		File LIBRARY_PATH = new File(PATH + Constants.PATHS.LIBRARY_PATH);
		LIBRARY_PATH.mkdir();

		File LOG_PATH = new File(PATH + Constants.PATHS.LOG_PATH);
		LOG_PATH.mkdir();

		File WEB_PATH = new File(PATH + Constants.PATHS.WEB_PATH);
		WEB_PATH.mkdir();

		File CONFIG_FILE = new File(PATH + "/" + Constants.FILES.CONFIG_FILE);
		CONFIG_FILE.createNewFile();

		PropertyParser propertyParser = new PropertyParser();

		Property<String> props = new Property<>();
		props.put("port", "80");

		loader.writeText(CONFIG_FILE.toPath(), propertyParser.compose(props));

		File LAUNCH_BAT = new File(PATH + "/" + Constants.FILES.LAUNCH_BAT);
		LAUNCH_BAT.createNewFile();

		loader.writeText(LAUNCH_BAT.toPath(), "@echo off" + Constants.DOS_LINE_BREAK + "java -cp \"NodeOS.jar;lib/*\" com.prototype.Prototype");

		File LAUNCH_BASH = new File(PATH + "/" + Constants.FILES.LAUNCH_BASH);
		LAUNCH_BASH.createNewFile();

		loader.writeText(LAUNCH_BASH.toPath(), "java -cp \"NodeOS.jar:lib/*\" com.prototype.Prototype");

		if(args.length == 2) {

			if(args[1].startsWith("eclipse=") && args[1].split("=").length == 2) {

				String name = args[1].split("=")[1];

				File PROJECT_FILE = new File(PATH + "/" + Constants.FILES.PROJECT_FILE);
				PROJECT_FILE.createNewFile();

				String data = Constants.TEMPLATES.PROJECT_TEMPLATE.replaceAll("\\@\\{name\\}", name);

				loader.writeText(PROJECT_FILE.toPath(), data);

				File CLASSPATH_FILE = new File(PATH + "/" + Constants.FILES.CLASSPATH_FILE);
				CLASSPATH_FILE.createNewFile();

				loader.writeText(CLASSPATH_FILE.toPath(), Constants.TEMPLATES.CLASSPATH_TEMPLATE);

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

		logger.logFile(Paths.get(Prototype.PATH + Constants.PATHS.LOG_PATH));

		File CONFIG_FILE = new File(Prototype.PATH + "/" + Constants.FILES.CONFIG_FILE);

		if(CONFIG_FILE.exists()) {

			loader.loadConfigurations(environment);
			loader.loadRequests(requests);
			loader.loadTemplates(templates);
			loader.loadMessages(messages);

			String updateSoftware = environment.get("update.software");

			if(updateSoftware != null && updateSoftware.equals("true")) {

				updater.updateSoftware();
			}

			String updateDomain = environment.get("update.domain");

			if(updateDomain != null && updateDomain.equals("true")) {

				String dnsDomain = environment.get("dns.domain");
				String dnsPassword = environment.get("dns.password");
				String dnsServer = environment.get("dns.server");

				if(dnsDomain != null && !dnsDomain.isBlank() && dnsPassword != null && !dnsPassword.isBlank() && dnsServer != null && !dnsServer.isBlank()) {

					updater.updateDomain(dnsDomain, Utils.encode(dnsDomain + ":" + dnsPassword), dnsServer);
				}
			}

			String port = environment.get("port");

			if(port != null) {

				server.start(Integer.valueOf(port));
			}
			else {

				logger.error(Messages.UNDEFINED.getMessage("port"));
			}
		}
		else {

			logger.warn("Project must be initialized!");

			System.exit(0);
		}
	}

	private void reload() throws Exception {

		File CONFIG_FILE = new File(Prototype.PATH + "/" + Constants.FILES.CONFIG_FILE);

		if(CONFIG_FILE.exists()) {

			loader.loadConfigurations(environment);
			loader.loadRequests(requests);
			loader.loadTemplates(templates);
			loader.loadMessages(messages);
		}
		else {

			logger.warn("Project must be initialized!");
		}
	}

	public static Path path(String ref, String path) {

		return Paths.get(Prototype.PATH + ref + path);
	}

	public static Property<String> env() {

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

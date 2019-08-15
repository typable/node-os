package com.prototype;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.prototype.constants.Constants;
import com.prototype.format.Formatter;
import com.prototype.http.HTTPServer;
import com.prototype.loader.Loader;
import com.prototype.logger.Logger;
import com.prototype.logger.Logger.Messages;
import com.prototype.parse.PropertyParser;
import com.prototype.reflect.Caller;
import com.prototype.service.SessionService;
import com.prototype.type.Property;
import com.prototype.type.Request;
import com.prototype.type.Session;
import com.prototype.update.Updater;
import com.prototype.util.Utils;


public class Prototype {

	/*
	TODO Event
	TODO Runtime
	 */

	public static final String VERSION = "1.3.4";

	public static String PATH;

	private Property<Object> environment;
	private Property<String> messages;

	private List<File> templates;
	private List<Caller<Request>> requests;
	private List<Session> sessions;

	private Logger logger;
	private Loader loader;
	private Updater updater;
	private HTTPServer server;
	private SessionService sessionService;
	private Formatter formatter;

	public Prototype(String[] args) {

		try {

			environment = new Property<>();
			messages = new Property<>();

			templates = new ArrayList<>();
			requests = new ArrayList<>();
			sessions = new ArrayList<>();

			logger = new Logger(this);
			loader = new Loader(this);
			updater = new Updater(this);
			sessionService = new SessionService(this);
			formatter = new Formatter(this);

			server = new HTTPServer(this);

			environment.put("environment", environment);
			environment.put("messages", messages);
			environment.put("templates", templates);
			environment.put("requests", requests);
			environment.put("logger", logger);
			environment.put("loader", loader);
			environment.put("updater", updater);
			environment.put("server", server);
			environment.put("formatter", formatter);

			Prototype.PATH = new File(".").getCanonicalPath();

			if(args.length >= 1) {

				if(args[0].equals("-init")) {

					init(args);
				}
				else if(args[0].equals("-reload")) {

					reload();
				}
				else {

					logger.warn("Invalid command!");
				}
			}
			else {

				launch();
			}
		}
		catch(Exception ex) {

			logger.error(Messages.FATAL_ERROR.getMessage(), ex);
		}
	}

	public static void main(String[] args) {

		new Prototype(args);
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

			String updateSoftware = (String) environment.get("update.software");

			if(updateSoftware != null && updateSoftware.equals("true")) {

				updater.updateSoftware();
			}

			String updateDomain = (String) environment.get("update.domain");

			if(updateDomain != null && updateDomain.equals("true")) {

				String dnsDomain = (String) environment.get("dns.domain");
				String dnsPassword = (String) environment.get("dns.password");
				String dnsServer = (String) environment.get("dns.server");

				if(dnsDomain != null && !dnsDomain.isBlank() && dnsPassword != null && !dnsPassword.isBlank() && dnsServer != null && !dnsServer.isBlank()) {

					updater.updateDomain(dnsDomain, Utils.encode(dnsDomain + ":" + dnsPassword), dnsServer);
				}
			}

			String port = (String) environment.get("port");
			String ssl = (String) environment.get("ssl");
			String sslKey = (String) environment.get("ssl.key");
			String sslPassword = (String) environment.get("ssl.password");

			if(port != null) {

				if(ssl != null && sslKey != null && sslPassword != null) {

					server.start(Integer.valueOf(port), Boolean.valueOf(ssl), sslKey, sslPassword);
				}
				else {

					server.start(Integer.valueOf(port));
				}
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

	public static Path path(String path) {

		return Paths.get(Prototype.PATH + path);
	}

	public Property<Object> getEnvironment() {

		return environment;
	}

	public Property<String> getMessages() {

		return messages;
	}

	public List<File> getTemplates() {

		return templates;
	}

	public List<Caller<Request>> getRequests() {

		return requests;
	}

	public List<Session> getSessions() {

		return sessions;
	}

	public Logger getLogger() {

		return logger;
	}

	public Loader getLoader() {

		return loader;
	}

	public Updater getUpdater() {

		return updater;
	}

	public HTTPServer getServer() {

		return server;
	}

	public SessionService getSessionService() {

		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {

		this.sessionService = sessionService;
	}

	public Formatter getFormatter() {

		return formatter;
	}

	public void setFormatter(Formatter formatter) {

		this.formatter = formatter;
	}
}

package com.prototype.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.core.base.Condition;
import com.core.lang.Property;
import com.core.parse.PropertyParser;
import com.core.reflect.Caller;
import com.core.reflect.Reflect;
import com.prototype.Prototype;
import com.prototype.constants.Constants;
import com.prototype.factory.HTTPServerFactory;
import com.prototype.format.Formatter;
import com.prototype.http.HTTPServer;
import com.prototype.loader.Loader;
import com.prototype.logger.Logger;
import com.prototype.service.SessionService;
import com.prototype.type.Request;
import com.prototype.type.Session;
import com.prototype.update.Updater;
import com.prototype.util.HTTPUtils;


public class Core implements Runnable {

	public static final String CODE = "core";

	public static Property<Object> environment;

	public Logger logger;
	public Loader loader;
	public Updater updater;
	public Formatter formatter;
	public SessionService sessionService;

	public Property<String> configurations;
	public Property<String> messages;

	public List<Caller<Request>> requests;
	public List<Session> sessions;
	public List<File> templates;

	public HTTPServer server;

	public Core() {

		environment = new Property<>();
		configurations = new Property<>();
		messages = new Property<>();

		requests = new ArrayList<>();
		sessions = new ArrayList<>();
		templates = new ArrayList<>();
	}

	public void init() throws Exception {

		Prototype.PATH = new File(".").getCanonicalPath();

		logger = new Logger(Prototype.path(Constants.PATHS.LOG_PATH));
		environment.put(Logger.CODE, logger);

		loader = new Loader();
		environment.put(Loader.CODE, loader);

		updater = new Updater();
		environment.put(Updater.CODE, updater);

		formatter = new Formatter();
		environment.put(Formatter.CODE, formatter);

		sessionService = new SessionService();
		environment.put("sessionService", sessionService);

		environment.put(Core.CODE, this);

		inject();
	}

	@Override
	public void run() {

		try {

			loader.loadConfigurations(configurations);
			environment.put("configurations", configurations);

			File configFile = Prototype.path("/" + Constants.FILES.CONFIG_FILE).toFile();

			if(!configFile.exists()) {

				logger.warn("Project must be initialized!");

				return;
			}

			Boolean debug = configurations.get("log.debug", Boolean.class);

			if(Condition.isNotNull(debug)) {

				logger.setDebug(debug);
			}

			Boolean save = configurations.get("log.save", Boolean.class);

			if(Condition.isNotNull(save)) {

				logger.setSave(save);
			}

			update();

			loader.loadRequests(requests);
			environment.put("requests", requests);

			loader.loadMessages(messages);
			environment.put("messages", messages);

			loader.loadTemplates(templates);
			environment.put("templates", templates);

			environment.put("sessions", sessions);

			Integer port = configurations.get("port", Integer.class, true);

			Boolean ssl = configurations.get("ssl", Boolean.class);

			if(Condition.isNotNull(ssl)) {

				String key = configurations.get("ssl.key", true);
				String password = configurations.get("ssl.password", true);

				server = HTTPServerFactory.create(port, ssl, Prototype.path(key), password);
			}
			else {

				server = HTTPServerFactory.create(port);
			}

			environment.put(HTTPServer.CODE, server);

			inject();

			server.run();
		}
		catch(Exception ex) {

			ex.printStackTrace();
		}
	}

	public void setup() throws Exception {

		// TODO setup()
		File sourceDirectory = Prototype.path(Constants.PATHS.SOURCE_PATH).toFile();
		sourceDirectory.mkdir();

		File classDirectory = Prototype.path(Constants.PATHS.CLASS_PATH).toFile();
		classDirectory.mkdir();

		File libaryDirectory = Prototype.path(Constants.PATHS.LIBRARY_PATH).toFile();
		libaryDirectory.mkdir();

		File webDirectory = Prototype.path(Constants.PATHS.WEB_PATH).toFile();
		webDirectory.mkdir();

		File resourceDirectory = Prototype.path(Constants.PATHS.RESOURCE_PATH).toFile();
		resourceDirectory.mkdir();

		File logDirectory = Prototype.path(Constants.PATHS.LOG_PATH).toFile();
		logDirectory.mkdir();

		File configFile = Prototype.path("/" + Constants.FILES.CONFIG_FILE).toFile();
		configFile.createNewFile();

		Property<String> args = new Property<>();
		args.put("port", "80");

		PropertyParser propertyParser = new PropertyParser();

		loader.writeText(configFile.toPath(), propertyParser.compose(args));

		File projectFile = Prototype.path("/" + Constants.FILES.PROJECT_FILE).toFile();
		projectFile.createNewFile();

		loader.writeText(projectFile.toPath(), Constants.TEMPLATES.PROJECT_TEMPLATE);

		File classpathFile = Prototype.path("/" + Constants.FILES.CLASSPATH_FILE).toFile();
		classpathFile.createNewFile();

		loader.writeText(classpathFile.toPath(), Constants.TEMPLATES.CLASSPATH_TEMPLATE);

		logger.info("Project successfully initialized");
	}

	public void update() {

		Boolean updateSoftware = configurations.get("update.software", Boolean.class);

		if(Condition.isNotNull(updateSoftware) && Condition.isTrue(updateSoftware)) {

			updater.updateSoftware();
		}

		Boolean updateDomain = configurations.get("update.domain", Boolean.class);

		if(Condition.isNotNull(updateDomain) && Condition.isTrue(updateDomain)) {

			String domain = configurations.get("dns.domain", true);
			String password = configurations.get("dns.password", true);
			String server = configurations.get("dns.server", true);

			String auth = HTTPUtils.encode(domain + ":" + password);

			updater.updateDomain(domain, auth, server);
		}
	}

	private void inject() throws Exception {

		for(String key : environment.keys()) {

			Reflect.inject(environment.get(key), environment);
		}
	}
}

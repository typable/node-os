package os.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import os.handler.Controller;
import os.server.HttpServer;
import os.type.Loader;
import os.type.Logger;
import os.type.Logger.Messages;
import os.type.Session;
import os.util.File;
import os.util.Property;


public class Core {

	public static Property<String> PROPERTIES;
	public static Property<String> LANGUAGES;
	public static Property<Session> SESSIONS;
	public static List<Controller> CONTROLLERS;
	public static Logger LOGGER;
	public static Loader LOADER;

	public static final String CONFIG_PATH = "/config.properties";
	public static final String RESOURCE_PATH = "/src";

	public static int PORT;
	public static String ROOT;

	private List<Class<?>> instances;
	private HttpServer server;

	public Core() {

		try {

			PROPERTIES = new Property<String>();
			LANGUAGES = new Property<String>();
			SESSIONS = new Property<Session>();
			CONTROLLERS = new ArrayList<Controller>();
			LOGGER = new Logger();
			LOADER = new Loader();

			instances = new ArrayList<Class<?>>();
			server = new HttpServer();
		}
		catch(Exception e) {

			e.printStackTrace();
		}
	}

	public void launch() throws Exception {

		loadConfigurations();

		PORT = Integer.parseInt(getRequired("port"));
		ROOT = Core.getRequired("root").replaceAll("\\*", getCurrentPath());

		loadLanguages();
		loadControllers();

		server.launch();
	}

	public void addController(Class<?> instance) {

		instances.add(instance);
	}

	private void loadControllers() throws Exception {

		for(Class<?> instance : instances) {

			Property<Object> fields = new Property<Object>();
			fields.set("logger", LOGGER);
			fields.set("loader", LOADER);
			fields.set("config", PROPERTIES);

			Controller controller = new Controller(instance);
			controller.inject(fields);

			CONTROLLERS.add(controller);
		}
	}

	private void loadConfigurations() throws IOException {

		File configFile = new File(getCurrentPath() + CONFIG_PATH);

		if(configFile.exists()) {

			try {

				configFile.load();

				PROPERTIES = configFile.getProps();

				LOGGER.info("Configurations loaded");
			}
			catch(IOException e) {

				LOGGER.error("Failed to load Configurations!", e);
			}
		}
		else {

			try {

				configFile.create();

				if(configFile.exists()) {

					LOGGER.info("Configuration file created");

					Property<String> props = new Property<String>();
					props.set("port", "80");
					props.set("root", "*/publish");

					configFile.setProps(props);
					configFile.save();

					loadConfigurations();
				}
				else {

					LOGGER.error("Failed to create new file!");
				}
			}
			catch(IOException e) {

				LOGGER.error("Failed to create new file!", e);
			}
		}
	}

	private void loadLanguages() throws IOException {

		File file = new File(ROOT + RESOURCE_PATH + "/lang/lang.properties");

		if(file.exists()) {

			file.load();
			LANGUAGES = file.getProps();
		}
	}

	public static String getCurrentPath() throws IOException {

		return new java.io.File(".").getCanonicalPath().replaceAll("\\\\", "\\/");
	}

	public static String getRelativePath(String path) throws IOException {

		return path.contains("*") ? path.replaceAll("\\*", ROOT) : path;
	}

	public static String getResourcePath(String path) throws IOException {

		return ROOT + path;
	}

	public static String get(String key) {

		return PROPERTIES.hasKey(key) ? PROPERTIES.get(key) : null;
	}

	public static String getRequired(String key) {

		if(get(key) == null) {

			LOGGER.error(Messages.UNDEFINED.getMessage(key));
			stop();
		}

		return get(key);
	}

	public static Session getSession(String uuid) {

		for(String uuid_ : SESSIONS.keys()) {

			if(uuid_.equals(uuid)) {

				return SESSIONS.get(uuid_);
			}
		}
		return null;
	}

	public static void stop() {

		LOGGER.info(Messages.SERVER_STOPPED.getMessage());
		System.exit(0);
	}
}

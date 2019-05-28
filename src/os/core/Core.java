package os.core;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import os.server.HttpServer;
import os.service.AuthenticationService;
import os.service.SessionService;
import os.service.UserService;
import os.type.Loader;
import os.type.Logger;
import os.type.Logger.Messages;
import os.type.Request;
import os.type.holder.EventHolder;
import os.type.holder.RequestHolder;
import util.file.PropertyFile;
import util.type.Property;


public class Core {

	public static Property<String> PROPERTIES;
	public static Property<String> LANGUAGES;
	public static List<RequestHolder> REQUESTS;
	public static List<EventHolder> EVENTS;
	public static Logger LOGGER;
	public static Loader LOADER;

	public static UserService userService;
	public static SessionService sessionService;
	public static AuthenticationService authenticationService;

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
			LOGGER = new Logger();
			LOADER = new Loader();
			REQUESTS = new ArrayList<RequestHolder>();
			EVENTS = new ArrayList<EventHolder>();

			userService = new UserService();
			sessionService = new SessionService();
			authenticationService = new AuthenticationService();

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

		userService.loadUsers();

		loadLanguages();
		loadHandlers();

		server.launch();
	}

	public void addHandler(Class<?> instance) {

		instances.add(instance);
	}

	private void loadHandlers() throws Exception {

		Property<Object> fields = new Property<Object>();
		fields.set("logger", LOGGER);
		fields.set("loader", LOADER);
		fields.set("config", PROPERTIES);
		fields.set("userService", userService);
		fields.set("sessionService", sessionService);
		fields.set("authenticationService", authenticationService);

		for(Class<?> type : instances) {

			try {

				Object instance = type.getConstructor().newInstance();

				for(Method method : type.getDeclaredMethods()) {

					for(Annotation annotation : method.getAnnotations()) {

						if(annotation instanceof Request) {

							RequestHolder holder = new RequestHolder((Request) annotation, instance, method);
							holder.inject(type, fields);

							REQUESTS.add(holder);
						}
					}
				}
			}
			catch(Exception e) {

				e.printStackTrace();
			}
		}
	}

	private void loadConfigurations() throws IOException {

		// File configFile = new File(getCurrentPath() + CONFIG_PATH);

		PropertyFile configFile = new PropertyFile(getCurrentPath() + CONFIG_PATH);

		if(configFile.exists()) {

			try {

				configFile.load();

				PROPERTIES = configFile.getProperty();

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

					configFile.setData(props);
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

		PropertyFile file = new PropertyFile(ROOT + RESOURCE_PATH + "/lang/lang.properties");

		if(file.exists()) {

			file.load();
			LANGUAGES = file.getProperty();
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

	public static void stop() {

		LOGGER.info(Messages.SERVER_STOPPED.getMessage());
		System.exit(0);
	}
}

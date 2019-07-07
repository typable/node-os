package os.core;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import core.reflect.Callback;
import core.reflect.Instance;
import os.server.HttpServer;
import os.service.AuthenticationService;
import os.service.SessionService;
import os.service.UserService;
import os.type.Loader;
import os.type.Request;
import os.type.holder.RequestHolder;
import util.file.PropertyFile;
import util.log.Logger;
import util.log.Logger.Messages;
import util.type.Property;


public class Core {

	public static Property<String> PROPERTIES;
	public static Property<String> LANGUAGES;
	public static Property<String> TEXTS;
	public static List<File> TEMPLATES;
	public static List<RequestHolder> REQUESTS;
	public static Logger LOGGER;
	public static Loader LOADER;

	public static UserService userService;
	public static SessionService sessionService;
	public static AuthenticationService authenticationService;

	public static final String CONFIG_PATH = "/config.properties";
	public static final String RESOURCE_PATH = "/src";
	public static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;
	public static final String DEFAULT_LANG = "de";

	public static int PORT;
	public static String ROOT;

	private List<Instance> instances;
	private HttpServer server;

	public Core() {

		PROPERTIES = new Property<String>();
		LANGUAGES = new Property<String>();
		LOGGER = new Logger();
		LOADER = new Loader();
		REQUESTS = new ArrayList<RequestHolder>();
		TEMPLATES = new ArrayList<File>();

		userService = new UserService();
		sessionService = new SessionService();
		authenticationService = new AuthenticationService();

		instances = new ArrayList<Instance>();
		server = new HttpServer();
	}

	public void launch() throws Exception {

		loadConfigurations();

		PORT = Integer.parseInt(getRequired("port"));
		ROOT = Core.getRequired("root").replaceAll("\\*", getCurrentPath());

		userService.loadUsers();

		loadLanguages();
		loadTexts(DEFAULT_LANG);
		loadTemplates();
		loadHandlers();

		server.launch();
	}

	public void addHandler(Class<?> type) throws Exception {

		instances.add(new Instance(type));
	}

	private void loadHandlers() throws Exception {

		Property<Object> args = new Property<Object>();
		args.set("logger", LOGGER);
		args.set("loader", LOADER);
		args.set("config", PROPERTIES);
		args.set("userService", userService);
		args.set("sessionService", sessionService);
		args.set("authenticationService", authenticationService);

		for(Instance instance : instances) {

			try {

				for(Method method : instance.getType().getDeclaredMethods()) {

					for(Annotation annotation : method.getAnnotations()) {

						if(annotation instanceof Request) {

							RequestHolder holder = new RequestHolder((Request) annotation, new Callback(instance, method));
							instance.inject(args);

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

	private void loadConfigurations() throws Exception {

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

					configFile.setProperty(props);
					configFile.save();

					loadConfigurations();
				}
				else {

					LOGGER.error("Failed to create new file!");
				}
			}
			catch(Exception e) {

				LOGGER.error("Failed to create new file!", e);
			}
		}
	}

	private void loadLanguages() throws Exception {

		PropertyFile file = new PropertyFile(ROOT + RESOURCE_PATH + "/lang/lang.properties");

		if(file.exists()) {

			file.load();
			LANGUAGES = file.getProperty();
		}
	}

	public static void loadTexts(String lang) throws Exception {

		PropertyFile file = new PropertyFile(ROOT + RESOURCE_PATH + "/lang/lang-" + lang + ".properties");

		if(file.exists()) {

			file.load();
			TEXTS = file.getProperty();
		}
	}

	public static void loadTemplates() throws Exception {

		File directory = new File(ROOT + RESOURCE_PATH + "/template/");

		if(directory.exists()) {

			for(File file : directory.listFiles()) {

				if(file.getAbsolutePath().endsWith(".html")) {

					TEMPLATES.add(file);
				}
			}
		}
	}

	public static String getCurrentPath() throws IOException {

		return new File(".").getCanonicalPath().replaceAll("\\\\", "\\/");
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

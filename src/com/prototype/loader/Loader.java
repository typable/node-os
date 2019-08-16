package com.prototype.loader;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.prototype.Prototype;
import com.prototype.constants.Constants;
import com.prototype.core.Core;
import com.prototype.logger.Logger;
import com.prototype.logger.Logger.Messages;
import com.prototype.parse.PropertyParser;
import com.prototype.reflect.Caller;
import com.prototype.reflect.Inject;
import com.prototype.reflect.Instance;
import com.prototype.type.Controller;
import com.prototype.type.Property;
import com.prototype.type.Request;


public class Loader extends ClassLoader {

	public static final String NAME = "prototype.loader";
	public static final String PREFIX = "[Loader] ";

	@Inject(name = Core.NAME)
	private Core core;

	@Inject(name = Logger.NAME)
	private Logger logger;

	@Inject(name = "configurations")
	private Property<String> configurations;

	private PropertyParser propertyParser;

	public Loader() {

		propertyParser = new PropertyParser();
	}

	public void loadConfigurations(Property<String> configurations) throws Exception {

		File file = Prototype.path("/" + Constants.FILES.CONFIG_FILE).toFile();

		if(file.exists()) {

			String data = readText(file.toPath());

			if(data != null) {

				Property<String> props = propertyParser.parse(data);

				for(String key : props.keys()) {

					configurations.put(key, props.get(key));
				}
			}

			logger.info(PREFIX + "Configurations loaded");
		}
		else {

			logger.warn(PREFIX + Messages.NOT_FOUND.getMessage(Constants.FILES.CONFIG_FILE));
		}
	}

	public void loadRequests(List<Caller<Request>> requests) throws Exception {

		final String CLASS_PATH = Prototype.PATH + Constants.PATHS.CLASS_PATH;

		int count = 0;

		for(File file : loadFiles(Paths.get(CLASS_PATH), ".*\\.class")) {

			String path = file.getAbsolutePath();
			String name = path.substring(CLASS_PATH.length() + 1, path.length() - ".class".length()).replaceAll("\\\\|\\/", ".");

			Class<?> controller = loadController(name, file.toPath());

			if(controller != null) {

				Object instance = Instance.of(controller);
				Instance.inject(controller, instance, core.environment);

				for(Method method : controller.getDeclaredMethods()) {

					for(Annotation annotation : method.getAnnotations()) {

						if(annotation instanceof Request) {

							Request request = (Request) annotation;

							if(!request.ignore()) {

								Caller<Request> caller = new Caller<Request>(request, method, instance);

								requests.add(caller);

								count++;
							}
						}
					}
				}
			}
		}

		logger.info(PREFIX + "Requests loaded (" + count + ")");
	}

	public void loadTemplates(List<File> templates) throws Exception {

		final String TEMPLATE_PATH = Constants.PATHS.RESOURCE_PATH + "/template";

		int count = 0;
		File directory = new File(Prototype.PATH + "/" + TEMPLATE_PATH);

		if(directory.exists() && directory.isDirectory()) {

			for(File file : loadFiles(directory.toPath(), ".*\\.html")) {

				templates.add(file);

				count++;
			}
		}

		logger.info(PREFIX + "Templates loaded (" + count + ")");
	}

	public void loadMessages(Property<String> messages) throws Exception {

		final String MESSAGE_PATH = Constants.PATHS.RESOURCE_PATH + "/message";

		int count = 0;
		File directory = new File(Prototype.PATH + "/" + MESSAGE_PATH);

		if(directory.exists() && directory.isDirectory()) {

			for(File file : loadFiles(directory.toPath(), ".*\\.properties")) {

				String data = readText(file.toPath());

				Property<String> props = propertyParser.parse(data);

				for(String key : props.keys()) {

					messages.put(key, props.get(key));

					count++;
				}
			}
		}

		logger.info(PREFIX + "Messages loaded (" + count + ")");
	}

	public String readText(Path path) throws Exception {

		return Files.readString(path, Constants.CHARSET);
	}

	public byte[] read(Path path) throws Exception {

		return Files.readAllBytes(path);
	}

	public void writeText(Path path, String data) throws Exception {

		Files.writeString(path, data, Constants.CHARSET);
	}

	public void write(Path path, byte[] data) throws Exception {

		Files.write(path, data);
	}

	public File[] loadFiles(Path path) {

		return loadFiles(path, ".*");
	}

	public File[] loadFiles(Path path, String pattern) {

		List<File> files = new ArrayList<>();
		File directory = path.toFile();

		if(directory.exists() && directory.isDirectory()) {

			for(File file : directory.listFiles()) {

				if(file.isFile()) {

					if(file.getName().matches(pattern)) {

						files.add(file);
					}
				}
				else if(file.isDirectory()) {

					for(File subfile : loadFiles(file.toPath(), pattern)) {

						files.add(subfile);
					}
				}
			}
		}

		return files.toArray(new File[files.size()]);
	}

	public Class<?> loadController(String name, Path path) throws Exception {

		Class<?> loadedClass = loadClass(name, path);

		for(Annotation annotation : loadedClass.getAnnotations()) {

			if(annotation.annotationType() == Controller.class) {

				return loadedClass;
			}
		}

		return null;
	}

	public Class<?> loadClass(String name, Path path) throws Exception {

		byte[] data = read(path);

		return defineClass(name, data, 0, data.length);
	}

	public <T> T loadClass(String name, Path path, Class<T> type) throws Exception {

		byte[] data = read(path);

		Class<?> loadedClass = defineClass(name, data, 0, data.length);

		if(loadedClass != null) {

			if(type.isAssignableFrom(loadedClass)) {

				return type.cast(loadedClass.getConstructor().newInstance());
			}
		}

		return null;
	}
}

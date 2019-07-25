package com.prototype.loader;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.prototype.Prototype;
import com.prototype.environment.Environment;
import com.prototype.logger.Logger;
import com.prototype.logger.Logger.Messages;
import com.prototype.parse.PropertyParser;
import com.prototype.reflect.Callback;
import com.prototype.reflect.Instance;
import com.prototype.type.Controller;
import com.prototype.type.Extension;
import com.prototype.type.Property;
import com.prototype.type.Request;
import com.prototype.type.RequestHolder;


public class Loader {

	private String PATH;
	private Charset CHARSET;
	private Logger LOGGER;

	private PropertyParser propertyParser;

	public Loader() {

		PATH = Prototype.path();
		CHARSET = Prototype.constant().CHARSET;
		LOGGER = Prototype.logger();

		propertyParser = new PropertyParser();
	}

	public void loadConfigurations(Environment environment) throws Exception {

		final String CONFIG_FILE = Prototype.constant().CONFIG_FILE;

		File file = new File(PATH + "/" + CONFIG_FILE);

		if(file.exists()) {

			String data = readText(file.toPath());

			if(data != null) {

				Property<String> props = propertyParser.parse(data);

				for(String key : props.keys()) {

					environment.put(key, props.get(key));
				}
			}

			LOGGER.info("Configurations loaded");
		}
		else {

			LOGGER.warn(Messages.NOT_FOUND.getMessage(CONFIG_FILE));
		}
	}

	public void loadRequests(List<RequestHolder> requests) throws Exception {

		final String CLASS_PATH = Prototype.path() + Prototype.constant().CLASS_PATH;

		int count = 0;

		for(File file : loadFiles(Paths.get(CLASS_PATH), ".*\\.class")) {

			String path = file.getAbsolutePath();
			String name = path.substring(CLASS_PATH.length() + 1, path.length() - ".class".length()).replaceAll("\\\\|\\/", ".");

			Class<?> controller = loadController(name, file.toPath());

			if(controller != null) {

				Instance instance = new Instance(controller);

				for(Method method : instance.getType().getDeclaredMethods()) {

					for(Annotation annotation : method.getAnnotations()) {

						if(annotation instanceof Request) {

							RequestHolder holder = new RequestHolder((Request) annotation, new Callback(instance, method));
							instance.inject(Prototype.env());

							Prototype.request().add(holder);

							count++;
						}
					}
				}
			}
		}

		LOGGER.info("Requests loaded (" + count + ")");
	}

	public void loadTemplates(List<File> templates) throws Exception {

		final String TEMPLATE_PATH = Prototype.constant().RESOURCE_PATH + "/template";

		int count = 0;
		File directory = new File(PATH + "/" + TEMPLATE_PATH);

		if(directory.exists() && directory.isDirectory()) {

			for(File file : loadFiles(directory.toPath(), ".*\\.html")) {

				Prototype.template().add(file);

				count++;
			}
		}

		LOGGER.info("Templates loaded (" + count + ")");
	}

	public void loadMessages(Property<String> messages) throws Exception {

		final String MESSAGE_PATH = Prototype.constant().RESOURCE_PATH + "/message";

		int count = 0;
		File directory = new File(PATH + "/" + MESSAGE_PATH);

		if(directory.exists() && directory.isDirectory()) {

			for(File file : loadFiles(directory.toPath(), ".*\\.properties")) {

				String data = readText(file.toPath());

				Property<String> props = propertyParser.parse(data);

				for(String key : props.keys()) {

					Prototype.message().put(key, props.get(key));

					count++;
				}
			}
		}

		LOGGER.info("Messages loaded (" + count + ")");
	}

	public String readText(Path path) throws Exception {

		return Files.readString(path, CHARSET);
	}

	public byte[] read(Path path) throws Exception {

		return Files.readAllBytes(path);
	}

	public void writeText(Path path, String data) throws Exception {

		Files.writeString(path, data, CHARSET);
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

	public Class<?> loadExtension(String name, Path path) throws Exception {

		Class<?> loadedClass = loadClass(name, path);

		for(Annotation annotation : loadedClass.getAnnotations()) {

			if(annotation.annotationType() == Extension.class) {

				return loadedClass;
			}
		}

		return null;
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

		URLClassLoader loader = new URLClassLoader(new URL[] { path.toUri().toURL() });

		Class<?> loadedClass = loader.loadClass(name);

		loader.close();

		if(loadedClass != null) {

			return loadedClass;
		}

		return null;
	}

	public <T> T loadClass(String name, Path path, Class<T> type) throws Exception {

		URLClassLoader loader = new URLClassLoader(new URL[] { path.toUri().toURL() });

		Class<?> loadedClass = loader.loadClass(name);

		loader.close();

		if(loadedClass != null) {

			if(type.isAssignableFrom(loadedClass)) {

				return type.cast(loadedClass.getConstructor().newInstance());
			}
		}

		return null;
	}

}

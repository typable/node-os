package com.prototype.loader;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.prototype.Prototype;
import com.prototype.environment.Environment;

import os.type.holder.RequestHolder;
import util.log.Logger;
import util.log.Logger.Messages;
import util.parse.PropertyParser;
import util.type.Property;


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

			String data = read(file.toPath());

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

	public void loadRequests(List<RequestHolder> requests) {

		//
	}

	public void loadTemplates(List<File> templates) {

		//
	}

	public void loadMessages(Property<String> messages) {

		//
	}

	public String read(Path path) throws Exception {

		return Files.readString(path, CHARSET);
	}

	public void write(Path path, String data) throws Exception {

		Files.writeString(path, data, CHARSET);
	}
}

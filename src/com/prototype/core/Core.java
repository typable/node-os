package com.prototype.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.prototype.Prototype;
import com.prototype.format.Formatter;
import com.prototype.lifecycle.LifeCycle;
import com.prototype.loader.Loader;
import com.prototype.logger.Logger;
import com.prototype.reflect.Caller;
import com.prototype.reflect.Instance;
import com.prototype.type.Property;
import com.prototype.type.Request;
import com.prototype.type.Session;
import com.prototype.update.Updater;


public class Core extends LifeCycle {

	public static final String NAME = "prototype.core";

	public Property<Object> environment;
	public Property<String> configurations;
	public Property<String> messages;

	public List<Caller<Request>> requests;
	public List<Session> sessions;
	public List<File> templates;

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

		Logger logger = new Logger();
		environment.put(Logger.NAME, logger);

		Loader loader = new Loader();
		environment.put(Loader.NAME, loader);

		Updater updater = new Updater();
		environment.put(Updater.NAME, updater);

		Formatter formatter = new Formatter();
		environment.put(Formatter.NAME, formatter);

		environment.put(Core.NAME, this);

		inject();

		loader.loadConfigurations(configurations);
		environment.put("configurations", configurations);

		loader.loadRequests(requests);
		environment.put("requests", requests);

		loader.loadMessages(messages);
		environment.put("messages", messages);

		loader.loadTemplates(templates);
		environment.put("templates", templates);

		inject();
	}

	@Override
	public void run() {

		//
	}

	private void inject() throws Exception {

		for(String key : environment.keys()) {

			Instance.inject(environment.get(key), environment);
		}
	}
}

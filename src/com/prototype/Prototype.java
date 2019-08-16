package com.prototype;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import com.prototype.constants.Constants;
import com.prototype.core.Core;
import com.prototype.factory.HTTPServerFactory;
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

	private Core core;

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

		try(Core core = new Core()) {

			core.init();
			core.run();

			/*
			if(args.length >= 1) {
			
				if(args[0].equals("-init")) {
			
					init(args);
				}
				else {
			
					logger.warn("Invalid command!");
				}
			}
			else {
			
				launch();
			}
			 */
		}
		catch(Exception ex) {

			// TODO Exception
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

		logger.logFile(path(Constants.PATHS.LOG_PATH));

		File configFile = path("/" + Constants.FILES.CONFIG_FILE).toFile();

		if(!configFile.exists()) {

			logger.warn("Project must be initialized!");

			return;
		}

		update();

		Optional<Object> port_opt = environment.getNullable("port");
		Optional<Object> ssl_opt = environment.getNullable("ssl");
		Optional<Object> ssl_key_opt = environment.getNullable("ssl.key");
		Optional<Object> ssl_password_opt = environment.getNullable("ssl.password");

		if(!port_opt.isPresent()) {

			logger.error(Messages.UNDEFINED.getMessage("port"));

			return;
		}

		int port = Integer.parseInt((String) port_opt.get());

		if(ssl_opt.isPresent() && ssl_key_opt.isPresent() && ssl_password_opt.isPresent()) {

			boolean ssl = Boolean.parseBoolean((String) ssl_opt.get());
			Path ssl_key = Paths.get((String) ssl_key_opt.get());
			String ssl_password = (String) ssl_password_opt.get();

			server = HTTPServerFactory.create(port, ssl, ssl_key, ssl_password);
		}
		else {

			server = HTTPServerFactory.create(port);
		}

		server.run();
	}

	private void update() throws Exception {

		Optional<Object> update_software_opt = environment.getNullable("update.software");
		Optional<Object> update_domain_opt = environment.getNullable("update.domain");

		if(update_software_opt.isPresent()) {

			boolean update_software = Boolean.parseBoolean((String) update_software_opt.get());

			if(update_software) {

				updater.updateSoftware();
			}
		}

		if(update_domain_opt.isPresent()) {

			boolean update_domain = Boolean.parseBoolean((String) update_domain_opt.get());

			if(update_domain) {

				Optional<Object> dns_domain_opt = environment.getNullable("dns.domain");
				Optional<Object> dns_password_opt = environment.getNullable("dns.password");
				Optional<Object> dns_server_opt = environment.getNullable("dns.server");

				if(dns_domain_opt.isPresent() && dns_password_opt.isPresent() && dns_server_opt.isPresent()) {

					String dns_domain = (String) dns_domain_opt.get();
					String dns_password = (String) dns_password_opt.get();
					String dns_server = (String) dns_server_opt.get();

					String dns_auth = Utils.encode(dns_domain + ":" + dns_password);

					updater.updateDomain(dns_domain, dns_auth, dns_server);
				}
				else {

					logger.warn(Messages.UNDEFINED.getMessage("dns.<domain,password,server>"));
				}
			}
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

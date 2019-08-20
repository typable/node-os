package com.prototype;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.prototype.core.Core;


public class Prototype {

	/*
	TODO Event
	TODO Runtime
	 */

	public static final String VERSION = "1.3.4";

	public static String PATH;

	public static void run(String[] args) {

		try(Core core = new Core()) {

			core.init();
			core.run();
		}
		catch(Exception ex) {

			ex.printStackTrace();
		}
	}

	/*
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
	*/

	public static Path path(String ref, String path) {

		return Paths.get(Prototype.PATH + ref + path);
	}

	public static Path path(String path) {

		return Paths.get(Prototype.PATH + path);
	}
}

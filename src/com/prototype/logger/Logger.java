package com.prototype.logger;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {

	private SimpleDateFormat format;
	private SimpleDateFormat fileFormat;
	private File file;

	private String DEFAULT_LINE_BREAK;

	public Logger() {

		format = new SimpleDateFormat("HH:mm:ss");
		fileFormat = new SimpleDateFormat("DD-MM-YYYY");

		final String osName = System.getProperty("os.name");

		DEFAULT_LINE_BREAK = osName.startsWith("Windows") ? "\r\n" : "\n";
	}

	public Logger(String path) {

		this();

		if(new File(path).isDirectory()) {

			file = new File(path + fileFormat.format(new Date()) + ".log");

			try {

				if(!file.exists()) {

					file.createNewFile();
				}
			}
			catch(Exception e) {

				//
			}
		}
	}

	public void log(Logger.Type type, String message) {

		String value = format.format(new Date()) + " | [" + type.name() + "] " + message;

		System.out.println(value);

		if(file != null) {

			try {

				String data = Files.readString(file.toPath(), StandardCharsets.ISO_8859_1);
				data += (value + DEFAULT_LINE_BREAK);
				Files.writeString(file.toPath(), data, StandardCharsets.ISO_8859_1);
			}
			catch(Exception e) {

				//
			}
		}
	}

	public void info(String message) {

		log(Type.INFO, message);
	}

	public void warn(String message) {

		log(Type.WARN, message);
	}

	public void error(String message) {

		log(Type.ERROR, message);
	}

	public void error(String message, Exception ex) {

		if(ex.getMessage() != null) {

			log(Type.ERROR, message + ": " + ex.getMessage());
		}
		else {

			log(Type.ERROR, message + ":");
			ex.printStackTrace();
		}
	}

	public void debug(String message) {

		log(Type.DEBUG, message);
	}

	public enum Type {

		INFO, WARN, ERROR, DEBUG;
	}

	public enum Messages {

		SERVER_STARTED("Server started on port: {0}"), SERVER_STOPPED("Server stopped!"), NOT_FOUND("'{0}' could not be found!"), UNDEFINED("The parameter '{0}' is undefined!"), FATAL_ERROR("A fatal error occured!");

		private String message;

		private Messages(String message) {

			this.message = message;
		}

		public String getMessage(String... parameters) {

			for(int i = 0; i < parameters.length; i++) {

				message = message.replaceAll("\\{\\d\\}", parameters[i]);
			}

			return message;
		}

		public String getTemplateMessage() {

			return message;
		}
	}
}

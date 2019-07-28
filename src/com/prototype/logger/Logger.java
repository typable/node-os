package com.prototype.logger;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.prototype.Prototype;
import com.prototype.loader.Loader;


public class Logger {

	private Loader LOADER;

	private DateTimeFormatter dateFormatter;
	private DateTimeFormatter timeFormatter;
	private File file;

	private String DEFAULT_LINE_BREAK;

	public Logger() {

		dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		final String osName = System.getProperty("os.name");

		DEFAULT_LINE_BREAK = osName.startsWith("Windows") ? "\r\n" : "\n";
	}

	public void logFile(Path path) {

		LOADER = Prototype.loader();

		if(path.toFile().isDirectory()) {

			LocalDate date = LocalDate.now();

			file = new File(path + "/" + date.format(dateFormatter) + ".log");

			try {

				if(!file.exists()) {

					file.createNewFile();
				}
			}
			catch(Exception e) {

				e.printStackTrace();
			}
		}
	}

	public void log(Logger.Type type, String message) {

		LocalTime time = LocalTime.now();

		String value = time.format(timeFormatter) + " | [" + type.name() + "] " + message;

		System.out.println(value);

		if(file != null) {

			try {

				String data = LOADER.readText(file.toPath());
				data += (value + DEFAULT_LINE_BREAK);
				LOADER.writeText(file.toPath(), data);
			}
			catch(Exception e) {

				e.printStackTrace();
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

		Throwable throwable = ex.getCause() != null ? ex.getCause() : ex;

		String errorMessage = throwable.getClass().getName() + ": " + throwable.getMessage() + DEFAULT_LINE_BREAK;

		for(StackTraceElement trace : throwable.getStackTrace()) {

			errorMessage += "     at " + trace.getClassName() + "." + trace.getMethodName() + "(" + trace.getFileName() + ":" + trace.getLineNumber() + ")" + DEFAULT_LINE_BREAK;
		}

		log(Type.ERROR, message + ": " + errorMessage);
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

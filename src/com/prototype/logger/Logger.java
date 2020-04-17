package com.prototype.logger;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.core.reflect.Inject;
import com.core.reflect.Injectable;
import com.prototype.core.Core;
import com.prototype.loader.Loader;


public class Logger implements Injectable
{
	public static final String CODE = "logger";

	@Inject(code = Loader.CODE)
	private Loader loader;

	private DateTimeFormatter dateFormatter;
	private DateTimeFormatter timeFormatter;
	private File file;
	private boolean debug;
	private boolean save;

	private String DEFAULT_LINE_BREAK;

	public Logger(Path path)
	{
		dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		final String osName = System.getProperty("os.name");

		DEFAULT_LINE_BREAK = osName.startsWith("Windows") ? "\r\n" : "\n";

		if(path.toFile().isDirectory())
		{
			LocalDate date = LocalDate.now();

			file = new File(path + "/" + date.format(dateFormatter) + ".log");
		}

		inject(this, Core.environment);
	}

	public void log(Logger.Type type, String message)
	{
		if(type == Logger.Type.DEBUG && !debug)
		{
			return;
		}

		LocalTime time = LocalTime.now();

		String value = time.format(timeFormatter) + " | [" + type.name() + "] " + message;

		System.out.println(value);

		if(save)
		{
			if(file != null)
			{
				try
				{
					if(!file.exists())
					{
						file.createNewFile();
					}

					String data = loader.readText(file.toPath());
					data += (value + DEFAULT_LINE_BREAK);
					loader.writeText(file.toPath(), data);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void info(String message)
	{
		log(Type.INFO, message);
	}

	public void warn(String message)
	{
		log(Type.WARN, message);
	}

	public void error(String message)
	{
		log(Type.ERROR, message);
	}

	public void error(String message, Exception ex)
	{
		Throwable throwable = ex.getCause() != null ? ex.getCause() : ex;

		String errorMessage = ex.getClass().getName() + ": " + throwable
		      .getMessage() + DEFAULT_LINE_BREAK;

		for(StackTraceElement trace : throwable.getStackTrace())
		{
			errorMessage += "     at " + trace.getClassName() + "." + trace
			      .getMethodName() + "(" + trace
			            .getFileName() + ":" + trace.getLineNumber() + ")" + DEFAULT_LINE_BREAK;
		}

		log(Type.ERROR, message + ": " + errorMessage);
	}

	public void debug(String message)
	{
		log(Type.DEBUG, message);
	}

	public void debug(String message, Exception ex)
	{
		Throwable throwable = ex.getCause() != null ? ex.getCause() : ex;

		String errorMessage = ex.getClass().getName() + ": " + throwable
		      .getMessage() + DEFAULT_LINE_BREAK;

		for(StackTraceElement trace : throwable.getStackTrace())
		{
			errorMessage += "     at " + trace.getClassName() + "." + trace
			      .getMethodName() + "(" + trace
			            .getFileName() + ":" + trace.getLineNumber() + ")" + DEFAULT_LINE_BREAK;
		}

		log(Type.DEBUG, message + ": " + errorMessage);
	}

	public enum Type
	{
		INFO, WARN, ERROR, DEBUG;
	}

	public enum Messages
	{
		SERVER_STARTED("Server started on port: {0}"), SERVER_STOPPED("Server stopped!"), NOT_FOUND("'{0}' could not be found!"), UNDEFINED("The parameter '{0}' is undefined!"), FATAL_ERROR("A fatal error occured!");

		private String message;

		private Messages(String message)
		{
			this.message = message;
		}

		public String getMessage(String... parameters)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				message = message.replaceAll("\\{\\d\\}", parameters[i]);
			}

			return message;
		}

		public String getTemplateMessage()
		{
			return message;
		}
	}

	public boolean isDebug()
	{
		return debug;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}

	public boolean isSave()
	{
		return save;
	}

	public void setSave(boolean save)
	{
		this.save = save;
	}
}

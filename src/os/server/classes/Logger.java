package os.server.classes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	
	public Logger() {
		
		
	}
	
	public void log(Logger.Type type, String message) {
		
		System.out.println(format.format(new Date()) + " | [" + type.name() + "] " + message);
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
		
		log(Type.ERROR, message + ": " + ex.getMessage());
	}
	
	public enum Type {
		
		INFO, WARN, ERROR;
	}
	
	public enum Messages {
		
		SERVER_STARTED("Server started on port: {0}"),
		SERVER_STOPPED("Server stopped!"),
		NOT_FOUND("'{0}' could not be found!"),
		UNDEFINED("The parameter '{0}' is undefined!"),
		FATAL_ERROR("A fatal error occured!");
		
		private String message;
		
		private Messages(String message) {
			
			this.message = message;
		}
		
		public String getMessage(String... parameters) {
			
			for(int i = 0; i <parameters.length; i++) {
				
				message = message.replaceAll("\\{\\d\\}", parameters[i]);
			}
			
			return message;
		}
		
		public String getTemplateMessage() {
			
			return message;
		}
	}
}

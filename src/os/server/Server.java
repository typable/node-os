package os.server;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import os.server.Logger.Messages;
import os.server.handler.Handler;
import os.server.handler.Request;
import os.server.handler.Response;
import os.server.note.Controller;
import os.server.note.RequestHandler;
import os.server.type.ContentType;
import os.util.PropertyFile;
import os.util.Utils;

public class Server {

	private ServerSocket serverSocket;
	
	private List<Class<?>> controllerList = new ArrayList<Class<?>>();
	private List<Handler> handlerList = new ArrayList<Handler>();
	private Logger logger = new Logger();
	
	public static final String CONFIG_PATH = "/config.properties";
	public static final String RESOURCE_PATH = "/src";
	
	public static HashMap<String, String> configurations = new HashMap<String, String>();
	
	private int port;
	private String rootPath;
	
	public Server() {
		
		
	}
	
	public void launch() {
		
		for(Class<?> controller : controllerList) {
			
			for(Annotation anno : controller.getAnnotations()) {
				
				if(anno instanceof Controller) {
					
					for(Method method : controller.getDeclaredMethods()) {
						
						for(Annotation annotation : method.getAnnotations()) {

							if(annotation instanceof RequestHandler) {

								Handler handler = new Handler(this);
								handler.setHandler((RequestHandler) annotation);
								handler.setController(controller);
								handler.setExecutor(method);
								
								handlerList.add(handler);
							}
						}
					}
				}
			}
		}
		
		try {
			
			loadConfigurations();
			
			if(!loadRequirements()) {
				
				close();
			}
			
			serverSocket = new ServerSocket(port);
			
			logger.info(Messages.SERVER_STARTED.getMessage("" + port));
			
			while(!serverSocket.isClosed()) {
				
				Socket socket = serverSocket.accept();
				
				new Thread(() -> {
					
					try {
						
						Request request = new Request(this, socket);
						request.request();
						
						Response response = new Response(this, socket);
						
						if(request.getUrl() != null) {
							
							String url = request.getUrl();		
							Class<?> controller = null;
							Method method = null;
							
							for(Handler handler : handlerList) {
								
								if(handler.getHandler().url().equals(url) && handler.getHandler().method() == request.getMethod()) {
									
									method = handler.getExecutor();
									controller = handler.getController();
								}
							}
							
							if(method != null) {
								
								method.invoke(controller.getConstructor().newInstance(), request, response);
							}
							else if(url.startsWith("/src/")) {
								
								response.showPage(url, ContentType.getByFile(url));
							}
							else {
								
								logger.warn(Messages.NOT_FOUND.getMessage(url));
								response.notFound();
							}
						}
						
						response.commit();
					}
					catch(IOException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException e) {
						
						logger.error(Messages.FATAL_ERROR.getMessage());
						e.printStackTrace();
						
						close();
					}
					
				}).start();
			}
		}
		catch(IOException e) {
			
			logger.error(Messages.FATAL_ERROR.getMessage());
			e.printStackTrace();
			
			close();
		}
	}
	
	public void close() {
		
		logger.info(Messages.SERVER_STOPPED.getMessage());
		
		System.exit(0);
	}
	
	public void registerController(Class<?> controller) {
		
		controllerList.add(controller);
	}
	
	private void loadConfigurations() throws IOException {
		
		PropertyFile configFile = new PropertyFile(Utils.getCurrentPath() + CONFIG_PATH);
		
		if(configFile.exists()) {
			
			try {
				
				configFile.load();
				
				configurations = configFile.getProps();
				
				logger.info("Configurations loaded");
			}
			catch(IOException e) {
				
				logger.warn("Failed to load Configurations!");
				e.printStackTrace();
			}
		}
		else {
			
			try {
				
				configFile.create();
				
				if(configFile.exists()) {
					
					logger.info("Configuration file created");
					
					HashMap<String, String> props = new HashMap<String, String>();
					props.put("port", "80");
					props.put("root", "*/public");
					
					configFile.setProps(props);
					configFile.save();
					
					loadConfigurations();
				}
				else {
					
					logger.error("Failed to create new file!");
				}
			}
			catch(IOException e) {
				
				logger.error("Failed to create new file!");
				e.printStackTrace();
			}
		}
	}
	
	private boolean loadRequirements() throws IOException {
		
		boolean succeeded = true;
		
		String portProperty = getProperty("port");
		
		if(Utils.notEmpty(portProperty)) {
			
			port = Integer.parseInt(portProperty);
		}
		else {
			
			logger.warn(Messages.UNDEFINED.getMessage("port"));
			
			close();
		}
		
		String rootProperty = getProperty("root");
		
		if(Utils.notEmpty(rootProperty)) {
			
			if(rootProperty.contains("*")) {
				
				rootProperty = rootProperty.replaceAll("\\*", Utils.getCurrentPath());
			}
			
			rootPath = rootProperty;
			
			logger.info("Required properties loaded");
		}
		else {
			
			logger.warn(Messages.UNDEFINED.getMessage("root"));
			
			succeeded = false;
		}
		
		return succeeded;
	}
	
	public String getProperty(String key) {
		
		if(configurations != null && configurations.containsKey(key)) {
			
			return configurations.get(key);
		}

		return null;
	}

	public ServerSocket getServerSocket() {
	
		return serverSocket;
	}
	
	public List<Handler> getHandlerList() {
	
		return handlerList;
	}
	
	public Logger getLogger() {
	
		return logger;
	}
	
	public int getPort() {
		
		return port;
	}
	
	public String getRootPath() {
		
		return rootPath;
	}
}

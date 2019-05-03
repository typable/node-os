package os.server.protocol;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import os.server.Connection;

public class FTPConnection extends Connection {

	public FTPConnection(String host, int port) {
		
		super(host, port);
	}
	
	public FTPConnection(String host, int port, boolean isSSL) {
		
		super(host, port, isSSL);
	}
	
	public void send(File file, String path) throws IOException {
		
		if(file.exists() && file.isFile()) {
			
			connect(() -> {
				
				try {
					
					byte[] data = Files.readAllBytes(file.toPath());
					
					emit("PATH " + path);
					System.out.println(read());
					emit("FILE " + file.getName() + " " + data.length);
					emit(data);
					System.out.println(read());
					emit("QUIT");
					System.out.println(read());
					quit();
				}
				catch(IOException e) {
					
					e.printStackTrace();
				}
			});
		}
	}
}

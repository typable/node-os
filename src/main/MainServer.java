package main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

import os.connection.Connection;
import os.util.Utils;

public class MainServer {

	public static void main(String[] args) throws IOException {
		
		ServerSocket serverSocket = new ServerSocket(21);
		
		while(!serverSocket.isClosed()) {
			
			Connection con = new Connection(serverSocket.accept());
			con.connect(() -> {
				
				String line;
				String fileName = "none.txt";
				String path = "C:/DATA/FTP";
				int length = 0;
				
				try {
					
					while(Utils.notEmpty(line = con.read())) {
						
						if(line.equals("ECHO")) {
							
							con.emit("220");
						}
						
						if(line.startsWith("PATH")) {
							
							path = line.substring(5, line.length());
							con.emit("250");
						}
						
						if(line.startsWith("FILE")) {
							
							String[] line_ = line.split(" ");
							
							fileName = line_[1];
							length = Integer.parseInt(line_[2]);
							
							BufferedInputStream in = new BufferedInputStream(con.getSocket().getInputStream());
							
							byte[] data = in.readNBytes(length);
							
							File file = new File(path + "/" + fileName);
							file.createNewFile();
							
							FileOutputStream out = new FileOutputStream(file);
							out.write(data);
							out.close();
							
							con.emit("354");
						}
						
						if(line.equals("QUIT")) {
							
							con.emit("221");
							
							serverSocket.close();
							System.exit(0);
						}
					}
				}
				catch(IOException e) {
					
					e.printStackTrace();
				}
			});
		}
	}
}

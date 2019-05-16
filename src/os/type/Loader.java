package os.type;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import os.core.Core;
import os.type.Logger.Messages;

public class Loader {

	public Loader() {

		
	}
	
	public byte[] loadFile(String path) {
		
		try {
			
			path = Core.getRelativePath(path);
			
			File file = new File(path);
			InputStream in;
			
			if(file.exists() && file.isFile()) {
				
				in = new FileInputStream(file);
				
				byte[] data = in.readAllBytes();
				
				in.close();
				
				return data;
			}
		}
		catch(Exception e) {
			
			
		}
		
		Core.LOGGER.warn(Messages.NOT_FOUND.getMessage(path));
		
		return null;
	}
	
	public void saveFile(String path, byte[] data) {
		
		try {
			
			File file = new File(path);
			OutputStream out;
			
			if(file.exists() && file.isFile()) {
				
				out = new FileOutputStream(file);
				out.write(data);
				out.close();
			}
		}
		catch(Exception e) {
			
			Core.LOGGER.warn("Failed to save file!");
		}
	}
}

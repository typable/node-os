package os.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Utils {
	
	public static String getCurrentPath() throws IOException {
		
		return new File(".").getCanonicalPath().replaceAll("\\\\", "\\/");
	}
	
	public static boolean notEmpty(String line) {
		
		return (line != null && !line.equals("")) ? true : false;
	}
	
	public static void keySet(HashMap<String, String> attributes, String separator, String code) {
		
		if(code.contains(separator)) {
			
			String[] parts = code.split(separator);
			
			attributes.put(parts[0], parts[1]);
		}
	}
}

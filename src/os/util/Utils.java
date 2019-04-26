package os.util;

import java.io.File;
import java.io.IOException;

public class Utils {
	
	public static String getCurrentPath() throws IOException {
		
		return new File(".").getCanonicalPath();
	}
	
	public static boolean notEmpty(String line) {
		
		return (line != null && !line.equals("")) ? true : false;
	}
}

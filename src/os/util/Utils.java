package os.util;

public class Utils {
	
	public static boolean notEmpty(String line) {
		
		return (line != null && !line.equals("")) ? true : false;
	}
}

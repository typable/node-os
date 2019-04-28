package os.util;

public class URLParser {
	
	public static String parseURL(String code) {
		
		code = code.replaceAll("\\+", " ");
		code = code.replaceAll("%40", "@");
		code = code.replaceAll("%21", "!");
		
		return code;
	}
}
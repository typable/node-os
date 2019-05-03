package os.util;

import java.util.HashMap;

public class Parser {
	
	public static String parseURL(String code) {
		
		code = code.replaceAll("\\+", " ");
		code = code.replaceAll("%40", "@");
		code = code.replaceAll("%21", "!");
		
		return code;
	}
	
	public static String parseHTML(String code, HashMap<String, String> attributes) {
		
		for(String key : attributes.keySet()) {
			
			code = code.replaceAll("\\@\\{" + key + "\\}", attributes.get(key));
		}
		
		return code;
	}
}
package os.util;

import java.util.HashMap;

public class Parser {
	
	public static String parseURL(String code) {
		
		code = code.replaceAll("\\+", " ");
		code = code.replaceAll("%40", "@");
		code = code.replaceAll("%21", "!");
		
		return code;
	}
	
	public static String parseHref(String code, String lang) {
		
		if(!lang.equals("en")) {
			
			// return code.replaceAll("href=\"/\"", "href='/?lang=de'");
			
			return code.replaceAll("(href|action)=(\"|')(/[\\w?&=+]*)(\"|')", "$1='$3?lang=" + lang + "'");
		}
		
		return code;
	}
	
	public static String parseHTML(String code, HashMap<String, String> attributes) {
		
		for(String key : attributes.keySet()) {
			
			code = code.replaceAll("\\@\\{" + key + "\\}", attributes.get(key));
		}
		
		return code;
	}

	public static String parseLang(String code, String lang, Property languages) {

		for(String key : languages.keys()) {
			
			if(key.startsWith(lang)) {
				
				code = code.replaceAll("\\@\\{lang:" + key.substring(3, key.length()) + "\\}", languages.get(key));
			}
		}
		
		return code;
	}
}
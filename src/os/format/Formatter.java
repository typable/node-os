package os.format;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import util.type.Property;


public class Formatter {

	public static String parseURL(String code) {

		return URLDecoder.decode(code, StandardCharsets.UTF_8);
	}

	public static String parseHTML(String code, Property<String> attributes) {

		for(String key : attributes.keys()) {

			if(attributes.get(key) != null) {

				code = code.replaceAll("\\@\\{" + key + "\\}", attributes.get(key));
			}
			else {

				code = code.replaceAll("\\@\\{" + key + "\\}", "");
			}
		}

		code = code.replaceAll("\\@\\{[^{]*\\}", "");

		return code;
	}

	public static String parseLang(String code, String lang, Property<String> languages) {

		for(String key : languages.keys()) {

			if(key.startsWith(lang)) {

				code = code.replaceAll("\\@\\{lang:" + key.substring(3, key.length()) + "\\}", languages.get(key));
			}
		}

		return code;
	}
}
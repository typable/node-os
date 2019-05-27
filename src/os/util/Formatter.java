package os.util;

public class Formatter {

	public static String parseURL(String code) {

		code = code.replaceAll("\\+", " ");
		code = code.replaceAll("%40", "@");
		code = code.replaceAll("%21", "!");
		code = code.replaceAll("%2F", "/");

		return code;
	}

	public static String parseHref(String code, String lang) {

		if(!lang.equals("en")) {

			return code.replaceAll("(href|action)=(\"|')(/[\\w?&=+]*)(\"|')", "$1='$3?lang=" + lang + "'");
		}

		return code;
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
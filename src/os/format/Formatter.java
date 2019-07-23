package os.format;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;

import com.prototype.type.Property;

import os.core.Core;


public class Formatter {

	public static String parseURL(String code) {

		return URLDecoder.decode(code, Core.DEFAULT_CHARSET);
	}

	public static String parse(String code, Property<String> attributes) throws Exception {

		code = parseText(code);
		code = parseTemplate(code, attributes);
		// TODO code = parseLang(code, getLanguage(), Core.LANGUAGES);
		code = parseHTML(code, attributes);

		return code;
	}

	public static String parseHTML(String code, Property<String> attributes) {

		for(String key : attributes.keys()) {

			if(attributes.get(key) != null) {

				code = code.replaceAll("\\@\\{" + key + "\\}", attributes.get(key));
			}
			else {

				// code = code.replaceAll("\\@\\{" + key + "\\}", "");
			}
		}

		for(String key : Core.PROPERTIES.keys()) {

			if(Core.PROPERTIES.get(key) != null) {

				code = code.replaceAll("\\@\\{" + key + "\\}", Core.PROPERTIES.get(key));
			}
			else {

				// code = code.replaceAll("\\@\\{" + key + "\\}", "");
			}
		}

		// code = code.replaceAll("\\@\\{[^{]*\\}", "");

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

	public static String parseText(String code) {

		if(Core.TEXTS != null) {

			for(String key : Core.TEXTS.keys()) {

				code = code.replaceAll("\\@\\{text:" + key + "\\}", Core.TEXTS.get(key));
			}
		}

		return code;
	}

	public static String parseTemplate(String code, Property<String> attributes) throws Exception {

		for(File file : Core.TEMPLATES) {

			if(file != null) {

				File textFile = new File(file.getAbsolutePath());

				String key = file.getName().contains(".") ? file.getName().split("\\.")[0] : file.getName();

				if(textFile.exists()) {

					String text = Files.readString(file.toPath(), Core.DEFAULT_CHARSET);

					text = parseText(text);
					// TODO code = parseLang(code, getLanguage(), Core.LANGUAGES);
					text = parseHTML(text, attributes);

					if(key != null) {

						code = code.replaceAll("\\@\\{template:" + key + "\\}", text);
					}
				}
			}
		}

		return code;
	}
}
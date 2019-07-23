package com.prototype.format;

import java.io.File;
import java.net.URLDecoder;

import com.prototype.Prototype;
import com.prototype.type.Property;
import com.prototype.util.Utils;


public class Formatter {

	public static String parseURL(String code) {

		return URLDecoder.decode(code, Prototype.constant().CHARSET);
	}

	public static Property<String> parseQuery(String code) {

		Property<String> props = new Property<>();

		if(code.contains("&")) {

			for(String param : code.split("&")) {

				Utils.addAttribute(props, "=", param);
			}
		}
		else {

			Utils.addAttribute(props, "=", code);
		}

		return props;
	}

	public static String parse(String code, Property<String> attributes) throws Exception {

		code = parseText(code);
		code = parseTemplate(code, attributes);
		code = parseHTML(code, attributes);

		return code;
	}

	public static String parseHTML(String code, Property<String> attributes) {

		for(String key : attributes.keys()) {

			if(attributes.get(key) != null) {

				code = code.replaceAll("\\@\\{" + key + "\\}", attributes.get(key));
			}
		}

		for(String key : Prototype.env().keys()) {

			if(Prototype.env().get(key) != null) {

				code = code.replaceAll("\\@\\{" + key + "\\}", (String) Prototype.env().get(key));
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

	public static String parseText(String code) {

		if(Prototype.message() != null) {

			for(String key : Prototype.message().keys()) {

				code = code.replaceAll("\\@\\{text:" + key + "\\}", Prototype.message().get(key));
			}
		}

		return code;
	}

	public static String parseTemplate(String code, Property<String> attributes) throws Exception {

		for(File file : Prototype.template()) {

			if(file != null) {

				File textFile = new File(file.getAbsolutePath());

				String key = file.getName().contains(".") ? file.getName().split("\\.")[0] : file.getName();

				if(textFile.exists()) {

					String text = Prototype.loader().read(file.toPath());

					text = parseText(text);
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

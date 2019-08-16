package com.prototype.format;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

import com.prototype.constants.Constants;
import com.prototype.loader.Loader;
import com.prototype.reflect.Inject;
import com.prototype.type.Parameter;
import com.prototype.type.Property;
import com.prototype.util.Utils;


public class Formatter {

	public static final String NAME = "prototype.formatter";

	@Inject(name = Loader.NAME)
	private Loader loader;

	@Inject(name = "configurations")
	private Property<String> conifgurations;

	@Inject(name = "templates")
	private List<File> templates;

	@Inject(name = "messages")
	private Property<String> messages;

	public static String parseURL(String code) {

		return URLDecoder.decode(code, Constants.CHARSET);
	}

	public static Property<Parameter> parseQuery(String code) {

		Property<Parameter> params = new Property<>();

		if(code.contains("&")) {

			for(String param : code.split("&")) {

				Utils.addParameter(params, "=", param);
			}
		}
		else {

			Utils.addParameter(params, "=", code);
		}

		return params;
	}

	public static String parseHTML(String code, Property<String> attributes) {

		for(String key : attributes.keys()) {

			if(attributes.get(key) != null) {

				code = code.replaceAll("\\@\\{" + key + "\\}", attributes.get(key));
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

	public Formatter() {

		//
	}

	public String parse(String code, Property<String> attributes) throws Exception {

		code = parseText(code);
		code = parseConfigurations(code);
		code = parseTemplate(code, attributes);
		code = parseHTML(code, attributes);

		return code;
	}

	public String parseConfigurations(String code) {

		if(conifgurations != null) {

			for(String key : conifgurations.keys()) {

				if(conifgurations.get(key) instanceof String) {

					String value = (String) conifgurations.get(key);

					code = code.replaceAll("\\@\\{env:" + key + "\\}", value);
				}
			}
		}

		return code;
	}

	public String parseText(String code) {

		if(messages != null) {

			for(String key : messages.keys()) {

				code = code.replaceAll("\\@\\{text:" + key + "\\}", messages.get(key));
			}
		}

		return code;
	}

	public String parseTemplate(String code, Property<String> attributes) throws Exception {

		for(File file : templates) {

			if(file != null) {

				File textFile = new File(file.getAbsolutePath());

				String key = file.getName().contains(".") ? file.getName().split("\\.")[0] : file.getName();

				if(textFile.exists()) {

					String text = loader.readText(file.toPath());

					text = parseText(text);
					text = parseConfigurations(text);
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

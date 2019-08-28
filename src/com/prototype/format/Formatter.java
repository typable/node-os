package com.prototype.format;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

import com.core.base.Condition;
import com.core.base.Environment;
import com.core.lang.Property;
import com.core.reflect.Inject;
import com.core.reflect.Injectable;
import com.prototype.core.Core;
import com.prototype.loader.Loader;
import com.prototype.type.Parameter;
import com.prototype.util.HTTPUtils;


public class Formatter implements Injectable {

	public static final String CODE = "formatter";

	@Inject(code = Loader.CODE)
	private Loader loader;

	@Inject(code = "configurations")
	private Property<String> conifgurations;

	@Inject(code = "templates")
	private List<File> templates;

	@Inject(code = "messages")
	private Property<String> messages;

	public static String format(String code, String tag, String key, String text) {

		code = code.replaceAll("\\@\\{" + (Condition.notBlank(tag) ? tag + ":" : "") + key + "\\}", text);

		return code;
	}

	public static String format(String code, String tag, Property<String> args) {

		for(String key : args.keys()) {

			code = format(code, tag, key, args.get(key));
		}

		return code;
	}

	public static String parseURL(String code) {

		return URLDecoder.decode(code, Environment.CHARSET);
	}

	public static Property<Parameter> parseQuery(String code) {

		Property<Parameter> params = new Property<>();

		if(code.contains("&")) {

			for(String param : code.split("&")) {

				HTTPUtils.addParameter(params, "=", param);
			}
		}
		else {

			HTTPUtils.addParameter(params, "=", code);
		}

		return params;
	}

	public Formatter() {

		inject(this, Core.environment);
	}

	public String parse(String code, Property<String> args, boolean injectTemplate) throws Exception {

		code = format(code, "text", messages);
		code = format(code, "env", conifgurations);

		/** inject templates **/
		if(injectTemplate) {

			code = injectTemplate(code, args);
		}

		/** format attributes **/
		code = format(code, null, args);

		return code;
	}

	public String injectTemplate(String code, Property<String> args) throws Exception {

		for(File file : templates) {

			if(Condition.notNull(file)) {

				File textFile = new File(file.getAbsolutePath());

				String key = file.getName().contains(".") ? file.getName().split("\\.")[0] : file.getName();

				if(textFile.exists()) {

					String text = loader.readText(file.toPath());

					text = parse(text, args, false);

					if(Condition.notNull(key)) {

						code = format(code, "template", key, text);
					}
				}
			}
		}

		return code;
	}
}

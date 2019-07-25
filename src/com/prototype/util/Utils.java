package com.prototype.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.prototype.type.Property;


public class Utils {

	public static void keySet(Property<String> attributes, String separator, String code) {

		if(code.contains(separator)) {

			String[] parts = code.split(separator);

			attributes.put(parts[0], parts[1]);
		}
	}

	public static void addAttribute(Property<String> property, String delimiter, String code) {

		if(code.contains(delimiter)) {

			String[] args = code.split(delimiter, -1);

			property.put(args[0], args[1]);
		}
	}

	public static String encode(String value) {

		return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.ISO_8859_1));
	}

	public static String whitespace(int length) {

		String code = "";

		for(int i = 0; i < length; i++) {

			code += " ";
		}

		return code;
	}
}

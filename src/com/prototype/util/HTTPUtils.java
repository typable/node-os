package com.prototype.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.core.lang.Property;
import com.core.util.Utils;
import com.prototype.type.Parameter;


public class HTTPUtils extends Utils
{
	public static void addParameter(Property<Parameter> params, String delimiter, String code)
	{
		if(code.contains(delimiter))
		{
			String[] args = code.split(delimiter, -1);

			Parameter param = new Parameter(args[0]);
			param.setValue(args[1]);

			params.put(args[0], param);
		}
	}

	public static String encode(String value)
	{
		return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.ISO_8859_1));
	}
}

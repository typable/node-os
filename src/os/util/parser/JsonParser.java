package os.util.parser;

import os.util.JsonObject;
import os.util.Utils;


public class JsonParser implements Parser<JsonObject, String> {

	@Override
	public JsonObject parse(String source) {

		JsonObject target = new JsonObject();

		source = source.substring(1, source.length() - 1);

		String[] splits = Utils.split(source, ',', new char[] { '{', '[' }, new char[] { '}', ']' });

		for(String s : splits) {

			int index_ = s.indexOf(":");
			String key = s.substring(1, index_ - 1);
			String value = s.substring(index_ + 2, s.length());

			char start = value.charAt(0);
			char end = value.charAt(value.length() - 1);

			Object obj_ = null;

			if(start == '{' && end == '}') {

				value = value.substring(1, value.length() - 1);

				obj_ = parse(value);
			}
			else if(start == '[' && end == ']') {

				value = value.substring(1, value.length() - 1);

				String[] splits_ = Utils.split(value, ',', new char[] { '{', '[' }, new char[] { '}', ']' });

				JsonObject[] obj__ = new JsonObject[splits_.length];

				for(int i = 0; i < splits_.length; i++) {

					obj__[i] = parse(splits_[i]);
				}

				obj_ = obj__;
			}
			else if(start == '"' && end == '"') {

				value = value.substring(1, value.length() - 1);

				obj_ = value;
			}
			else {

				if(value.equals("true") || value.equals("false")) {

					obj_ = Boolean.valueOf(value);
				}
				else if(value.contains(".")) {

					obj_ = Double.parseDouble(value);
				}
				else {

					obj_ = Integer.parseInt(value);
				}
			}

			target.set(key, obj_);
		}

		return target;
	}

	@Override
	public String compose(JsonObject source) {

		String target = "{";

		int i = 0;
		int len = source.keys().size() - 1;

		for(String key : source.keys()) {

			Object obj_ = source.values().get(key);

			target += "\"" + key + "\": ";

			if(obj_ instanceof JsonObject) {

				target += compose((JsonObject) obj_);
			}
			else if(obj_ instanceof JsonObject[]) {

				target += "[";

				int i_ = 0;
				int len_ = ((JsonObject[]) obj_).length - 1;

				for(JsonObject obj__ : ((JsonObject[]) obj_)) {

					if(obj__ != null) {

						target += compose(obj__);

						if(i_ < len_) {

							target += ", ";
						}

						i_++;
					}
				}

				target += "]";
			}
			else if(obj_ instanceof String) {

				target += "\"" + (String) obj_ + "\"";
			}
			else if(obj_ instanceof Integer || obj_ instanceof Double || obj_ instanceof Boolean) {

				target += String.valueOf(obj_);
			}
			else {

				target += "null";
			}

			if(i < len) {

				target += ", ";
			}

			i++;
		}

		target += "}";

		return target;
	}
}

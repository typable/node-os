package os.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import os.util.parser.JsonParser;
import os.util.parser.PropertyParser;


public class File {

	private java.io.File file;
	private FileInputStream in;
	private FileOutputStream out;
	private byte[] data;

	private JsonParser jsonParser;
	private PropertyParser propertyParser;

	public File(String path) {

		file = new java.io.File(path);

		jsonParser = new JsonParser();
		propertyParser = new PropertyParser();
	}

	public void load() throws IOException {

		in = new FileInputStream(file);

		data = in.readAllBytes();
		in.close();
	}

	public void save() throws IOException {

		out = new FileOutputStream(file);

		out.write(data);
		out.close();
	}

	public boolean exists() {

		return file.exists();
	}

	public void create() throws IOException {

		file.createNewFile();
	}

	public void setData(byte[] data) {

		this.data = data;
	}

	public byte[] getData() {

		return data;
	}

	public String getDataAsString() {

		return new String(data, StandardCharsets.UTF_8);
	}

	public void setJson(JsonObject obj) {

		String data = jsonParser.compose(obj);

		setData(data.getBytes());
	}

	public JsonObject getJson() {

		JsonObject json = jsonParser.parse(getDataAsString());

		return json;
	}

	public void setProps(Property<String> props) {

		String data = propertyParser.compose(props);

		setData(data.getBytes());
	}

	public Property<String> getProps() {

		Property<String> property = propertyParser.parse(getDataAsString());

		return property;
	}
}

package os.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class File {

	private java.io.File file;
	private FileInputStream in;
	private FileOutputStream out;
	private byte[] data;
	
	public File(String path) {
		
		file = new java.io.File(path);
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
	
	public void setJson(JsonObject obj) {
		
		setData(obj.stringify().getBytes());
	}
	
	public JsonObject getJson() {
		
		return JsonObject.parse(new String(getData()));
	}
	
	public void setProps(Property props) {
		
		setData(props.stringify().getBytes());
	}
	
	public Property getProps() {
		
		return Property.parse(new String(getData()));
	}
}

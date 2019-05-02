package os.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class JsonFile extends File {

	private static final long serialVersionUID = 1L;

	private JsonObject jsonObject = new JsonObject();
	
	public JsonFile(String filePath) {

		super(filePath);
	}

	public void load() throws IOException {

		FileInputStream fileInput = new FileInputStream(this);

		jsonObject.json(new String(fileInput.readAllBytes()));
		fileInput.close();
	}

	public void save() throws IOException {

		FileOutputStream fileOutput = new FileOutputStream(this);

		fileOutput.write(jsonObject.stringify().getBytes());
		fileOutput.close();
	}
	
	public void create() throws IOException {
		
		createNewFile();
	}

	public void setJson(JsonObject jsonObject) {
		
		this.jsonObject = jsonObject;
	}

	public JsonObject getJson() {
		
		return jsonObject;
	}
}

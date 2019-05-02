package os.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class PropertyFile extends File {

	private static final long serialVersionUID = 1L;
	
	private Properties props = new Properties();

	public PropertyFile(String filePath) {

		super(filePath);
	}

	public void load() throws IOException {

		FileInputStream fileInput = new FileInputStream(this);

		props.load(fileInput);

		fileInput.close();
	}

	public void save() throws IOException {

		FileOutputStream fileOutput = new FileOutputStream(this);

		props.store(fileOutput, "");

		fileOutput.close();
	}
	
	public void create() throws IOException {
		
		createNewFile();
	}

	public void setProps(HashMap<String, String> properties) {

		Properties props = new Properties();
		
		for(String key : properties.keySet()) {
			
			props.setProperty(key, properties.get(key));
		}
		
		this.props = props;
	}

	public HashMap<String, String> getProps() {

		HashMap<String, String> properties = new HashMap<String, String>();
		
		Set<Object> keys = props.keySet();
		
		for(Object key : keys) {
			
			properties.put((String) key, (String) props.get(key));
		}
		
		return properties;
	}
}
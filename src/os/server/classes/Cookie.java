package os.server.classes;


public class Cookie {

	private String key;
	private String value;
	private int age;
	
	public Cookie(String key, String value) {

		this.key = key;
		this.value = value;
	}

	public String getKey() {
	
		return key;
	}
	
	public void setKey(String key) {
	
		this.key = key;
	}
	
	public String getValue() {
	
		return value;
	}
	
	public void setValue(String value) {
	
		this.value = value;
	}
	
	public int getAge() {
	
		return age;
	}
	
	public void setAge(int age) {
	
		this.age = age;
	}
}

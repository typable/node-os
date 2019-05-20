package os.type;

public class Cookie {

	private String key;
	private String value;
	private long age;

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

	public long getAge() {

		return age;
	}

	public void setAge(long age) {

		this.age = age;
	}
}

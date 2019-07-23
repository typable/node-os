package os.type;

import java.util.Date;

import util.type.Property;


public class Session {

	private String uuid;
	private final long timeout = 1000 * 60 * 60;
	private long creationTime;
	private long timeoutTime;
	private long age;
	private Property<Object> attributes;

	public Session(String uuid) {

		this.uuid = uuid;

		Date date = new Date();

		creationTime = date.getTime();
		timeoutTime = creationTime + timeout;
		age = 0;

		attributes = new Property<Object>();
	}

	public void update() {

		Date date = new Date();

		timeoutTime = date.getTime() + timeout;
		age = date.getTime() - creationTime;
	}

	public String getUUID() {

		return uuid;
	}

	public long getCreationTime() {

		return creationTime;
	}

	public void setCreationTime(long creationTime) {

		this.creationTime = creationTime;
	}

	public long getTimeoutTime() {

		return timeoutTime;
	}

	public void setTimeoutTime(long timeoutTime) {

		this.timeoutTime = timeoutTime;
	}

	public long getAge() {

		return age;
	}

	public void setAge(long age) {

		this.age = age;
	}

	public Property<Object> getAttributes() {

		return attributes;
	}

	public void setAttributes(Property<Object> attributes) {

		this.attributes = attributes;
	}

	public Object getAttribute(String key) {

		return attributes.get(key);
	}

	public void setAttribute(String key, Object value) {

		attributes.put(key, value);
	}

	public long getTimeout() {

		return timeout;
	}
}

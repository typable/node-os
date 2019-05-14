package os.server.classes;


public class User {

	private String uuid;
	private String name;
	private String email;
	private String password;
	private String profileUrl;
	
	public User(String name, String email, String password) {
		
		uuid = email;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public String getUuid() {
		
		return uuid;
	}

	public void setUuid(String uuid) {
		
		this.uuid = uuid;
	}

	public String getName() {
		
		return name;
	}

	public void setName(String name) {
		
		this.name = name;
	}

	public String getEmail() {
		
		return email;
	}

	public void setEmail(String email) {
		
		this.email = email;
	}

	public String getPassword() {
		
		return password;
	}

	public void setPassword(String password) {
		
		this.password = password;
	}

	public String getProfileUrl() {
		
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		
		this.profileUrl = profileUrl;
	}
}

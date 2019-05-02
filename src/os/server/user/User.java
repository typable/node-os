package os.server.user;

import java.util.UUID;

public class User {

	private UUID uuid;
	private String name;
	private String email;
	private String password;
	private String profileUrl;
	
	public User(String name, String email, String password) {
		
		UUID.fromString(email);
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public UUID getUuid() {
		
		return uuid;
	}

	public void setUuid(UUID uuid) {
		
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

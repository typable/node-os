package os.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import os.core.Core;
import os.server.classes.User;
import os.util.File;
import os.util.JsonObject;

public class UserService {

	private List<User> userList;
	private User currentUser;
	
	public UserService() throws IOException {
		
		userList = new ArrayList<User>();
	}
	
	public void loadUsers() throws IOException {
		
		File file = new File(Core.ROOT + Core.RESOURCE_PATH + "/database/users.json");
		
		if(file.exists()) {
			
			file.load();
			
			JsonObject json = file.getJson();
			JsonObject[] users = json.getArray("users");
			
			for(JsonObject user : users) {
				
				userList.add(new User(user.getString("name"), user.getString("email"), user.getString("password")));
			}
		}
	}
	
	public User getUserByUUID(String uuid) {
		
		for(User user : userList) {
			
			if(user.getUuid().equals(uuid)) {
				
				return user;
			}
		}
		
		return null;
	}
	
	public User getCurrentUser() {
		
		return currentUser;
	}
	
	public List<User> getAllUsers() {
		
		return userList;
	}
}

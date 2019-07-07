package os.service;

import java.util.ArrayList;
import java.util.List;

import os.core.Core;
import os.handler.HttpRequest;
import os.type.Session;
import os.type.User;
import util.file.JSONFile;
import util.type.JSONObject;


public class UserService extends Service {

	private List<User> users;

	public UserService() {

		users = new ArrayList<User>();
	}

	public void loadUsers() {

		try {

			JSONFile file = new JSONFile(Core.ROOT + "/src/database/users.json");

			if(file.exists()) {

				file.load();

				JSONObject obj = file.getJSONObject();

				for(JSONObject user : obj.getJSONObjectArray("users")) {

					users.add(new User(user.getString("name"), user.getString("email"), user.getString("password")));
				}
			}
		}
		catch(Exception e) {

			e.printStackTrace();
		}
	}

	public User getCurrentUser(HttpRequest request) {

		Session session = Core.sessionService.getCurrentSession(request);

		if(session != null) {

			return (User) session.getAttribute("user");
		}

		return null;
	}

	public List<User> getUsers() {

		return users;
	}

	public void setUsers(List<User> users) {

		this.users = users;
	}
}

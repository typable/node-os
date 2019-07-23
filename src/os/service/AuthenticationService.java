package os.service;

import os.type.User;


@Deprecated
public class AuthenticationService extends Service {

	public AuthenticationService() {

		//
	}

	public User login(String email, String password) {

		//		for(User user : Core.userService.getUsers()) {
		//
		//			if(email.equals(user.getEmail()) && password.equals(user.getPassword())) {
		//
		//				return user;
		//			}
		//		}

		return null;
	}

	public User register(String name, String email, String password) {

		return null;
	}
}

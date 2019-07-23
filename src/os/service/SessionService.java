package os.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import os.handler.HttpRequest;
import os.handler.HttpResponse;
import os.type.Cookie;
import os.type.Session;
import os.type.User;


@Deprecated
public class SessionService extends Service {

	private List<Session> sessions;

	public SessionService() {

		sessions = new ArrayList<Session>();
	}

	public Session createSession(HttpResponse response) {

		return createSession(null, response);
	}

	public Session createSession(User user, HttpResponse response) {

		String uuid = UUID.randomUUID().toString();

		Session session = new Session(uuid);

		if(user != null) {

			session.setAttribute("user", user);
		}

		sessions.add(session);

		Cookie cookie = new Cookie("_uuid", uuid);
		cookie.setAge(60 * 60 * 24);

		response.addCookie(cookie);

		return null;
	}

	public Session getCurrentSession(HttpRequest request) {

		Cookie cookie = request.getCookie("_uuid");

		return getCurrentSession(cookie);
	}

	public Session getCurrentSession(Cookie cookie) {

		if(cookie != null) {

			for(Session session : sessions) {

				if(session.getUUID().equals(cookie.getValue())) {

					return session;
				}
			}
		}

		return null;
	}

	public List<Session> getSessions() {

		return sessions;
	}

	public void setSessions(List<Session> sessions) {

		this.sessions = sessions;
	}
}

package com.prototype.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.core.reflect.Inject;
import com.core.reflect.Injectable;
import com.core.service.Service;
import com.prototype.core.Core;
import com.prototype.http.HTTPRequest;
import com.prototype.http.HTTPResponse;
import com.prototype.type.Cookie;
import com.prototype.type.Session;


public class SessionService extends Service implements Injectable {

	@Inject(code = "sessions")
	private List<Session> sessions;

	public SessionService() {

		inject(this, Core.environment);
	}

	public void prepareSession(HTTPRequest request, HTTPResponse response) {

		if(request.hasCookie(Session.USID)) {

			Cookie cookie = request.getCookie(Session.USID);

			for(Session session : sessions) {

				if(session.getUid() != null && cookie.getValue() != null) {

					if(session.getUid().equals(cookie.getValue())) {

						request.setSession(session);
					}
				}
			}
		}
		else {

			Session session = new Session(UUID.randomUUID().toString());

			Cookie cookie = Cookie.of(session);
			cookie.setAge(60 * 60 * 24);

			request.setSession(session);
			response.addCookie(cookie);
		}
	}

	@Override
	public void run() {

		//
	}

	@Override
	public void close() throws IOException {

		//
	}
}

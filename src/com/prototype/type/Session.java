package com.prototype.type;

import com.prototype.Prototype;


public class Session extends Property<Object> {

	private String uid;

	public static Session of(Cookie cookie) {

		if(cookie != null) {

			for(Session session : Prototype.sessions()) {

				if(session.getUid() != null && cookie.getValue() != null) {

					if(session.getUid().equals(cookie.getValue())) {

						return session;
					}
				}
			}
		}

		return null;
	}

	public Session(String uid) {

		this.uid = uid;
	}

	public String getUid() {

		return uid;
	}
}

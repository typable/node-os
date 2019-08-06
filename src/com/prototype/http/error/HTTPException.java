package com.prototype.http.error;

public class HTTPException extends Exception {

	private static final long serialVersionUID = -3683174684211982378L;

	private HTTPError error;

	public HTTPException(HTTPError error) {

		this.error = error;
	}

	public HTTPException(HTTPError error, String message) {

		super(message);

		this.error = error;
	}

	public HTTPError getError() {

		return error;
	}
}

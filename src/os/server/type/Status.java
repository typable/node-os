package os.server.type;

public enum Status {

	// GET RESPONSE
	OK(200, "OK"), // Request succeeded

	// POST/PUT RESPONSE
	CREATED(201, "CREATED"), // uploaded for the first time

	// REDIRECTIONS:
	FOUND(302, "FOUND"), // update <--- USE THIS to normal redirect
	TEMPORARY_REDIRECT(307, "TEMPORARY REDIRECT"), // don't update!
	PERMANENT_REDIRECT(308, "PERMANENT REDIRECT"), // would be cached!

	// FAILS
	BAD_REQUEST(400, "BAD REQUEST"), // invalid request!
	FORBIDDEN(403, "FORBIDDEN"), NOT_FOUND(404, "NOT_FOUND"), INTERNAL_SERVER_ERROR(500, "BAD INTERNAL_SERVER_ERROR"),
	SERVICE_UNAVAILABLE(503, "SERVICE UNAVAILABLE");

	private int code;
	private String type;

	private Status(int code, String type) {

		this.code = code;
		this.type = type;
	}

	public int getCode() {

		return code;
	}

	public String getType() {

		return type;
	}

	public String getMessage() {

		return code + " " + type;
	}
}

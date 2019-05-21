package os.type;

public enum Header {

	/**
	 * WWW-Authenticate<br/>
	 * Defines the authentication method that should be used to gain access to a
	 * resource.
	 */
	WWW_AUTHENTICATE("WWW-Authenticate", null),

	/**
	 * Authorization<br/>
	 * Contains the credentials to authenticate a user agent with a server.
	 */
	AUTHORIZATION("Authorization", null),

	/**
	 * Proxy-Authenticate<br/>
	 * Defines the authentication method that should be used to gain access to a
	 * resource behind a Proxy server.
	 */
	PROXY_AUTHENTICATE("Proxy-Authenticate", null),

	/**
	 * Proxy-Authorization<br/>
	 * Contains the credentials to authenticate a user agent with a proxy server.
	 */
	PROXY_AUTHORIZATION("Proxy-Authorization", null),

	/**
	 * Clear-Site-Data<br/>
	 * Clears browsing data (e.g. cookies, storage, cache) associated with the
	 * requesting website.
	 */
	CLEAR_SITE_DATA("Clear-Site-Data", new String[] { "cache", "cookies", "storage", "executionContexts", "*" }),

	/**
	 * Accept<br/>
	 * Informs the server about the types of data that can be sent back. It is
	 * MIME-type.
	 */
	ACCEPT("Accept", null),

	/**
	 * Accept-Charset<br/>
	 * Informs the server about which character set the client is able to
	 * understand. Accept-Encoding
	 */
	ACCEPT_CHARSET("Accept-Charset", null),

	/**
	 * Accept-Encoding<br/>
	 * Informs the server about the encoding algorithm, usually a compression
	 * algorithm, that can be used on the resource sent back.
	 */
	ACCEPT_ENCODING("Accept-Encoding", null),

	/**
	 * Accept-Language<br/>
	 * Informs the server about the language the server is expected to send back.
	 * This is a hint and is not necessarily under the full control of the user: the
	 * server should always pay attention not to override an explicit user choice
	 * (like selecting a language in a drop down list).
	 */
	ACCEPT_LANGUAGE("Accept-Language", null),

	/**
	 * Content-Type<br/>
	 * Indicates the media type of the resource.
	 */
	CONTENT_TYPE("Content-Type", null),

	/**
	 * Content-Length<br/>
	 * Indicates the size of the entity-body, in decimal number of octets, sent to
	 * the recipient.
	 */
	CONTENT_LENGTH("Content-Length", null),

	/**
	 * Content-Encoding<br/>
	 * Used to specify the compression algorithm.<br/>
	 * <br/>
	 * Options: indentity, compress, deflate, gzip, br
	 */
	CONTENT_ENCODING("Content-Encoding", new String[] { "indentity", "compress", "deflate", "gzip", "br" }),

	/**
	 * Content-Language<br/>
	 * Describes the language(s) intended for the audience, so that it allows a user
	 * to differentiate according to the users' own preferred language.
	 */
	CONTENT_LANGUAGE("Content-Language", null),

	/**
	 * Content-Location<br/>
	 * Indicates an alternate location for the returned data.
	 */
	CONTENT_LOCATION("Content-Location", null),

	/**
	 * Content-Disposition<br/>
	 * Is a response header if the resource transmitted should be displayed inline
	 * (default behavior when the header is not present), or it should be handled
	 * like a download and the browser should present a 'Save As' window.<br/>
	 * <br/>
	 * Options: inline, attachment, form-data
	 */
	CONTENT_DISPOSITION("Content-Disposition", new String[] { "inline", "attachment", "form-data" }),

	/**
	 * Cookie<br/>
	 * Contains stored HTTP cookies previously sent by the server with the
	 * Set-Cookie header.
	 */
	COOKIE("Cookie", null),

	/**
	 * Set-Cookie<br/>
	 * Send cookies from the server to the user agent.
	 */
	SET_COOKIE("Set-Cookie", null),

	/**
	 * Location<br/>
	 * Indicates the URL to redirect a page to.
	 */
	LOCATION("Location", null),

	/**
	 * Sec-WebSocket-Accept<br/>
	 * The Sec-WebSocket-Accept header is used in the websocket opening handshake.
	 * It would appear in the response headers. That is, this is header is sent from
	 * server to client to inform that server is willing to initiate a websocket
	 * connection.
	 */
	SEC_WEBSOCKET_ACCEPT("Sec-WebSocket-Accept", null),

	;

	private String code;
	private String[] options;

	private Header(String code, String[] options) {

		this.code = code;
		this.options = options;
	}

	public String getCode() {

		return code;
	}

	public String[] getOptions() {

		return options;
	}

}

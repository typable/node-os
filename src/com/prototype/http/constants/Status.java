package com.prototype.http.constants;

/**
 * The status-code element is a three-digit integer code giving the<br/>
 * result of the attempt to understand and satisfy the request.<br/>
 * <br/>
 * HTTP status codes are extensible. HTTP clients are not required to<br/>
 * understand the meaning of all registered status codes, though such<br/>
 * understanding is obviously desirable. However, a client MUST<br/>
 * understand the class of any status code, as indicated by the first<br/>
 * digit, and treat an unrecognized status code as being equivalent to<br/>
 * the x00 status code of that class, with the exception that a<br/>
 * recipient MUST NOT cache a response with an unrecognized status code.<br/>
 * <br/>
 * For example, if an unrecognized status code of 471 is received by a<br/>
 * client, the client can assume that there was something wrong with its<br/>
 * request and treat the response as if it had received a 400 (Bad<br/>
 * Request) status code. The response message will usually contain a<br/>
 * representation that explains the status.<br/>
 * <br/>
 * The first digit of the status-code defines the class of response.<br/>
 * The last two digits do not have any categorization role.<br/>
 * There are five values for the first digit:<br/>
 * <br/>
 * o 1xx (Informational): The request was received, continuing process<br/>
 * <br/>
 * o 2xx (Successful): The request was successfully received,<br/>
 * understood, and accepted<br/>
 * <br/>
 * o 3xx (Redirection): Further action needs to be taken in order to<br/>
 * complete the request<br/>
 * <br/>
 * o 4xx (Client Error): The request contains bad syntax or cannot be<br/>
 * fulfilled<br/>
 * <br/>
 * Source: <a href=
 * "https://tools.ietf.org/rfcmarkup?doc=7231#section-6">https://tools.ietf.org/rfcmarkup?doc=7231</a><br/>
 */
public enum Status {

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.2.1">100
	 * (Continue)</a> status code indicates that the initial part of a<br/>
	 * request has been received and has not yet been rejected by the server.<br/>
	 * <br/>
	 * Type: Informational<br/>
	 */
	CONTINUE(100, "CONTINUE"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.3.1">200
	 * (OK)</a> status code indicates that the request has succeeded.<br/>
	 * <br/>
	 * Type: Successful<br/>
	 */
	OK(200, "OK"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.3.2">201
	 * (Created)</a> status code indicates that the request has been<br/>
	 * fulfilled and has resulted in one or more new resources being created.<br/>
	 * <br/>
	 * Type: Successful<br/>
	 */
	CREATED(201, "CREATED"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.3.3">202
	 * (Accepted)</a> status code indicates that the request has been<br/>
	 * accepted for processing, but the processing has not been completed.<br/>
	 * <br/>
	 * Type: Successful<br/>
	 */
	ACCEPTED(202, "ACCEPTED"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.3.5">204 (No
	 * Content)</a> status code indicates that the server has<br/>
	 * successfully fulfilled the request and that there is no additional content to
	 * send in the response payload body.<br/>
	 * <br/>
	 * Type: Successful<br/>
	 */
	NO_CONTENT(204, "NO CONTENT"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.4.2">301
	 * (Moved Permanently)</a> status code indicates that the target<br/>
	 * resource has been assigned a new permanent URI and any future references to
	 * this resource ought to use one of the enclosed URIs.<br/>
	 * <br/>
	 * Type: Redirection<br/>
	 */
	MOVED_PERMANENTLY(301, "MOVED PERMANENTLY"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.4.3">302
	 * (Found)</a> status code indicates that the target resource<br/>
	 * resides temporarily under a different URI.<br/>
	 * <br/>
	 * Type: Redirection<br/>
	 */
	FOUND(302, "FOUND"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.4.7">307
	 * (Temporary Redirect)</a> status code indicates that the target<br/>
	 * resource resides temporarily under a different URI and the user agent MUST
	 * NOT change the request method if it performs an automatic<br/>
	 * redirection to that URI.<br/>
	 * <br/>
	 * Type: Redirection<br/>
	 */
	TEMPORARY_REDIRECT(307, "TEMPORARY REDIRECT"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.5.1">400
	 * (Bad Request)</a> status code indicates that the server cannot or<br/>
	 * will not process the request due to something that is perceived to be a
	 * client error (e.g., malformed request syntax, invalid request<br/>
	 * message framing, or deceptive request routing).<br/>
	 * <br/>
	 * Type: Client Error<br/>
	 */
	BAD_REQUEST(400, "BAD REQUEST"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.5.3">403
	 * (Forbidden)</a> status code indicates that the server understood<br/>
	 * the request but refuses to authorize it.<br/>
	 * <br/>
	 * Type: Client Error<br/>
	 */
	FORBIDDEN(403, "FORBIDDEN"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.5.4">404
	 * (Not Found)</a> status code indicates that the origin server did<br/>
	 * not find a current representation for the target resource or is not willing
	 * to disclose that one exists.<br/>
	 * <br/>
	 * Type: Client Error<br/>
	 */
	NOT_FOUND(404, "NOT_FOUND"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.5.7">408
	 * (Request Timeout)</a> status code indicates that the server did<br/>
	 * not receive a complete request message within the time that it was prepared
	 * to wait.<br/>
	 * <br/>
	 * Type: Client Error<br/>
	 */
	REQUEST_TIMEOUT(408, "REQUEST TIMEOUT"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.5.13">415
	 * (Unsupported Media Type)</a> status code indicates that the<br/>
	 * origin server is refusing to service the request because the payload is in a
	 * format not supported by this method on the target resource.<br/>
	 * <br/>
	 * Type: Client Error<br/>
	 */
	UNSUPPORTED_MEDIA_TYPE(415, "UNSUPPORTED MEDIA TYPE"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.6.1">500
	 * (Internal Server Error)</a> status code indicates that the server<br/>
	 * encountered an unexpected condition that prevented it from fulfilling the
	 * request.<br/>
	 * <br/>
	 * Type: Server Error<br/>
	 */
	INTERNAL_SERVER_ERROR(500, "BAD INTERNAL_SERVER_ERROR"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.6.3">502
	 * (Bad Gateway)</a> status code indicates that the server, while<br/>
	 * acting as a gateway or proxy, received an invalid response from an inbound
	 * server it accessed while attempting to fulfill the request.<br/>
	 * <br/>
	 * Type: Server Error<br/>
	 */
	BAD_GATEWAY(502, "BAD GATEWAY"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.6.4">503
	 * (Service Unavailable)</a> status code indicates that the server<br/>
	 * is currently unable to handle the request due to a temporary overload or
	 * scheduled maintenance, which will likely be alleviated after some<br/>
	 * delay.<br/>
	 * <br/>
	 * Type: Server Error<br/>
	 */
	SERVICE_UNAVAILABLE(503, "SERVICE UNAVAILABLE"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.6.5">504
	 * (Gateway Timeout)</a> status code indicates that the server,<br/>
	 * while acting as a gateway or proxy, did not receive a timely response from an
	 * upstream server it needed to access in order to complete the<br/>
	 * request.<br/>
	 * <br/>
	 * Type: Server Error<br/>
	 */
	GATEWAY_TIMEOUT(504, "GATEWAY TIMEOUT"),

	/**
	 * The <a href="https://tools.ietf.org/rfcmarkup?doc=7231#section-6.6.6">505
	 * (HTTP Version Not Supported)</a> status code indicates that the<br/>
	 * server does not support, or refuses to support, the major version of HTTP
	 * that was used in the request message.<br/>
	 * <br/>
	 * Type: Server Error<br/>
	 */
	HTTP_VERSION_NOT_SUPPORTED(505, "HTTP VERSION NOT SUPPORTED");

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

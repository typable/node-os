package os.type;


public class Header {

	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String LOCATION = "Location";
	public static final String SET_COOKIE = "Set-Cookie";
	public static final String COOKIE = "Cookie";
	
	public class ContentEncoding {
		
		public static final String IDENTITY = "indentity";
		public static final String COMPRESS = "compress";
		public static final String DEFLATE = "deflate";
		public static final String GZIP = "gzip";
		public static final String BR = "br";
	}
}

package os.server.type;

public enum ContentEncoding {

	IDENTITY("indentity"),
	COMPRESS("compress"),
	DEFALTE("deflate"),
	GZIP("gzip"),
	BR("br");
	
	private String type;

	private ContentEncoding(String type) {

		this.type = type;
	}

	public String getType() {

		return type;
	}
}

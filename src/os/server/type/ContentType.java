package os.server.type;

public enum ContentType {

	PLAIN("text/plain", "txt"),
	HTML("text/html", "html"),
	CSS("text/css", "css"),
	JAVASCRIPT("text/javascript", "js"),
	MP4("video/mp4", "mp4"),
	MP3("audio/mp3", "mp3"),
	JPEG("image/jpeg", "jpeg"),
	JPG("image/jpg", "jpg"),
	PNG("image/png", "png"),
	JSON("application/json", "json")
	;
	
	private String type;
	private String fileType;

	private ContentType(String type, String fileType) {

		this.type = type;
		this.fileType = fileType;
	}
	
	public static ContentType getByFile(String fileName) {
		
		if(fileName.contains(".")) {
			
			if(fileName.split("\\.").length == 2) {
				
				String fileType = fileName.split("\\.")[1];
				
				for(ContentType contentType : ContentType.values()) {
					
					if(contentType.getFileType().equals(fileType)) {
						
						return contentType;
					}
				}
			}
		}
		
		return ContentType.PLAIN;
	}
	
	public static ContentType getByType(String type) {
		
		for(ContentType contentType : ContentType.values()) {
			
			if(contentType.getType().equals(type)) {
				
				return contentType;
			}
		}
		
		return null;
	}

	public String getType() {

		return type;
	}
	
	public String getFileType() {
		
		return fileType;
	}
}

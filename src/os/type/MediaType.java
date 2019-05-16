package os.type;


public enum MediaType {

	TEXT_PLAIN("text/plain", "txt"),
	TEXT_HTML("text/html", "html"),
	TEXT_CSS("text/css", "css"),
	TEXT_JAVASCRIPT("text/javascript", "js"),
	APPLICATION_JSON("application/json", "json");
	
	private String type;
	private String fileType;
	
	private MediaType(String type, String fileType) {

		this.type = type;
		this.fileType = fileType;
	}
	
	public static MediaType getByType(String type) {
		
		for(MediaType mediaType : MediaType.values()) {
			
			if(mediaType.getType().equals(type)) {
				
				return mediaType;
			}
		}
		
		return MediaType.TEXT_PLAIN;
	}
	
	public static MediaType getByFileType(String fileType) {
		
		if(fileType.contains(".")) {
			
			if(fileType.split("\\.").length == 2) {
				
				fileType = fileType.split("\\.")[1];
				
				for(MediaType mediaType : MediaType.values()) {
					
					if(mediaType.getFileType().equals(fileType)) {
						
						return mediaType;
					}
				}
			}
		}
		
		return MediaType.TEXT_PLAIN;
	}
	
	public String getType() {
		
		return type;
	}
	
	public String getFileType() {
		
		return fileType;
	}
}

package os.type.constants;

/**
 * A media type (also known as a Multipurpose Internet Mail Extensions or MIME
 * type) is a standard that indicates the nature and format of a document, file,
 * or assortment of bytes.
 */
public enum MediaType {

	/**
	 * Plain Text<br/>
	 * <br/>
	 * MIME Type: text/plain<br/>
	 * File Types: txt
	 */
	TEXT_PLAIN("text/plain", new String[] { "txt" }),

	/**
	 * Hypertext Markup Language<br/>
	 * <br/>
	 * MIME Type: text/html<br/>
	 * File Types: html
	 */
	TEXT_HTML("text/html", new String[] { "html" }),

	/**
	 * Cascading Style Sheet<br/>
	 * <br/>
	 * MIME Type: text/css<br/>
	 * File Types: css
	 */
	TEXT_CSS("text/css", new String[] { "css" }),

	/**
	 * Javascript<br/>
	 * <br/>
	 * MIME Type: text/javascript<br/>
	 * File Types: js
	 */
	TEXT_JAVASCRIPT("text/javascript", new String[] { "js" }),

	/**
	 * Javascript Object Node<br/>
	 * <br/>
	 * MIME Type: application/json<br/>
	 * File Types: json
	 */
	APPLICATION_JSON("application/json", new String[] { "json" }),

	/**
	 * Extensible Markup Language<br/>
	 * <br/>
	 * MIME Type: application/xml<br/>
	 * File Types: xml
	 */
	APPLICATION_XML("application/xml", new String[] { "xml" }),

	/**
	 * Java Archive<br/>
	 * <br/>
	 * MIME Type: application/java-archive<br/>
	 * File Types: jar
	 */
	APPLICATION_JAR("application/java-archive", new String[] { "jar" }),

	/**
	 * Portable Document Format<br/>
	 * <br/>
	 * MIME Type: application/pdf<br/>
	 * File Types: pdf
	 */
	APPLICATION_PDF("application/pdf", new String[] { "pdf" }),

	/**
	 * Archive File Format<br/>
	 * <br/>
	 * MIME Type: application/zip<br/>
	 * File Types: zip
	 */
	APPLICATION_ZIP("application/zip", new String[] { "zip" }),

	/**
	 * Binary<br/>
	 * <br/>
	 * MIME Type: application/octet-stream<br/>
	 * File Types: null
	 */
	APPLICATION_OCTET_STREAM("application/octet-stream", null),

	/**
	 * Graphics Interchange Format<br/>
	 * <br/>
	 * MIME Type: image/gif<br/>
	 * File Types: gif
	 */
	IMAGE_GIF("image/gif", new String[] { "gif" }),

	/**
	 * Joint Photographic Experts Group<br/>
	 * <br/>
	 * MIME Type: image/jpeg<br/>
	 * File Types: jpeg, jpg
	 */
	IMAGE_JPEG("image/jpeg", new String[] { "jpeg", "jpg" }),

	/**
	 * Portable Network Graphic<br/>
	 * <br/>
	 * MIME Type: image/png<br/>
	 * File Types: png
	 */
	IMAGE_PNG("image/png", new String[] { "png" }),

	/**
	 * Scalable Vector Image<br/>
	 * <br/>
	 * MIME Type: image/svg+xml<br/>
	 * File Types: svg
	 */
	IMAGE_SVG("image/svg+xml", new String[] { "svg" }),

	/**
	 * Waveform Audio File Format<br/>
	 * <br/>
	 * MIME Type: audi/wave<br/>
	 * File Types: wav
	 */
	AUDIO_WAVE("audio/wave", new String[] { "wav" }),

	/**
	 * Moving Picture Experts Group<br/>
	 * <br/>
	 * MIME Type: audio/mpeg<br/>
	 * File Types: mp3
	 */
	AUDIO_MPEG("audio/mpeg", new String[] { "mp3" }),

	/**
	 * Moving Picture Experts Group<br/>
	 * <br/>
	 * MIME Type: video/mpeg<br/>
	 * File Types: mp4
	 */
	VIDEO_MPEG("video/mpeg", new String[] { "mp4" }),

	/**
	 * TrueType Font<br/>
	 * <br/>
	 * MIME Type: font/ttf<br/>
	 * File Types: ttf
	 */
	FONT_TTF("font/ttf", new String[] { "ttf" });

	private String type;
	private String[] fileTypes;

	private MediaType(String type, String[] fileTypes) {

		this.type = type;
		this.fileTypes = fileTypes;
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

					String[] fileTypes = mediaType.getFileTypes();

					if(fileType != null) {

						for(String type : fileTypes) {

							if(type.equals(fileType)) {

								return mediaType;
							}
						}
					}
				}
			}
		}

		return MediaType.TEXT_PLAIN;
	}

	public String getType() {

		return type;
	}

	public String[] getFileTypes() {

		return fileTypes;
	}
}

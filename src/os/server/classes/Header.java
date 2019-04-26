package os.server.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import os.server.type.ContentEncoding;
import os.server.type.ContentType;

public class Header {

	private HashMap<String, String> header;
	
	public Header() {
		
		header = new HashMap<String, String>();
	}
	
	public void setContentType(ContentType contentType) {
		
		header.put("Content-Type", contentType.getType() + " charset=UTF-8");
	}
	
	public ContentType getContentType() {
		
		if(header.containsKey("Content-Type")) {
			
			String contentType = header.get("Content-Type");
			
			if(contentType.indexOf(" ") != -1) {
				
				contentType = contentType.split(" ")[0];
			}
			
			return ContentType.getByType(contentType);
		}
		
		return null;
	}
	
	public void setContentLength(int contentLength) {
		
		header.put("Content-Length", String.valueOf(contentLength));
	}
	
	public Integer getContentLength() {
		
		if(header.containsKey("Content-Length")) {
			
			try {
				
				return Integer.parseInt(header.get("Content-Length"));
			}
			catch(NumberFormatException ex) {
				
				return null;
			}
		}
		
		return null;
	}
	
	public void setContentEncoding(ContentEncoding... contentEncodingArray) {
		
		List<String> contentEncodingList = new ArrayList<String>();
		
		for(ContentEncoding contentEncoding : contentEncodingArray) {
			
			contentEncodingList.add(contentEncoding.getType());
		}
		
		header.put("Content-Encoding", String.join(", ", contentEncodingList));
	}
	
	public List<ContentEncoding> getContentEncoding() {
		
		if(header.containsKey("Content-Encoding")) {
			
			List<ContentEncoding> contentEncodingList = new ArrayList<ContentEncoding>();
			
			String contentEncodingArray = header.get("Content-Encoding");
			
			for(String contentEncodingString : contentEncodingArray.split(", ")) {
				
				ContentEncoding contentEncoding = ContentEncoding.valueOf(contentEncodingString);
				
				if(contentEncoding != null) {
					
					contentEncodingList.add(contentEncoding);
				}
			}
			
			return contentEncodingList;
		}
		
		return null;
	}
	
	public void setCookie(String key, String value, int age) {
		
		header.put("Set-Cookie", key + "=" + value + "; Max-Age=" + age + "; Expires=" + age);
	}
	
	public HashMap<String, String> getCookies() {
		
		HashMap<String, String> cookieList = new HashMap<String, String>();
		
		if(header.containsKey("Cookie")) {
			
			String cookiesString = header.get("Cookie");
			
			if(cookiesString.contains("; ")) {
				
				for(String cookieSplit : cookiesString.split("; ")) {
					
					if(cookieSplit.contains("=")) {
						
						cookieList.put(cookieSplit.split("=")[0], cookieSplit.split("=")[1]);
					}
				}
			}
			else {
				
				if(cookiesString.contains("=")) {
					
					cookieList.put(cookiesString.split("=")[0], cookiesString.split("=")[1]);
				}
			}
		}
		
		return cookieList;
	}
	
	public void setLocation(String location) {
		
		header.put("Location", location);
	}
	
	public String getLocation() {
		
		if(header.containsKey("Location")) {
			
			return header.get("Location");
		}
		
		return null;
	}

	public HashMap<String, String> getHeader() {
		
		return header;
	}
	
	public void setHeader(HashMap<String, String> header) {
		
		this.header = header;
	}
}

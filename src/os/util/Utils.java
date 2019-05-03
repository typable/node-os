package os.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {
	
	public static String getCurrentPath() throws IOException {
		
		return new File(".").getCanonicalPath().replaceAll("\\\\", "\\/");
	}
	
	public static boolean notEmpty(String line) {
		
		return (line != null && !line.equals("")) ? true : false;
	}
	
	public static void keySet(HashMap<String, String> attributes, String separator, String code) {
		
		if(code.contains(separator)) {
			
			String[] parts = code.split(separator);
			
			attributes.put(parts[0], parts[1]);
		}
	}
	
	public static String[] split(String line, char splitter, char[] start, char[] end) {
		
		List<Integer> splits = new ArrayList<Integer>();
		int i = 0;
		int scope = 0;
		
		for(char c : line.toCharArray()) {
			
			for(char cs : start) {
				
				if(c == cs) {
					
					scope++;
				}
			}
			
			for(char ce : end) {
				
				if(c == ce) {
					
					scope--;
				}
			}
			
			if(c == splitter && scope == 0) {
				
				splits.add(i);
			}
			
			i++;
		}
		
		int last = 0;
		int index = 0;
		
		String[] json_ = new String[splits.size() + 1];
		
		for(int i_ = 0; i_ < splits.size() + 1; i_++) {
			
			if(i_ < splits.size()) {
				
				index = splits.get(i_);
			}
			else {
				
				index = line.length();
			}
			
			json_[i_] = line.substring(last, index);
			
			last = index + 2;
		}
		
		return json_;
	}
}

package main;

import java.io.File;
import java.io.IOException;

public class NodeOS {
	
	public NodeOS() {

		
	}

	public static String getCurrentPath() throws IOException {
		
		return new File(".").getCanonicalPath();
	}
}

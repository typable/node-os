package main;

import os.core.Core;

public class Launch {

	public static void main(String[] args) throws Exception {
		
		Core core = new Core();
		core.addController(PageController.class);
		
		core.launch();
	}
}

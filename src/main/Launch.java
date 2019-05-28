package main;

import os.core.Core;


public class Launch {

	public static void main(String[] args) throws Exception {

		Core core = new Core();
		core.addHandler(PageHandler.class);

		core.launch();
	}
}

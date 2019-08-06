package com.prototype.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.prototype.Prototype;


public class Updater {

	public Updater() {

		try {

			URL url = new URL("http://localhost/update?version=" + Prototype.VERSION);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int code = connection.getResponseCode();

			if(code == 200) {

				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				System.out.println(reader.readLine());
			}
		}
		catch(Exception e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		new Updater();
	}
}

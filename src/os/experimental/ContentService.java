package os.experimental;

import os.service.Service;
import util.type.JSONObject;


public class ContentService extends Service {

	public ContentService() {

		//
	}

	public JSONObject prepareImage(String id, String name, String tags, String description) {

		JSONObject obj = new JSONObject();
		obj.set("id", id);
		obj.set("name", name);
		obj.set("tags", tags);
		obj.set("description", description);

		return obj;
	}
}

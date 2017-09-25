package utils;

import com.google.gson.JsonObject;

public class Utils {
	public static String getErrorMessage() {

		JsonObject json = new JsonObject();
		json.addProperty("rs", "2");
		json.addProperty("message", "Unexpected Error");
		return json.toString();
	}

	public static String getLoginAgain() {
		JsonObject json = new JsonObject();
		json.addProperty("rs", "3");
		json.addProperty("message", "Login again");
		return json.toString();
	}
	
}

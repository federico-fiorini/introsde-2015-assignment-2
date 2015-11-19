package rest.lifestyle.client.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

	JSONObject jsonObj;
	
	/**
	 * Load JSON string
	 * @param json
	 */
	public void loadJson(String json) {
		jsonObj = new JSONObject(json);
	}
	
	/**
	 * Get JSON array
	 * @param expr
	 * @return
	 */
	public JSONArray getArray(String expr) {
		return jsonObj.getJSONArray(expr);
	}
	
	/**
	 * Get JSON element
	 * @param expr
	 * @return
	 */
	public String getElement(String expr) {
		try {
			return jsonObj.getString(expr);
		} catch (JSONException e) {}
		
		try {
			return Integer.toString(jsonObj.getInt(expr));
		} catch (JSONException e) {}
		
		try {
			return Double.toString(jsonObj.getDouble(expr));
		} catch (JSONException e) {}
		
		return "";
	}
}

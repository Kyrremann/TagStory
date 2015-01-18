package no.tagstory.utils;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommunicationManager {

	private ServerCommunication serverCommunication;
	private ArrayList<NameValuePair> params, headers;

	private static final int POST = ServerCommunication.POST;
	private static final int GET = ServerCommunication.GET;

	private static final String URL_TAGSTORY = "http://beta.tagstory.no";
	private static final String URL_API = URL_TAGSTORY + "/api";
	private static final String URL_TAG = URL_API + "/tag";
	private static final String URL_STORY = URL_API + "/story";
	private static final String URL_STORIES = URL_API + "/stories";

	private static final String HEADER_DESCRIPTION = "description";
	private static final String HEADER_GPS = "gps";
	private static final String ERROR = "error";
	private static final String CODE = "code";

	public CommunicationManager() {
		this.serverCommunication = new ServerCommunication();
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	private void parseResponse() throws JSONException {
		if (serverCommunication.getResponseCode() == 200) {
			JSONObject object = new JSONObject(serverCommunication.getResponse());
			if (contains(object.names(), "error")) {
				// TODO: Error-handling missing
				Log.e(object.get(CODE).toString(), object.get(ERROR).toString());
			}
		}
	}

	public JSONArray getAllStories() {

		try {
			serverCommunication.execute(GET, URL_STORIES);
			parseResponse();
			return new JSONArray(serverCommunication.getResponse());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getStory(String id) {

		try {
			serverCommunication.execute(GET, URL_STORY + id);
			Log.d(Integer.toString(serverCommunication.getResponseCode()),
					serverCommunication.getResponse());
			if (serverCommunication.getResponseCode() == 200) {
				JSONObject object = new JSONObject(
						serverCommunication.getResponse());
				if (contains(object.names(), "error")) {
					// TODO: Error-handling missing
					Log.e(object.get(CODE).toString(), object.get(ERROR)
							.toString());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @param desc
	 * @param gps
	 * @return the newly created tag <bold>id</bold>
	 */
	public String postNewTag(String desc, String gps) {
		try {
			params.clear();
			params.add(new BasicNameValuePair(HEADER_DESCRIPTION, desc));
			params.add(new BasicNameValuePair(HEADER_GPS, gps));
			serverCommunication.execute(POST, URL_TAG, params, null);
			Log.d(Integer.toString(serverCommunication.getResponseCode()),
					serverCommunication.getResponse());

			if (serverCommunication.getResponseCode() == 200) {
				JSONObject object = new JSONObject(
						serverCommunication.getResponse());
				if (contains(object.names(), "error")) {
					// TODO: Error-handling missing
					Log.e(object.get(CODE).toString(), object.get(ERROR)
							.toString());
				}
				System.out.println("JSON = " + object);
				return object.getString("uuid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public boolean contains(JSONArray jsonArray, String name) {
		for (int i = 0; i < jsonArray.length(); i++) {
			String value = "";
			try {
				value = jsonArray.getString(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (value.equals(name))
				return true;
		}

		return false;
	}
}

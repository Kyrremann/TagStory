package no.tagstory.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MarketServerCommunication {

	public static void synchronizeSets(final Context context) {
		AsyncTask<Void, Void, JSONArray> task = new AsyncTask<Void, Void, JSONArray>() {

			private ProgressDialog progressDialog;

			@Override
			protected JSONArray doInBackground(Void... v) {
				try {
					return getListOfStories();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(context);
				progressDialog.setCancelable(false);
				progressDialog.setMessage("Updating the market");
				progressDialog.show();

			}

			@Override
			protected void onPostExecute(JSONArray jsonArray) {
				super.onPostExecute(jsonArray);
				progressDialog.cancel();
				// :\
			}
		};
		task.execute();
		return;
	}

	private static JSONArray getListOfStories() throws IOException, JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://tagstory.herokuapp.com/stories/json");
		String content = client.execute(get, new BasicResponseHandler());

		return new JSONArray(content);
	}
}

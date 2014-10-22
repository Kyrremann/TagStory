package no.tagstory.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MarketServerCommunication {

	public static JSONObject synchronizeSets(Context context) throws ExecutionException, InterruptedException {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Test");

		AsyncTask<ProgressDialog, Void, JSONObject> task = new AsyncTask<ProgressDialog, Void, JSONObject>() {

			private ProgressDialog progressDialog;

			@Override
			protected JSONObject doInBackground(ProgressDialog... progressDialogs) {
				progressDialog = progressDialogs[0];
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
				progressDialog.show();
			}

			@Override
			protected void onPostExecute(JSONObject jsonObject) {
				super.onPostExecute(jsonObject);
			}
		};

		return task.execute(progressDialog).get();
	}

	private static JSONObject getListOfStories() throws IOException, JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://stackoverflow.com/a/b/c?param=value");
		String content = client.execute(get, new BasicResponseHandler());

		return new JSONObject(content);
	}
}

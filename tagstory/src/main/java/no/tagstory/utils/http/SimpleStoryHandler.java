package no.tagstory.utils.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import no.tagstory.R;

public class SimpleStoryHandler extends Handler {

	public interface SimpleCallback {
		void onMessageDone();
		void onMessageFail(int error);
	}

	public static final int MESSAGE_DONE = 0;
	public static final int MESSAGE_FAIL_JSON = -1;
	public static final int MESSAGE_FAIL_HTTP = -2;

	private SimpleCallback simpleCallback;

	public SimpleStoryHandler(SimpleCallback simpleCallback) {
		this.simpleCallback = simpleCallback;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case MESSAGE_DONE:
				simpleCallback.onMessageDone();
				break;
			case MESSAGE_FAIL_HTTP:
			case MESSAGE_FAIL_JSON:
				simpleCallback.onMessageFail(msg.what);
				break;
			default:
				break;
		}
	}
}

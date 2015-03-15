package no.tagstory.utils.http;

import android.os.Handler;
import android.os.Message;

public class SimpleStoryHandler extends Handler {

	public interface SimpleCallback {
		void onMessageDone();
		void onMessageInfo(int arg1, int arg2);
		void onMessageFail(int error);
	}

	public static final int MESSAGE_DONE = 0;
	public static final int MESSAGE_FAIL_JSON = -1;
	public static final int MESSAGE_FAIL_HTTP = -2;
	public static final int MESSAGE_FAILED = -3;
	public static final int MESSAGE_SHORT_INFO = 1;

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
			case MESSAGE_SHORT_INFO:
				simpleCallback.onMessageInfo(msg.arg1, msg.arg2);
			case MESSAGE_FAIL_HTTP:
			case MESSAGE_FAIL_JSON:
			case MESSAGE_FAILED:
				simpleCallback.onMessageFail(msg.what);
				break;
			default:
				break;
		}
	}
}

package com.chat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import data.ChatContract.ChatsEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

	public final static String[] HELLO = { "Hellow", "How are U?", "LOL",
			"What's your Name?", "I, Robot", "Follow white rabbit" };
	private static final String LOG_TAG = "myLogs";

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		final String message = "What`s+your+problem?";
		Thread sendM = new Thread(new Runnable() {

			@Override
			public void run() {
				
				Log.d(LOG_TAG, "Sending: " + message);
				sendMassage(message.replace("\\s", "\u002B"));

			}
		});
		
		Calendar cal = Calendar.getInstance();
		long time = cal.getTimeInMillis();
		String time_str = String.valueOf(time);
		String user = "User";
		ContentValues cv = new ContentValues();
		cv.put(ChatsEntry.COLUMN_TIME, time);
		cv.put(ChatsEntry.COLUMN_SENDER, user);
		cv.put(ChatsEntry.COLUMN_MESSAGE, message);
		Uri insUri = getActivity().getContentResolver()
				.insert(ChatsEntry.buildTimeUri(time), cv);
		Log.d(LOG_TAG, "ins id = " + ChatsEntry.getTimeFromUri(insUri));
		sendM.start();

		// toCallAsyns();

		return rootView;
	}

	private void sendMassage(String massage) {
		/*
		 * http://www.botlibre.com/rest/botlibre/form-chat?instance=165&message=what
		 * +is+a+chat+bot
		 */
		URL myUrl = null;
		HttpURLConnection urlConnection = null;
		try {
			myUrl = new URL(
					"http://www.botlibre.com/rest/botlibre/form-chat?instance=165&application=6833358163211651434&message="
							+ massage);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create the request robot
		try {
			urlConnection = (HttpURLConnection) myUrl.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Read the input stream into a String
		InputStream inputStream = null;
		try {
			inputStream = urlConnection.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (inputStream == null) {
			Log.d(LOG_TAG, "ups");
		} else {
			String resp = getMassageFromXML(inputStream);
			Log.d(LOG_TAG, resp);
		}

	}

	private String getMassageFromXML(InputStream inputStream) {
		String response = "No response";

		XmlPullParserFactory factory;
		XmlPullParser xpp = null;
		int eventType = 0;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			xpp = factory.newPullParser();
			xpp.setInput(inputStream, null);
			eventType = xpp.getEventType();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String currentTag = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				currentTag = xpp.getName();
			} else if (eventType == XmlPullParser.TEXT) {
				if ("message".equals(currentTag)) {
					response = xpp.getText();
				}
			}
			try {
				eventType = xpp.next();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return response;
	}

	public void toCallAsyns() {
		final Handler handler = new Handler();
		Timer timer = new Timer();
		TimerTask doAsynchronousTask = new TimerTask() {

			@Override
			public void run() {
				handler.post(new Runnable() {

					@Override
					public void run() {
						MyAsyncTask task = new MyAsyncTask();
						task.execute();

					}
				});

			}
		};
		Random r = new Random();
		timer.schedule(doAsynchronousTask, 0, 2000 + r.nextInt(2000));
		// execute in every 2 + random(2) sec

	}

	private class MyAsyncTask extends AsyncTask<String, Object, String> {

		@Override
		protected String doInBackground(String... params) {

			Random r = new Random();
			Log.d("myLogs", HELLO[r.nextInt(5)]);
			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Update UI
		}
	}

}

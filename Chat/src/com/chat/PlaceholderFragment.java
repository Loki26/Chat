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
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import data.ChatContract.ChatsEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements
		LoaderCallbacks<Cursor> {

	public final static String[] HELLO = { "Hellow", "How are U?", "LOL",
			"What's your Name?", "I, Robot", "Follow white rabbit" };
	private static final String LOG_TAG = "myLogs";
	String message;
	DialogAdapter mAdapter;
	Thread sendM;
	Button btn;
	ListView lv;
	int pos;

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		final EditText ins_message = (EditText) rootView
				.findViewById(R.id.editText1);

		btn = (Button) rootView.findViewById(R.id.button1);

		lv = (ListView) rootView.findViewById(R.id.listView1);
		mAdapter = new DialogAdapter(getActivity(), null, 0);
		lv.setAdapter(mAdapter);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				message = ins_message.getText().toString();
				if (!message.equals("") && message != null) {

					ins_message.setText("");
					message = message.replace("#",
							"<font color='#FF0000'>#</font>");
					Log.d(LOG_TAG, message);
					Calendar cal = Calendar.getInstance();
					long time = cal.getTimeInMillis();
					String user = "User";

					ContentValues cv = new ContentValues();
					cv.put(ChatsEntry.COLUMN_TIME, time);
					cv.put(ChatsEntry.COLUMN_SENDER, user);
					cv.put(ChatsEntry.COLUMN_MESSAGE, message);
					getActivity().getContentResolver().insert(
							ChatsEntry.CONTENT_URI, cv);
					Log.d(LOG_TAG, "Sent: " + message);
					pos = lv.getFirstVisiblePosition();
					MyAsyncTask send = new MyAsyncTask();
					send.execute();

				}
			}
		});

		// toCallAsyns();

		return rootView;
	}

	@Override
	public void onResume() {
		getLoaderManager().restartLoader(0, null, this);
		super.onResume();
	}

	private void sendMassage(String message) {
		/*
		 * http://www.botlibre.com/rest/botlibre/form-chat?instance=165&message=what
		 * +is+a+chat+bot
		 */
		URL myUrl = null;
		HttpURLConnection urlConnection = null;
		try {
			myUrl = new URL(
					"http://www.botlibre.com/rest/botlibre/form-chat?instance=165&application=6833358163211651434&message="
							+ message);

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

		String resp;
		if (inputStream == null) {
			Log.d(LOG_TAG, "ups");
			resp = "There is no net connection, so let me say: "
					+ HELLO[new Random().nextInt(5)];
		} else {
			resp = getMassageFromXML(inputStream);
			Log.d(LOG_TAG, resp);
		}
		Calendar cal = Calendar.getInstance();
		long time = cal.getTimeInMillis();
		String user = "Bot";
		resp.replace("#",
				"<font color='#FF0000'>#</font>");
		ContentValues cv = new ContentValues();
		cv.put(ChatsEntry.COLUMN_TIME, time);
		cv.put(ChatsEntry.COLUMN_SENDER, user);
		cv.put(ChatsEntry.COLUMN_MESSAGE, resp);
		Uri insUri = getActivity().getContentResolver().insert(
				ChatsEntry.CONTENT_URI, cv);
		Log.d(LOG_TAG, "ins id = " + ChatsEntry.getTimeFromUri(insUri));

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

	private class MyAsyncTask extends AsyncTask<String, Object, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.d(LOG_TAG, "Sending from Async: " + message);
			sendMassage(message.replace(" ", "+"));

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Update UI
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String projection[] = new String[] { ChatsEntry._ID,
				ChatsEntry.COLUMN_MESSAGE, ChatsEntry.COLUMN_SENDER,
				ChatsEntry.COLUMN_TIME };
		return new CursorLoader(getActivity(), ChatsEntry.CONTENT_URI,
				projection, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		mAdapter.swapCursor(arg1);
		lv.setSelection(mAdapter.getCount() - 1);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);

	}

}

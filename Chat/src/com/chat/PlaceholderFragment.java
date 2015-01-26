package com.chat;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

	public final static String[] HELLO = { "Hellow", "How are U?", "LOL",
			"What's your Name?", "I, Robot", "Follow white rabbit" };

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		toCallAsyns();

		return rootView;
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

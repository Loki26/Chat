package com.chat;


import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class DialogAdapter extends CursorAdapter {

	public DialogAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		int layoutId = R.layout.qoute;

		View view = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TextView message = (TextView) view.findViewById(R.id.message_txt);
		message.setText(Html.fromHtml(cursor.getString(1)));
		TextView sender = (TextView) view.findViewById(R.id.sender_txt);
		sender.setText(cursor.getString(2));
		
	}

}

package data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import data.ChatContract.ChatsEntry;

public class ChatProvider extends ContentProvider {

	private static final String LOG_TAG = "myLogs";

	private static final int TIME = 100;
	private static final int USER_AND_MESSAGE = 200;
	private static final int ALL_ENTRIES = 300;
	private static final int DELETE_ALL = 400;

	private static final UriMatcher sUriMatcher = buildUriMatcher();

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = ChatContract.CONTENT_AUTHORITY;
		matcher.addURI(authority, ChatContract.PATH_CHAT + "/*", TIME);
		matcher.addURI(authority, ChatContract.PATH_CHAT + "/#",
				USER_AND_MESSAGE);
		matcher.addURI(authority, ChatContract.PATH_CHAT, ALL_ENTRIES);

		return matcher;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		DBHelper mHelper = new DBHelper(getContext());
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor retCursor;
		switch (sUriMatcher.match(uri)) {
		case TIME:
			// set time in selection ARGs
			retCursor = db.query(ChatsEntry.TABLE_NAME, projection, selection,
					selectionArgs, null, null, null);
			break;
		case ALL_ENTRIES:
			retCursor = db.query(ChatsEntry.TABLE_NAME, projection, selection,
					selectionArgs, null, null, null);
			break;
		default:
			throw new UnsupportedOperationException("Damn Unknown uri: " + uri);
		}
		
		/*db.close();
		mHelper.close();*/
		retCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return retCursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		DBHelper mHelper = new DBHelper(getContext());
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Log.d(LOG_TAG, "Got uri" + uri);

		long ret_id;
		Uri ret_uri = null;
		switch (sUriMatcher.match(uri)) {
		case TIME:
			Log.d(LOG_TAG, "worked for time");
			break;
		case USER_AND_MESSAGE:

			break;
		case ALL_ENTRIES:
			Log.d(LOG_TAG, "lets do it");
			ret_id = db.insert(ChatsEntry.TABLE_NAME, null, values);
			if (ret_id > 0) {
				ret_uri = ContentUris.withAppendedId(ChatsEntry.CONTENT_URI,
						ret_id);
			}

			break;
		default:
			throw new UnsupportedOperationException("Damn Unknown uri: " + uri);
		}
		db.close();
		mHelper.close();
		getContext().getContentResolver().notifyChange(ret_uri, null);
		return ret_uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		DBHelper mHepler = new DBHelper(getContext());
		SQLiteDatabase db = mHepler.getWritableDatabase();
		int row_amount = 0;
		switch (sUriMatcher.match(uri)) {
		case DELETE_ALL:
			row_amount = db.delete(ChatsEntry.TABLE_NAME, null, null);
			break;
		}
		
		
		return row_amount;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}

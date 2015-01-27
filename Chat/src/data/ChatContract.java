package data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ChatContract {

	public static final String CONTENT_AUTHORITY = "com.chat.app";

	public static final Uri BASE_CONTENT_URI = Uri.parse("content://"
			+ CONTENT_AUTHORITY);

	public static final String PATH_CHAT = "Chat";

	public final static class ChatsEntry implements BaseColumns {

		public static final String TABLE_NAME = "Chats";
		// uri
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_CHAT).build();

		public static final String COLUMN_MESSAGE = "massege";

		public static final String COLUMN_TIME = "time";

		public static final String COLUMN_SENDER = "sender";
		
		public static Uri buildTimeUri(long time) {
			return ContentUris.withAppendedId(CONTENT_URI, time);
		}
		
		public static long getTimeFromUri(Uri uri) {
			return Long.valueOf(uri.getPathSegments().get(1));
		}
		
		public static Uri buildTimeUserMessageUri(String time, String user, String message) {
			return CONTENT_URI.buildUpon().appendPath(time).appendPath(user).appendPath(message).build();
		}
		
		public static String getUserFromUri (Uri uri) {
			return uri.getPathSegments().get(2);
		}
		
		public static String getMessageFromUri (Uri uri) {
			return uri.getPathSegments().get(3);
		}
		

	}

}

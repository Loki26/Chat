package data;

import android.net.Uri;
import android.provider.BaseColumns;

public class ChatContract {
	
	public static final String CONTENT_AUTHORITY = "com.Chat.app";

	public static final Uri BASE_CONTENT_URI = Uri.parse("content://"
			+ CONTENT_AUTHORITY);

	public static final String PATH_CHAT = "Chat";
	
	public final static class ChatsEntry implements BaseColumns {
		
		public static final String TABLE_NAME = "Chats";
		
		public static final String COLUMN_MESSAGE = "massege";
		
		public static final String COLUMN_TIME = "time";
		
		public static final String COLUMN_SENDER = "sender";
		
	}

}

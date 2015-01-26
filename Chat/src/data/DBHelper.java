package data;

import data.ChatContract.ChatsEntry;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final int DB_VERSION = 1;
	public static String DB_NAME = "Chat.db";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		final String SQL_CREATE_CHAT_TABLE = "CREATE TABLE " + ChatsEntry.TABLE_NAME + " (" + 
				ChatsEntry._ID + " INTEGER PRIMARY KEY," +
				ChatsEntry.COLUMN_TIME + " INTEGER NOT NULL " +
				ChatsEntry.COLUMN_SENDER + " TEXT NOT NULL " +
				ChatsEntry.COLUMN_MESSAGE + " TEXT );";
		
		db.execSQL(SQL_CREATE_CHAT_TABLE);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + ChatsEntry.TABLE_NAME);
        onCreate(db);
		
	}

}

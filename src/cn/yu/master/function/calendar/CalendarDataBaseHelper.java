package cn.yu.master.function.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CalendarDataBaseHelper extends SQLiteOpenHelper{
	
	private static final String TAG = "CalendarDataBaseHelper";
	
	public static final String DB_NAME = "calendar";
	
	public static final String DB_NAME_DB = "calendar.db";
	
	private static final int VERSION = 1;
	
	private String CREATE_TABLE_DAY = "CREATE TABLE IF NOT EXISTS calendar (_id " +
							"INTEGER PRIMARY KEY AUTOINCREMENT," +
							" day VARCHAR NOT NULL," +
							" title VARCHAR NOT NULL," +
							" event VARCHAR NOT NULL)";
	
	public CalendarDataBaseHelper(Context context) {
		super(context, DB_NAME_DB, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_DAY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e(TAG, "db had upgrade...............");
	}
}

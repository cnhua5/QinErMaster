package cn.yu.master.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QAlarmDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String tag = "QAlarmDatabaseHelper";

	private static final String DATABASE_NAME = "alarms.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "alarms";

	public QAlarmDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ "_id INTEGER PRIMARY KEY," + "hour INTEGER, "
				+ "minutes INTEGER, " + "daysofweek INTEGER, "
				+ "alarmtime INTEGER, " + "enabled INTEGER, "
				+ "message TEXT, " + "alert TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS alarms");
		onCreate(db);
	}
}

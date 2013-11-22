package cn.yu.master.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QAlarmDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String tag = "QAlarmDatabaseHelper";

	private static final String DATABASE_NAME = "alarms.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "alarms";
	public static final String TABLE_MON_NAME = "monday";
	public static final String TABLE_TUE_NAME = "tuesday";
	public static final String TABLE_WED_NAME = "wednesday";
	public static final String TABLE_TUR_NAME = "thursday";
	public static final String TABLE_FRI_NAME = "friday";
	public static final String TABLE_SAT_NAME = "saturday";
	public static final String TABLE_SUN_NAME = "sunday";

	public QAlarmDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e(tag, "database......onCreate");
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ "_id INTEGER PRIMARY KEY," + "hour INTEGER, "
				+ "minutes INTEGER, " + "daysofweek INTEGER, "
				+ "alarmtime INTEGER, " + "enabled INTEGER, "
				+ "message TEXT, " + "alert TEXT);");
		db.execSQL("CREATE TABLE "//monday
				+ TABLE_MON_NAME
				+ " (_id INTEGER PRIMARY KEY, " +
				"hour INTEGER, " +
				"minutes INTEGER, " +
				"alarmtime INTEGER, " +
				"enabled INTEGER);");
		db.execSQL("CREATE TABLE "//tuesday
				+ TABLE_TUE_NAME
				+ " (_id INTEGER PRIMARY KEY, " +
				"hour INTEGER, " +
				"minutes INTEGER, " +
				"alarmtime INTEGER, " +
				"enabled INTEGER);");
		db.execSQL("CREATE TABLE "//wednesday
				+ TABLE_WED_NAME
				+ " (_id INTEGER PRIMARY KEY, " +
				"hour INTEGER, " +
				"minutes INTEGER, " +
				"alarmtime INTEGER, " +
				"enabled INTEGER);");
		db.execSQL("CREATE TABLE "//thursday
				+ TABLE_TUR_NAME
				+ " (_id INTEGER PRIMARY KEY, " +
				"hour INTEGER, " +
				"minutes INTEGER, " +
				"alarmtime INTEGER, " +
				"enabled INTEGER);");
		db.execSQL("CREATE TABLE "//friday
				+ TABLE_FRI_NAME
				+ " (_id INTEGER PRIMARY KEY, " +
				"hour INTEGER, " +
				"minutes INTEGER, " +
				"alarmtime INTEGER, " +
				"enabled INTEGER);");
		db.execSQL("CREATE TABLE "//satuday
				+ TABLE_SAT_NAME
				+ " (_id INTEGER PRIMARY KEY, " +
				"hour INTEGER, " +
				"minutes INTEGER, " +
				"alarmtime INTEGER, " +
				"enabled INTEGER);");
		db.execSQL("CREATE TABLE "//sunday
				+ TABLE_SUN_NAME
				+ " (_id INTEGER PRIMARY KEY, " +
				"hour INTEGER, " +
				"minutes INTEGER, " +
				"alarmtime INTEGER, " +
				"enabled INTEGER);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS alarms");
		onCreate(db);
	}
}

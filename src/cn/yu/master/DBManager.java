package cn.yu.master;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import cn.yu.master.function.calendar.CalendarDataBaseHelper;

public abstract class DBManager {

	private static final String TAG = "DBManager";

	public static HashMap<String, SQLiteOpenHelper> dbs;

	public DBManager() {
	}

	public static void initDBs(Context mContext) {
		// canledar DB
		initDBhelper(CalendarDataBaseHelper.DB_NAME,
				new CalendarDataBaseHelper(mContext));
	}

	private static void initDBhelper(String name,
			SQLiteOpenHelper mSQLiteOpenHelper) {
		dbs = new HashMap<String, SQLiteOpenHelper>();
		dbs.put(name, mSQLiteOpenHelper);
	}

	public SQLiteDatabase getDB(String name) {
		Log.e(TAG, "dbs size = " + dbs.size());
		return ((SQLiteOpenHelper)dbs.get(name)).getReadableDatabase();
	}

	public static boolean insertObjects(SQLiteDatabase db, String... insertSQL) {
		db.beginTransaction();
		try {
			for (int i = 0; i < insertSQL.length; i++) {
				Log.e(TAG, "execSQL : " + insertSQL.toString());
				db.execSQL(insertSQL[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
		return true;
	}
	
	public abstract Object getObject();
	public abstract Object readCursor(Cursor mCursor);
}

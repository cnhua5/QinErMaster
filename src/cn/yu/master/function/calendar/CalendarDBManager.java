package cn.yu.master.function.calendar;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.yu.master.DBManager;

public class CalendarDBManager extends DBManager {

	private static final String TAG = "CalendarDBManager";

	public CalendarDBManager() {
	}

	public Cursor getObjectsByDay(String DBname, String day) {
		if (null == DBname || "".equals(DBname)) {
			return null;
		}
		String sql = null;
		SQLiteDatabase db = getCalendarDB(DBname);
		sql = "select * from calendar where day = '" + day + "';";
		Cursor cursor = db.rawQuery(sql, null);
		return cursor;
	}

	@Override
	public ArrayList<CalendarDBObject> readCursor(Cursor result) {
		ArrayList<CalendarDBObject> objects = new ArrayList<CalendarDBObject>();
		result.moveToFirst();
		while (!result.isAfterLast()) {
			CalendarDBObject object = new CalendarDBObject();
			int id = result.getInt(0);
			Log.e(TAG, "Read calendar(id = " + id + " from DB......)");
			object.day = result.getString(1);
			object.title = result.getString(2);
			object.event = result.getString(3);
			objects.add(object);
			result.moveToNext();
		}
		result.close();
		return objects;
	}

	public String formatCalendar(Cursor result) {
		StringBuffer sb = new StringBuffer();
		result.moveToFirst();
		while (!result.isAfterLast()) {
			int id = result.getInt(0);
			Log.e(TAG, "Read calendar(id = " + id + " from DB......)");
			String day = formatDay(result.getString(1));
			String title = result.getString(2);
			String event = result.getString(3);
			String format = "Day:" + day + "\n" + "Title:" + title + "\n"
					+ "Event:" + event + "\n";
			sb.append(format);
			result.moveToNext();
		}
		result.close();
		return sb.toString();
	}

	private String formatDay(String day) {
		day = day.replace("!", "年");
		day = day.replace("@", "月");
		day = day.replace("#", "日");
		return day;
	}

	@Override
	public Object getObject() {
		return null;
	}

	public SQLiteDatabase getCalendarDB(String dbName) {
		return getDB(CalendarDataBaseHelper.DB_NAME);
	}
}

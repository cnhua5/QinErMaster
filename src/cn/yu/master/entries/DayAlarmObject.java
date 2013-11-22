package cn.yu.master.entries;

import android.net.Uri;
import android.provider.BaseColumns;

public class DayAlarmObject {
	public int id;
	public int hour;
	public int minutes;
	public long alarmTime;
	public boolean enabled;

	public static class DayColumns implements BaseColumns {

		public static final Uri MONDAY_CONTENT_URI = Uri
				.parse("content://cn.yu.master/monday");

		public static final String HOUR = "hour";

		public static final String MINUTES = "minutes";

		public static final String ALARM_TIME = "alarmtime";

		public static final String ENABLED = "enabled";

		public static final String WHERE_ENABLED = ENABLED + "=1";

		public static final String SORT_ORDER = HOUR + "," + MINUTES + " ASC,"
				+ _ID + " DESC";

		public static final int ALARM_ID_INDEX = 0;
		public static final int ALARM_HOUR_INDEX = 1;
		public static final int ALARM_MINUTES_INDEX = 2;
		public static final int ALARM_TIME_INDEX = 3;
		public static final int ALARM_ENABLED_INDEX = 4;
	}

	@Override
	public String toString() {
		return "Alarm{" + ", id=" + id + ", enabled=" + enabled + ", hour="
				+ hour + ", minutes=" + minutes + ", time=" + alarmTime + '}';
	}
}

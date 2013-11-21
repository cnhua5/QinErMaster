package cn.yu.master.entries;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class AlarmObject {

	private static final String tag = "AlarmObject";

	public int id;
	public boolean enabled;
	public int hour;
	public int minutes;
	public DaysOfWeek daysOfWeek;
	public long time;
	public String label;
	public String alertString;

	public AlarmObject() {
	}

	@Override
    public String toString() {
        return "Alarm{" +
                "alert=" + alertString +
                ", id=" + id +
                ", enabled=" + enabled +
                ", hour=" + hour +
                ", minutes=" + minutes +
                ", daysOfWeek=" + daysOfWeek +
                ", time=" + time +
                ", label='" + label + '\'' +
                '}';
    }
	
	public static class QAlarmColumns implements BaseColumns {

		public static final Uri CONTENT_URI = Uri
				.parse("content://cn.yu.master/alarm");

		public static final String HOUR = "hour";

		public static final String MINUTES = "minutes";

		public static final String DAYS_OF_WEEK = "daysofweek";

		public static final String ALARM_TIME = "alarmtime";

		public static final String ENABLED = "enabled";

		public static final String MESSAGE = "message";

		public static final String ALERT = "alert";

		public static final String DEFAULT_SORT_ORDER = HOUR + ", " + MINUTES
				+ " ASC" + ", " + _ID + " DESC";

		public static final String WHERE_ENABLED = ENABLED + "=1";

		static final String[] ALARM_QUERY_COLUMNS = { _ID, HOUR, MINUTES,
				DAYS_OF_WEEK, ALARM_TIME, ENABLED, MESSAGE, ALERT };

		public static final int ALARM_ID_INDEX = 0;
		public static final int ALARM_HOUR_INDEX = 1;
		public static final int ALARM_MINUTES_INDEX = 2;
		public static final int ALARM_DAYS_OF_WEEK_INDEX = 3;
		public static final int ALARM_TIME_INDEX = 4;
		public static final int ALARM_ENABLED_INDEX = 5;
		public static final int ALARM_MESSAGE_INDEX = 6;
		public static final int ALARM_ALERT_INDEX = 7;
	}

	public static final class DaysOfWeek {

		private static int[] DAY_MAP = new int[] { Calendar.MONDAY,
				Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY,
				Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY, };

		private static HashMap<Integer, Integer> DAY_TO_BIT_MASK = new HashMap<Integer, Integer>();
		static {
			for (int i = 0; i < DAY_MAP.length; i++) {
				DAY_TO_BIT_MASK.put(DAY_MAP[i], i);
			}
		}

		private int mDays;

		public DaysOfWeek(int days) {
			mDays = days;
		}

		public String toString(Context context, boolean showNever) {
			return toString(context, showNever, false);
		}

		public String toAccessibilityString(Context context) {
			return toString(context, false, true);
		}

		private String toString(Context context, boolean showNever,
				boolean forAccessibility) {
			StringBuilder ret = new StringBuilder();

			if (mDays == 0) {
				return showNever ? "never" : "";
			}

			if (mDays == 0x7f) {
				return "everyday";
			}

			int dayCount = 0, days = mDays;
			while (days > 0) {
				if ((days & 1) == 1)
					dayCount++;
				days >>= 1;
			}

			DateFormatSymbols dfs = new DateFormatSymbols();
			String[] dayList = (forAccessibility || dayCount <= 1) ? dfs
					.getWeekdays() : dfs.getShortWeekdays();

			for (int i = 0; i < 7; i++) {
				if ((mDays & (1 << i)) != 0) {
					ret.append(dayList[DAY_MAP[i]]);
					dayCount -= 1;
					if (dayCount > 0)
						ret.append(", ");
				}
			}
			return ret.toString();
		}

		private boolean isSet(int day) {
			return ((mDays & (1 << day)) > 0);
		}

		public void setDayOfWeek(int dayOfWeek, boolean set) {
			final int bitIndex = DAY_TO_BIT_MASK.get(dayOfWeek);
			set(bitIndex, set);
		}

		public void set(int day, boolean set) {
			if (set) {
				mDays |= (1 << day);
			} else {
				mDays &= ~(1 << day);
			}
		}

		public void set(DaysOfWeek dow) {
			mDays = dow.mDays;
		}

		public int getCoded() {
			return mDays;
		}

		public HashSet<Integer> getSetDays() {
			final HashSet<Integer> set = new HashSet<Integer>();
			for (int i = 0; i < 7; i++) {
				if (isSet(i)) {
					set.add(DAY_MAP[i]);
				}
			}
			return set;
		}

		public boolean[] getBooleanArray() {
			boolean[] ret = new boolean[7];
			for (int i = 0; i < 7; i++) {
				ret[i] = isSet(i);
			}
			return ret;
		}

		public boolean isRepeatSet() {
			return mDays != 0;
		}

		public int getNextAlarm(Calendar c) {
			if (mDays == 0) {
				return -1;
			}
			int today = (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;
			int day = 0;
			int dayCount = 0;
			for (; dayCount < 7; dayCount++) {
				day = (today + dayCount) % 7;
				if (isSet(day)) {
					break;
				}
			}
			return dayCount;
		}

		@Override
		public String toString() {
			return "DaysOfWeek{" + "mDays=" + mDays + '}';
		}
	}
}

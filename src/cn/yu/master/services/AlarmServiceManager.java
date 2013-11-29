package cn.yu.master.services;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.yu.master.entries.AlarmObject;
import cn.yu.master.entries.AlarmObject.DaysOfWeek;
import cn.yu.master.entries.AlarmObject.QAlarmColumns;

public class AlarmServiceManager {

	private static final String tag = "AlarmServiceManager";

	private Context mContext;

	private AlarmsContentHandler mAlarmsContentHandler;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

		};
	};

	public AlarmServiceManager(Context context) {
		this.mContext = context;

		mAlarmsContentHandler = new AlarmsContentHandler(
				mContext.getContentResolver());
		registerObsever();
	}

	public void setOnAlarmContentListener(
			AlarmContentListener alarmContentListener) {
		this.mAlarmContentListener = alarmContentListener;
	}

	public void registerObsever() {
		mContext.getContentResolver().registerContentObserver(
				AlarmObject.QAlarmColumns.CONTENT_URI, true,
				new QAlarmContentObserver(mHandler));
	}

	public ArrayList<AlarmObject> getAllAlarms() {
		return getAlarmsFromCursor(mAlarmsContentHandler.queryAlarms());
	}

	public void updateAlarm(AlarmObject obj) {
		mAlarmsContentHandler.updateAlarm(obj);
	}

	public void addAlarm(AlarmObject obj) {
		mAlarmsContentHandler.insertAlarm(obj);
	}

	public long getLastestAlarm(ArrayList<AlarmObject> alarms) {
		long now = System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(now);
		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMin = c.get(Calendar.MINUTE);
		for (int i = 0; i < alarms.size(); i++) {
			AlarmObject obj = alarms.get(i);
			int hour = obj.hour;
			int min = obj.minutes;
			AlarmObject.DaysOfWeek daysOfWeek = obj.daysOfWeek;
			if (nowHour > hour || (nowHour == hour && nowMin > min)) {
			} else {
				Log.e(tag, "the last alarm is " + i);
				return calculateAlarm(hour, min, daysOfWeek).getTimeInMillis();
			}
		}
		return -1;
	}

	private Calendar calculateAlarm(int hour, int minute,
			AlarmObject.DaysOfWeek daysOfWeek) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());

		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);

		if (hour < nowHour || hour == nowHour && minute <= nowMinute) {
			c.add(Calendar.DAY_OF_YEAR, 1);
		}
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		int addDays = daysOfWeek.getNextAlarm(c);
		if (addDays > 0)
			c.add(Calendar.DAY_OF_WEEK, addDays);
		return c;
	}

	private ArrayList<AlarmObject> getAlarmsFromCursor(Cursor c) {
		if (null == c) {
			Log.e(tag, "alarm cursor is null..");
			return null;
		}
		ArrayList<AlarmObject> alarms = new ArrayList<AlarmObject>();
		try {
			if (c.moveToFirst()) {
				do {
					AlarmObject obj = new AlarmObject();
					obj.id = c.getInt(QAlarmColumns.ALARM_ID_INDEX);
					obj.enabled = c.getInt(QAlarmColumns.ALARM_ENABLED_INDEX) == 1;
					obj.hour = c.getInt(QAlarmColumns.ALARM_HOUR_INDEX);
					obj.minutes = c.getInt(QAlarmColumns.ALARM_MINUTES_INDEX);
					obj.daysOfWeek = new DaysOfWeek(
							c.getInt(QAlarmColumns.ALARM_DAYS_OF_WEEK_INDEX));
					obj.time = c.getLong(QAlarmColumns.ALARM_TIME_INDEX);
					obj.label = c.getString(QAlarmColumns.ALARM_MESSAGE_INDEX);
					obj.alertString = c
							.getString(QAlarmColumns.ALARM_ALERT_INDEX);
					Log.e(tag, obj.toString());
					alarms.add(obj);
				} while (c.moveToNext());
			}
		} finally {
			c.close();
		}
		return alarms;
	}

	private AlarmContentListener mAlarmContentListener;

	class QAlarmContentObserver extends ContentObserver {

		private static final String tag = "QAlarmContentObserver";

		public QAlarmContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			super.onChange(selfChange, uri);
			mAlarmContentListener.contentChange(selfChange, uri);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
		}
	}

	public interface AlarmContentListener {
		public void contentChange(boolean selfChange, Uri uri);
	}
}

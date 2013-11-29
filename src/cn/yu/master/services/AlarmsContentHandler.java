package cn.yu.master.services;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import cn.yu.master.entries.AlarmObject;

public class AlarmsContentHandler {

	private static final String tag = "AlarmsContentHandler";
	
	private ContentResolver mContentResolver;

	public AlarmsContentHandler(ContentResolver contentResolver) {
		this.mContentResolver = contentResolver;
	}

	private ContentValues createContentValues(AlarmObject alarm) {
		ContentValues values = new ContentValues(8);
		if (alarm.id != -1) {
			values.put(AlarmObject.QAlarmColumns._ID, alarm.id);
		}
		values.put(AlarmObject.QAlarmColumns.ENABLED, alarm.enabled ? 1 : 0);
		values.put(AlarmObject.QAlarmColumns.HOUR, alarm.hour);
		values.put(AlarmObject.QAlarmColumns.MINUTES, alarm.minutes);
		values.put(AlarmObject.QAlarmColumns.ALARM_TIME, alarm.time);
//		values.put(AlarmObject.QAlarmColumns.DAYS_OF_WEEK,
//				alarm.daysOfWeek.getCoded());
		return values;
	}

	public void insertAlarm(AlarmObject alarm) {
		mContentResolver.insert(AlarmObject.QAlarmColumns.CONTENT_URI,
				createContentValues(alarm));
	}

	public void updateAlarm(AlarmObject alarm) {
		mContentResolver.update(ContentUris.withAppendedId(
				AlarmObject.QAlarmColumns.CONTENT_URI, alarm.id),
				createContentValues(alarm), null, null);
	}

	public Cursor queryAlarms() {
		Uri queryUri = AlarmObject.QAlarmColumns.CONTENT_URI;
		return mContentResolver.query(queryUri, null, null, null,
				AlarmObject.QAlarmColumns.DEFAULT_SORT_ORDER);
	}
}

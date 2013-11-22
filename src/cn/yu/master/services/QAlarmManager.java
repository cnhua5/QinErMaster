package cn.yu.master.services;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import cn.yu.master.entries.AlarmObject;
import cn.yu.master.entries.DayAlarmObject.DayColumns;

public class QAlarmManager {

	public Cursor queryAlarms(ContentResolver contentResolver, int index) {
		Uri queryUri = null;
		switch (index) {
		case 0:
			queryUri = AlarmObject.QAlarmColumns.CONTENT_URI;
			break;
		case 1:
			queryUri = DayColumns.MONDAY_CONTENT_URI;
			break;
		default:
			break;
		}
		return contentResolver.query(queryUri, null, null, null,
				AlarmObject.QAlarmColumns.DEFAULT_SORT_ORDER);
	}

	public void insertAlarm(ContentResolver contentResolver, AlarmObject alarm) {
		ContentValues value = new ContentValues();
		contentResolver.insert(AlarmObject.QAlarmColumns.CONTENT_URI, value);
	}
}

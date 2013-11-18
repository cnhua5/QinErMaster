package cn.yu.master.services;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import cn.yu.master.entries.AlarmObject;

public class QAlarmManager {

	public void init() {
	}
	
	public Cursor queryAlarms(ContentResolver contentResolver) {
		return contentResolver.query(AlarmObject.QAlarmColumns.CONTENT_URI,
				null, null, null, AlarmObject.QAlarmColumns.DEFAULT_SORT_ORDER);
	}

	public void insertAlarm(ContentResolver contentResolver, AlarmObject alarm) {
		ContentValues value = new ContentValues();
		contentResolver.insert(AlarmObject.QAlarmColumns.CONTENT_URI, value);
	}
}

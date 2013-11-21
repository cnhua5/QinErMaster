package cn.yu.master.entries;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class Alarm {
	
	private static final String tag = "Alarm";

	private AlarmManager mAlarmManager;

	private Context mContext;

	public Alarm(Context context) {
		this.mContext = context;
		mAlarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
	}

	public void setAlarm(long millis) {
		Intent intent = new Intent("cn.yu.master.ALARM");
		PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		long now = System.currentTimeMillis();
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, millis, pi);
	}

	public void setRepeatAlarm() {
		Intent intent = new Intent("cn.yu.master.ALARM");
		PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		long atTimeMillis = System.currentTimeMillis() + 10000;
		mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				atTimeMillis, 900000, pi);
	}
	
	
}

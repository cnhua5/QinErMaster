package cn.yu.master.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.e("----------", "alarm going off....");
		Intent service = new Intent("cn.yu.master.services.ClockService");
		context.startService(service);
	}
}

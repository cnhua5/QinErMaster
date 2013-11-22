package cn.yu.master.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent("cn.yu.master.services.ClockService");
		service.putExtra("alarm", true);
		context.startService(service);
	} 
}

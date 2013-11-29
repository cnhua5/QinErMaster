package cn.yu.master.activities;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import cn.yu.master.R;
import cn.yu.master.entries.Alarm;
import cn.yu.master.entries.AlarmObject;
import cn.yu.master.services.AlarmServiceManager;
import cn.yu.master.services.AlarmServiceManager.AlarmContentListener;

public class ClockListFragment extends Fragment implements OnClickListener {

	private static final String TAG = "ClockListFragment";

	private View rootView;
	private Context mContext;

	private TextView AM_PM;
	private EditText HOUR;
	private EditText MINUTE;
	private Button ADD_ALARM;

	private ListView ALARM_LIST;

	private ArrayList<AlarmObject> alarms;

	private AlarmServiceManager mAlarmServiceManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.text_super, null);
		mContext = rootView.getContext();
		ADD_ALARM = (Button) rootView.findViewById(R.id.alarm_add_bt);
		ADD_ALARM.setOnClickListener(this);
		AM_PM = (TextView) rootView.findViewById(R.id.alarm_time_AP);
		AM_PM.setOnClickListener(this);
		HOUR = (EditText) rootView.findViewById(R.id.alarm_time_hour);
		MINUTE = (EditText) rootView.findViewById(R.id.alarm_time_min);
		ALARM_LIST = (ListView) rootView.findViewById(R.id.alarm_list);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Alarm alarm = new Alarm(mContext);

		mAlarmServiceManager = new AlarmServiceManager(mContext);
		mAlarmServiceManager.setOnAlarmContentListener(new AlarmContentListener() {
			
			@Override
			public void contentChange(boolean selfChange, Uri uri) {
				Log.e(TAG, "selfChange = " + selfChange + "  uir = " + uri.getPath());
			}
		});
		alarms = mAlarmServiceManager.getAllAlarms();
		if (alarms != null) {
			ClockAdapter mAdapter = new ClockAdapter();
			ALARM_LIST.setAdapter(mAdapter);
		}
		long lastestAlarmMillis = mAlarmServiceManager.getLastestAlarm(alarms);
		alarm.setAlarm(lastestAlarmMillis);
	}

	@Override
	public void onClick(View v) {
		if (v == ADD_ALARM) {
			AlarmObject obj = new AlarmObject();
			obj.hour = Integer.parseInt(HOUR.getText().toString());
			obj.minutes = Integer.parseInt(MINUTE.getText().toString());
			mAlarmServiceManager.addAlarm(obj);
		} else if (v == AM_PM) {
			AM_PM.setText("AM".equals(AM_PM.getText()) ? "PM" : "AM");
		}
	}

	class ClockAdapter extends BaseAdapter {

		class ViewHolder {
			TextView date;
			Button enable;
		}

		@Override
		public int getCount() {
			return alarms.size();
		}

		@Override
		public Object getItem(int arg0) {
			return alarms.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = arg1;
			ViewHolder holder = null;
			if (arg1 == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.alarm_list_item, null);
				holder = new ViewHolder();
				holder.date = (TextView) view
						.findViewById(R.id.alarm_list_date);
				holder.enable = (Button) view
						.findViewById(R.id.alarm_list_enable);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			final AlarmObject obj = alarms.get(arg0);
			holder.date.setText(obj.hour + "时" + obj.minutes + "分");
			holder.enable.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.e(TAG, "update enable....");
					AlarmObject objj = obj;
					objj.enabled = !obj.enabled;
					mAlarmServiceManager.updateAlarm(objj);
				}
			});
			return view;
		}
	}
}

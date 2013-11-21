package cn.yu.master.activities;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.yu.master.R;
import cn.yu.master.entries.Alarm;
import cn.yu.master.entries.AlarmObject;
import cn.yu.master.services.AlarmsHandler;
import cn.yu.master.services.QAlarmManager;

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

	private QAlarmManager mQAlarmManager;

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
		mQAlarmManager = new QAlarmManager();
		Alarm alarm = new Alarm(mContext);

		AlarmsHandler alarmsHandler = new AlarmsHandler();
		alarms = alarmsHandler.getAlarmsFromCursor(mQAlarmManager
				.queryAlarms(mContext.getContentResolver()));
		if (alarms != null) {
			ClockAdapter mAdapter = new ClockAdapter();
			ALARM_LIST.setAdapter(mAdapter);
		}
		long lastestAlarmMillis = alarmsHandler.getLastestAlarm(alarms);
		alarm.setAlarm(lastestAlarmMillis);
		
	}

	@Override
	public void onClick(View v) {
		if (v == ADD_ALARM) {
			Toast.makeText(mContext, "Add new Alarm!!!!", 1000).show();
		} else if (v == AM_PM) {
			AM_PM.setText("AM".equals(AM_PM.getText()) ? "PM" : "AM");
		}
	}

	class ClockAdapter extends BaseAdapter {

		class ViewHolder {
			TextView date;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return alarms.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return alarms.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
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
				view.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			AlarmObject obj = alarms.get(arg0);
			holder.date.setText(obj.hour + "时" + obj.minutes + "分");
			return view;
		}
	}
}

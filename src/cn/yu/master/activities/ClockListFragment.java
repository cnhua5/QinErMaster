package cn.yu.master.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.yu.master.R;
import cn.yu.master.entries.AlarmObject;
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

	private Cursor mCursor;

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
		mCursor = mQAlarmManager.queryAlarms(mContext.getContentResolver());
		ClockAdapter mAdapter = new ClockAdapter(mCursor);
		ALARM_LIST.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		if (v == ADD_ALARM) {
			Toast.makeText(mContext, "Add new Alarm!!!!", 1000).show();
		} else if (v == AM_PM) {
			AM_PM.setText("AM".equals(AM_PM.getText()) ? "PM" : "AM");
		}
	}

	class ClockAdapter extends CursorAdapter {

		public ClockAdapter(Cursor c) {
			super(mContext, c);
		}

		class ViewHolder {
			TextView date;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return super.getView(position, convertView, parent);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ViewHolder holder = (ViewHolder) view.getTag();
			int hour = mCursor
					.getInt(AlarmObject.QAlarmColumns.ALARM_HOUR_INDEX);
			int min = mCursor
					.getInt(AlarmObject.QAlarmColumns.ALARM_MINUTES_INDEX);
			holder.date.setText(hour + "时" + min + "分");
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.alarm_list_item, null);
			ViewHolder holder = new ViewHolder();
			holder.date = (TextView) view.findViewById(R.id.alarm_list_date);
			view.setTag(holder);
			return view;
		}
	}
}

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

public class ClockListFragment extends Fragment implements OnClickListener {

	private View rootView;
	private Context mContext;

	private TextView AM_PM;
	private EditText HOUR;
	private EditText MINUTE;
	private Button ADD_ALARM;

	private ListView ALARM_LIST;

	private ArrayList<String> dates;

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
		dates = new ArrayList<String>();
		dates.add("12:00");
		dates.add("06:00");
		ClockAdapter mAdapter = new ClockAdapter();
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

	class ClockAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dates.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dates.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		class ViewHolder {
			TextView date;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			View view = convertView;
			ViewHolder holder;
			if (null == convertView) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.alarm_list_item, null);
				convertView = view;
				holder = new ViewHolder();
				holder.date = (TextView) view
						.findViewById(R.id.alarm_list_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.date.setText(dates.get(arg0));
			return view;
		}
	}
}

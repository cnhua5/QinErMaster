package cn.yu.master.function.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.yu.master.R;
import cn.yu.master.function.calendar.CalendarGridView.ItemClickCallBack;

public class CalendarFragment extends Fragment {

	private static final String TAG = "CalendarFragment";
	private static final int REQUEST_CODE = 0;
	private static final int RESULT_OK = 0;
	
	private int selectPos;
	private int longSelectPos;

	private View calendarView;
	private TextView currYearView;
	private TextView currMonthView;
	private TextView currDayView;
	private TextView details;
	private CalendarGridView mCalendarGrid;
	private int currentYear, currentMonth;
	private CalendarDBManager mCalendarDBManager;

	public CalendarFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCalendarDBManager = new CalendarDBManager();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		calendarView = inflater.inflate(R.layout.left_calendar, null);
		mCalendarGrid = (CalendarGridView) calendarView
				.findViewById(R.id.left_calendar_gv);
		mCalendarGrid.setOnCalendarClickListener(new ItemClickCallBack() {

			@Override
			public void OnCalendarItemClick(AdapterView<?> arg0, int position) {
				selectPos = position;
				loadDetails(listDays.get(position));
			}

			@Override
			public boolean OnCalendarItemLongClick(AdapterView<?> arg0,
					int position) {
				longSelectPos = position;
				loadForAddDetails(listDays.get(position));
				return true;
			}
		});
		currYearView = (TextView) calendarView
				.findViewById(R.id.left_calendar_gv_year);
		currMonthView = (TextView) calendarView
				.findViewById(R.id.left_calendar_gv_month);
		currDayView = (TextView) calendarView
				.findViewById(R.id.left_calendar_gv_day);
		details = (TextView) calendarView
				.findViewById(R.id.left_calendar_details);
		details.setMovementMethod(ScrollingMovementMethod.getInstance());
		return calendarView;
	}

	private void loadDetails(String day) {
		Cursor cursor = mCalendarDBManager.getObjectsByDay(CalendarDataBaseHelper.DB_NAME, day);
		details.setText(mCalendarDBManager.formatCalendar(cursor));
	}

	private void loadForAddDetails(String day) {
		Intent intent = new Intent("yu.master.calendar.detail");
		intent.putExtra("detail_day", day);
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			if(selectPos == longSelectPos) {
				loadDetails(listDays.get(longSelectPos));
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.e(TAG, "onActivityCreated()");
		currentYear = calendar.get(Calendar.YEAR);
		currentMonth = calendar.get(Calendar.MONTH);
		currentDay = calendar.get(Calendar.DAY_OF_MONTH);
		currYearView.setText("" + currentYear + "年");
		currMonthView.setText("" + (currentMonth + 1) + "月");
		currDayView.setText("" + currentDay + "日");
		GridAdapter mAdapter = new GridAdapter(getActivity(), days);
		calculateDays(currentYear, currentMonth);
		mCalendarGrid.setAdapter(mAdapter);
		super.onActivityCreated(savedInstanceState);
	}

	public int currentDay = -1, currentDay1 = -1, currentDayIndex = -1;
	private Calendar calendar = Calendar.getInstance();
	private List<String> listDays = null;
	private String[] days = new String[42];

	private String[] calculateDays(int currentYear, int currentMonth) {
		calendar.set(currentYear, currentMonth, 1);
		listDays = new ArrayList<String>();
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		int monthDays = 0;
		int prevMonthDays = 0;
		monthDays = getMonthDays(currentYear, currentMonth);
		if (currentMonth == 0) {
			prevMonthDays = getMonthDays(currentYear - 1, 11);
		} else {
			prevMonthDays = getMonthDays(currentYear, currentMonth - 1);
		}
		List<String> listDay = new ArrayList<String>();

		// ---------------����--------------------
		String month = currentMonth < 10 ? "0" + (currentMonth) : String
				.valueOf(currentMonth);
		for (int i = week, day = prevMonthDays; i > 1; i--, day--) {
			days[i - 2] = " " + String.valueOf(day);
			String d = day < 10 ? "0" + String.valueOf(day) : String
					.valueOf(day);
			if (month.equals("00")) {
				month = "12";
				listDay.add((currentYear - 1) + "!" + month + "@" + d + "#");
				month = "00";
			} else {
				listDay.add(currentYear + "!" + month + "@" + d + "#");
			}
		}
		for (int i = listDay.size() - 1; i >= 0; i--) {
			listDays.add(listDay.get(i));
		}
		// -----------------����------------------
		month = currentMonth < 9 ? "0" + (currentMonth + 1) : String
				.valueOf(currentMonth + 1);
		for (int day = 1, i = week - 1; day <= monthDays; day++, i++) {
			days[i] = String.valueOf(day);
			String d = day < 10 ? "0" + String.valueOf(day) : String
					.valueOf(day);
			if (day == currentDay) {
				currentDayIndex = i;
			}
			listDays.add(currentYear + "!" + month + "@" + d + "#");
		}
		// -----------------����------------------
		month = currentMonth < 8 ? "0" + (currentMonth + 2) : String
				.valueOf(currentMonth + 2);
		for (int i = week + monthDays - 1, day = 1; i < days.length; i++, day++) {
			days[i] = " " + String.valueOf(day);
			String d = day < 10 ? "0" + String.valueOf(day) : String
					.valueOf(day);
			if (month.equals("13")) {
				month = "01";
				listDays.add((currentYear + 1) + "!" + month + "@" + d + "#");
				month = "13";
			} else {
				listDays.add(currentYear + "!" + month + "@" + d + "#");
			}
		}
		// -----------------------------------
		return days;
	}

	private int getMonthDays(int year, int month) {
		month++;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12: {
			return 31;
		}
		case 4:
		case 6:
		case 9:
		case 11: {
			return 30;
		}
		case 2: {
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
				return 29;
			else
				return 28;
		}
		}
		return 0;
	}

	class GridAdapter extends BaseAdapter {
		private String fromTo;
		private TextView tv;
		private String[] days = new String[42];
		private LayoutInflater inflater;
		private HashMap<Integer, View> views = new HashMap<Integer, View>();

		public HashMap<Integer, View> getViews() {
			return views;
		}

		public void setViews(HashMap<Integer, View> views) {
			this.views = views;
		}

		public GridAdapter(Context context, String[] days) {
			this.days = days;
			inflater = LayoutInflater.from(context);
		}

		public void removeAll() {
			days = new String[42];
			notifyDataSetChanged();
		}

		public void addItems(String[] dayys) {
			for (int i = 0; i < days.length; i++) {
				days[i] = dayys[i];
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return days.length;
		}

		@Override
		public Object getItem(int position) {
			return days[position];
		}

		public TextView getGridItemView(int position) {
			return (TextView) ((LinearLayout) views.get(position))
					.getChildAt(0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = views.get(position);
			TextView info_rili_gtv = null;
			if (view == null) {
				convertView = inflater.inflate(R.layout.left_calendar_griditem,
						null);
				view = convertView;
				views.put(position, view);
				if (position == days.length - 1) {
				}
			}

			info_rili_gtv = (TextView) view.findViewById(R.id.info_grid_tv);
			info_rili_gtv.setText(String.valueOf(days[position]));
			for (int i = 0; i < days.length; i++) {
				if (info_rili_gtv.getText().toString().startsWith(" ")) {
					info_rili_gtv.setTextColor(Color.GRAY);
				} else {
					info_rili_gtv.setTextColor(Color.BLACK);
				}
			}
			return view;
		}
	}
}

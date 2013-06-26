package cn.yu.master.function.calendar;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.yu.master.DBManager;
import cn.yu.master.R;

public class CalendarDayDetailActivity extends Activity {
	
	private static final String TAG = "CalendarDayDetailActivity";
	private SQLiteDatabase mSQLiteDatabase;
	private static final int RESULT_OK = 0;
	
	private String day;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.left_calendar_detail);
		CalendarDBManager dbManager = new CalendarDBManager();
		mSQLiteDatabase = dbManager.getDB(CalendarDataBaseHelper.DB_NAME);
		showDayTitle();
		initRecord();
	}
	
	private void showDayTitle() {
		TextView dayTv = (TextView) findViewById(R.id.calendar_day);
		day = getIntent().getStringExtra("detail_day");
		String day2 = day;
		day2 = day2.replace("!", "Äê");
		day2 = day2.replace("@", "ÔÂ");
		day2 = day2.replace("#", "ÈÕ");
		dayTv.setText(day2);
	}
	
	private void initRecord() {
		final EditText titleET = (EditText) findViewById(R.id.calendar_detail_title);
		final EditText eventET = (EditText) findViewById(R.id.calendar_detail_event);
		Button record = (Button) findViewById(R.id.calendar_detail_record);
		record.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String title = titleET.getText().toString();
				String event = eventET.getText().toString();
				if(null != title && !title.trim().equals("")) {
					String sql = "insert into " + CalendarDataBaseHelper.DB_NAME
							+ "(day, title, event)"
							+ " values('" + day
							+ "' , '" + title
							+ "' , '" + event + "')";
					Log.e(TAG, sql);
					if(DBManager.insertObjects(mSQLiteDatabase, sql)) {
						setResult(RESULT_OK, null);
						finish();
					};
				}
			}
		});
	}
}

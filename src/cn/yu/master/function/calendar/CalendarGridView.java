package cn.yu.master.function.calendar;

import cn.yu.master.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

public class CalendarGridView extends GridView {
	
	private static final String TAG = "CalendarGridView";
	private int lastClickPos = -1;
	
	public CalendarGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CalendarGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CalendarGridView(Context context) {
		super(context);
	}
	
	protected void banClickItems() {
		//TODO
	}

	protected void setOnCalendarClickListener(final ItemClickCallBack mItemClickCallBack) {
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				banClickItems();
				LinearLayout posView = (LinearLayout) getChildAt(arg2);
				if(-1 != lastClickPos) {
					getChildAt(lastClickPos).setBackgroundDrawable(null);
				}
				lastClickPos = arg2;
				posView.setBackgroundResource(R.color.blue);
				mItemClickCallBack.OnCalendarItemClick(arg0, arg2);
			}
		});
		setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				return mItemClickCallBack.OnCalendarItemLongClick(arg0, arg2);
			}
		});
	}

	@Override
	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(listener);
	}
	
	@Override
	public void setOnItemLongClickListener(
			android.widget.AdapterView.OnItemLongClickListener listener) {
		super.setOnItemLongClickListener(listener);
	}
	
	interface ItemClickCallBack{
		void OnCalendarItemClick(AdapterView<?> arg0, int position);
		boolean OnCalendarItemLongClick(AdapterView<?> arg0, int position);
	};
}

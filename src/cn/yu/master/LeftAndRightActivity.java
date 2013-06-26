package cn.yu.master;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import cn.yu.master.function.calendar.CalendarFragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class LeftAndRightActivity extends BaseActivity {

	private static final String TAG = "LeftAndRightActivity";
	
	private SlidingMenu mSlidingMenu;
	private FragmentManager mFragmentManager;

	public LeftAndRightActivity() {
		super(R.string.left_and_right);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBManager.initDBs(getApplicationContext());
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mFragmentManager = getSupportFragmentManager();
		setContentView(R.layout.content_frame);
		
		replace(R.id.content_frame,new SampleListFragment(new String[] { "content!!!!", "content!!!!" }));

		mSlidingMenu.setSecondaryMenu(R.layout.menu_frame_two);
		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
		
		replace(R.id.menu_frame_two,new SampleListFragment(new String[] { "00!!!!", "00!!!!" }));
	}

	@Override
	public void switchPageViewContent(int position) {
		switch (position) {
		case 0:
			Log.e(TAG, "switchPageViewContent---->二维码");
			replace(R.id.content_frame,new SampleListFragment(new String[] { "二维码!!!!", "二维码!!!!" }));
			mSlidingMenu.setStatic(false);
			break;
		case 1:
			Log.e(TAG, "switchPageViewContent---->日历");
			replace(R.id.content_frame,new CalendarFragment());
			mSlidingMenu.setStatic(false);
			break;
		case 2:
			Log.e(TAG, "switchPageViewContent------>其他");
			replace(R.id.content_frame,new SampleListFragment(new String[] { "其他!!!!", "其他!!!!" }));
			mSlidingMenu.setStatic(false);
			break;
		default:
			break;
		}
	}
	
	private void replace(int rId, Fragment frag) {
		mFragmentManager.beginTransaction()
		.replace(rId, frag).commit();
	}
}

package cn.yu.master;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import cn.yu.master.activities.FragmentKeyListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public abstract class BaseActivity extends SlidingFragmentActivity {

	private static final String TAG = "BaseActivity";

	private int mTitleRes;
	protected ListFragment mFrag;

	public BaseActivity(int mTitleRes) {
		super();
		this.mTitleRes = mTitleRes;
	}

	public void setActivityTitle(String title) {
		setTitle(title);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(mTitleRes);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new LeftListFragment(new String[] { "Alarm", "File" },
					new LeftListFragment.ActivityCallBack() {

						@Override
						public void callBack(int position) {
							switchPageViewContent(position);
						}
					});
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	private FragmentKeyListener mListener;

	public FragmentKeyListener getFragmentKeyListener() {
		return mListener;
	}

	public void setFragmentKeyListener(FragmentKeyListener listener) {
		mListener = listener;
	}

	public abstract void switchPageViewContent(int position);
}

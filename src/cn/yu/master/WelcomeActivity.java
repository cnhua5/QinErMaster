package cn.yu.master;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {

	private static final String TAG = "WelcomeActivity";
	private static final int ANIM_START = 0;
	
	private boolean Debuging = true;

	private Animation fadeIn;
	private ImageView forg;

	private Handler mHander = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ANIM_START:
				if(Debuging){
					enterMain();
					return;
				}
				forg.startAnimation(fadeIn);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qin_welcome);
		ImageView back = (ImageView) findViewById(R.id.welcome_back);
		forg = (ImageView) findViewById(R.id.welcome_forg);

		fadeIn = AnimationUtils.loadAnimation(WelcomeActivity.this,
				R.anim.fade);
		fadeIn.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				enterMain();
			}
		});
		mHander.sendEmptyMessageDelayed(ANIM_START, 1000);
	}
	
	private void enterMain() {
		forg.setVisibility(View.GONE);
		Intent intent = new Intent(WelcomeActivity.this,
				LeftAndRightActivity.class);
		startActivity(intent);
		finish();
	}
}

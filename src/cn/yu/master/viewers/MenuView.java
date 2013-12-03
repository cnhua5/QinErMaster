package cn.yu.master.viewers;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MenuView extends RelativeLayout {

	private static final String tag = "MenuView";

	private float touchY;

	public MenuView(Context context, float touchY) {
		super(context);
		this.touchY = touchY;
		layoutMenu(touchY);
	}

	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLongClickable(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.e(tag, "widthMeasureSpec = " + widthMeasureSpec + " ="
				+ heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		Log.e(tag, "l = " + l + " t=" + t + " r=" + r + " b=" + b);
	}

	private void layoutMenu(float viewY) {
		Log.e(tag, "viewY = " + viewY);
		for (int i = 0; i < 4; i++) {
			Button bt = new Button(getContext());
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			double angel = 30 * (3 - i) - 45;
			double x = 100 * Math.cos(angel);
			double y = 100 * Math.sin(angel) + viewY;
			Log.e(tag, "(x,y) = (" + x + ", " + y + ")" + "angel = " + angel);
			lp.leftMargin = (int) x;
			lp.topMargin = (int) y;
			bt.setLayoutParams(lp);
			bt.setText("menu");
			addView(bt, lp);
		}
	}
}

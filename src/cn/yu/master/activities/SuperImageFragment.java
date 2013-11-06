package cn.yu.master.activities;

import cn.yu.master.R;
import cn.yu.master.viewers.SuperImageViewer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SuperImageFragment extends Fragment {
	
	private View iamgeLayout;
	private SuperImageViewer imageViewer;
	
	public SuperImageFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		iamgeLayout = inflater.inflate(R.layout.image_super, null);
		return iamgeLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		imageViewer = (SuperImageViewer) iamgeLayout.findViewById(R.id.image_super_image);
		imageViewer.setImageResource(R.drawable.welcome);
	}
}

package cn.lanmei.com.smartmall.goods;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.lanmei.com.smartmall.R;

public class VerticalFragment3 extends Fragment {


	private boolean hasInited = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_test_ui, null);

		return rootView;
	}


}

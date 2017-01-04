package com.common.login;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.SharePreferenceUtil;

import java.util.List;

import cn.lanmei.com.smartmall.R;


public class GuidePagerAdapter extends PagerAdapter {

	// 界面列表
	private final List<View> views;
	private final Activity activity;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	private OnToTntentListener onToTntentListener;

	public GuidePagerAdapter(List<View> views, Activity activity,OnToTntentListener onToTntentListener) {
		this.views = views;
		this.activity = activity;
		this.onToTntentListener = onToTntentListener;
	}

	// 销毁arg1位置的界面
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	// 获得当前界面数
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	// 初始化arg1位置的界面
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1), 0);
		if (arg1 == views.size() - 1) {
			TextView mStartWeiboImageButton = (TextView) arg0.findViewById(R.id.iv_start);
			mStartWeiboImageButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 设置已经引导
					setGuided();
					goHome();
				}

			});
		}
		return views.get(arg1);
	}

	private void goHome() {
		// 跳转
		if (onToTntentListener!=null)
			onToTntentListener.onToIntentListener();

	}

	/**
	 * 
	 * method desc：设置已经引导过了，下次启动不用再次引导
	 */
	private void setGuided() {
		SharePreferenceUtil.putBoolean(Common.KEY_launch_nostart,true);
	}

	// 判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}


	public interface OnToTntentListener{
		public void onToIntentListener();
	}

}

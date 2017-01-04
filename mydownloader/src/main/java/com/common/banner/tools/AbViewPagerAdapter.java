package com.common.banner.tools;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;


/**
 * 
 * 名称：AbViewPagerAdapter.java 
 * 描述：一个通用的ViewPager适配器
 */


public class AbViewPagerAdapter extends PagerAdapter {
	
	/** The m context. */
	private Context mContext;
	
	/** The m list views. */
	private ArrayList<String> mListViews = null;
	
	/** The m views. */
//	private HashMap <Integer,View> mViews = null;

	private AbOnItemClickListener mOnItemClickListener;
	private AbOnTouchListener mAbOnTouchListener;

	private ImageView img;
	protected DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener imgListener;

	/**
	 * Instantiates over new ab view pager adapter.
	 * @param context the context
	 * @param mListViews the m list views
	 */
	public AbViewPagerAdapter(Context context, ArrayList<String> mListViews,
							  DisplayImageOptions options,

							  ImageLoadingListener imgListener,
							  AbOnItemClickListener mOnItemClickListener,
							  AbOnTouchListener mAbOnTouchListener) {
		this.mContext = context;
		this.mListViews = mListViews;
		this.options =options;
		this.imgListener = imgListener;
//		this.mViews = new HashMap <Integer,View>();
		this.mAbOnTouchListener=mAbOnTouchListener;
		this.mOnItemClickListener=mOnItemClickListener;

	}

	public void setmAbOnTouchListener(AbOnTouchListener mAbOnTouchListener) {
		this.mAbOnTouchListener = mAbOnTouchListener;
	}

	public void setmOnItemClickListener(AbOnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

	public void setImgListener(DisplayImageOptions options,
							   ImageLoader imageLoader,
							   ImageLoadingListener imgListener) {
		this.imageLoader = imageLoader;
		this.options =options;
		this.imgListener = imgListener;
	}

	/**
	 * 描述：获取数量.
	 *
	 * @return the count
	 * @see PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return mListViews.size();
	}



	public void refreshView(ArrayList<String> mListViews){

		if(mListViews!=null){
			this.mListViews = mListViews;
			notifyDataSetChanged();
		}
	}


	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		img = new ImageView(mContext);
		img.setScaleType(ImageView.ScaleType.FIT_XY);
		img.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		imageLoader.displayImage(mListViews.get(position), img, options, imgListener);


		img .setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mOnItemClickListener != null) {
					mOnItemClickListener.onClick(position);
				}
			}
		});
		img.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (mAbOnTouchListener != null) {
					mAbOnTouchListener.onTouch(event);
				}
				return false;
			}
		});
		container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

		return img;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object obj) {
		((ViewPager) container).removeView((View)obj);
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		if (view instanceof ImageView) {
			return ((ImageView) view) == obj;
		} else {
			return view == obj;
		}
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void restoreState(Parcelable parcelable, ClassLoader classLoader) {

	}
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}

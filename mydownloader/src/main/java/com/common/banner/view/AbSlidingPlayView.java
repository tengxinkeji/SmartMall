
package com.common.banner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.common.banner.tools.AbInnerViewPager;
import com.common.banner.tools.AbOnChangeListener;
import com.common.banner.tools.AbOnItemClickListener;
import com.common.banner.tools.AbOnScrollListener;
import com.common.banner.tools.AbOnTouchListener;
import com.common.banner.tools.AbViewPagerAdapter;
import com.mydownloader.cn.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;



/**
 * 名称：AbPlayView 描述：可播放显示的View.
 * 
 * 
 */

public class AbSlidingPlayView extends LinearLayout {

	/** The tag. */
	private String TAG = "AbSlidingPlayView";

	/** The Constant D. */
	@SuppressWarnings("unused")
	private  final boolean D = 	true;

	/** The context. */
	private Context context;

	/** The m view pager. */
	private AbInnerViewPager mViewPager;

	/** The page line layout. */
	private LinearLayout pageLineLayout;

	/** The layout params pageLine. */
	public LayoutParams pageLineLayoutParams = null;

	/** The i. */
	private int count, position;

	/** The hide image. */
	private int displayImage, hideImage;

	protected DisplayImageOptions options;
	private ImageLoadingListener imgListener;

	/** The m on item click listener. */
	private AbOnItemClickListener mOnItemClickListener;

	/** The m ab change listener. */
	private AbOnChangeListener mAbChangeListener;

	/** The m ab scrolled listener. */
	private AbOnScrollListener mAbScrolledListener;

	/** The m ab touched listener. */
	private AbOnTouchListener mAbOnTouchListener;

	/** The layout params ff. */
	public LayoutParams layoutParamsFF = null;

	/** The layout params fw. */
	public LayoutParams layoutParamsFW = null;

	/** The layout params wf. */
	public LayoutParams layoutParamsWF = null;

	/** The m list views. */
	private ArrayList<String> mListViews = null;

	/** The m ab view pager adapter. */
	private AbViewPagerAdapter mAbViewPagerAdapter = null;

	/** 导航的点父View */
	private LinearLayout mPageLineLayoutParent = null;

	/** The page line horizontal gravity. */
	private int pageLineHorizontalGravity = Gravity.RIGHT;

	/** 播放的方向 */
	private int playingDirection = 0;

	/** 播放的开关 */
	private boolean play = false;
	/** 播放的间隔时间 */
	private int sleepTime = 5000;
	/** 播放方向方式（1顺序播放和0来回播放） */
	private int playType = 1;

	private int scWidth;

	/**
	 * 创建一个AbSlidingPlayView
	 * 
	 * @param context
	 */
	public AbSlidingPlayView(Context context) {
		super(context);
		initView(context,null);
	}

	/**
	 * 从xml初始化的AbSlidingPlayView
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public AbSlidingPlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context,attrs);
	}
	

	/**
	 * 
	 * 描述：初始化这个View
	 * 
	 * @param context
	 * @throws
	 */
	@SuppressWarnings("ResourceType")
	public void initView(Context context,AttributeSet attrs) {
		this.context = context;



		TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.AbSlidingPlayView);
		int point_position=0;
		if (typedArray.hasValue(R.styleable.AbSlidingPlayView_point_position)){
			point_position = typedArray.getInt(R.styleable.AbSlidingPlayView_point_position, 0);
		}
		typedArray.recycle();
		layoutParamsFF = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParamsFW = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWF = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		pageLineLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(255, 255, 255));

		RelativeLayout mRelativeLayout = new RelativeLayout(context);

		mViewPager = new AbInnerViewPager(context);
		// 手动创建的ViewPager,如果用fragment必须调用setId()方法设置一个id
		mViewPager.setId(R.id.banner);
		// 导航的点
		mPageLineLayoutParent = new LinearLayout(context);
		mPageLineLayoutParent.setPadding(0, 0, 0, 0);
		pageLineLayout = new LinearLayout(context);
		pageLineLayout.setPadding(15, 1, 15, 15);
		pageLineLayout.setVisibility(View.INVISIBLE);
//		mPageLineLayoutParent.addView(pageLineLayout, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		lp1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		mRelativeLayout.addView(mViewPager, lp1);
		
		RelativeLayout.LayoutParams line = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		line.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		switch (point_position){
			case 0:
				line.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
				break;
			case 1:
				line.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
				break;
			case 2:
				line.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
				break;
		}

		mRelativeLayout.addView(pageLineLayout, line);

		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		mRelativeLayout.addView(mPageLineLayoutParent, lp2);

		addView(mRelativeLayout, layoutParamsFW);

		
		//得到导航点的图片资源文件
//		displayImage =getBitmapFormSrc(R.drawable.dot_focused);
//		hideImage =getBitmapFormSrc(R.drawable.dot_normal);
		displayImage = R.drawable.dot_focused;
		hideImage = R.drawable.dot_normal;

		mListViews = new ArrayList<String>();
		mAbViewPagerAdapter = new AbViewPagerAdapter(context, mListViews,
				options,imgListener,
				mOnItemClickListener, mAbOnTouchListener);
		mViewPager.setAdapter(mAbViewPagerAdapter);
		mViewPager.setFadingEdgeLength(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				makesurePosition();
				onPageSelectedCallBack(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				onPageScrolledCallBack(position);
			}

		});


	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(widthMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result = measureSpec;


		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = measureSpec*9/16;

		return result;
	}

	/**
	 * 得到获取图片
	 * @param name 图片的名字
	 * @return
	 */
	private Bitmap getBitmapFormSrc(int name){
		Bitmap bitmap=null;
		bitmap= BitmapFactory.decodeResource(getResources(), name);
//		try {
////			InputStream is=getResources().getAssets().open(name);
//
//		} catch (IOException e) {
//			Log.d(TAG, "获取图片异常："+e.getMessage());
//		}
		return bitmap;
	}

	/**
	 * 创建导航点.
	 */
	public void creatIndex() {
		// 显示下面的点
		pageLineLayout.removeAllViews();
		mPageLineLayoutParent.setHorizontalGravity(pageLineHorizontalGravity);
		pageLineLayout.setGravity(Gravity.CENTER);
		pageLineLayout.setVisibility(View.VISIBLE);
		count = mListViews.size();
		for (int j = 0; j < count; j++) {
			ImageView imageView = new ImageView(context);
			pageLineLayoutParams.setMargins(10, 10, 10, 10);
			imageView.setLayoutParams(pageLineLayoutParams);
			if (j == 0) {
				imageView.setImageResource(displayImage);
			} else {
				imageView.setImageResource(hideImage);
			}
			pageLineLayout.addView(imageView, j);
		}
	}

	/**
	 * 定位点的位置.
	 */
	public void makesurePosition() {
		position = mViewPager.getCurrentItem();
		for (int j = 0; j < count; j++) {
			if (position == j) {
				((ImageView) pageLineLayout.getChildAt(position)).setImageResource(displayImage);
			} else {
				((ImageView) pageLineLayout.getChildAt(j)).setImageResource(hideImage);
			}
		}
	}

	/**
	 * 描述：添加可播放视图.
	 * 
	 * @param view
	 *            the view
	 */
	public void addView(String view) {
		mListViews.add(view);

		mAbViewPagerAdapter.notifyDataSetChanged();
		creatIndex();
	}

	/**
	 * 描述：添加可播放视图列表.
	 * 
	 * @param views
	 *            the views
	 */
	public void addViews(List<String> views) {
		mListViews.addAll(views);
		mAbViewPagerAdapter.refreshView(mListViews);
		creatIndex();
	}

	/**
	 * 描述：删除可播放视图.
	 * 
	 */
	@Override
	public void removeAllViews() {
		mListViews.clear();
		mAbViewPagerAdapter.notifyDataSetChanged();
		creatIndex();
	}

	/**
	 * 描述：设置页面切换事件.
	 * 
	 * @param position
	 *            the position
	 */
	private void onPageScrolledCallBack(int position) {
		if (mAbScrolledListener != null) {
			mAbScrolledListener.onScroll(position);
		}

	}

	/**
	 * 描述：设置页面切换事件.
	 * 
	 * @param position
	 *            the position
	 */
	private void onPageSelectedCallBack(int position) {
		if (mAbChangeListener != null) {
			mAbChangeListener.onChange(position);
		}

	}

	/** 用与轮换的 handler. */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				//显示播放的页面
				ShowPlay();
				if (play) {
					handler.postDelayed(runnable, sleepTime);
				}
			}
		}

	};

	/** 用于轮播的线程. */
	private Runnable runnable = new Runnable() {
		public void run() {
			if (mViewPager != null) {
				handler.sendEmptyMessage(0);
			}
		}
	};

	/**
	 * 描述：自动轮播. sleepTime 播放的间隔时间
	 */
	public void startPlay() {
		if (handler != null) {
			play = true;
			handler.postDelayed(runnable, sleepTime);
		}
	}

	/**
	 * 描述：自动轮播.
	 */
	public void stopPlay() {
		if (handler != null) {
			play = false;
			handler.removeCallbacks(runnable);
		}
	}

	/**
	 * 设置点击事件监听.
	 * 
	 * @param onItemClickListener
	 *            the new on item click listener
	 */
	public void setOnItemClickListener(AbOnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
		mAbViewPagerAdapter.setmOnItemClickListener(onItemClickListener);
	}

	/**
	 * 描述：设置页面切换的监听器.
	 * 
	 * @param abChangeListener
	 *            the new on page change listener
	 */
	public void setOnPageChangeListener(AbOnChangeListener abChangeListener) {
		mAbChangeListener = abChangeListener;
		mAbViewPagerAdapter.setmAbOnTouchListener(mAbOnTouchListener);
	}

	/**
	 * 描述：设置图片加载监听器.
	 *
	 */
	public void setImgListener(DisplayImageOptions options,

							   ImageLoadingListener imgListener) {

		this.options =options;
		this.imgListener = imgListener;
	}

	/**
	 * 描述：设置页面滑动的监听器.
	 * 
	 * @param abScrolledListener
	 *            the new on page scrolled listener
	 */
	public void setOnPageScrolledListener(AbOnScrollListener abScrolledListener) {
		mAbScrolledListener = abScrolledListener;
	}

	/**
	 * 
	 * 描述：设置页面Touch的监听器.
	 * 
	 * @param abOnTouchListener
	 * @throws
	 */
	public void setOnTouchListener(AbOnTouchListener abOnTouchListener) {
		mAbOnTouchListener = abOnTouchListener;
	}

	/**
	 * Sets the page line image.
	 * 
	 * @param displayImage
	 *            the display image
	 * @param hideImage
	 *            the hide image
	 */
//	public void setPageLineImage(Bitmap displayImage, Bitmap hideImage) {
//		this.displayImage = displayImage;
//		this.hideImage = hideImage;
//		creatIndex();
//
//	}
	public void setPageLineImage(int displayImage, int hideImage) {
		this.displayImage = displayImage;
		this.hideImage = hideImage;
		creatIndex();

	}

	/**
	 * 描述：获取这个滑动的ViewPager类.
	 * 
	 * @return the view pager
	 */
	public ViewPager getViewPager() {
		return mViewPager;
	}

	/**
	 * 描述：获取当前的View的数量.
	 * 
	 * @return the count
	 */
	public int getCount() {
		return mListViews.size();
	}

	/**
	 * 描述：设置页显示条的位置,在AddView前设置.
	 * 
	 * @param horizontalGravity
	 *            the new page line horizontal gravity
	 */
	public void setPageLineHorizontalGravity(int horizontalGravity) {
		pageLineHorizontalGravity = horizontalGravity;
	}

	/**
	 * 如果外层有ScrollView需要设置.
	 * 
	 * @param parentScrollView
	 *            the new parent scroll view
	 */
	public void setParentScrollView(ScrollView parentScrollView) {
		this.mViewPager.setParentScrollView(parentScrollView);
	}

	/**
	 * 如果外层有ListView需要设置.
	 * 
	 * @param parentListView
	 *            the new parent list view
	 */
	public void setParentListView(ListView parentListView) {
		this.mViewPager.setParentListView(parentListView);
	}

	/**
	 * 
	 * 描述：设置导航点的背景
	 * 
	 * @param resid
	 * @throws
	 */
	public void setPageLineLayoutBackground(int resid) {
		pageLineLayout.setBackgroundResource(resid);
	}

	/**
	 * 描述：设置播放的间隔时间
	 * @param sleepTime  间隔时间单位是毫秒
	 */
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	/**
	 *  描述：设置播放方向的方式（1顺序播放和0来回播放） playType 
	 * @param playType    为0表示来回播放，为1表示顺序播放
	 */
	public void setPlayType(int playType) {
		this.playType = playType;
	}


	/**
	 * 描述：播放显示界面（1顺序播放和0来回播放） playType 为0表示来回播放，为1表示顺序播放
	 */
	public void ShowPlay() {
		//总页数
		int count = mListViews.size();
		// 当前显示的页数
		int i = mViewPager.getCurrentItem();
		switch (playType) {
		case 0:
			// 来回播放
			if (playingDirection == 0) {
				if (i == count - 1) {
					playingDirection = -1;
					i--;
				} else {
					i++;
				}
			} else {
				if (i == 0) {
					playingDirection = 0;
					i++;
				} else {
					i--;
				}
			}
			break;
		case 1:
			// 顺序播放
			if (i == count - 1) {
				i = 0;
			} else {
				i++;
			}

			break;

		default:
			break;
		}
		// 设置显示第几页
		mViewPager.setCurrentItem(i, true);
	}

}

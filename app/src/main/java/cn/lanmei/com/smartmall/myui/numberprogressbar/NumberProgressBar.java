package cn.lanmei.com.smartmall.myui.numberprogressbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;


import com.common.app.degexce.L;

import cn.lanmei.com.smartmall.R;




/**
 * Created by daimajia on 14-4-30.
 */
public class NumberProgressBar extends View {

    private int mMaxProgress = 100;

    /**
     * Current progress, can not exceed the max progress.
     */
    private int mCurrentProgress = 0;

    /**
     * The progress text size.
     */
    private float mTextSize;

    private float mProgressHeight;

    /**
     * The suffix of the number.
     */
    private String mSuffix = "";

    /**
     * The prefix.
     */
    private String mPrefix = "";



    private int mTotalWidth, mTotalHeight;
    /**
     * For save and restore instance of progressbar.
     */
    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_PREFIX = "prefix";


    private static final int PROGRESS_TEXT_VISIBLE = 0;


    private RectF rectFbg=new RectF(0,0,0,0);
    private RectF mDynamicRect=new RectF(0,0,0,0);

    /**
     * The text that to be drawn in onDraw().
     */
    private String mCurrentDrawText;


    /**
     * The Paint of the progress text.
     */
    private Paint mTextPaint;



    private Paint mBitPaint;
    private Resources mResources;
    private PorterDuffXfermode mXfermode;
    /**
     * Listener
     */
    private OnProgressBarListener mListener;


    public NumberProgressBar(Context context) {
        this(context, null);
    }

    public NumberProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberProgressBarStyle);
    }

    public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mProgressHeight = dp2px(3.5f);
        mTextSize = sp2px(10);
        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberProgressBar,
                defStyleAttr, 0);
        mTextSize = attributes.getDimension(com.mydownloader.cn.R.styleable.NumberProgressBar_progress_text_size, mTextSize);
        setProgress(attributes.getInt(R.styleable.NumberProgressBar_progress_current, 0));
        setMax(attributes.getInt(R.styleable.NumberProgressBar_progress_max, 100));
        mResources = getResources();
        attributes.recycle();
        initializePainters();


    }
    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) dp2px(100.0f);
    }

    @Override
    protected int getSuggestedMinimumHeight() {

        return (int) (mTextSize*2+mProgressHeight)+4;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec+30, false));

    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        if (isWidth) {
            mTotalWidth=result;
        } else {
            mTotalHeight=result;
        }
        return result;
    }



    @Override
    protected void onDraw(Canvas canvas) {

        int top =getHeight();
        L.MyLog("","top:"+top);
        canvas.drawText("0", 0, top, mTextPaint);
        float mDrawTextWidth = mTextPaint.measureText("" + getMax());
        float mDrawTextStart = getWidth() - getPaddingRight() - mDrawTextWidth;
        canvas.drawText(""+getMax(), mDrawTextStart, top, mTextPaint);
        calculateDrawRectF(canvas);
    }


    private void initializePainters() {


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(ContextCompat.getColor(getContext(),R.color.white));
        mTextPaint.setTextSize(mTextSize);

        // 初始化paint
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        mBitPaint.setColor(Color.RED);

        // 叠加处绘制源图
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }


    private void calculateDrawRectF(Canvas canvas) {


        mCurrentDrawText = String.format("%d", getProgress());
//        mCurrentDrawText = String.format("%d", getProgress() * 100 / getMax());
        mCurrentDrawText = mPrefix + mCurrentDrawText + mSuffix;

        float top=(getHeight()-mProgressHeight)/2;
        float mCurrentTop=getProgress()*getWidth()/getMax();
        float mDrawTextWidth = mTextPaint.measureText("" + getMax());
        canvas.drawText(mCurrentDrawText, mCurrentTop-(mDrawTextWidth/3), mTextSize, mTextPaint);

        mDynamicRect.left=0;
        mDynamicRect.top=top;
        mDynamicRect.right=mCurrentTop;
        mDynamicRect.bottom=top+mProgressHeight;

        rectFbg.left=0;
        rectFbg.top=top;
        rectFbg.right=getWidth();
        rectFbg.bottom=top+mProgressHeight;

        // 存为新图层
        int saveLayerCount = canvas.saveLayer(0, 0, mTotalWidth, mTotalHeight, mBitPaint,
                Canvas.ALL_SAVE_FLAG);
        // 绘制目标图
        mBitPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectFbg,dp2px(mProgressHeight),dp2px(mProgressHeight),mBitPaint);
        mBitPaint.setColor(Color.YELLOW);
        // 设置混合模式
        mBitPaint.setXfermode(mXfermode);
        // 绘制源图形
        canvas.drawRect(mDynamicRect, mBitPaint);
        // 清除混合模式
        mBitPaint.setXfermode(null);
        // 恢复保存的图层；
        canvas.restoreToCount(saveLayerCount);



    }



    public int getProgress() {
        return mCurrentProgress;
    }

    public int getMax() {
        return mMaxProgress;
    }


    public void setMax(int maxProgress) {
        if (maxProgress > 0) {
            this.mMaxProgress = maxProgress;
            invalidate();
        }
    }

    public void setSuffix(String suffix) {
        if (suffix == null) {
            mSuffix = "";
        } else {
            mSuffix = suffix;
        }
    }

    public String getSuffix() {
        return mSuffix;
    }

    public void setPrefix(String prefix) {
        if (prefix == null)
            mPrefix = "";
        else {
            mPrefix = prefix;
        }
    }

    public String getPrefix() {
        return mPrefix;
    }

    public void incrementProgressBy(int by) {
        if (by > 0) {
            setProgress(getProgress() + by);
        }

        if(mListener != null){
            mListener.onProgressChange(getProgress(), getMax());
        }
    }

    public void setProgress(int progress) {
        if (progress <= getMax() && progress >= 0) {
            this.mCurrentProgress = progress;
            invalidate();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());

        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_PROGRESS, getProgress());
        bundle.putString(INSTANCE_SUFFIX, getSuffix());
        bundle.putString(INSTANCE_PREFIX, getPrefix());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;

            initializePainters();
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            setPrefix(bundle.getString(INSTANCE_PREFIX));
            setSuffix(bundle.getString(INSTANCE_SUFFIX));

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }



    public void setOnProgressBarListener(OnProgressBarListener listener){
        mListener = listener;
    }
}

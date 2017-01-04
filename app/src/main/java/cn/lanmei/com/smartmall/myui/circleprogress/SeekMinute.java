package cn.lanmei.com.smartmall.myui.circleprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.common.app.StaticMethod;

import cn.lanmei.com.smartmall.R;

/**
 * TODO: document your custom view class.
 */
public class SeekMinute extends View {

    private TextPaint mPaint;
    private float mTextWidth;
    private float mTextHeight;
    private int maxProgress=60;
    private int mProgress=1;
    int rotate;
    protected float mCenterX;
    protected float mCenterY;
    private float radius_1;
    private float mSectionHeight;
    protected int colorSelect;
    protected  XChartCalc xChartCalc;
    private boolean myStart=true;//是否可用
    private boolean inRangeR_1;



    private OnSeekCircleChangeListener mOnSeekCircleChangeListener;

    /**
     * Sets a listener to receive notifications of changes to the SeekCircle's
     * progress level. Also provides notifications of when the user starts and
     * stops a touch gesture within the SeekCircle.
     *
     * @param listener
     *            The seek circle notification listener
     *
     * @see SeekCircle.OnSeekCircleChangeListener
     */
    public void setOnSeekCircleChangeListener(OnSeekCircleChangeListener listener)
    {
        mOnSeekCircleChangeListener = listener;
    }

    private boolean mTrackingTouch = false;
    private int mRevolutions = 0;
    private float mOldX;

    public SeekMinute(Context context) {
        super(context);
        init(null, 0);
    }

    public SeekMinute(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SeekMinute(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        xChartCalc = new XChartCalc();
        // Set up a default TextPaint object
        mPaint = new TextPaint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.LEFT);
        colorSelect= ContextCompat.getColor(getContext(),R.color.txtColor_bar);
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {

        /*mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;*/


    }

    private void updateDimensions(int width, int height)
    {
        // Update center position
        mCenterX = width / 2.0f;
        mCenterY = height / 2.0f;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > height)
            super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        updateDimensions(getWidth(), getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        updateDimensions(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        int mRadius=Math.min(contentWidth,contentHeight)/2;
        canvas.save();
        canvas.translate(mCenterX,mCenterY);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(0,0,mRadius,mPaint);
        int count=maxProgress;
        mPaint.setColor(colorSelect);
        canvas.drawCircle(0,0, StaticMethod.dip2px(getContext(),2),mPaint);
        int cirAngle;
        String txt="00";
        mPaint.setTextSize(StaticMethod.dip2px(getContext(),14));
        mTextWidth = mPaint.measureText(txt);
        mSectionHeight=mTextWidth*1.5f;
        radius_1 =  mRadius-mTextWidth;
        rotate = 360/count;
        cirAngle=rotate*(mProgress-1)-90+rotate;
        xChartCalc.CalcArcEndPointXY(0f,0f,radius_1,cirAngle);
        canvas.drawCircle(xChartCalc.getPosX(),xChartCalc.getPosY(), mSectionHeight/2,mPaint);
        canvas.drawLine(0,0,xChartCalc.getPosX(),xChartCalc.getPosY(),mPaint);


        mPaint.setColor(Color.BLACK);
        DrawText drawText=new DrawText();
        for (int i=0;i<count;i+=5){
            cirAngle=rotate*i-90;
            txt=i+"";
            xChartCalc.CalcArcEndPointXY(0f,0f,radius_1,cirAngle);
            drawText.setParams(txt,xChartCalc.getPosX(),xChartCalc.getPosY(),mPaint);
            drawText.draw(canvas);
        }
        canvas.restore();


    }

    public int getProgress() {
        if (mProgress==60)
            mProgress=0;
        return mProgress;
    }

    /**
     * Set progress
     *
     * @param progress
     */
    public void setProgress(int progress)
    {
        updateProgress(progress);
    }

    public boolean isStart() {
        return myStart;
    }

    public void setStart(boolean enable) {
        this.myStart = enable;
        invalidate();
    }

    protected boolean updateProgress(int progress)
    {
        // Clamp progress
        progress = Math.max(0, Math.min(maxProgress, progress));

        if (progress != mProgress)
        {
            mProgress = progress;
            invalidate();

            return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (!isStart())
            return super.onTouchEvent(event);
        // Right hand coordinates X to the right, Y up
        float x =event.getX()-mCenterX;
        float y =mCenterY-event.getY() ;

        float distance = (float) Math.sqrt(x * x + y * y);
        inRangeR_1 = Math.abs(distance - radius_1) <= mSectionHeight;
        boolean inRange = inRangeR_1;
        boolean inDeadZone = false; // distance <= mRadius * 0.2f; // 20%
        // deadzone to avoid some quick flips

        boolean updateProgress = false;

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                if (mOnSeekCircleChangeListener != null)
                    mOnSeekCircleChangeListener.onStartTrackingTouch(this);
                if (inRange)
                {
//					SystemConst.seekCiecleFirst = true;
                    mTrackingTouch = true;
                    mOldX = x;
                    mRevolutions = 0;
                    updateProgress = true;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (mTrackingTouch && !inDeadZone)
                    updateProgress = true;
                break;

            case MotionEvent.ACTION_UP:
                if (mTrackingTouch && !inDeadZone)
                    updateProgress = true;
                mTrackingTouch = false;
                if (mOnSeekCircleChangeListener != null)
                    mOnSeekCircleChangeListener.onStopTrackingTouch(this);
//				SystemConst.seekCiecleFirst = false;
                break;

            case MotionEvent.ACTION_CANCEL:
//				SystemConst.seekCiecleFirst = false;
                mTrackingTouch = false;
                if (mOnSeekCircleChangeListener != null)
                    mOnSeekCircleChangeListener.onStopTrackingTouch(this);
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        if (updateProgress)
        {
            // Calculate absolute position [0, 1] with 0 & 1 both at 12-o-clock


            double atan2 = Math.atan2(-x, -y);
            float position = (float) ((atan2+ Math.PI) / (Math.PI * 2.0));
            int progress = Math.round(position * ((float) maxProgress));


            if (event.getAction() != MotionEvent.ACTION_DOWN)
            {
                updateRevolutions(x, y);

                float absPosition = (float)mRevolutions + position;

                // Clamp progress
                if (absPosition < 0.0f)
                    progress = 0;
                else if (absPosition > 1.0f)
                    progress = maxProgress;
            }

            mOldX = x;
            updateTouchProgress(progress);

            return true;
        }

        return super.onTouchEvent(event);
    }

    /**
     * Update the number of revolutions we're at
     * @param x X position
     * @param y Y position
     */
    private void updateRevolutions(float x, float y)
    {
        // We're in the upper half and X just flipped
        boolean leftFlip = y > 0.0f && mOldX >= 0.0f && x < 0.0f;
        boolean rightFlip = y > 0.0f && mOldX <= 0.0f && x > 0.0f;

        if (leftFlip)
            mRevolutions -= 1.0f;
        else if (rightFlip)
            mRevolutions += 1.0f;

        // Clamp windings to [-1, 1]
        mRevolutions = Math.max(-1, Math.min(1, mRevolutions));
    }
    private void updateTouchProgress(int progress)
    {
        boolean result = updateProgress(progress);
        if (result)
        {
            if (mOnSeekCircleChangeListener != null)
                mOnSeekCircleChangeListener.onProgressChanged(this, getProgress(), true);
        }
    }

}

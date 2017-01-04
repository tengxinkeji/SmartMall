package cn.lanmei.com.smartmall.myui.circleprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.common.app.StaticMethod;

/**
 * 画刻度盘
 */
public class DrawProgress implements DrawGraphics{
    Context mContext;
    // 创建画笔
    Paint paint;
    float mCenterX;
    float mCenterY;
    float mmRadius;
    int mProgress;
    int mMaxProgress;
    int mColor1;
    int mColor2;
    int mColor3;
    int startRoate;
    int start;
    int step;
    int offsetAngle;
    boolean mEnable;

    public DrawProgress(Context context,float cx, float cy, float radius,
                        int mProgress, int mMaxProgress,
                        int mColor1, int mColor2, int mColor3,
                        int startRoate,int offsetAngle, int start, int step) {
        this.mContext=context;
        this.mCenterX=cx;
        this.mCenterY=cy;
        this.mmRadius=radius;
        this.mProgress=mProgress;
        this.mMaxProgress=mMaxProgress;
        this.mColor1=mColor1;
        this.mColor2=mColor2;
        this.mColor3=mColor3;
        this.startRoate=startRoate;
        this.offsetAngle=offsetAngle;
        this.start=start;
        this.step=step;
        mEnable=false;
        paint = new Paint();
        paint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了

    }

    public int getmProgress() {
        return mProgress;
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
    }

    public boolean ismEnable() {
        return mEnable;
    }

    public void setmEnable(boolean mEnable) {
        this.mEnable = mEnable;
    }

    @Override
    public void draw(Canvas canvas) {
        /**
         * 画最外层的大圆环
         */
        if (ismEnable()){
            paint.setColor(mColor2); //设置圆环的颜色
        }else{
            paint.setColor(mColor3); //设置圆环的颜色
        }

        int int100= StaticMethod.dip2px(mContext,35);
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(int100); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(mCenterX, mCenterY, mmRadius, paint); //画出圆环


        if (ismEnable()){
            /**
             * 画圆弧 ，画圆环的进度
             */

            //设置进度是实心还是空心
            paint.setStrokeWidth(int100); //设置圆环的宽度
            paint.setColor(mColor1);  //设置进度的颜色

            float arcLeft = mCenterX - mmRadius;
            float arcTop  = mCenterY - mmRadius ;
            float arcRight = mCenterX + mmRadius ;
            float arcBottom = mCenterY + mmRadius ;
            RectF oval = new RectF(arcLeft ,arcTop,arcRight,arcBottom);  //用于定义的圆弧的形状和大小的界限
            paint.setStyle(Paint.Style.STROKE);
            int roate=(360-(offsetAngle*2) )* mProgress / mMaxProgress;
            canvas.drawArc(oval, startRoate+offsetAngle,roate , false, paint);  //根据进度画圆弧
        }




        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        if (ismEnable()){
            paint.setColor(mColor1);
        }else{
            paint.setColor(mColor3);
        }

        int txtSize=(int) mmRadius/2;
        paint.setTextSize(txtSize);
//		int percent = (int)(((float)mProgress / (float)mMaxProgress) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        String txt=(mProgress+start)+"°C";
        float textWidth = paint.measureText(txt);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        canvas.drawText(txt , mCenterX - textWidth / 2, mCenterY+txtSize/3 , paint); //画出进度百分比
    }
}

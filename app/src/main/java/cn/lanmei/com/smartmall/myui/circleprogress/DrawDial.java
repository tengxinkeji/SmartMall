package cn.lanmei.com.smartmall.myui.circleprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.common.app.StaticMethod;

/**
 * 画刻度盘
 */
public class DrawDial implements DrawGraphics{
    private Context context;

    // 创建画笔
    Paint paint;
    float cx;
    float cy;
    float radius;
    XChartCalc xChartCalc;

    int start = 0;//起始值
    int count =0; //刻度数
    int step=0;//步长
    int startRoate=0;
    int offsetAngle;
    int startColor;

    public DrawDial( Context context,
                     float cx, float cy, float radius,
                     int start,int count,int step,int startRoate,int offsetAngle,
                     int startColor) {
        this.context=context;
        this.paint = new Paint();
        this.cx=cx;
        this.cy=cy;
        this.radius=radius;
        this.start=start;
        this.count=count;
        this.step=step;
        this.startRoate=startRoate;
        this.offsetAngle=offsetAngle;
        this.startColor=startColor;
        paint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
        //设置为空心
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        xChartCalc = new XChartCalc();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(cx, cy); //将位置移动画纸的坐标点:150,150
//        canvas.drawCircle(0, 0, radius, paint); //画圆圈
        int int40= StaticMethod.dip2px(context,35);
        float  y=radius-int40;
        canvas.drawCircle(0, 0, y, paint); //画圆圈

        Paint tmpPaint = new Paint(paint); //文字画笔对象
        int int14= StaticMethod.dip2px(context,12);
        tmpPaint.setTextSize(int14);
        tmpPaint.setColor(Color.WHITE);
        int rotate = (360-(offsetAngle*2))/count;
        String txt="00";
        DrawText drawText=new DrawText();
        for (int i=0;i<count;i++){
            int cirAngle=rotate*i+startRoate+offsetAngle;
            txt=(i*step+start)+"";
            xChartCalc.CalcArcEndPointXY(0f,0f,radius-int14*1.3f,cirAngle+rotate/2);
            drawText.setParams(txt,xChartCalc.getPosX(),xChartCalc.getPosY(),tmpPaint);
            drawText.draw(canvas);
        }

        /**开始标志*/
        float int20= StaticMethod.dip2px(context,10);
        paint.setColor(startColor);
        paint.setStrokeWidth(4f);
        canvas.drawLine(0f, y+int20, 0, int20*2.5f+y, paint);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1f);
        canvas.rotate(offsetAngle,0f,0f); //旋转画纸
        canvas.drawLine(0f, y+(int20*0.8f), 0, y+(int20*2.5f), paint);
        canvas.rotate(rotate,0f,0f); //旋转画纸

        for(int i=0 ; i <count-1 ; i++){
            canvas.drawLine(0f, y+(int20*0.8f), 0, y+(int20*2.5f), paint);
            canvas.rotate(rotate,0f,0f); //旋转画纸
        }
        canvas.drawLine(0f, y+(int20*0.8f), 0, y+(int20*2.5f), paint);
        /*paint.setColor(startColor);
        paint.setStrokeWidth(2f);
        canvas.drawLine(0f, y, 0, y+int20, paint);*/
        canvas.rotate(rotate,0f,0f); //旋转画纸

        canvas.restore();
    }
}

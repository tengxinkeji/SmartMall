package cn.lanmei.com.smartmall.myui.circleprogress;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

/**
 * 画刻度盘
 */
public class DrawCircle implements DrawGraphics{
    // 创建画笔
    Paint p;
    float cx;
    float cy;
    float radius;

    public DrawCircle(float cx, float cy, float radius) {
        this.cx=cx;
        this.cy=cy;
        this.radius=radius;
        p =new Paint();
        p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
        /* 设置渐变色 这个正方形的颜色是改变的 */
        Shader mShader = new LinearGradient(cx, cy-radius, cx, cy+radius,
                new int[] { Color.parseColor("#36c0fe"),  Color.parseColor("#104E8B")}
                , null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        p.setShader(mShader);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(cx, cy, radius, p);// 大圆
    }
}

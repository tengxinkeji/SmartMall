package cn.lanmei.com.smartmall.myui.circleprogress;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 画刻度盘
 */
public class DrawText implements DrawGraphics{
    // 创建画笔
    String text ;
    float x ;
    float y;
    Paint paint ;
    Rect rect=new Rect();

   public void setParams(String text , float x ,float y,Paint paint){
        this.text=text;
        this.x=x;
        this.y=y;
        this.paint=paint;

    }
    @Override
    public void draw(Canvas canvas) {
        paint.getTextBounds(text, 0, text.length(), rect);

//        L.MyLog("",text+":"+"xy:"+x+","+y+"---"
//                +String.format("%d,%d,%d,%d", rect.left,rect.top, rect.right,rect.bottom));
        canvas.drawText(text
                ,x - (rect.right + rect.left)/2
                ,y- (rect.bottom + rect.top)/2, paint);
    }
}

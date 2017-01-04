package cn.lanmei.com.smartmall.myui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/11/19.
 */

public class ImageGoodsView extends ImageView {
    public ImageGoodsView(Context context) {
        super(context);
    }

    public ImageGoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageGoodsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageGoodsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,widthMeasureSpec);
    }
}

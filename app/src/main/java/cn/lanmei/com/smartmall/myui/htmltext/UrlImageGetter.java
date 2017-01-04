package cn.lanmei.com.smartmall.myui.htmltext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.common.app.StaticMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

public class UrlImageGetter implements Html.ImageGetter {
    Context c;
    TextView container;
    int width;
    int position;
    HtmlTextView.OnHtmlImgPathListener onHtmlImgPathListener;

    /**
     *
     * @param t
     * @param c
     */
    public UrlImageGetter(TextView t, Context c, HtmlTextView.OnHtmlImgPathListener onHtmlImgPathListener) {
        this.c = c;
        this.container = t;
        this.onHtmlImgPathListener=onHtmlImgPathListener;
        position=0;
        width = c.getResources().getDisplayMetrics().widthPixels- StaticMethod.dip2px(c,20);
    }

    @Override
    public Drawable getDrawable(String source) {
        onHtmlImgPathListener.onHtmlImgPathListener(position,source);
        position++;
        final UrlDrawable urlDrawable = new UrlDrawable();
        ImageLoader.getInstance().loadImage(source,
            new SimpleImageLoadingListener() {
                @Override
                public File onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    int btmWidth = loadedImage.getWidth();


                    float scaleWidth = ((float) width) / btmWidth;


                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleWidth);
                    loadedImage = Bitmap.createBitmap(loadedImage, 0, 0,
                            loadedImage.getWidth(), loadedImage.getHeight(),
                            matrix, true);
                    urlDrawable.bitmap = loadedImage;
                    urlDrawable.setBounds(0, 0, loadedImage.getWidth(),
                        loadedImage.getHeight());
                    container.invalidate();
                    container.setText(container.getText()); // ??????
                    return null;
                }
            });

        return urlDrawable;
    }

    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }
}
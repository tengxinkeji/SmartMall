package cn.lanmei.com.smartmall.main;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.app.StaticMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

import cn.jpush.android.api.JPushInterface;
import cn.lanmei.com.smartmall.jpush.MyReceiver;
import cn.lanmei.com.smartmall.R;

/**
 * Created by Administrator on 2016/7/25.
 */
public abstract class BaseActionActivity extends BaseActivity{

    public FrameLayout frameLayoutBar;
    public RelativeLayout layoutHead;
    public TextView txtLeft;
    public TextView txtCenter;
    public TextView txtRight;

    private View inflateView;
    private FrameLayout frameLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base);
        /*初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。*/
        JPushInterface.init(getApplicationContext());
        imageLoader = ImageLoader.getInstance();
        frameLayoutBar= (FrameLayout) findViewById(R.id.layout_bar);
        layoutAppInfo = (LinearLayout) findViewById(R.id.app_info);

        layoutHead = (RelativeLayout)frameLayoutBar.findViewById(R.id.app_bar);
        txtLeft= (TextView)layoutHead.findViewById(R.id.bar_left);
        txtCenter= (TextView)layoutHead.findViewById(R.id.bar_center);
        txtRight= (TextView)layoutHead.findViewById(R.id.bar_right);


        frameLayout = (FrameLayout)findViewById(R.id.layout_base);

        ButtomClick buttomClick=new ButtomClick();
        txtLeft.setOnClickListener(buttomClick);
        txtRight.setOnClickListener(buttomClick);
        layoutAppInfo.setOnClickListener(buttomClick);

        loadViewLayout();
        mfindViewById();

    }








    public abstract void loadViewLayout();
    public abstract void mfindViewById();


    public void setMContentView(Fragment fragment){
        setMContentView(fragment,fragment.getTag());
    }
    public void setMContentView(Fragment fragment,String tag){
        fm.beginTransaction()
                .replace(R.id.layout_base,fragment,tag)
                .commit();
    }



    public void setMContentView(@LayoutRes int layoutResID){
        inflateView = LayoutInflater.from(BaseActionActivity.this).inflate(layoutResID, null);
        frameLayout.removeAllViews();
        frameLayout.addView(inflateView);
    }

    public View mfindViewById(int id){
        if (inflateView!=null)
            return inflateView.findViewById(id);
        else
            return null;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }




    private class ButtomClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bar_left:
                    onHeadLeftButton(v);
                    break;
                case R.id.bar_right:
                    onHeadRightButton(v);
                    break;
                case R.id.app_info:
                    String[] info= MyReceiver.printBundle(MyReceiver.bundle);
                    toDevErrDetail(info[0],info[1]);
                    break;

            }
        }
    }

    protected void onHeadLeftButton(View v) {
            finish();
    }

    protected void onHeadRightButton(View v) {

    }

    protected void setHeadCentertText(CharSequence text) {
        txtCenter.setText(text);
    }

    protected void setHeadLeftText(CharSequence text) {
        txtLeft.setBackgroundColor(Color.TRANSPARENT);
        txtLeft.setText(text);
    }

    protected void setHeadShow(boolean isShow) {
        if (isShow)
            layoutHead.setVisibility(View.VISIBLE);
        else
            layoutHead.setVisibility(View.GONE);
    }

    protected void setHeadLeftText(int resid) {
        txtLeft.setBackgroundColor(Color.TRANSPARENT);
        txtLeft.setText(resid);
    }

    protected void setHeadLeftImg(int resid) {
        /*Bitmap b = BitmapFactory.decodeResource(getResources(),resid);
        ImageSpan imgSpan = new ImageSpan(this, b);
        SpannableString spanString = new SpannableString("icon");
        spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtLeft.setText(spanString);*/
        Drawable drawable = ContextCompat.getDrawable(this, resid);
        int int40= StaticMethod.dip2px(this,30);
        drawable.setBounds(0,0,int40,int40);
//        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        txtLeft.setCompoundDrawables(drawable,null,null,null);
        txtLeft.setText("");
    }

    protected void setHeadRightText(CharSequence text) {
        txtRight.setBackgroundColor(Color.TRANSPARENT);
        txtRight.setText(text);
    }

    protected void setHeadRightText(int resid) {
        txtRight.setBackgroundColor(Color.TRANSPARENT);
        txtRight.setText(getResources().getString(resid));
    }

    protected void setHeadRightImg(int resid) {
       /* Bitmap b = BitmapFactory.decodeResource(getResources(),resid);
        ImageSpan imgSpan = new ImageSpan(this, b);
        SpannableString spanString = new SpannableString("icon");
        spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtRight.setText(spanString);*/
        Drawable drawable = ContextCompat.getDrawable(this, resid);
        int int40= StaticMethod.dip2px(this,30);
        drawable.setBounds(0,0,int40,int40);
//        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        txtRight.setCompoundDrawables(drawable,null,null,null);
        txtRight.setText("");
    }




}

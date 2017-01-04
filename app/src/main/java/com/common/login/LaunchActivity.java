package com.common.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.MainActionActivity;


/**
 * @author Administrator 启动引导页
 * Created by Administrator on 2015/10/30.
 */
public class LaunchActivity extends Activity implements ViewPager.OnPageChangeListener ,GuidePagerAdapter.OnToTntentListener{
    private ViewPager vp;
    private GuidePagerAdapter vpAdapter;
    private List<View> views;

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;
    private int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_launch);

        // 初始化页面
        initViews();

        // 初始化底部小点
        initDots();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.what_new_four, null));
        views.add(inflater.inflate(R.layout.what_new_one, null));
        views.add(inflater.inflate(R.layout.what_new_two, null));
        views.add(inflater.inflate(R.layout.what_new_three, null));


        // 初始化Adapter
        vpAdapter = new GuidePagerAdapter(views, this,this);

        vp = (ViewPager) findViewById(R.id.launch_vp);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.launch_ll);
        count=ll.getChildCount();
        dots = new ImageView[views.size()];

        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }


    private void setCurrentDot(int position) {
        if (position < 0 || position > count- 1 || currentIndex == position) {
            return;
        }
        for(int i=0;i<count;i++ ){
            dots[i].setEnabled(true);
            dots[i].setImageResource(R.drawable.dot_normal);
        }
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        dots[position].setImageResource(R.drawable.dot_focused);
        currentIndex = position;


    }

    // 不可点击返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurrentDot(arg0);
    }

    @Override
    public void onToIntentListener() {
        startActivity(new Intent(this, MainActionActivity.class));
       /* boolean isLogin= SharePreferenceUtil.getBoolean(Common.KEY_is_login,false);
        if (isLogin){
            if (SharePreferenceUtil.getInt(Common.User_Type,0)==1)//商家
                startActivity(new Intent(this, MainActionActivity.class));
            else
                startActivity(new Intent(this, MainActionActivity.class));
        }else{//登录
            startActivity(new Intent(this, LoginActionActivity.class));
        }*/
        finish();
    }
}

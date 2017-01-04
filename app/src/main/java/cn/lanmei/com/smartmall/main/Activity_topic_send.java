package cn.lanmei.com.smartmall.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import cn.lanmei.com.smartmall.customization.F_custom_right;


public class Activity_topic_send extends BaseActionActivity {
    private F_custom_right fCustomRight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {

        fCustomRight=F_custom_right.newInstance(0);
        setMContentView(fCustomRight);
    }

    @Override
    public void mfindViewById() {
        txtLeft.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fCustomRight.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setTitle(String title) {
        setHeadCentertText(title);
    }

    @Override
    public void showFrament(int casePositon) {
        showFrament(casePositon,null);
    }

    @Override
    public void showFrament(int casePositon, Bundle data) {

    }

    @Override
    public void addFrament(Fragment fragment, String tag) {

    }

    @Override
    public void backFragment(String currentTag) {
        this.finish();
    }
}

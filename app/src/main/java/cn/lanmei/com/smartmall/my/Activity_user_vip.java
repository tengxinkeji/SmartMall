package cn.lanmei.com.smartmall.my;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_user_vip extends BaseActionActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {

        setMContentView(F_user_vip.newInstance());
    }

    @Override
    public void mfindViewById() {
        txtLeft.setVisibility(View.VISIBLE);

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

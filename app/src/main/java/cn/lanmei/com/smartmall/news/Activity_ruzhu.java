package cn.lanmei.com.smartmall.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActionActivity;

/**
 * 入驻指南
 * */
public class Activity_ruzhu extends BaseActionActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        setMContentView(R.layout.activity_ruzhu);

    }

    @Override
    public void mfindViewById() {
        setHeadCentertText("入驻指南");
    }

    @Override
    public void setTitle(String title) {

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

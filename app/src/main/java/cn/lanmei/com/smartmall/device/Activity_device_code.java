package cn.lanmei.com.smartmall.device;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_device_code extends BaseActionActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        setMContentView(new F_device_code());
    }

    @Override
    public void mfindViewById() {

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

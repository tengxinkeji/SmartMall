package cn.lanmei.com.smartmall.repair;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActionActivity;
import cn.lanmei.com.smartmall.my.F_repair;


public class Activity_repair extends BaseActionActivity {
    F_repair f_repair;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        f_repair=F_repair.newInstance();
        setMContentView(f_repair);
    }

    @Override
    public void mfindViewById() {
        txtLeft.setVisibility(View.VISIBLE);
        setHeadRightText(getResources().getString(R.string.refer));
    }

    @Override
    protected void onHeadRightButton(View v) {
        super.onHeadRightButton(v);
        Intent to=new Intent(this, Activity_repair_detail.class);
        startActivity(to);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        f_repair.onActivityResult(requestCode, resultCode, data);
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

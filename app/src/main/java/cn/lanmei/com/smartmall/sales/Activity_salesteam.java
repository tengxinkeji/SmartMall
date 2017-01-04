package cn.lanmei.com.smartmall.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.common.app.Common;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_salesteam extends BaseActionActivity {

    F_salesteam f_salesteam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        f_salesteam=new F_salesteam();
        setMContentView(f_salesteam);
    }

    @Override
    public void mfindViewById() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Common.NONE:
            case Common.PHOTO_PICK:
            case Common.PHOTO_GRAPH:
            case Common.PHOTO_RESOULT:
                f_salesteam.onActivityResult(requestCode, resultCode, data);
                break;
        }
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

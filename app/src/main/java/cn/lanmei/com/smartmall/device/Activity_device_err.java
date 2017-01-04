package cn.lanmei.com.smartmall.device;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.common.app.Common;
import com.common.app.degexce.L;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_device_err extends BaseActionActivity {
    String TAG="Activity_device_err";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent()!=null){
            String devNo = getIntent().getStringExtra(Common.DEV_device_no);
            String devCode = getIntent().getStringExtra(Common.DEV_device_fault);
            setMContentView(F_device_err.newInstance(devNo,devCode));
        }else {
            L.MyLog("",getIntent()+"");
            finish();
        }

    }

    @Override
    public void loadViewLayout() {

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

package cn.lanmei.com.smartmall.device;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.common.app.Common;
import com.espressif.iot.esptouch.EsptouchResult;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_device_scan extends BaseActionActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
//        setMContentView(R.layout.activity_connect_wifi);
        if (getIntent()!=null){
            Bundle bundle=getIntent().getBundleExtra(Common.KEY_bundle);
            if (bundle!=null){
                EsptouchResult esptouchResult = (EsptouchResult) bundle.getSerializable(Common.KEY_bundle);
                if (esptouchResult!=null){
                    setMContentView(F_device_scan.newInstance(esptouchResult.getBssid(),esptouchResult.getInetAddress()));
                    return;
                }
            }

        }
        setMContentView(new F_device_scan());

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

package cn.lanmei.com.smartmall.device;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.common.app.Common;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_device_manager_list extends BaseActionActivity {
    private String TAG="Activity_device_manager_list";
    F_device_manager_list f_device_manager_list;

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
//      setMContentView(R.layout.activity_connect_wifi);
        id= getIntent().getStringExtra(Common.KEY_bundle);
        f_device_manager_list=new F_device_manager_list(id);
        setMContentView(f_device_manager_list);
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

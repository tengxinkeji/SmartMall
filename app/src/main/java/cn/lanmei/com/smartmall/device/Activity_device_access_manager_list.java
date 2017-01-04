package cn.lanmei.com.smartmall.device;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.common.app.Common;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.main.BaseActionActivity;
import cn.lanmei.com.smartmall.model.M_DevSub;


public class Activity_device_access_manager_list extends BaseActionActivity {
    private String TAG="Activity_device_access_manager_list";
    F_device_access_manager device_access_manager;

    private String id;
    private List<M_DevSub> devSubs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
//      setMContentView(R.layout.activity_connect_wifi);
        Bundle bundle = getIntent().getBundleExtra(Common.KEY_bundle);
        id=bundle.getString(Common.KEY_id);
        List<M_DevSub> temp = (List<M_DevSub>)bundle.getSerializable(Common.KEY_bundle);

        devSubs = new ArrayList<>();
        for (M_DevSub item:temp){
            if (item.getDev().getMaster()!=1)
                devSubs.add(item);
        }
        device_access_manager=new F_device_access_manager(id,devSubs);
        setMContentView(device_access_manager);
    }

    @Override
    public void mfindViewById() {
        setHeadRightText("保存");
    }

    @Override
    protected void onHeadRightButton(View v) {
        super.onHeadRightButton(v);
        device_access_manager.updateAccess();
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

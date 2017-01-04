package cn.lanmei.com.smartmall.device;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.common.app.Common;
import com.common.app.degexce.L;
import com.demo.smarthome.device.Dev;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.dialog.DF_devices_reset;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterString;
import cn.lanmei.com.smartmall.dialog.PopWindow_List;
import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_device_detail extends BaseActionActivity {
    private String TAG="Activity_device_detail";
    F_device_detail f_device_detail;

    private Dev mDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
//        setMContentView(R.layout.activity_connect_wifi);
        mDevice= (Dev) getIntent().getBundleExtra(Common.KEY_bundle).getSerializable(Common.KEY_bundle);
        f_device_detail=new F_device_detail(mDevice);
        setMContentView(f_device_detail);
    }

    @Override
    public void mfindViewById() {
        if (mDevice.getMaster()==1){
            setHeadRightImg(R.mipmap.icon_menu);
        }else {
            setHeadRightText("解除绑定");
        }

    }

    @Override
    protected void onHeadRightButton(View v) {
        super.onHeadRightButton(v);
        if (mDevice.getMaster()==1){
            List<String> lists=new ArrayList<String>();
            lists.add("保存");
            lists.add("添加成员");
            lists.add("解除绑定");
            lists.add("重置设备");
            new PopWindow_List<AdapterString>(this,
                    new AdapterString(this, lists),
                    new PopWindow_List.PopWindowItemClick() {
                        @Override
                        public void onPopWindowItemClick(int position) {
                            L.MyLog(TAG,position+"");
                            switch (position){
                                case 0:
                                    f_device_detail.saveDevice();
                                    break;
                                case 1:
                                    f_device_detail.addDeviceManage();
                                    break;
                                case 2:
                                    f_device_detail.delDevice();
                                    break;
                                case 3:
                                    new DF_devices_reset(mDevice).show(fm,"DF_devices_reset");
                                    break;
                            }
                        }
                    }).showPopupWindow(v);

        }else {
            f_device_detail.delDevice();
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

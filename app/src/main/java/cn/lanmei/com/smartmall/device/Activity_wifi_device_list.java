package cn.lanmei.com.smartmall.device;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.common.app.Common;
import com.espressif.iot.esptouch.EsptouchResult;

import java.util.List;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_wifi_device_list extends BaseActionActivity {
    private String TAG="Activity_wifi_device_connectbind";
    F_wifi_device_list f_wifi_device_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
//        setMContentView(R.layout.activity_connect_wifi);
        List<EsptouchResult> results = (List<EsptouchResult>) getIntent()
                .getBundleExtra(Common.KEY_bundle)
                .getSerializable(Common.KEY_bundle);
        f_wifi_device_list=new F_wifi_device_list(results);
        setMContentView(f_wifi_device_list);
    }

    @Override
    public void mfindViewById() {
//        setHeadRightImg(R.mipmap.icon_moshi_01);
    }

    @Override
    protected void onHeadRightButton(View v) {
        super.onHeadRightButton(v);

        /*List<String> lists=new ArrayList<String>();
        lists.add("保存");
        lists.add("添加成员");
        lists.add("解除绑定");
        new PopWindow_List<AdapterString>(this,
                new AdapterString(this, lists),
                new PopWindow_List.PopWindowItemClick() {
                    @Override
                    public void onPopWindowItemClick(int position) {
                        L.MyLog(TAG,position+"");
                    }
                }).showPopupWindow(v);*/
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

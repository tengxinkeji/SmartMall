package cn.lanmei.com.smartmall.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


/**收货地址*/
public class Activity_manager_address extends BaseActionActivity {
    F_list_address fListAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        fListAddress = new F_list_address();
        setMContentView(fListAddress);
        setHeadRightText("添加");
    }

    @Override
    public void mfindViewById() {

    }

    @Override
    protected void onHeadRightButton(View v) {
        super.onHeadRightButton(v);
        fListAddress.addrAddorModify(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fListAddress.onActivityResult(requestCode, resultCode, data);
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

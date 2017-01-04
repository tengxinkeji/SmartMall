package cn.lanmei.com.smartmall.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_list_order extends BaseActionActivity {
    F_list_order f_list_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        f_list_order=new F_list_order();
        setMContentView(f_list_order,"F_list_order");
    }

    @Override
    public void mfindViewById() {

    }

    public void refreshOrderList(){
        f_list_order.p=1;
        f_list_order.requestList();
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

package cn.lanmei.com.smartmall.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.common.app.Common;

import cn.lanmei.com.smartmall.main.BaseActionActivity;
import cn.lanmei.com.smartmall.model.M_Goods;


/**确认订单*/
public class Activity_order_ok extends BaseActionActivity {
    F_order_ok fOrderOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*StaticMethod.showInfo(this, getResources().getString(R.string.order_info));
        finish();*/
    }

    @Override
    public void loadViewLayout() {
        Intent intent= getIntent();
        if (intent!=null){
            Bundle data=intent.getBundleExtra(Common.KEY_bundle);
            if (data!=null){
                M_Goods mGoods= (M_Goods)data .getSerializable(Common.KEY_bundle);
                if (mGoods!=null){
                    fOrderOk = F_order_ok.newInstance(mGoods);
                }else {
                    fOrderOk = new F_order_ok();
                }
            }else {
                fOrderOk = new F_order_ok();
            }
        }

        setMContentView(fOrderOk);
    }

    @Override
    public void mfindViewById() {

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fOrderOk.onActivityResult(requestCode, resultCode, data);
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

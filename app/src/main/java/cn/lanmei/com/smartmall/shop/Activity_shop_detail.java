package cn.lanmei.com.smartmall.shop;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.common.app.Common;

import cn.lanmei.com.smartmall.main.BaseActionActivity;

/**商品首页*/
public class Activity_shop_detail extends BaseActionActivity {
    private int shopUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {

        if (getIntent()!=null){
            shopUid=getIntent().getIntExtra(Common.KEY_id,0);
            setMContentView(F_shop_detail.newInstance(shopUid));
        }else {
            finish();
        }

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

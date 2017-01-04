package cn.lanmei.com.smartmall.goods;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.common.app.Common;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


/**地址添加修改*/
public class Activity_address_addormodify extends BaseActionActivity {
    private int addrId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        if (getIntent()!=null)
            addrId=getIntent().getIntExtra(Common.KEY_id,0);
        setMContentView(F_address_modify.newInstance(addrId));
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

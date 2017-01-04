package cn.lanmei.com.smartmall.myuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.common.app.Common;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


/**银行卡管理*/
public class Activity_manager_bank extends BaseActionActivity {
    F_list_bank f_list_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        f_list_bank = new F_list_bank();
        setMContentView(f_list_bank);
        setHeadRightText("添加");
    }

    @Override
    public void mfindViewById() {

    }

    @Override
    protected void onHeadRightButton(View v) {
        super.onHeadRightButton(v);
        addBank();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        f_list_bank.onActivityResult(requestCode, resultCode, data);
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

    private void addBank(){
        Intent toAddBank=new Intent(this,Activity_bank_addormodify.class);
        toAddBank.putExtra(Common.KEY_id,0);
        startActivityForResult(toAddBank, Common.requestCode_bank_add);
    }
}

package cn.lanmei.com.smartmall.myuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.common.app.Common;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActionActivity;

/**我的账户*/
public class Activity_user_money extends BaseActionActivity {
    private TextView txtLeft;
    private TextView txtRight;
    private F_list_moneylog f_list_moneylog;
    private F_withdraw_deposit f_withdraw_deposit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        setMContentView(R.layout.activity_list_attention);
    }

    @Override
    public void mfindViewById() {
        txtLeft=(TextView) findViewById(R.id.txt_left);
        txtRight=(TextView) findViewById(R.id.txt_right);
        txtLeft.setText("账户明细");
        txtRight.setText("申请提现");

        txtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeft();

            }
        });
        txtRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRight();

            }
        });

        showLeft();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== Common.requestCode_bank_select
                &&resultCode==Common.resultCode_bank_select){//银行卡选择返回
            f_withdraw_deposit.onActivityResult(requestCode, resultCode, data);
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

    private void showLeft(){
        txtLeft.setBackgroundResource(R.drawable.bg_corners_left_true);
        txtLeft.setTextColor(ContextCompat.getColor(this,R.color.white));
        txtRight.setBackgroundResource(R.drawable.bg_corners_right_false);
        txtRight.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
        if (f_list_moneylog==null){
            f_list_moneylog= F_list_moneylog.newInstance();
        }
        fm.beginTransaction()
                .replace(R.id.layout_custom,f_list_moneylog,"F_list_moneylog")
                .commit();
    }
    private void showRight(){
        txtLeft.setBackgroundResource(R.drawable.bg_corners_left_false);
        txtLeft.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
        txtRight.setBackgroundResource(R.drawable.bg_corners_right_true);
        txtRight.setTextColor(ContextCompat.getColor(this,R.color.white));
        if (f_withdraw_deposit==null){
            f_withdraw_deposit= F_withdraw_deposit.newInstance();
        }
        fm.beginTransaction()
                .replace(R.id.layout_custom,f_withdraw_deposit,"F_withdraw_deposit")
                .commit();
    }
}

package cn.lanmei.com.smartmall.my;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_list_attention extends BaseActionActivity {
    private TextView txtLeft;
    private TextView txtRight;
    private F_list_shop f_list_shop;
    private F_list_fixshop f_list_fixshop;

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
        if (f_list_shop==null){
            f_list_shop= F_list_shop.newInstance();
        }
        fm.beginTransaction()
                .replace(R.id.layout_custom,f_list_shop,"F_list_shop")
                .commit();
    }
    private void showRight(){
        txtLeft.setBackgroundResource(R.drawable.bg_corners_left_false);
        txtLeft.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
        txtRight.setBackgroundResource(R.drawable.bg_corners_right_true);
        txtRight.setTextColor(ContextCompat.getColor(this,R.color.white));
        if (f_list_fixshop==null){
            f_list_fixshop= F_list_fixshop.newInstance();
        }
        fm.beginTransaction()
                .replace(R.id.layout_custom,f_list_fixshop,"F_list_fixshop")
                .commit();
    }
}

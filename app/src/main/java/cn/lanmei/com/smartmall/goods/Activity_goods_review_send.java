package cn.lanmei.com.smartmall.goods;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.common.app.Common;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_goods_review_send extends BaseActionActivity {
    private F_goods_review_send f_goods_review;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        if (getIntent()!=null){
            int goodsId=getIntent().getIntExtra(Common.KEY_id,0);
            f_goods_review=F_goods_review_send.newInstance(goodsId);
        }else {
            finish();
        }


        setMContentView(f_goods_review);
    }

    @Override
    public void mfindViewById() {
        txtLeft.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        f_goods_review.onActivityResult(requestCode, resultCode, data);
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

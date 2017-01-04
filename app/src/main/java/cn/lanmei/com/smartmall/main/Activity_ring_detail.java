package cn.lanmei.com.smartmall.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.common.app.Common;

import cn.lanmei.com.smartmall.model.M_ring;


public class Activity_ring_detail extends BaseActionActivity {
    F_ring_detail f_ring_detail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        M_ring ring=null;
        if (getIntent()!=null){
            ring=(M_ring)getIntent().getSerializableExtra(Common.KEY_bundle);

        }else {
            finish();
        }
        f_ring_detail=F_ring_detail.newInstance(ring);
        setMContentView(f_ring_detail);
    }

    @Override
    public void mfindViewById() {
        txtLeft.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        f_ring_detail.onActivityResult(requestCode, resultCode, data);
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

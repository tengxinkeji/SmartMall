package cn.lanmei.com.smartmall.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.common.app.Common;

import cn.lanmei.com.smartmall.model.M_topic;


public class Activity_topic_detail extends BaseActionActivity {
    private F_topic_detail f_topic_detail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        M_topic topic=null;
        if (getIntent()!=null){
            topic= (M_topic) getIntent().getSerializableExtra(Common.KEY_bundle);
        }

        f_topic_detail=F_topic_detail.newInstance(topic);
        setMContentView(f_topic_detail);
    }

    @Override
    public void mfindViewById() {
        txtLeft.setVisibility(View.VISIBLE);

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

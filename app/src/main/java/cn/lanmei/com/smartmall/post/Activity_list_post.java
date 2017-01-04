package cn.lanmei.com.smartmall.post;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_list_post extends BaseActionActivity {
    private F_list_post fListPost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {

        fListPost=F_list_post.newInstance();
        setMContentView(fListPost);
    }

    @Override
    public void mfindViewById() {
        txtLeft.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fListPost.onActivityResult(requestCode, resultCode, data);
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

package cn.lanmei.com.smartmall.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.common.app.Common;

import cn.lanmei.com.smartmall.model.M_news;
import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_news_detail extends BaseActionActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        if (getIntent()!=null){
            M_news news = (M_news) getIntent().getSerializableExtra(Common.KEY_bundle);
            setMContentView(F_news_details.newInstance(news),"F_news_details");
        }else {
            finish();
        }

    }

    @Override
    public void mfindViewById() {

    }

    @Override
    public void setTitle(String title) {
        setHeadCentertText("行业资讯");
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

package cn.lanmei.com.smartmall.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.common.app.Common;

import java.util.ArrayList;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.fragment.F_pic;


public class Activity_pic extends BaseActionActivity {
    private static ArrayList<String> urlList=new ArrayList<>();
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        if (getIntent()!=null){
            urlList=getIntent().getStringArrayListExtra(Common.KEY_listString);
            position=getIntent().getIntExtra(Common.KEY_position,0);
        }else {
            finish();
        }
        setMContentView(R.layout.activity_viewpager);
    }

    @Override
    public void mfindViewById() {
        setHeadShow(false);
        MyAdapter mAdapter = new MyAdapter(fm);
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        if (position>0)
            mPager.setCurrentItem(position);
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


    /**
     * 有状态的 ，只会有前3个存在 其他销毁， 前1个， 中间， 下一个
     */
    public static class MyAdapter extends FragmentStatePagerAdapter {


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return urlList.size();
        }

        //得到每个item
        @Override
        public Fragment getItem(int position) {
            return F_pic.newInstance(urlList.get(position));
        }


        // 初始化每个页卡选项
        @Override
        public Object instantiateItem(ViewGroup arg0, int arg1) {
            // TODO Auto-generated method stub
            return super.instantiateItem(arg0, arg1);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            System.out.println( "position Destory" + position);
            super.destroyItem(container, position, object);
        }

    }
}

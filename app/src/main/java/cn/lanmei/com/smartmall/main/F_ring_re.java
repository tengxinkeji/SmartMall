package cn.lanmei.com.smartmall.main;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.banner.tools.AbOnItemClickListener;
import com.common.banner.view.AbSlidingPlayView;
import com.common.datadb.DBManagerCategory;
import com.common.myinterface.DataCallBack;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pulltorefresh.library.PullToRefreshBase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterIcon_ring;
import cn.lanmei.com.smartmall.adapter.AdapterTopic;
import cn.lanmei.com.smartmall.categorygoods.ActionActivity_Goods_Category;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.model.M_ad;
import cn.lanmei.com.smartmall.model.M_ad_item;
import cn.lanmei.com.smartmall.model.M_ring;
import cn.lanmei.com.smartmall.model.M_topic;
import cn.lanmei.com.smartmall.my.Activity_chat;
import cn.lanmei.com.smartmall.parse.ParserAd;
import cn.lanmei.com.smartmall.parse.ParserRing;
import cn.lanmei.com.smartmall.parse.ParserTopic;
import cn.lanmei.com.smartmall.search.Activity_search;


/**
 *
 */
public class F_ring_re extends BaseScrollFragment {
    private String TAG="F_ring_re";

    private AbSlidingPlayView ad;


    private MyListView myListView;
    private GridView mGridView_Cate;
    private List<M_ring> categroys;
    private AdapterIcon_ring adapterIcon;

    private List<M_topic> mTopics;
    private AdapterTopic adapterTopic;

    private int adHeight;


    private int p=1;

    private Resources res;
    private DBManagerCategory dbManagerCategory;
    private List<M_ad_item> ads;

    public static F_ring_re newInstance() {
        F_ring_re fragment = new F_ring_re();

        return fragment;
    }



    @Override
    public void init(){
        res=getResources();
        tag=res.getString(R.string.menu_3);
        dbManagerCategory=new DBManagerCategory(mContext);
        mTopics=new ArrayList<>();
        adapterTopic=new AdapterTopic(mContext,mTopics);



        categroys=new ArrayList<>();
        int width= res.getDimensionPixelSize(R.dimen.img_category_width);
        adapterIcon=new AdapterIcon_ring(mContext,categroys,StaticMethod.dip2px(mContext,width));
    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_ring_recommend);
    }

    @Override
    public void findViewById() {
        setScrollViewMode(PullToRefreshBase.Mode.BOTH);
        initAdImg();
        mGridView_Cate = (GridView) findViewById(R.id.grid_categroy);
        myListView = (MyListView) findViewById(R.id.list_topic);


        horizontal_layout();
        mGridView_Cate.setAdapter(adapterIcon);
        mGridView_Cate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toList=new Intent(getActivity(), Activity_ring_detail.class);
                toList.putExtra(Common.KEY_bundle,categroys.get(position));

                getActivity().startActivity(toList);
            }
        });

        myListView.setAdapter(adapterTopic);


    }

    @Override
    public void requestServerData() {
        requestAd();
        requestCategory();
        refresh();
    }



    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }

    public void requestCategory(){


            RequestParams requestParams = new RequestParams(NetData.ACTION_post_list);
            requestParams.setBaseParser(new ParserRing());
            getDataFromServer(requestParams, new SimpleDataCallBack<List<M_ring>,Integer>(p){
                @Override
                public void onPre(Integer obj) {
                    super.onPre(obj);
                }

                @Override
                public void processData(Integer obj, List<M_ring> parserData) {
                    super.processData(obj, parserData);
                    if (obj==1)
                        categroys.clear();
                    if (parserData.size()>0)
                        p++;

                    categroys.addAll(parserData);
                    horizontal_layout();
                    adapterIcon.refreshData(categroys);

                }

                @Override
                public void onComplete(Integer obj) {
                    super.onComplete(obj);
                    mPullRefreshScrollView.onRefreshComplete();
                }
            });






    }

    /**刷新*/
    public void refresh() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_topic_list);
        requestParams.addParam("p",p);
        requestParams.setBaseParser(new ParserTopic());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_topic>,Integer>(1) {

            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_topic> parserData) {
                super.processData(obj, parserData);
                L.MyLog(TAG,"processData---flag:"+obj);
                if (obj==1)
                    mTopics.clear();
                if (parserData!=null&&parserData.size()>0){
                    mTopics.addAll(parserData);
                    p++;
                }
                adapterTopic.refreshData(mTopics);
            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                mPullRefreshScrollView.onRefreshComplete();
            }


        });

    }

    public void requestAd() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_adpic);
        requestParams.addParam("classid",6);
        requestParams.setBaseParser(new ParserAd());
        getDataFromServer(requestParams, new DataCallBack<M_ad>() {
            @Override
            public void onPre() {

            }

            @Override
            public void processData(M_ad parserData) {
                if (parserData!=null){
                    refreshAd(parserData.getAdImgUrl());
                    ads=parserData.getAds();
                }

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        p=1;
        requestServerData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);

        refresh();
    }

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.menu:
                    getActivity().startActivity(new Intent(getActivity(), ActionActivity_Goods_Category.class));
                    break;
                case R.id.chat:
                    boolean islogin = SharePreferenceUtil.getBoolean(Common.KEY_is_login, false);
                    if (!islogin){
                        Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                        startActivity(toLogin);
                        break;
                    }else {
                        Intent toHx=new Intent(getActivity(), Activity_chat.class);
                        //toHx.putExtra("index",getActivity() instanceof MainActionActivity);
                        getActivity().startActivity(toHx);
                    }

                    break;

                case R.id.search:
                    getActivity().startActivity(new Intent(getActivity(), Activity_search.class));
                    break;

            }
        }
    };




    private void initAdImg(){
//        pointLayout=(LinearLayout) findViewById(R.id.menu0_layout_point);
//        adLayout = (RelativeLayout) findViewById(R.id.ad_layout);
        ad= (AbSlidingPlayView) findViewById(R.id.main_menu_0_ad);
        initViewPager(ad);
        ad.post(new Runnable() {
            @Override
            public void run() {
                adHeight = ad.getHeight(); //height is ready
                L.MyLog(TAG,"adHeight:"+adHeight);
            }
        });

    }

    /**广告图刷新*/
    private void refreshAd(List<String> allListView){
        ad.removeAllViews();
        ad.addViews(allListView);
    }

    private void initViewPager(AbSlidingPlayView viewPager) {
        int placeholder= MyApplication.getInstance().getPlaceholder();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeholder)
                .showImageForEmptyUri(placeholder)
                .showImageOnFail(placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        int itemWidth = StaticMethod.dip2px(getContext(),170);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, itemWidth);
        viewPager.setLayoutParams(params);
        //设置播放方式为顺序播放
        viewPager.setPlayType(1);
        //设置播放间隔时间
        viewPager.setSleepTime(3000);

        viewPager.setImgListener(options,  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public File onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                return null;
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        List<String> allListView = new ArrayList<String>();
        allListView.add("drawable://" + R.drawable.img_logo);



//        for (int i = 0; i < 3; i++) {
//            //导入ViewPager的布局
//
//            allListView.add("http://img2.3lian.com/2014/f7/5/d/2" + i + ".jpg");
//        }


        viewPager.addViews(allListView);
        //开始轮播
        viewPager.startPlay();
        viewPager.setOnItemClickListener(new AbOnItemClickListener() {
            @Override
            public void onClick(int position) {
                //跳转到详情界面
//				Intent intent = new Intent(getActivity(), BabyActivity.class);
//				startActivity(intent);
                Log.i("广告位置：",position+"");
                if (ads!=null&&ads.size()>0){
                    F_index_recommend.toAdDetails((BaseActivity)mContext,ads.get(position));
                }
            }
        });
    }
    //gridview横向布局方法
    public void horizontal_layout(){
        int size = categroys.size();
        int width=res.getDimensionPixelSize(R.dimen.img_category_width)+20;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int allWidth = (int) (width * size * density);
        int itemWidth = (int) (width * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        mGridView_Cate.setLayoutParams(params);// 设置GirdView布局参数
        mGridView_Cate.setColumnWidth(itemWidth);// 列表项宽
        mGridView_Cate.setHorizontalSpacing(10);// 列表项水平间距
        mGridView_Cate.setStretchMode(GridView.NO_STRETCH);
        mGridView_Cate.setNumColumns(size);//总长度
    }

}

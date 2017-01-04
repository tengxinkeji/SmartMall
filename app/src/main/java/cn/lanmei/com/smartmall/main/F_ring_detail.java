package cn.lanmei.com.smartmall.main;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.banner.tools.AbOnItemClickListener;
import com.common.banner.view.AbSlidingPlayView;
import com.common.myinterface.DataCallBack;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.CircleImageView;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterSimTopic;
import cn.lanmei.com.smartmall.adapter.AdapterTopic;
import cn.lanmei.com.smartmall.model.M_ad;
import cn.lanmei.com.smartmall.model.M_ring;
import cn.lanmei.com.smartmall.model.M_topic;
import cn.lanmei.com.smartmall.parse.ParserAd;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.parse.ParserTopic;


/**
 *
 */
public class F_ring_detail extends BaseScrollFragment {
    private String TAG="F_ring_detail";

    private AbSlidingPlayView ad;
    private CircleImageView imgTopic;
    private TextView txtTopicName;
    private TextView txtTopicNum;
    private TextView txtTopicReNum;
    private ImageView imgAtte;
    private TextView txtTopicAll;
    private TextView txtTopicEssential;
    private View viewIndicator;

    private MyListView myListViewTopic;
    private MyListView myListView;

    private List<M_topic> mTopics;
    private AdapterTopic adapterTopic;

    private List<M_topic> mTopicsAll;
    private AdapterSimTopic adapterTopicAll;

    private int adHeight;


    private int p=1;
    private M_ring mRing;
    private Resources res;
    private int recommend=0;

    public static F_ring_detail newInstance(M_ring mring) {
        F_ring_detail fragment = new F_ring_detail();
        Bundle data=new Bundle();
        data.putSerializable("mRing",mring);

        fragment.setArguments(data);

        return fragment;
    }



    @Override
    public void init(){
        res=getResources();
        tag=res.getString(R.string.menu_3);
        if(getArguments()!=null){
            mRing=(M_ring) getArguments().getSerializable("mRing");
            tag=mRing.getName();
        }

        mTopics=new ArrayList<>();
        adapterTopic=new AdapterTopic(mContext,mTopics);

        mTopicsAll=new ArrayList<>();
        adapterTopicAll=new AdapterSimTopic(mContext,mTopicsAll);
    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_ring_detail);
    }

    @Override
    public void findViewById() {
        setScrollViewMode(PullToRefreshBase.Mode.BOTH);
        initAdImg();
        imgTopic = (CircleImageView) findViewById(R.id.img_logo);
        refreshImg(mRing.getUrl(),imgTopic);
        txtTopicName = (TextView) findViewById(R.id.txt_topic_name);
        txtTopicName.setText(mRing.getName());
        txtTopicNum = (TextView) findViewById(R.id.txt_ring_topic_num);
        txtTopicNum.setText(mRing.getPost_count()+"");
        txtTopicReNum = (TextView) findViewById(R.id.txt_ring_retopic_num);
        txtTopicReNum.setText(mRing.getReviews_count()+"");
        imgAtte = (ImageView) findViewById(R.id.txt_topic_atte);
        txtTopicAll = (TextView) findViewById(R.id.txt_topic_all);
        txtTopicAll.setSelected(true);
        txtTopicEssential = (TextView) findViewById(R.id.txt_topic_essential);
        viewIndicator = findViewById(R.id.view_indicator);

        myListViewTopic = (MyListView) findViewById(R.id.list_topic_essential);
        myListView = (MyListView) findViewById(R.id.list_topic);

        imgAtte.setOnClickListener(onClickListener);
        txtTopicAll.setOnClickListener(onClickListener);
        txtTopicEssential.setOnClickListener(onClickListener);



        myListViewTopic.setAdapter(adapterTopicAll);
        myListView.setAdapter(adapterTopic);
    }

    @Override
    public void requestServerData() {
        requestAd();
        refresh();
        refreshTopicTop();
    }



    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }

    /**recommend int 1、精华 2、最热 3、置顶 4、推荐*/
    public void refresh() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_topic_list);
        requestParams.addParam("cid",mRing.getId());
        requestParams.addParam("p",p);
        if (recommend>0)
            requestParams.addParam("recommend",recommend);
        requestParams.setBaseParser(new ParserTopic());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_topic>,Integer>(p) {

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
    /**recommend int 1、精华 2、最热 3、置顶 4、推荐*/
    public void refreshTopicTop() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_topic_list);
        requestParams.addParam("cid",mRing.getId());
        requestParams.addParam("recommend",3);
        requestParams.setBaseParser(new ParserTopic());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_topic>,Integer>(p) {

            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_topic> parserData) {
                super.processData(obj, parserData);
                mTopicsAll.clear();
                mTopicsAll.addAll(parserData);
                adapterTopicAll.refreshData(mTopicsAll);
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
                if (parserData!=null)
                    refreshAd(parserData.getAdImgUrl());
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
        refresh();
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

                case R.id.txt_topic_atte:
                    attentionPost(mRing.getId());
                    break;
                case R.id.txt_topic_all:
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            txtTopicAll.getWidth(),StaticMethod.dip2px(mContext,2));
                    lp.leftMargin=txtTopicAll.getLeft();
                    viewIndicator.setLayoutParams(lp);
                    txtTopicAll.setSelected(true);
                    txtTopicEssential.setSelected(false);
                    p=1;
                    recommend=0;
                    refresh();
                    break;
                case R.id.txt_topic_essential:
                    LinearLayout.LayoutParams lpE = new LinearLayout.LayoutParams(
                            txtTopicEssential.getWidth(),StaticMethod.dip2px(mContext,2));
                    lpE.leftMargin=txtTopicEssential.getLeft();
                    viewIndicator.setLayoutParams(lpE);
                    txtTopicAll.setSelected(false);
                    txtTopicEssential.setSelected(true);
                    p=1;
                    recommend=1;
                    refresh();
                    break;

            }
        }
    };


    private void attentionPost(int id){

        RequestParams requestParams = new RequestParams(NetData.ACTION_post_do_follow);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("cid",id);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new SimpleDataCallBack<JSONObject,Integer>(id) {
            @Override
            public void processData(Integer obj, JSONObject parserData) {
                super.processData(obj, parserData);
                if (parserData!=null){
                    try {
                        StaticMethod.showInfo(mContext,parserData.getString("info"));
                        if (parserData.getInt("status")==1){


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

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
        allListView.add("drawable://" + R.mipmap.launch_1);
        allListView.add("drawable://" + R.mipmap.launch_2);
        allListView.add("drawable://" + R.mipmap.launch_3);


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
            }
        });
    }


}

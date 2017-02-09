package cn.lanmei.com.smartmall.main;


import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.common.myui.MyGridView;
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
import cn.lanmei.com.smartmall.adapter.AdapterGoods;
import cn.lanmei.com.smartmall.adapter.AdapterImg_1;
import cn.lanmei.com.smartmall.categorygoods.ActionActivity_Goods_Category;
import cn.lanmei.com.smartmall.device.Activity_dev;
import cn.lanmei.com.smartmall.goods.ActivityGoodsDetail_2;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.model.M_ad;
import cn.lanmei.com.smartmall.model.M_ad_item;
import cn.lanmei.com.smartmall.my.Activity_chat;
import cn.lanmei.com.smartmall.repair.Activity_repair;
import cn.lanmei.com.smartmall.parse.ParserAd;
import cn.lanmei.com.smartmall.parse.ParserGoods;
import cn.lanmei.com.smartmall.parse.ParserGoodsCategory;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.search.Activity_list_goods;
import cn.lanmei.com.smartmall.search.Activity_search;
import cn.lanmei.com.smartmall.search.F_list_goods;


/**
 *首页-推荐
 *
 */
public class
F_index extends BaseScrollFragment {

    private String TAG="F_index_recommend";
    private AbSlidingPlayView ad;
    private View vHead;
    private ImageView imgMenu;
    private TextView editSearch;
    private ImageView imgChat;
    private TextView txtChatCount;

    private LinearLayout[] layoutIndex;
    private MyGridView mGridView_Re;
    private MyGridView mGridView;


    private List<String> cateRe;
    private List<Integer> cateReId;
    private AdapterImg_1 adapterImg;

    private List<M_Goods> goodsList;
    private AdapterGoods adapterGoods;

    private int adHeight;
    private List<M_ad_item> ads;
    private List<M_ad_item> adsRe;

    private Resources res;
    private DBManagerCategory dbManagerCategory;


    @Override
    public  void init(){
        res = mContext.getResources();
        tag = res.getString(R.string.menu_1);

        dbManagerCategory=new DBManagerCategory(mContext);

        cateRe=new ArrayList<>();
        cateReId=new ArrayList<>();
        int w=StaticMethod.wmWidthHeight(mContext)[0];
        adapterImg=new AdapterImg_1(mContext,cateRe,w,w*9/20);
        ads=new ArrayList<>();
        adsRe=new ArrayList<>();
    }


    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_index);
        vHead = LayoutInflater.from(mContext).inflate(R.layout.layout_search, layoutHead,false);
        vHead.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        vHead.getBackground().setAlpha(0);
        loadHeadViewLayout(vHead);
    }

    @Override
    public void findViewById() {
        initAdImg();
        imgMenu = (ImageView)vHead. findViewById(R.id.menu);
        editSearch = (TextView) vHead. findViewById(R.id.search);
        imgChat = (ImageView) vHead. findViewById(R.id.chat);
        txtChatCount= (TextView)vHead. findViewById(R.id.txt_chatcount);

        layoutIndex=new LinearLayout[4];
        layoutIndex[0]= (LinearLayout) findViewById(R.id.index_goods);
        layoutIndex[1]= (LinearLayout) findViewById(R.id.index_sales);
        layoutIndex[2]= (LinearLayout) findViewById(R.id.index_repair);
        layoutIndex[3]= (LinearLayout) findViewById(R.id.index_dev);

        mGridView_Re = (MyGridView) findViewById(R.id.grid_index_rec);
        mGridView = (MyGridView) findViewById(R.id.gridview);

        imgMenu.setOnClickListener(onClickListener);
        imgChat.setOnClickListener(onClickListener);
        editSearch.setOnClickListener(onClickListener);
        for (int i=0;i<layoutIndex.length;i++){
            layoutIndex[i].setOnClickListener(onClickListener);
        }



        mGridView_Re.setAdapter(adapterImg);
        mGridView_Re.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toAdDetails((BaseActivity)mContext,adsRe.get(position));
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toDetail=new Intent(getActivity(),ActivityGoodsDetail_2.class);
                toDetail.putExtra(Common.KEY_id,goodsList.get(position).getRecordId());
                getActivity().startActivity(toDetail);
            }
        });
        goodsList=new ArrayList<>();
        adapterGoods=new AdapterGoods(mContext,goodsList);
        mGridView.setAdapter(adapterGoods);
        mGridView_Re.setFocusable(false);
        mGridView.setFocusable(false);
    }

    @Override
    public void requestServerData() {

        requestAd();
        requestAd_re();
        requestCategory();
        refresh();

    }


    @Override
    public void onScrollChangedListener(int l, int t, int oldl, int oldt) {
        super.onScrollChangedListener(l, t, oldl, oldt);
        if (t>0&&t<adHeight){
            vHead.getBackground().setAlpha(t*255/adHeight);
        }else if (t<=0){
            vHead.getBackground().setAlpha(0);
        }else if(t>=adHeight){
            vHead.getBackground().setAlpha(255);
        }



    }

    public void refreshChatCount(int count){
        if (count<1){
            txtChatCount.setVisibility(View.GONE);
        }else {
            txtChatCount.setText(count+"");
            txtChatCount.setVisibility(View.VISIBLE);
        }
    }

    public void requestCategory(){
        RequestParams requestParams = new RequestParams(NetData.ACTION_category_isupdate);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {

            }

            @Override
            public void processData(JSONObject parserData) {
                if (parserData!=null){
                    try {
                        if (parserData.getInt("status")==1){
                            JSONObject data = parserData.optJSONObject("data");
                            long time = data.getLong("timestamp")*1000;
                            long upTime = SharePreferenceUtil.getLong(Common.KEY_category_uptime, 0l);
                            if (upTime<time){
                                updateCategory();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
            }
        });


    }

    public void updateCategory(){
        RequestParams requestParams = new RequestParams(NetData.ACTION_Shop_category);
        requestParams.setBaseParser(new ParserGoodsCategory());
        getDataFromServer(requestParams, new DataCallBack<List<ContentValues>>() {
            @Override
            public void onPre() {

            }

            @Override
            public void processData(List<ContentValues> parserData) {
                dbManagerCategory .updateData(parserData);
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
            }
        });


    }

    /**刷新*/
    public void refresh() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_Shop_goods_list);
        requestParams.addParam("type",4);
        requestParams.setBaseParser(new ParserGoods());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_Goods>,Integer>(1) {

            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_Goods> parserData) {
                super.processData(obj, parserData);
                L.MyLog(TAG,"processData---flag:"+obj);
                goodsList.clear();
                goodsList.addAll(parserData);
                adapterGoods.refreshData(goodsList);
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
        requestParams.addParam("classid",2);
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

    public void requestAd_re() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_adpic);
        requestParams.addParam("classid",5);
        requestParams.setBaseParser(new ParserAd());
        getDataFromServer(requestParams, new DataCallBack<M_ad>() {
            @Override
            public void onPre() {

            }

            @Override
            public void processData(M_ad parserData) {
                if (parserData!=null){
                    cateRe=parserData.getAdImgUrl();
                    cateReId=parserData.getAdImgId();
                    adapterImg.refreshData(cateRe);
                    adsRe=parserData.getAds();
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
        requestServerData();
    }




    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle data;
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
                case R.id.index_goods:
                    Intent toGoods=new Intent(getActivity(),Activity_list_goods.class);
                    data=new Bundle();
                    data.putInt(F_list_goods.KEY_type,Activity_list_goods.TYPE_category);
                    data.putInt(F_list_goods.KEY_parentId,0);
//                    data.putString(F_list_goods.KEY_key,key);
                    toGoods.putExtra(Common.KEY_bundle,data);
                    getActivity().startActivity(toGoods);
                    break;
                case R.id.index_sales:
                    Intent toSales=new Intent(getActivity(),Activity_list_goods.class);
                    data=new Bundle();
                    data.putInt(F_list_goods.KEY_type,Activity_list_goods.TYPE_sale);
                    data.putInt(F_list_goods.KEY_parentId,0);
//                    data.putString(F_list_goods.KEY_key,key);
                    toSales.putExtra(Common.KEY_bundle,data);
                    getActivity().startActivity(toSales);
                    break;
                case R.id.index_repair:
                    if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                        Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                        startActivity(toLogin);
                        break;
                    }
                    getActivity().startActivity(new Intent(getActivity(), Activity_repair.class));
                    break;
                case R.id.index_dev:
                    if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                        Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                        startActivity(toLogin);
                        break;
                    }
                    getActivity().startActivity(new Intent(getActivity(), Activity_dev.class));
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

        int itemWidth = StaticMethod.wmWidthHeight(mContext)[0]*9/16;
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
                    toAdDetails((BaseActivity)mContext,ads.get(position));
                }
            }
        });
    }



    public static void toAdDetails(BaseActivity baseActivity,M_ad_item adItem){
        String id="0";
        Intent intent;
        if (adItem.getLink().startsWith("http")){
            intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(adItem.getLink());
            intent.setData(content_url);
            baseActivity.startActivity(intent);

        }else if (adItem.getLink().startsWith("c_")){//商品分类
            id=adItem.getLink().substring(2);
            intent=new Intent(baseActivity,Activity_list_goods.class);
            Bundle data=new Bundle();
            data.putInt(F_list_goods.KEY_type,Activity_list_goods.TYPE_category);
            data.putInt(F_list_goods.KEY_parentId,Integer.parseInt(id));
            data.putString(F_list_goods.KEY_key,"");
            intent.putExtra(F_list_goods.KEY_key,"");
            intent.putExtra(Common.KEY_bundle,data);
            baseActivity.startActivity(intent);

        }else if (adItem.getLink().startsWith("g_")){//商品id
            id=adItem.getLink().substring(2);
            intent=new Intent(baseActivity,ActivityGoodsDetail_2.class);
            intent.putExtra(Common.KEY_id,Integer.parseInt(id));
            baseActivity.startActivity(intent);

        }else if (adItem.getLink().startsWith("p_")){//帖子id
            id=adItem.getLink().substring(2);
            intent=new Intent(baseActivity, Activity_topic_detail.class);
            intent.putExtra(Common.KEY_bundle,Integer.parseInt(id));

            baseActivity.startActivity(intent);

        }else if (adItem.getLink().startsWith("pc_")){//圈子id
            id=adItem.getLink().substring(3);
            intent=new Intent(baseActivity, Activity_ring_detail.class);
            intent.putExtra(Common.KEY_bundle,Integer.parseInt(id));
            baseActivity.startActivity(intent);

        }else if (adItem.getLink().startsWith("#1")){//会员商品
            intent=new Intent(baseActivity,Activity_list_goods.class);
            Bundle data=new Bundle();
            data.putInt(F_list_goods.KEY_type,Activity_list_goods.TYPE_sale);
            data.putInt(F_list_goods.KEY_parentId,Integer.parseInt(id));
            data.putString(F_list_goods.KEY_key,"");
            intent.putExtra(F_list_goods.KEY_key,"");
            intent.putExtra(Common.KEY_bundle,data);
            baseActivity.startActivity(intent);

        }else if (adItem.getLink().startsWith("#2")){//促销商品
            intent=new Intent(baseActivity,Activity_list_goods.class);
            Bundle data=new Bundle();
            data.putInt(F_list_goods.KEY_type,Activity_list_goods.TYPE_Vgoods);
            data.putInt(F_list_goods.KEY_parentId,Integer.parseInt(id));
            data.putString(F_list_goods.KEY_key,"");
            intent.putExtra(F_list_goods.KEY_key,"");
            intent.putExtra(Common.KEY_bundle,data);
            baseActivity.startActivity(intent);

        }


    }
}

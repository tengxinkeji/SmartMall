package cn.lanmei.com.smartmall.shop;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.banner.tools.AbOnItemClickListener;
import com.common.banner.view.AbSlidingPlayView;
import com.common.myinterface.DataCallBack;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyGridView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterGoods;
import cn.lanmei.com.smartmall.goods.ActivityGoodsDetail_2;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.parse.ParserGoods;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *商品首页
 */
public class F_shop_index extends BaseScrollFragment {
    private String TAG="F_shop_index";
    private EditText eSearch;
    private TextView txtSearchOk;
    private ImageView imgShopBg;
    private ImageView imgShop;
    private TextView txtShopName;
    private TextView txtShopNum;
    private LinearLayout layoutAttention;
    private ImageView imgAttStatus;
    private TextView txtTabs_1;
    private TextView txtTabs_2;
    private TextView txtTabs_3;
    private AbSlidingPlayView ad;
    private int adHeight;
    private MyGridView myGridView;

    private Resources res;
    private Bundle data;
    private int shopUid;
    private List<M_Goods> categoryGoodses;
    private AdapterGoods adapterGoods;
    private int p=1;
    /*1:最新商品 2:特色商品 3:热卖排行 4:推荐商品*/
    private int type=0;

    private boolean isCollect ;
    public String shopImgUrl;

    public static F_shop_index newInstance(int shopUid) {
        F_shop_index fragment = new F_shop_index();
        Bundle data=new Bundle();
        data.putInt("shopUid",shopUid);
        fragment.setArguments(data);
        return fragment;
    }



    @Override
    public void init(){

        res = mContext.getResources();
        tag = "商品首页";
        data = getArguments();
        if (data!=null) {
            shopUid=data.getInt("shopUid",0);
            L.MyLog(TAG,shopUid+"");
        }
        categoryGoodses=new ArrayList<>();
        adapterGoods=new AdapterGoods(mContext,categoryGoodses);
    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_shop_index);
        p=1;
        requestCategoryGoods();
    }

    @Override
    public void findViewById() {
        setScrollViewMode(PullToRefreshBase.Mode.BOTH);
        initAdImg();
        eSearch=(EditText) findViewById(R.id.search);
        txtSearchOk=(TextView) findViewById(R.id.txt_search);
        imgShopBg=(ImageView) findViewById(R.id.img_shop_bg);
        imgShop=(ImageView) findViewById(R.id.img_shop);
        txtShopName=(TextView) findViewById(R.id.txt_shop_name);
        txtShopNum=(TextView) findViewById(R.id.txt_shop_num);
        layoutAttention=(LinearLayout) findViewById(R.id.layout_shop_attention);
        imgAttStatus=(ImageView) findViewById(R.id.img_shop_attention);
        txtTabs_1=(TextView) findViewById(R.id.txt_tabs_1);
        txtTabs_2=(TextView) findViewById(R.id.txt_tabs_2);
        txtTabs_3=(TextView) findViewById(R.id.txt_tabs_3);
        myGridView=(MyGridView) findViewById(R.id.gridview);
        myGridView.setAdapter(adapterGoods);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toDetail=new Intent(getActivity(),ActivityGoodsDetail_2.class);
                toDetail.putExtra(Common.KEY_bundle,categoryGoodses.get(position).getRecordId());
                getActivity().startActivity(toDetail);
            }
        });
        txtTabs_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=0;
                txtTabs_1.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_bar));
                txtTabs_2.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));
                txtTabs_3.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));
                p=1;
                requestCategoryGoods();
            }
        });

        txtTabs_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=1;
                txtTabs_2.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_bar));
                txtTabs_1.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));
                txtTabs_3.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));
                p=1;
                requestCategoryGoods();
            }
        });
        txtTabs_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=3;
                txtTabs_3.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_bar));
                txtTabs_2.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));
                txtTabs_1.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));
                p=1;
                requestCategoryGoods();
            }
        });
        layoutAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectGoods();
            }
        });

        imgShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDetail=new Intent(getActivity(),Activity_shop_detail.class);
                toDetail.putExtra(Common.KEY_id,shopUid);
                getActivity().startActivity(toDetail);
            }
        });
    }

    @Override
    public void requestServerData() {
        requestShopInfo();

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        p=1;
        requestCategoryGoods();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        requestCategoryGoods();
    }

    public void requestShopInfo(){
        RequestParams requestParams = new RequestParams(NetData.ACTION_shop_info);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("mid",shopUid);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {

            }

            @Override
            public void processData(JSONObject parserData) {
                parserInfo(parserData);
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
            }
        });


    }

    /**刷新*/
    private void requestCategoryGoods(){

        RequestParams requestParams = new RequestParams(NetData.ACTION_Shop_goods_list);
        requestParams.addParam("mid",shopUid);
        requestParams.addParam("type",type);
        requestParams.addParam("p",p);
        requestParams.setBaseParser(new ParserGoods());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_Goods>,Integer>(p){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_Goods> parserData) {
                super.processData(obj, parserData);
                if (obj==1)
                    categoryGoodses.clear();
                if (parserData!=null&&parserData.size()>0){
                    categoryGoodses.addAll(parserData);
                    p++;
                }
                adapterGoods.refreshData(categoryGoodses);

            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                mPullRefreshScrollView.onRefreshComplete();
            }
        });


    }

    /**关注商店*/
    public void collectGoods(){
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_follow);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("id",shopUid);
        requestParams.addParam("type",0);
        requestParams.addParam("act",isCollect?2:1);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {
                startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                try {
                    StaticMethod.showInfo(mContext,parserData.getString("info"));
                    if (parserData.getInt("status")==1){
                        isCollect=!isCollect;
                        imgAttStatus.setImageResource(isCollect
                                ?R.drawable.icon_attention_yes:R.drawable.icon_attention_no);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onComplete() {
                stopProgressDialog();
            }
        });
    }

    private void parserInfo(JSONObject parserData) {
        if (parserData==null)
            return;
        try {
            if (parserData.getInt("status")==1){
                JSONObject data=parserData.optJSONObject("data");
                if (data==null)
                    return;
                shopImgUrl=data.getString("logo");
                refreshImg(imgShopBg,data.getString("bg"));
                refreshImg(imgShop,shopImgUrl);


                txtShopName.setText(data.getString("name"));
                txtShopNum.setText("在售商品"+data.getString("goodsTotal")+"件");
                isCollect=data.getInt("follow")==1;
                imgAttStatus.setImageResource(data.getInt("follow")==1
                        ?R.drawable.icon_attention_yes:R.drawable.icon_attention_no);
                txtTabs_1.setText("全部\n（"+data.getString("goodsTotal")+"）");
                txtTabs_2.setText("上新\n（"+data.getString("goodsNew")+"）");
                txtTabs_3.setText("热销\n（"+data.getString("goodsHot")+"）");
                String pic = data.getString("pic");
                if (!TextUtils.isEmpty(pic)){
                    List<String> pics=new ArrayList<>();
                    if (pic.contains(",")){
                        String[] item = pic.split(",");
                        for (String picI:item)
                            pics.add(picI);
                    }else {
                        pics.add(pic) ;
                    }
                    refreshAd(pics);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
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
        allListView.add("drawable://" + R.drawable.img_bj);


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


    public void refreshImg(ImageView img,String imgPath) {
        if (!TextUtils.isEmpty(imgPath)&&!TextUtils.equals(imgPath,"null"))
            ((BaseActivity) getActivity()).getImageLoader().displayImage(
                    imgPath, img, ((BaseActivity) getActivity()).options, new SimpleImageLoadingListener());
    }
}

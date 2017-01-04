package cn.lanmei.com.smartmall.shop;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.dialog.DF_ewm;
import cn.lanmei.com.smartmall.model.M_news;
import cn.lanmei.com.smartmall.news.Activity_news_detail;


/**
 *商品首页
 */
public class F_shop_detail extends BaseScrollFragment {
    private String TAG="F_shop_index";

    private ImageView imgShopBg;
    private ImageView imgShop;
    private TextView txtShopName;
    private TextView txtShopNum;
    private LinearLayout layoutAttention;
    private ImageView imgAttStatus;

    private AbSlidingPlayView ad;
    private int adHeight;

    private TextView[] txtInfos;

    private Resources res;
    private Bundle data;
    private int shopUid;
    private M_news mNews;


    private String shopImgUrl;
    private boolean isCollect ;

    public static F_shop_detail newInstance(int shopUid) {
        F_shop_detail fragment = new F_shop_detail();
        Bundle data=new Bundle();
        data.putInt("shopUid",shopUid);
        fragment.setArguments(data);
        return fragment;
    }



    @Override
    public void init(){

        res = mContext.getResources();
        tag = "商店详情";
        data = getArguments();
        if (data!=null) {
            shopUid=data.getInt("shopUid",0);
            L.MyLog(TAG,shopUid+"");
        }

    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_shop_detail);

    }

    @Override
    public void findViewById() {
//        setScrollViewMode(PullToRefreshBase.Mode.BOTH);
        initAdImg();

        imgShopBg=(ImageView) findViewById(R.id.img_shop_bg);
        imgShop=(ImageView) findViewById(R.id.img_shop);
        txtShopName=(TextView) findViewById(R.id.txt_shop_name);
        txtShopNum=(TextView) findViewById(R.id.txt_shop_num);
        layoutAttention=(LinearLayout) findViewById(R.id.layout_shop_attention);
        imgAttStatus=(ImageView) findViewById(R.id.img_shop_attention);

        txtInfos=new TextView[11];
        txtInfos[0]=(TextView) findViewById(R.id.txt_shop_ewm);
        txtInfos[1]=(TextView) findViewById(R.id.txt_shop_detail);
        txtInfos[2]=(TextView) findViewById(R.id.txt_shop_info_name);
        txtInfos[3]=(TextView) findViewById(R.id.txt_shop_info_time_start);
        txtInfos[4]=(TextView) findViewById(R.id.txt_shop_info_time_end);
        txtInfos[5]=(TextView) findViewById(R.id.txt_shop_info_call);
        txtInfos[6]=(TextView) findViewById(R.id.txt_shop_authnum);
        txtInfos[7]=(TextView) findViewById(R.id.txt_shop_tax);
        txtInfos[8]=(TextView) findViewById(R.id.txt_shop_deadline);
        txtInfos[9]=(TextView) findViewById(R.id.txt_shop_principal);
        txtInfos[10]=(TextView) findViewById(R.id.txt_shop_type);

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

        txtInfos[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String url=NetData.HOST+NetData.ACTION_qrcode+"&act=2&uid="+ shopUid;
                DF_ewm dfEwm=new DF_ewm(url,shopImgUrl,true);
                dfEwm.show(getChildFragmentManager(),"DF_ewm");
            }
        });

        txtInfos[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNews!=null){
                    Intent toDetail=new Intent(getActivity(),Activity_news_detail.class);
                    toDetail.putExtra(Common.KEY_bundle,mNews);
                    getActivity().startActivity(toDetail);
                }

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

        requestShopInfo();
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

                txtInfos[2].setText(data.getString("name"));
                txtInfos[3].setText(data.getString("online_time"));
                txtInfos[4].setText(data.getString("offline_time"));
                txtInfos[5].setText(data.getString("tel"));
                txtInfos[6].setText(data.getString("license"));
                txtInfos[7].setText(data.getString("tax_nu"));
                txtInfos[8].setText(data.getString("shopkeeper"));
                txtInfos[9].setText(data.getString("name"));
                txtInfos[10].setText(data.getString("company_type"));

                mNews=new M_news(data.getString("name"),data.getString("desc"));
                mNews.setTime(data.getString("addtime"));

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

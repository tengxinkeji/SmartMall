package cn.lanmei.com.smartmall.shop;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.myui.htmltext.HtmlTextView;
import cn.lanmei.com.smartmall.myui.htmltext.OnHtmlImgListener;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *商品首页
 */
public class F_fixshop_index extends BaseScrollFragment implements OnHtmlImgListener{
    private String TAG="F_shop_index";

    private AbSlidingPlayView ad;
    private TextView txtName;
    private TextView txtVip;
    private TextView txtAddr;
    private TextView txtCall;
    private HtmlTextView txtDetail;

    private String fixShopUid;
    private int adHeight=170;
    private boolean isCollect ;
    private String callNum;

    public static F_fixshop_index newInstance(String shopUid) {
        F_fixshop_index fragment = new F_fixshop_index();
        Bundle data=new Bundle();
        data.putString("shopUid",shopUid);
        fragment.setArguments(data);
        return fragment;
    }



    @Override
    public void init(){


        tag = "商品首页";
        Bundle data = getArguments();
        if (data!=null) {
            fixShopUid=data.getString("shopUid");
            L.MyLog(TAG,fixShopUid+"");
        }

    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_fixshop_index);

    }

    @Override
    public void findViewById() {
//        setScrollViewMode(PullToRefreshBase.Mode.BOTH);
        initAdImg();
        txtName=(TextView) findViewById(R.id.txt_shop_name);
        txtVip=(TextView) findViewById(R.id.txt_shop_vip);
        txtAddr=(TextView) findViewById(R.id.txt_shop_addr);
        txtCall=(TextView) findViewById(R.id.txt_shop_call);
        txtDetail=(HtmlTextView) findViewById(R.id.txt_shop_detail);
        txtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(callNum)){
                    Intent toCall=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+callNum));
                    getActivity().startActivity(toCall);
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
        RequestParams requestParams = new RequestParams(NetData.ACTION_get_service_info);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("mid",fixShopUid);
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
        requestParams.addParam("id",fixShopUid);
        requestParams.addParam("type",1);
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
                        ActivityFixShop.txtBarAttention.setSelected(isCollect);

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
//                refreshImg(imgShopBg,data.getString("bg"));
//                refreshImg(imgShop,data.getString("logo"));
//
//
                txtName.setText(data.getString("name"));
                txtVip.setText("商家信息已认证");
                txtAddr.setText(data.getString("addr"));
                txtDetail.setHtmlFromString(data.getString("desc"),false,this);
                callNum=data.getString("tel");
                txtCall.setText(callNum);
                isCollect=data.getInt("follow")==1;
                ActivityFixShop.txtBarAttention.setSelected(isCollect);

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

    @Override
    public void OnHtmlImgListener(int position) {

    }
}

package cn.lanmei.com.smartmall.main;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterMerchant;
import cn.lanmei.com.smartmall.adapter.AdapterString;
import cn.lanmei.com.smartmall.dialog.PopWindow_List;
import cn.lanmei.com.smartmall.location.BaiduLocation;
import cn.lanmei.com.smartmall.model.M_merchant;
import cn.lanmei.com.smartmall.parse.ParserMerchant;
import cn.lanmei.com.smartmall.shop.ActivityFixShop;
import cn.lanmei.com.smartmall.shop.Activity_shop;


/**
 *附近商家
 *
 */
public class F_nearby_merchant extends BaseScrollFragment {

    private String TAG="F_nearby_merchant";
    private View layouthead;
    private TextView txtDistance;
    private TextView txtType;
    private MyListView myListView;

    private Resources res;
    private LocationClient mBdLocationClient;
    private BDLocation bdLocation;
    private int p=1;
    private List<M_merchant> mMerchants;
    private AdapterMerchant adapterMerchant;

    private String distance="0";
    private String type="shop";
    private boolean isFixShop;

    public static F_nearby_merchant newInstance(boolean isFixShop){
        F_nearby_merchant f_nearby_merchant=new F_nearby_merchant();
        Bundle data=new Bundle();
        data.putBoolean("isFixShop",isFixShop);
        f_nearby_merchant.setArguments(data);
        return f_nearby_merchant;
    }

    @Override
    public  void init(){

        res = mContext.getResources();

        if (getArguments()!=null)
            isFixShop=getArguments().getBoolean("isFixShop");
        if (isFixShop){
            tag = "维修商";
            type="service";
        }else {
            tag = res.getString(R.string.menu_2);
        }
        initLocation();
        mMerchants=new ArrayList<>();
        adapterMerchant=new AdapterMerchant(mContext,mMerchants);
    }

    private void initLocation(){
        new BaiduLocation((BaseActivity) getActivity(), new BaiduLocation.WHbdLocaionListener() {
            @Override
            public void bdLocaionListener(LocationClient mLocationClient, BDLocation location) {
                mBdLocationClient=mLocationClient;
                bdLocation=location;
                if (location!=null){
                    mBdLocationClient.stop();
                }
            }
        });
        /*imgUserAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBdLocationClient!=null)
                    mBdLocationClient.start();
            }
        });*/
    }

    @Override
    public void loadViewLayout() {
        layouthead= LayoutInflater.from(mContext).inflate(R.layout.layout_head_nearbymer,layoutHead,false);
        layouthead.setBackgroundColor(ContextCompat.getColor(mContext,R.color.bgColor));
        loadHeadViewLayout(layouthead);

        setContentView(R.layout.layout_nearby_merchant);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            initHead();
        }
    }
    private void initHead(){
//        ((BaseActionActivity)getActivity()).txtLeft.setVisibility(View.GONE);
        ((BaseActionActivity)getActivity()).txtRight.setVisibility(View.GONE);

    }

    @Override
    public void findViewById() {
        initHead();
        txtDistance= (TextView) layouthead.findViewById(R.id.txt_distance);
        txtType= (TextView) layouthead.findViewById(R.id.txt_type);
        if (isFixShop)
            txtType.setText("维修商▼");
        else
            txtType.setText("商家▼");
        myListView= (MyListView)findViewById(R.id.listview);
        myListView.setAdapter(adapterMerchant);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mMerchants.get(position).getType().equals("service")){//维修商
                    Intent toDetail=new Intent(mContext,ActivityFixShop.class);
                    toDetail.putExtra(Common.KEY_id,mMerchants.get(position).getUid());
                    mContext.startActivity(toDetail);
                }else {
                    Intent toDetail=new Intent(mContext,Activity_shop.class);
                    toDetail.putExtra(Common.KEY_id,mMerchants.get(position).getUid());
                    mContext.startActivity(toDetail);
                }
            }
        });

        txtDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopDistance(v);
            }
        });
        txtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopType(v);
            }
        });
    }

    @Override
    public void requestServerData() {
        p=1;
        refresh();
    }


    /**刷新*/
    public void refresh() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_Shop_nearby_shops);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        if (!distance.equals("0"))
            requestParams.addParam("distance",distance);
        if (type!=null)
            requestParams.addParam("type",type);
        if (bdLocation!=null){
            requestParams.addParam("lat",bdLocation.getLatitude());
            requestParams.addParam("lng",bdLocation.getLongitude());
        }
        requestParams.setBaseParser(new ParserMerchant());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_merchant>,Integer>(p){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_merchant> parserData) {
                super.processData(obj, parserData);
                if (obj==1)
                    mMerchants.clear();
                if (parserData.size()>0)
                    p++;
                mMerchants.addAll(parserData);
                adapterMerchant.refreshData(mMerchants);

            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                mPullRefreshScrollView.onRefreshComplete();
            }
        });

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        p=1;
        refresh();
    }


    /**
     * 范围
     * */
    public void showPopDistance(View v){
        List<String> lists=new ArrayList<String>();
        lists.add("全部");
        for(int i=1;i<=5;i++){
            lists.add((i*1000)+"米内");
        }

        new PopWindow_List<AdapterString>(mContext,
                new AdapterString(mContext, lists),
                new PopWindow_List.PopWindowItemClick() {
                    @Override
                    public void onPopWindowItemClick(int position) {
                        distance=position*1000+"";
                        if (position==0){
                            txtDistance.setText("不限");
                            type=null;
                        }else {
                            txtDistance.setText(distance+"米内");
                        }
                        p=1;
                        refresh();
                    }
                },
                StaticMethod.dip2px(mContext,160)).showPopupWindow(v);
    }

    /**
     * 类型
     * */
    public void showPopType(View v){
        List<String> lists=new ArrayList<String>();
        lists.add("商家");
        lists.add("维修商");

        new PopWindow_List<AdapterString>(mContext,
                new AdapterString(mContext, lists),
                new PopWindow_List.PopWindowItemClick() {
                    @Override
                    public void onPopWindowItemClick(int position) {

                        if (position==0){
                            type="shop";
                            txtType.setText("商家");
                        }else {
                            type="service";
                            txtType.setText("维修商");
                        }
                        p=1;
                        refresh();
                    }
                },
                StaticMethod.dip2px(mContext,160)).showPopupWindow(v);
    }


}

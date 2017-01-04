package cn.lanmei.com.smartmall.goods;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterReview;
import cn.lanmei.com.smartmall.model.M_Review;
import cn.lanmei.com.smartmall.parse.ParserReview;


/**
 *商品评价
 */
public class F_goods_review extends BaseScrollFragment {
    private String TAG="F_goods_review";
    private MyListView myListView;

    private List<M_Review> reviews;
    private AdapterReview adapterReview;

    private int goodsId;


    public static F_goods_review newInstance(int goodsId) {
        F_goods_review fragment = new F_goods_review();
        Bundle bundle = new Bundle();
        bundle.putInt("goodsId",goodsId);
//        bundle.putInt("goodsId",6);
        fragment.setArguments(bundle);
        return fragment;
    }





    @Override
    public void init() {
        if (getArguments()!=null){
            goodsId=getArguments().getInt("goodsId",0);
        }
        reviews=new ArrayList<>();
        adapterReview=new AdapterReview(mContext,reviews);
    }

    @Override
    public void loadViewLayout() {

        setContentView(R.layout.layout_listview);


    }

    @Override
    public void findViewById() {
        myListView=(MyListView) findViewById(R.id.listview);
        myListView.setDivider(new ColorDrawable(Color.WHITE));
        myListView.setDividerHeight(1);
        myListView.setAdapter(adapterReview);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent toDevDetail=new Intent(getActivity(),Activity_device_detail.class);
//                Bundle data=new Bundle();
//                data.putSerializable(Common.KEY_bundle,mDevices.get(position));
//                toDevDetail.putExtra(Common.KEY_bundle,data);
//                getActivity().startActivity(toDevDetail);
//                mOnFragmentInteractionListener.addFrament(new F_device_detail(mDevices.get(position)),"F_device_detail");
            }
        });
        isFirstRequest=true;

    }
    private boolean isFirstRequest;



    @Override
    public void requestServerData() {
        requestGoodsReview();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        isFirstRequest=false;
        p=1;
        requestServerData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        p++;
        requestGoodsReview();
    }
    private int p=1;
    /**商品评论*/
    private void requestGoodsReview(){
        RequestParams requestParams=new RequestParams(NetData.ACTION_Shop_goods_reviews);
        requestParams.addParam("id",goodsId);
        requestParams.addParam("p",p);
        requestParams.setBaseParser(new ParserReview());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_Review>,Integer>(p){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_Review> parserData) {
                super.processData(obj, parserData);
                if (p==1)
                    reviews.clear();
                reviews.addAll(parserData);
                adapterReview.refreshData(reviews);
            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                mPullRefreshScrollView.onRefreshComplete();
            }
        });
    }


    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }
}

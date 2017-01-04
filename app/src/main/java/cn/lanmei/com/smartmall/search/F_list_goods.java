package cn.lanmei.com.smartmall.search;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.degexce.L;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyGridView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterGoods;
import cn.lanmei.com.smartmall.dialog.PopupWindowFull_list;
import cn.lanmei.com.smartmall.dialog.PopupWindowSelect;
import cn.lanmei.com.smartmall.goods.ActivityGoodsDetail_2;
import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.parse.ParserGoods;


/**
 *
 */
public class F_list_goods extends BaseScrollFragment {

    private String TAG="F_list_goods";

    public static final String KEY_type="type";
    public static final String KEY_parentId="parentId";
    public static final String KEY_key="key";

    private MyGridView myListView;

    private Resources res;
    public int p=1;
    private List<M_Goods> mGoodses;
    private AdapterGoods adapterGoods;

    private int type=1;

    public int parentId=0;
    public int category;
    public String search;
    public int order;
    public boolean isVipgoods;
    public int typeGoods;
    public int brand;

    public static F_list_goods newInstance(Bundle data){
        F_list_goods f__listGoods =new F_list_goods();
        f__listGoods.setArguments(data);
        return f__listGoods;
    }

    @Override
    public  void init(){

        res = mContext.getResources();
        tag = "";
        Bundle data=getArguments();
        if (data!=null){
            type = data.getInt(KEY_type);
            parentId = data.getInt(KEY_parentId);
            search = data.getString(KEY_key);
            tag=search;
            category=parentId;
            switch (type){
                case Activity_list_goods.TYPE_sale:
                    tag=res.getString(R.string.menu_2);
                    parentId=0;
                    category=0;
                    isVipgoods=false;
                    brand=0;
                    break;
                case Activity_list_goods.TYPE_Vgoods:
                    tag=res.getString(R.string.goods_vip);
                    isVipgoods=true;
                    parentId=0;
                    category=0;
                    typeGoods=0;
                    brand=0;
                    break;
            }
        }
        mGoodses=new ArrayList<>();
        adapterGoods=new AdapterGoods(mContext,mGoodses);
    }



    @Override
    public void loadViewLayout() {

        setContentView(R.layout.layout_gridview);
    }



    @Override
    public void findViewById() {
        setScrollViewMode(PullToRefreshBase.Mode.BOTH);
        myListView= (MyGridView) findViewById(R.id.gridview);
        myListView.setAdapter(adapterGoods);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toDetail=new Intent(getActivity(),ActivityGoodsDetail_2.class);
                toDetail.putExtra(Common.KEY_id,mGoodses.get(position).getRecordId());
                getActivity().startActivity(toDetail);
            }
        });


    }


    @Override
    public void requestServerData() {
        p=1;
        refresh(category);
    }


    /**刷新*/
    public void refresh(int category) {
        RequestParams requestParams = new RequestParams(NetData.ACTION_Shop_goods_list);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        switch (type){
            case Activity_list_goods.TYPE_category:
                requestParams.addParam("category", category);
                break;
            case Activity_list_goods.TYPE_search:
                requestParams.addParam("keyword", search);
                break;
            case Activity_list_goods.TYPE_sale://促销
                requestParams.addParam("type", 5);
                break;
        }
        if (isVipgoods){
            requestParams.addParam("vgoods", 1);
        }
        if (typeGoods>0&&type!=Activity_list_goods.TYPE_sale){
            requestParams.addParam("type", typeGoods);
        }
        if (brand>0){
            requestParams.addParam("brand", brand);
        }

        requestParams.addParam("order", order);
        requestParams.addParam("p", p);
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
                    mGoodses.clear();
                if (parserData.size()>0)
                    p++;
                mGoodses.addAll(parserData);
                adapterGoods.refreshData(mGoodses);

            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                stopProgressDialog();
                mPullRefreshScrollView.onRefreshComplete();
            }
        });

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        p=1;
        refresh(category);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        refresh(category);
    }

    /**
     * 范围
     * */

    public void showPopDistance(View v){

        List<String> lists=new ArrayList<String>();
        lists.add("综合排序");
        lists.add("新品优先");
        lists.add("评论数从高到低排");

        new PopupWindowFull_list(mContext, lists, new PopupWindowFull_list.PopupListener() {

            @Override
            public void onItemClick(int position) {
                L.MyLog("",position+"");
                switch (position){
                    case 0:
                        order=7;
                        break;
                    case 1:
                        order=0;
                        break;
                    case 2:
                        order=6;
                        break;

                }
                p=1;
                refresh(category);
            }
        }).showPopupWindow(layoutHead);
    }

    /**
     * 类型
     * */
    public void showPopType(View v){


        new PopupWindowSelect(mContext,typeGoods, isVipgoods,order==1,
                parentId,category,
                brand,
                new PopupWindowSelect.PopupListener() {
                    @Override
                    public void onItemClick(boolean isVgoods, int type, boolean priceDesc, int categoryId, int brandId) {
                        if (priceDesc){
                            order=2;
                        }else {
                            order=1;
                        }
                        isVipgoods=isVgoods;
                        typeGoods=type;
                        category=categoryId;
                        brand=brandId;
                        p=1;
                        refresh(category);
                        L.MyLog("p",p+"");
                        L.MyLog("isVgoods",isVgoods+"");
                        L.MyLog("type",type+"");
                        L.MyLog("priceDesc",priceDesc+"");
                        L.MyLog("categoryId",categoryId+"");
                        L.MyLog("brandId",brandId+"");

                    }
                }).showPopupWindow(v);
    }


}

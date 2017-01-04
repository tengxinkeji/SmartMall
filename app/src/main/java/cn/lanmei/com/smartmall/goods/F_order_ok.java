package cn.lanmei.com.smartmall.goods;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.datadb.DBGoodsCartManager;
import com.common.myinterface.DataCallBack;
import com.common.myui.MyListView;
import com.common.myui.RoundImageView;
import com.common.net.NetData;
import com.common.net.ParserJsonManager;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterCartGoodsOk;
import cn.lanmei.com.smartmall.adapter.AdapterShipWay;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.model.KeyValue;
import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.model.M_address;
import cn.lanmei.com.smartmall.model.M_cart_goods;
import cn.lanmei.com.smartmall.parse.ParserAddress;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.pay.AlipayMyPay;


/**
 *确认订单
 */
public class F_order_ok extends BaseScrollFragment implements AdapterCartGoodsOk.ShoppingGoodsMoneyListener{


    private String TAG="F_order_ok";
    private LinearLayout layoutOrderAddr;
    private RoundImageView imgHead;
    private TextView txtName;
    private TextView txtAddr;
    private TextView txtPayMethod;
    private MyListView myListViewPayMethod;
    private View viewAccount;
    private TextView txtGoodsMoney;
    private TextView txtReferOrder;

    private MyListView myListView;
    private List<M_cart_goods> cartGoodses;
    private List<KeyValue> shippingMethods;
    private List<KeyValue> payTypes;
    private AdapterShipWay adapterShipWay;
    private Map<Integer,Double> mapFreight;
    private Map<Integer,Boolean> mapPayOnDelivery;//是否支持货到付款
    private Map<Integer,Boolean> mapGoodsHas;//是否还有此商品，默认都不存在
    private AdapterCartGoodsOk adapterCartGoodsOk;
    private Drawable drawableRight;
    private Drawable drawableUp;


    private Resources res;
    private ParserJsonManager parserJsonManager;

    private M_address mAddress;
    private String payMethodStr;
    private double goodsMoney;
    private String product_id="";
    private String num="";
    private boolean isNowPay=false;
    private M_Goods mGoods;


    public static F_order_ok newInstance(M_Goods mGoods){
        F_order_ok fOrderOk=new F_order_ok();
        Bundle data=new Bundle();
        data.putSerializable(Common.KEY_bundle,mGoods);
        fOrderOk.setArguments(data);
        return fOrderOk;
    }

    @Override
    public void init(){
        parserJsonManager=new ParserJsonManager(mContext);
        res = mContext.getResources();
        tag = "确认订单";

        if (getArguments()!=null){
            mGoods = (M_Goods) getArguments().getSerializable(Common.KEY_bundle);
            if (mGoods!=null){
                isNowPay=true;
                product_id=mGoods.getGoods_id()+"";
                num=mGoods.getGoodsCount()+"";
            }
        }

        cartGoodses=new ArrayList<>();
        mapGoodsHas=new HashMap<>();
        shippingMethods=new ArrayList<>();
        payTypes=new ArrayList<>();
        mapFreight=new HashMap<>();
        mapPayOnDelivery=new HashMap<>();
//        cartGoodses.addAll(dbGoodsCartManager.queryCartGoods());
        adapterCartGoodsOk=new AdapterCartGoodsOk(mContext,cartGoodses);
        adapterCartGoodsOk.setCartGoodsListener(this);


    }




    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_order_ok);
        viewAccount = LayoutInflater.from(mContext).inflate(R.layout.layout_order_account, layoutHead,false);
        loadBottomViewLayout(viewAccount);
    }



    @Override
    public void findViewById() {
        layoutOrderAddr= (LinearLayout) findViewById(R.id.layout_order_addr);
        imgHead= (RoundImageView) layoutOrderAddr.findViewById(R.id.img_head);
        txtName= (TextView) layoutOrderAddr.findViewById(R.id.txt_name);
        txtAddr= (TextView) layoutOrderAddr.findViewById(R.id.txt_addr);

        myListView= (MyListView) findViewById(R.id.listview_index);
        myListView.setAdapter(adapterCartGoodsOk);

        txtPayMethod= (TextView)findViewById(R.id.txt_shipping_method);
        myListViewPayMethod= (MyListView) findViewById(R.id.listview_pay_method);

        txtGoodsMoney= (TextView) viewAccount.findViewById(R.id.txt_goods_money);
        txtReferOrder= (TextView) viewAccount.findViewById(R.id.txt_refer_order);

        drawableRight= ContextCompat.getDrawable(mContext, R.drawable.icon_right_gray);
        drawableRight.setBounds(0,0,drawableRight.getMinimumWidth(),drawableRight.getMinimumHeight());
        drawableUp= ContextCompat.getDrawable(mContext,R.drawable.icon_pagedown_gray);
        drawableUp.setBounds(0,0,drawableUp.getMinimumWidth(),drawableUp.getMinimumHeight());
        txtPayMethod.setCompoundDrawables(null,null,myListViewPayMethod.getVisibility()==View.GONE?drawableRight:drawableUp,null);
        adapterShipWay=new AdapterShipWay(mContext,shippingMethods);
        adapterShipWay.setCheckListener(new AdapterShipWay.CheckListener() {
            @Override
            public void onCheckListener(String id) {
                payMethodStr=id;
            }
        });
        myListViewPayMethod.setAdapter(adapterShipWay);


        layoutOrderAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddress=new Intent(getActivity(),Activity_manager_address.class);
                getActivity().startActivityForResult(toAddress, Common.requestCode_addr_select);
            }
        });

        txtReferOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payRefer();
            }
        });

    }

    /**支付*/
    private void payRefer() {
        if (mAddress==null){
            StaticMethod.showInfo(mContext,"请选择收货地址");
            return;
        }
        if (TextUtils.isEmpty(payMethodStr)){
            StaticMethod.showInfo(mContext,"请选择支付方式");
            return;
        }
        referOrder();



    }

    private void refreshAddrUi(){
        if (mAddress==null)
            return;
        txtName.setText(mAddress.getAccept_name()+"\t\t\t"+mAddress.getMobile());
        txtAddr.setText(mAddress.getAddress());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.MyLog(TAG,"requestCode:"+requestCode+"---resultCode:"+resultCode);
        if (requestCode==Common.requestCode_addr_select){
            if (resultCode==Common.resultCode_addr_select){
                if (data!=null){
                    mAddress= (M_address) data.getBundleExtra(Common.KEY_bundle).getSerializable(Common.KEY_bundle);
                    refreshAddrUi();
                    L.MyLog(TAG,mAddress.getAddress());
                }

            }
        }
    }

    @Override
    public void requestServerData() {
        getDefaultAddr();
        refreshData();

    }
    public void refreshData() {
        if (isNowPay){
            cartGoodses=new ArrayList<>();
            M_cart_goods mCartGoods = new M_cart_goods();
            mCartGoods.setStoreId(mGoods.getGoodsStoreId());
            mCartGoods.setStoreName(mGoods.getGoodsStoreName());
            mCartGoods.setGoodsNum(mGoods.getGoodsCount());
            mCartGoods.setMoney(mGoods.getGoodsPrice());
            ArrayList<M_Goods> listGoods = new ArrayList<M_Goods>();
            listGoods.add(mGoods);
            mCartGoods.setGoodsList(listGoods);
            cartGoodses.add(mCartGoods);
        }else{
            cartGoodses=DBGoodsCartManager.dbGoodsCartManager.queryUserCartGoods();
        }
        mapGoodsHas.clear();
        if (cartGoodses.size()<1){
            adapterCartGoodsOk.refreshData(cartGoodses);
            return;
        }
        refresh();
    }


    /**
     * 获取下订单信息
     * */
    private void refresh() {

        if (!isNowPay){
            for (int i=0;i<cartGoodses.size();i++){
                List<M_Goods> item=cartGoodses.get(i).getGoodsList();
                for(int m=0;m<item.size();m++){
                    product_id+=item.get(m).getGoods_id()+",";
                    num+=item.get(m).getGoodsCount()+",";
                    mapGoodsHas.put(item.get(m).getGoods_id(),false);
                }
            }
            product_id=product_id.substring(0,product_id.length()-1);
            num=num.substring(0,num.length()-1);
        }

        RequestParams requestParams = new RequestParams(NetData.ACTION_Shop_order_info);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("num",num);
        requestParams.addParam("product_id",product_id);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {

            }

            @Override
            public void processData(JSONObject parserData) {
                try {
                    if (parserData.getInt("status")==1){
                       JSONObject data = parserData.optJSONObject("data");
                        if (data==null)
                            return;
                        JSONArray distribution = data.optJSONArray("distribution");
                        JSONObject item;
                        if (distribution!=null){
                            shippingMethods.clear();
                            for(int i=0;i<distribution.length();i++){
                                item=distribution.getJSONObject(i);
                                shippingMethods.add(new KeyValue(item.getString("id"),item.getString("name")));
                            }

                        }

                        JSONArray pay_type = data.optJSONArray("pay_type");
                        if (distribution!=null){
                            payTypes.clear();
                            for(int i=0;i<pay_type.length();i++){
                                item=pay_type.getJSONObject(i);
                                payTypes.add(new KeyValue(item.getString("id"),item.getString("c_name")));
                            }
                            payMethodStr=payTypes.get(0).getKey();
                            adapterShipWay.refreshData(payTypes);
                        }

                        JSONObject shops_info = data.optJSONObject("shops_info");
                        Iterator<String> keys = shops_info.keys();
                        int key;
                        M_Goods goodsPrices;

                        while(keys.hasNext()){//遍历JSONObject
                            key = Integer.parseInt(keys.next().toString()) ;
                            JSONObject shops_info_item = shops_info.optJSONObject(key+"");
                            mapPayOnDelivery.put(key,shops_info_item.getInt("pay_on_delivery") == 1);
                            JSONObject list = shops_info_item.optJSONObject("list");
                            Iterator<String> keyList = list.keys();
                            double express_fee=0.0d;
                            double express_fee_item=0.0d;
                            while(keyList.hasNext()){
                                int goodsId = Integer.parseInt(keyList.next().toString());
                                JSONObject goodsPrice = list.getJSONObject(goodsId + "");
                                goodsPrices=new M_Goods();
                                goodsPrices.setCost_price(goodsPrice.getDouble("cost_price"));
                                goodsPrices.setMarket_price(goodsPrice.getDouble("market_price"));
                                goodsPrices.setSell_price(goodsPrice.getDouble("sell_price"));
                                goodsPrices.setGoods_id(goodsId);
                                mapGoodsHas.put(goodsId,true);
                                if (isNowPay){
                                    cartGoodses.get(0).getGoodsList().get(0).setCost_price(goodsPrices.getCost_price());
                                    cartGoodses.get(0).getGoodsList().get(0).setMarket_price(goodsPrices.getMarket_price());
                                    cartGoodses.get(0).getGoodsList().get(0).setSell_price(goodsPrices.getSell_price());
                                    cartGoodses.get(0).getGoodsList().get(0).setGoods_id(goodsId);
                                }else{
                                    DBGoodsCartManager.dbGoodsCartManager.updateGoodsPrice(goodsPrices);
                                }


                                express_fee_item=goodsPrice.getDouble("express_fee");
                                if (express_fee<express_fee_item)
                                    express_fee=express_fee_item;
                            }
                            mapFreight.put(key,express_fee);
                        }


                        if (!isNowPay){
                            Set<Map.Entry<Integer, Boolean>> entrys = mapGoodsHas.entrySet();
                            for(Map.Entry<Integer, Boolean> entry:entrys ){
                                if (!entry.getValue()){//此商品不存在
                                    DBGoodsCartManager.dbGoodsCartManager.delGoods(entry.getKey());
                                }
                            }
                        }
                        adapterCartGoodsOk.setMapFreight(shippingMethods,mapFreight,mapPayOnDelivery);
                        if (!isNowPay)
                            cartGoodses=DBGoodsCartManager.dbGoodsCartManager.queryUserCartGoods();
                        adapterCartGoodsOk.refreshData(cartGoodses);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
            }
        });

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        refreshData();
    }

    /**获取默认地址*/
    private void getDefaultAddr(){
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_address);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("act", 0);
        requestParams.setBaseParser(new ParserAddress());
        getDataFromServer(requestParams, new DataCallBack<List<M_address>>() {

            @Override
            public void onPre() {

            }

            @Override
            public void processData(List<M_address> parserData) {
                for (int i=0;i<parserData.size();i++){
                    if (parserData.get(i).getIsDefault()==1){
                        mAddress=parserData.get(i);
                        refreshAddrUi();
                        break;
                    }
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**提交订单*/
    private void referOrder(){
        RequestParams requestParams=new RequestParams(NetData.ACTION_Shop_order);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("province", mAddress.getProvince());
        requestParams.addParam("city", mAddress.getCity());
        requestParams.addParam("area", mAddress.getArea());
        requestParams.addParam("mobile", mAddress.getMobile());
        requestParams.addParam("pay_type", payMethodStr);
        requestParams.addParam("product_id", product_id);
        Map<String, String> distribution = adapterCartGoodsOk.getMapDistribution();
        Set<Map.Entry<String, String>> entrys = distribution.entrySet();
        for(Map.Entry<String, String> entry:entrys){
            requestParams.addParam("distribution["+entry.getKey()+"]", entry.getValue());
        }
        requestParams.addParam("num", num);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {

            @Override
            public void onPre() {
                startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                parserInfo(parserData);
            }

            @Override
            public void onComplete() {
                stopProgressDialog();
            }
        });
    }


    /**提交订单结果*/
    private void parserInfo(JSONObject result) {


        if (result==null){
            StaticMethod.showInfo(mContext,"下单失败");
            return;
        }
        try {
            StaticMethod.showInfo(mContext,result.getString("info"));
            if (result.getInt("status")!=1){
                DBGoodsCartManager.dbGoodsCartManager.delCartGoods();
                return;
            }
            JSONObject data=result.optJSONObject("data");
            if (data==null){
                StaticMethod.showInfo(mContext,"下单失败");
                return;
            }

            double amount=data.getDouble("amount");
            String order_no=data.getString("order_no");
            switch (payMethodStr){
                case "1"://支付宝
                    new AlipayMyPay((BaseActivity) getActivity()).payV2(payMethodStr,amount,order_no);
                    break;
                case "7"://微信支付
                    break;
                case "8"://货到付款
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }





    @Override
    public void goodsCount(int num, double money) {

        goodsMoney=money;
        if (txtGoodsMoney!=null)
            txtGoodsMoney.setText("总计:¥"+money);
    }
}

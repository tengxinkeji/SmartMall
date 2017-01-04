package cn.lanmei.com.smartmall.order;


import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterOrder;
import cn.lanmei.com.smartmall.goods.Activity_goods_review_send;
import cn.lanmei.com.smartmall.main.BaseActionActivity;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.model.M_order;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.parse.ParserOrder;
import cn.lanmei.com.smartmall.pay.AlipayMyPay;


/**
 *订单列表
 *
 */
public class F_list_order extends BaseScrollFragment {

    private String TAG="F_list_order";
    private View layouthead;
    private TextView txtOrder_1;
    private TextView txtOrder_2;
    private TextView txtOrder_3;
    private TextView txtOrder_4;

    private MyListView myListView;

    private Resources res;

    public int p=1;
    private List<M_order> orders;
    private AdapterOrder adapterOrder;

    private int orderType=0;

    @Override
    public void init(){

        res = mContext.getResources();
        tag = "我的订单";
        orders=new ArrayList<>();
        adapterOrder=new AdapterOrder(mContext,orders);

        adapterOrder.setOrderListener(new AdapterOrder.OrderListener() {
            @Override
            public void onOrderListenerDel(int position,String name) {
                switch (name){
                    case "删除订单":
                        orderDel(orders.get(position).getId());
                        break;
                    case "退款中":
                        break;
                    case "取消订单":
                        orderCancle(orders.get(position).getId());
                        break;
                    case "退款":
                        orderQuitMoney(orders.get(position).getId());
                        break;
                }


            }

            @Override
            public void onOrderListenerOk(int position,String name) {
                switch (name){
                    case "晒单评价":
                        Intent toReview =new Intent(getActivity(), Activity_goods_review_send.class);
                        toReview.putExtra(Common.KEY_id,orders.get(position).getProduct().get(0).getRecordId());
                        getActivity().startActivity(toReview);
                        break;
                    case "联系卖家":

                        break;

                }
            }

            @Override
            public void onOrderListenerPay(int position,String name) {
                switch (name){
                    case "去付款":
                        double amount=orders.get(position).getOrderMoney();
                        String order_no=orders.get(position).getOrderNo();
                        switch (orders.get(position).getPayType()+""){
                            case "1"://支付宝
                                new AlipayMyPay((BaseActivity) getActivity())
                                        .payV2(orders.get(position).getPayType()+"",amount,order_no);
                                break;
                            case "7"://微信支付
                                break;
                            case "8"://货到付款
                                break;
                        }
                        break;
                    case "确认收货":
                        orderOkReciver(orders.get(position).getId());
                        break;
                }
            }
        });
    }



    @Override
    public void loadViewLayout() {
        layouthead= LayoutInflater.from(mContext).inflate(R.layout.layout_head_order,layoutHead,false);
        layouthead.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
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
        ((BaseActionActivity)getActivity()).txtRight.setVisibility(View.GONE);

    }

    @Override
    public void findViewById() {
        initHead();
        txtOrder_1= (TextView) layouthead.findViewById(R.id.txt_order_head_1);
        txtOrder_2= (TextView) layouthead.findViewById(R.id.txt_order_head_2);
        txtOrder_3= (TextView) layouthead.findViewById(R.id.txt_order_head_3);
        txtOrder_4= (TextView) layouthead.findViewById(R.id.txt_order_head_4);

        myListView= (MyListView)findViewById(R.id.listview);
        myListView.setDivider(ContextCompat.getDrawable(mContext,R.color.transparent));
        myListView.setDividerHeight(StaticMethod.dip2px(mContext,20));
        myListView.setAdapter(adapterOrder);

        txtOrder_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType=0;
                p=1;
                setTabs(0);
                requestList();
            }
        });
        txtOrder_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType=1;
                p=1;
                setTabs(1);
                requestList();
            }
        });

        txtOrder_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType=2;
                p=1;
                setTabs(2);
                requestList();
            }
        });

        txtOrder_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderType=5;
                p=1;
                setTabs(3);
                requestList();
            }
        });
    }

    @Override
    public void requestServerData() {
        p=1;
        requestList();
    }

    private void setTabs(int position){
        txtOrder_1.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));
        txtOrder_2.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));
        txtOrder_3.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));
        txtOrder_4.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));

        switch (position){
            case 0:
                txtOrder_1.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_bar));
                break;
            case 1:
                txtOrder_2.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_bar));
                break;
            case 2:
                txtOrder_3.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_bar));
                break;
            case 3:
                txtOrder_4.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_bar));
                break;
        }
    }

    /**刷新*/
    public void requestList() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_Member_order);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("shops",0);
        requestParams.addParam("p",p);
        requestParams.addParam("status",orderType);
        requestParams.setBaseParser(new ParserOrder());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_order>,Integer>(p){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_order> parserData) {
                super.processData(obj, parserData);
                if (obj==1)
                    orders.clear();
                if (parserData.size()>0)
                    p++;
                orders.addAll(parserData);
                adapterOrder.refreshData(orders);

            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                mPullRefreshScrollView.onRefreshComplete();
            }
        });

    }

    /**
     * 退款订单
     * */
    public void orderQuitMoney(int orderId) {
        RequestParams requestParams = new RequestParams(NetData.ACTION_order_edit);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("id",orderId);
        requestParams.addParam("status",6);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>(){

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
    /**
     * 取消订单
     * */
    public void orderCancle(int orderId) {
        RequestParams requestParams = new RequestParams(NetData.ACTION_order_edit);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("id",orderId);
        requestParams.addParam("status",3);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>(){

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
    /**
     * 删除订单
     * */
    public void orderDel(int orderId) {
        RequestParams requestParams = new RequestParams(NetData.ACTION_order_edit);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("id",orderId);
        requestParams.addParam("if_del",1);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>(){

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

    /**
     * 确认收货
     * */
    public void orderOkReciver(int orderId) {
        RequestParams requestParams = new RequestParams(NetData.ACTION_order_edit);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("id",orderId);
        requestParams.addParam("status",5);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>(){

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

    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        p=1;
        if (result!=null){
            try {
                StaticMethod.showInfo(mContext,result.getString("info"));
                if (result.getInt("status")==1){
                    requestList();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            StaticMethod.showInfo(mContext,"请求失败");
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        p=1;
        requestList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        p++;
        requestList();
    }
}

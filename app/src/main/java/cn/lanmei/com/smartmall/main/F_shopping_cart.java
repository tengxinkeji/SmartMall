package cn.lanmei.com.smartmall.main;


import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.MyApplication;
import com.common.app.degexce.L;
import com.common.datadb.DBGoodsCartManager;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.ParserJsonManager;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterCartGoods;
import cn.lanmei.com.smartmall.goods.Activity_order_ok;
import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.model.M_cart_goods;
import cn.lanmei.com.smartmall.parse.ParserShoppingCartGoods;


/**
 *购物车
 *
 */
public class F_shopping_cart extends BaseScrollFragment implements AdapterCartGoods.CartGoodsListener {

    private String TAG="F_shopping_cart";
    private LinearLayout layoutGoodsNo;
    private TextView txtToGoods;
    private MyListView myListView;
    private List<M_cart_goods> cartGoodses;
    private AdapterCartGoods adapterCartGoods;
    private View layoutBtm;
    private CheckBox boxAll;
    private static TextView txtMoney;
    private TextView txtAccount;

    private Resources res;
    private ParserJsonManager parserJsonManager;
    DBGoodsCartManager  dbGoodsCartManager;

    private static double money;

    @Override
    public void init(){
        parserJsonManager=new ParserJsonManager(mContext);
        res = mContext.getResources();
        tag = res.getString(R.string.menu_4);
        dbGoodsCartManager=DBGoodsCartManager.dbGoodsCartManager;
        cartGoodses=new ArrayList<>();
//        cartGoodses.addAll(dbGoodsCartManager.queryCartGoods());
        adapterCartGoods=new AdapterCartGoods(mContext,cartGoodses);
        adapterCartGoods.setCartGoodsListener(this);


    }

    private void setLayout(boolean hasGoods){
        if (hasGoods){
            if (layoutGoodsNo.getVisibility()==View.GONE)
                return;
            layoutGoodsNo.setVisibility(View.GONE);
            layoutBtm.setVisibility(View.VISIBLE);
            myListView.setVisibility(View.VISIBLE);
        }else {
            if (layoutGoodsNo.getVisibility()==View.VISIBLE)
                return;
            layoutGoodsNo.setVisibility(View.VISIBLE);
            layoutBtm.setVisibility(View.GONE);
            myListView.setVisibility(View.GONE);
        }
    }


    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_shoping_cart);
        layoutBtm= LayoutInflater.from(mContext).inflate(R.layout.layout_account,layoutBottom,false);
        layoutBtm.setBackgroundColor(ContextCompat.getColor(mContext,R.color.bgColor));
        loadBottomViewLayout(layoutBtm);


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            initHead();
        }
    }
    private void initHead(){
        ((BaseActionActivity)getActivity()).txtLeft.setVisibility(View.GONE);
        ((BaseActionActivity)getActivity()).txtRight.setVisibility(View.GONE);

    }

    @Override
    public void findViewById() {
        initHead();
        layoutGoodsNo= (LinearLayout) findViewById(R.id.layout_cart_goodsno);
        txtToGoods= (TextView) layoutGoodsNo.findViewById(R.id.txt_goods);
        myListView= (MyListView) findViewById(R.id.listview_index);
        myListView.setAdapter(adapterCartGoods);
        boxAll= (CheckBox) layoutBtm.findViewById(R.id.box_all);
        txtMoney= (TextView) layoutBtm.findViewById(R.id.txt_money);
        txtAccount= (TextView) layoutBtm.findViewById(R.id.txt_account);
        txtMoney.setText("合计："+money);

        boxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dbGoodsCartManager.updateAllGoodsSelect(isChecked);
                cartGoodses=dbGoodsCartManager.queryUserCartGoods();
                adapterCartGoods.refreshData(cartGoodses);
            }
        });
        txtToGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.showFrament(0);
            }
        });

        txtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toOrderOk=new Intent(getActivity(), Activity_order_ok.class);
                getActivity().startActivity(toOrderOk);
            }
        });
        setLayout(cartGoodses.size()>0);
    }

    @Override
    public void requestServerData() {
        refreshDB();
    }
    public void refreshDB() {
        cartGoodses.clear();
        cartGoodses.addAll(DBGoodsCartManager.dbGoodsCartManager.queryUserCartGoods());
        adapterCartGoods.refreshData(cartGoodses);
        setLayout(cartGoodses.size()>0);
        mPullRefreshScrollView.onRefreshComplete();
    }


    /**刷新*/
    private void refresh() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_Shop_cart_list);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.setBaseParser(new ParserShoppingCartGoods());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_Goods>,Integer>(1){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_Goods> parserData) {
                super.processData(obj, parserData);
                cartGoodses.clear();
                cartGoodses.addAll(DBGoodsCartManager.dbGoodsCartManager.queryUserCartGoods());
                adapterCartGoods.refreshData(cartGoodses);
                setLayout(cartGoodses.size()>0);
            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
            }
        });

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        refreshDB();
    }



    private void parserInfo(JSONObject result) {


        if (result==null){
            return;
        }
        try {
            if (result.getInt("status")!=1)
                return;
            result=result.getJSONObject("data");



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


   /***/
    public static void refreshCart(int gooodsCount, double mMoney) {
        money=mMoney;
        if (txtMoney!=null)
            txtMoney.setText("合计："+money);

    }

    @Override
    public void goodsCount(int positionStore, int childP, int goodsId, int count) {
        int c=DBGoodsCartManager.dbGoodsCartManager.updateGoodsCount(goodsId,count);
        L.MyLog(TAG,"updateGoodsCount:"+c+"---goodsId:"+goodsId+"---count:"+count);

    }

    @Override
    public void goodsDel(int positionStore, int childP, int goodsId) {
        L.MyLog(TAG,"购物车ada：del:goodsId："+goodsId);
        cartGoodses.get(positionStore).getGoodsList().remove(childP);
        if (cartGoodses.get(positionStore).getGoodsList().size()<1)
            cartGoodses.remove(positionStore);
        DBGoodsCartManager.dbGoodsCartManager.delGoods(goodsId);
        adapterCartGoods.refreshData(cartGoodses);
        setLayout(cartGoodses.size()>0);
    }
}

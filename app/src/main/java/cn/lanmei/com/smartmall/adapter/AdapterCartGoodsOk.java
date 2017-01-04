package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.myui.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.KeyValue;
import cn.lanmei.com.smartmall.model.M_cart_goods;

public class AdapterCartGoodsOk extends MyBaseAdapter<M_cart_goods> {
    private List<KeyValue> shippingMethods;
    private Map<Integer,Double> mapFreight;
    private Map<Integer,Boolean> mapPayOnDelivery;//是否支持货到付款
    private int goodsNum=0;
    private double goodsMoney=0;
    private int defaultMethodsP=0;
    public static final String despatchStr="1";//快递
    private Map<String,String> mapDistribution;

    private Drawable drawableRight;
    private Drawable drawableUp;
        public AdapterCartGoodsOk(Context mContext, List<M_cart_goods> mLists) {
            super(mContext,mLists);
            drawableRight= ContextCompat.getDrawable(mContext, R.drawable.icon_right_gray);
            drawableRight.setBounds(0,0,drawableRight.getMinimumWidth(),drawableRight.getMinimumHeight());
            drawableUp= ContextCompat.getDrawable(mContext,R.drawable.icon_pagedown_gray);
            drawableUp.setBounds(0,0,drawableUp.getMinimumWidth(),drawableUp.getMinimumHeight());
            shippingMethods=new ArrayList<>();
            mapDistribution=new HashMap<>();

        }

    @Override
    public void refreshData(List<M_cart_goods> lists) {
        super.refreshData(lists);

        if (shoppingGoodsMoneyListener!=null){
            goodsNum=0;
            goodsMoney=0;
            for (int i=0;i<lists.size();i++){
                M_cart_goods item = getItem(i);
                Double fre=0.0;
                if (mapFreight.containsKey(item.getStoreId()))
                    fre = mapFreight.get(item.getStoreId());
                lists.get(i).setFreight(fre);
                lists.get(i).setPayMethod(shippingMethods.get(defaultMethodsP).getKey());
                mapDistribution.put(item.getStoreId()+"",shippingMethods.get(defaultMethodsP).getKey());
                goodsNum+=getItem(i).getGoodsNum();
                switch (lists.get(i).getPayMethod()){
                    case despatchStr:
                        goodsMoney+=getItem(i).getFreight();
                        break;
                }
                goodsMoney+=getItem(i).getMoney();

            }
            shoppingGoodsMoneyListener.goodsCount(goodsNum,goodsMoney);
        }

    }

    public void setMapFreight(List<KeyValue> shippingMethods, Map<Integer,Double> mapFreight, Map<Integer,Boolean> mapPayOnDelivery) {
        this.shippingMethods=shippingMethods;
        this.mapFreight = mapFreight;
        this.mapPayOnDelivery = mapPayOnDelivery;



    }

    @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if (convertView == null){
                convertView=inflater.inflate(R.layout.layout_item_cart_goods_ok,parent,false);
                holder = new ViewHolder(convertView,position);

            }else{
                holder=(ViewHolder) convertView.getTag();
                holder.refresh(position);
            }
            M_cart_goods cartGoods=getItem(position);
            holder.txtType.setText(cartGoods.getStoreName()+"");
            holder.txtStoreNum.setText(cartGoods.getGoodsNum()+"件");
            holder.txtStoreMoney.setText("¥"+cartGoods.getMoney()+"");
            switch (cartGoods.getPayMethod()){
                case despatchStr:
                    holder.txtStoreFreight.setText("¥"+cartGoods.getFreight());
                    break;
                default:
                    holder.txtStoreFreight.setText("¥0.00");
            }
            return convertView;
        }

        protected class ViewHolder{

            public TextView txtType;
            public MyListView myListView;
            public TextView txtShipM;
            public MyListView myListViewSM;
            public TextView txtStoreMoneyTag;
            public LinearLayout layoutStroeMoney;
            public TextView txtStoreNum;
            public TextView txtStoreFreight;
            public TextView txtStoreMoney;

            private AdapterShipWay adapterShipWay;
            AdapterShoppingOK adapterShoppingOK;
            int positions;
            List<M_Goods> childList;
            public ViewHolder(View convertView, final int position) {
                this.positions=position;

                txtType=(TextView) convertView.findViewById(R.id.txt_type);
                myListView=(MyListView) convertView.findViewById(R.id.listview);
                txtShipM=(TextView) convertView.findViewById(R.id.txt_shipping_method);
                myListViewSM=(MyListView) convertView.findViewById(R.id.listview_shipping_method);
                txtStoreMoneyTag=(TextView) convertView.findViewById(R.id.txt_store_money);
                layoutStroeMoney=(LinearLayout) convertView.findViewById(R.id.layout_store);
                txtStoreNum=(TextView) layoutStroeMoney.findViewById(R.id.store_goods_num);
                txtStoreFreight=(TextView) layoutStroeMoney.findViewById(R.id.store_goods_freight);
                txtStoreMoney=(TextView) layoutStroeMoney.findViewById(R.id.store_goods_money);

                childList = getItem(position).getGoodsList();
                adapterShoppingOK=new AdapterShoppingOK(mContext,childList);
                myListView.setAdapter(adapterShoppingOK);

                adapterShipWay=new AdapterShipWay(mContext,shippingMethods);
                adapterShipWay.setCheckListener(new AdapterShipWay.CheckListener() {
                    @Override
                    public void onCheckListener(String id) {
                        setStoreMoney(id);
                        mapDistribution.put(getItem(positions).getStoreId()+"",id);
                        if (shoppingGoodsMoneyListener!=null){
                            goodsNum=0;
                            goodsMoney=0;
                            for (int i=0;i<getCount();i++){
                                goodsNum+=lists.get(i).getGoodsNum();
                                switch (lists.get(i).getPayMethod()){
                                    case despatchStr:
                                        goodsMoney+=lists.get(i).getFreight();
                                        break;
                                }
                                goodsMoney+=lists.get(i).getMoney();
                            }
                            shoppingGoodsMoneyListener.goodsCount(goodsNum,goodsMoney);
                        }
                    }
                });
                myListViewSM.setAdapter(adapterShipWay);


                txtShipM.setCompoundDrawables(null,null,myListViewSM.getVisibility()==View.GONE?drawableRight:drawableUp,null);
                txtStoreMoneyTag.setCompoundDrawables(null,null,layoutStroeMoney.getVisibility()==View.GONE?drawableRight:drawableUp,null);
                txtShipM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myListViewSM.setVisibility(myListViewSM.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                        txtShipM.setCompoundDrawables(null,null,myListViewSM.getVisibility()==View.GONE?drawableRight:drawableUp,null);

                    }
                });
                txtStoreMoneyTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutStroeMoney.setVisibility(layoutStroeMoney.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                        txtStoreMoneyTag.setCompoundDrawables(null,null,layoutStroeMoney.getVisibility()==View.GONE?drawableRight:drawableUp,null);

                    }
                });


                convertView.setTag(this);
            }

            public void refresh(int position){
                this.positions=position;
                childList = lists.get(positions).getGoodsList();
                adapterShoppingOK.refreshData(childList);
                adapterShipWay.refreshData(shippingMethods);
            }
            public void setStoreMoney(String id){
                lists.get(positions).setPayMethod(id);
                switch (id){
                    case despatchStr://快递
                        M_cart_goods item = lists.get(positions);
                        Double fre=0.00;
                        if (mapFreight.containsKey(item.getStoreId()))
                            fre = mapFreight.get(item.getStoreId());
                        lists.get(positions).setFreight(fre);
                        txtStoreFreight.setText("¥"+mapFreight.get(lists.get(positions).getStoreId())+"");
                        txtStoreMoney.setText("¥"+(lists.get(positions).getMoney()+lists.get(positions).getFreight())+"");
                        break;
                    default://自取
                        txtStoreFreight.setText("¥"+"0.00");
                        lists.get(positions).setFreight(0.00f);
                        txtStoreMoney.setText("¥"+lists.get(positions).getMoney()+"");
                }

            }
        }

    private ShoppingGoodsMoneyListener shoppingGoodsMoneyListener;

   public void setCartGoodsListener(ShoppingGoodsMoneyListener shoppingGoodsMoneyListener) {
        this.shoppingGoodsMoneyListener = shoppingGoodsMoneyListener;
    }

    public interface ShoppingGoodsMoneyListener{
        public void goodsCount(int num, double money);

    }

    public Map<String, String> getMapDistribution() {
        return mapDistribution;
    }
}


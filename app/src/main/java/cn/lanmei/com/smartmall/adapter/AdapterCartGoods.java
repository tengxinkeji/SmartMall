package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.common.datadb.DBGoodsCartManager;
import com.common.myui.MyListView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.model.M_cart_goods;

public class AdapterCartGoods extends MyBaseAdapter<M_cart_goods> {
    private Drawable drawableRight;
    private Drawable drawableUp;
        public AdapterCartGoods(Context mContext, List<M_cart_goods> mLists) {
            super(mContext,mLists);
            drawableRight= ContextCompat.getDrawable(mContext, R.drawable.icon_right_gray);
            drawableRight.setBounds(0,0,drawableRight.getMinimumWidth(),drawableRight.getMinimumHeight());
            drawableUp= ContextCompat.getDrawable(mContext,R.drawable.icon_pagedown_gray);
            drawableUp.setBounds(0,0,drawableUp.getMinimumWidth(),drawableUp.getMinimumHeight());
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if (convertView == null){
                convertView=inflater.inflate(R.layout.layout_item_cart_goods,parent,false);
                holder = new ViewHolder(convertView,position);

            }else{
                holder=(ViewHolder) convertView.getTag();
                holder.refresh(position);
            }
            M_cart_goods cartGoods=getItem(position);
            holder.txtType.setText(cartGoods.getStoreName()+"");
            holder.boxStore.setChecked(cartGoods.isSelect());


            return convertView;
        }

        protected class ViewHolder{
            public CheckBox boxStore;
            public TextView txtType;
            public MyListView myListView;
            AdapterShoppingCart adapterShoppingCart;
            int positions;
            List<M_Goods> childList;
            public ViewHolder(View convertView, final int position) {
                this.positions=position;
                boxStore=(CheckBox) convertView.findViewById(R.id.box_goods);
                txtType=(TextView) convertView.findViewById(R.id.txt_type);
                myListView=(MyListView) convertView.findViewById(R.id.listview);
                childList = getItem(position).getGoodsList();
                adapterShoppingCart=new AdapterShoppingCart(mContext,childList);
                myListView.setAdapter(adapterShoppingCart);
                txtType.setCompoundDrawables(null,null,myListView.getVisibility()==View.GONE?drawableRight:drawableUp,null);
                txtType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myListView.setVisibility(myListView.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                        txtType.setCompoundDrawables(null,null,myListView.getVisibility()==View.GONE?drawableRight:drawableUp,null);

                    }
                });

                boxStore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox checkBox = (CheckBox) v;
                        getItem(position).setSelect(!getItem(position).isSelect());
                        checkBox.setChecked(getItem(position).isSelect());

                        DBGoodsCartManager.dbGoodsCartManager.updateGoodsStoreSelect(getItem(position).getStoreId(),checkBox.isChecked());
                        DBGoodsCartManager.dbGoodsCartManager.queryUserCartGoods();
                        for (M_Goods m_goods:getItem(position).getGoodsList()){
                            m_goods.setSelect(checkBox.isChecked());
                        }
                        childList = getItem(position).getGoodsList();
                        adapterShoppingCart.refreshData(childList);
                    }
                });

                adapterShoppingCart.setShoppingCartGooodsListener(new AdapterShoppingCart.ShoppingCartGooodsListener() {
                    @Override
                    public void goodsCount(int postion, int goodsId, int count) {
                        if (cartGoodsListener!=null){
                            cartGoodsListener.goodsCount(positions,postion,goodsId,count);
                        }
                    }

                    @Override
                    public void goodsDel(int postion, int goodsId) {
                        if (cartGoodsListener!=null){
                            cartGoodsListener.goodsDel(positions,postion,goodsId);
                        }
                    }

                    @Override
                    public void goodsSelectChange() {
                        boolean storeSelect=true;
                        for (M_Goods mGoods:getItem(positions).getGoodsList()){
                            if (!mGoods.isSelect()){
                                storeSelect=false;
                                break;
                            }
                        }
                        boxStore.setChecked(storeSelect);
                    }
                });


                convertView.setTag(this);
            }

            public void refresh(int position){
                this.positions=position;
                childList = getItem(positions).getGoodsList();
                adapterShoppingCart.refreshData(childList);
            }
        }

    private CartGoodsListener cartGoodsListener;

   public void setCartGoodsListener(CartGoodsListener cartGoodsListener) {
        this.cartGoodsListener = cartGoodsListener;
    }

    public interface CartGoodsListener{
        public void goodsCount(int positionStore,int childP,int goodsId,int count);
        public void goodsDel(int positionStore,int childP,int goodsId);
    }
}


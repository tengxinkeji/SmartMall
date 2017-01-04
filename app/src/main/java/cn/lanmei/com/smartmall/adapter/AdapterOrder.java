package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.myui.MyListView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_order;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterOrder extends MyBaseAdapter<M_order> {


    public AdapterOrder(Context mContext, List<M_order> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){

            convertView=inflater.inflate(R.layout.layout_item_order,parent,false);
            holder = new ViewHolder(position,convertView);

        }else{
            holder=(ViewHolder) convertView.getTag();
            holder.setPst(position);
        }
        M_order order=getItem(position);
        holder.txtStoreName.setText(order.getStoreName());
        holder.txtOrderStauts.setText(order.getOrderStauts()==5?"已完成":"交易中");
        holder.txtGoodsAccountNum.setText("共计"+order.getOrderNum()+"件商品\t总计：");
        holder.txtGoodsAccountMoney.setText("¥"+order.getOrderMoney());
        orderStatus(order.getOrderStauts(),order.getOrderPayStauts(),holder.txtOrderStauts,holder.txtOrderCancle,holder.txtOrderOk,holder.txtGoodsPay);
        holder.txtOrderCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderListener!=null)
                    orderListener.onOrderListenerDel(position,((TextView)v).getText().toString());

            }
        });
        holder.txtOrderOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderListener!=null)
                    orderListener.onOrderListenerOk(position,((TextView)v).getText().toString());

            }
        });
        holder.txtGoodsPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderListener!=null)
                    orderListener.onOrderListenerPay(position,((TextView)v).getText().toString());
            }
        });
        imgLoader.displayImage(order.getGoodsImg(), holder.imgGoods, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public TextView txtStoreName;
        public TextView txtOrderStauts;
        public ImageView imgGoods;
        public MyListView mGoodsListView;
        public TextView txtGoodsAccountNum;
        public TextView txtGoodsAccountMoney;
        public TextView txtOrderCancle;
        public TextView txtOrderOk;
        public TextView txtGoodsPay;

        private int pst;
        private View convertView;
        private AdapterOrderGoods adapterOrderGoods;

        public ViewHolder(int pst_a, View convertView) {
            this.pst = pst_a;
            this.convertView = convertView;
            txtStoreName=(TextView) convertView.findViewById(R.id.txt_store_name);
            txtOrderStauts=(TextView) convertView.findViewById(R.id.txt_order_stauts);
            imgGoods=(ImageView) convertView.findViewById(R.id.goods_img);
            mGoodsListView=(MyListView) convertView.findViewById(R.id.listview);
            txtGoodsAccountNum=(TextView) convertView.findViewById(R.id.txt_goods_count);
            txtGoodsAccountMoney=(TextView) convertView.findViewById(R.id.txt_goods_money);

            txtOrderCancle=(TextView) convertView.findViewById(R.id.txt_order_cancle);
            txtOrderOk=(TextView) convertView.findViewById(R.id.txt_order_ok);
            txtGoodsPay=(TextView) convertView.findViewById(R.id.txt_order_pay);

            adapterOrderGoods=new AdapterOrderGoods(mContext,getItem(pst).getProduct());
            mGoodsListView.setAdapter(adapterOrderGoods);
            convertView.setTag(this);
        }

        public void setPst(int pst_b) {
            this.pst = pst_b;
            adapterOrderGoods.refreshData(getItem(pst).getProduct());
        }
    }

    private OrderListener orderListener;

    public void setOrderListener(OrderListener orderListener) {
        this.orderListener = orderListener;
    }

    public interface OrderListener{
        public void onOrderListenerDel(int position,String name);
        public void onOrderListenerOk(int position,String name);
        public void onOrderListenerPay(int position,String name);
    }

    private void orderStatus(int status,int payStatus,TextView txtStatus,TextView txtDel,TextView txtOk,TextView txtPay){
//        status 订单状态1:生成订单,2：确认订单,3取消订单,4作废订单,5完成订单',
//    `pay_status` tinyint(1) DEFAULT '0' COMMENT '支付状态 0：未支付，1：已支付，2：退款',

        switch (status)
        {
            case 3:
            case 4:
                txtDel.setText("删除订单");
                txtDel.setVisibility(View.VISIBLE);
                txtOk.setVisibility(View.GONE);
                txtPay.setVisibility(View.GONE);
                break;

            case 5:
                txtDel.setText("删除订单");
                txtOk.setText("晒单评价");
                txtDel.setVisibility(View.VISIBLE);
                txtOk.setVisibility(View.VISIBLE);
                txtPay.setVisibility(View.GONE);
                break;
            case 6:
                txtDel.setText("退款中");
                txtOk.setText("联系卖家");
                txtDel.setVisibility(View.VISIBLE);
                txtOk.setVisibility(View.VISIBLE);
                txtPay.setVisibility(View.GONE);
                break;
            default:
                switch (payStatus) {
                case 0:
                    txtDel.setText("取消订单");
                    txtOk.setText("联系卖家");
                    txtPay.setText("去付款");
                    txtDel.setVisibility(View.VISIBLE);
                    txtOk.setVisibility(View.VISIBLE);
                    txtPay.setVisibility(View.VISIBLE);

                    break;
                case 1:
                    txtDel.setText("退款");
                    txtOk.setText("联系卖家");
                    txtPay.setText("确认收货");
                    txtDel.setVisibility(View.VISIBLE);
                    txtOk.setVisibility(View.VISIBLE);
                    txtPay.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            break;
        }

    }

}

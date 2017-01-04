package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_Goods;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterGoods_1 extends MyBaseAdapter<M_Goods> {

    public AdapterGoods_1(Context mContext, List<M_Goods> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_goods,parent,false);
            holder.goodsImg=(ImageView) convertView.findViewById(R.id.goods_img);
            holder.goodsName=(TextView) convertView.findViewById(R.id.goods_name);
            holder.goodsDetail=(TextView) convertView.findViewById(R.id.goods_price);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        M_Goods goods=getItem(position);
        holder.goodsName.setText(goods.getGoodsName());
        holder.goodsDetail.setText("价格："+goods.getGoodsPrice()+"¥");


        imgLoader.displayImage(goods.getGoodsImg(), holder.goodsImg, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public ImageView goodsImg;
        public TextView goodsName;
        public TextView goodsDetail;


    }
}

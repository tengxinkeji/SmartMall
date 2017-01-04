package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.R;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterGoods_2 extends MyBaseAdapter<M_Goods> {

    public AdapterGoods_2(Context mContext, List<M_Goods> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_goods_2,parent,false);
            holder.goodsImg=(ImageView) convertView.findViewById(R.id.goods_img);
            holder.goodsPrice=(TextView) convertView.findViewById(R.id.goods_price);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        M_Goods goods=getItem(position);

        holder.goodsPrice.setText("¥"+goods.getGoodsPrice());


        imgLoader.displayImage(goods.getGoodsImg(), holder.goodsImg, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public ImageView goodsImg;
        public TextView goodsPrice;
    }
}

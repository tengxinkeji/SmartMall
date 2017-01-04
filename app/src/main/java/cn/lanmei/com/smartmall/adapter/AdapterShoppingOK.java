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
public class AdapterShoppingOK extends MyBaseAdapter<M_Goods> {

    public AdapterShoppingOK(Context mContext, List<M_Goods> mLists) {
        super(mContext,mLists);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if (convertView == null){
            convertView=inflater.inflate(R.layout.layout_item_shoping_ok,parent,false);
            holder = getHolder(convertView ,position);
            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
            holder.position=position;
        }
        M_Goods goods=getItem(position);
        holder.goodsName.setText(goods.getGoodsName()+"");
        holder.goodsPrice.setText("¥"+goods.getGoodsPrice());
        holder.goodsParams.setText(goods.getSpec()+"");
        holder.goodsNum.setText(goods.getGoodsCount()+"件");
        imgLoader.displayImage(goods.getGoodsImg(), holder.goodsImg, options, animateFirstListener);


        return convertView;
    }


    public class ViewHolder{
        public int position;
        public ImageView goodsImg;
        public TextView goodsName;
        public TextView goodsPrice;
        public TextView goodsNum;
        public TextView goodsParams;


    }



    public ViewHolder getHolder(View convertView,int position){
        ViewHolder holder = new ViewHolder();
        holder.position=position;
        holder.goodsImg=(ImageView) convertView.findViewById(R.id.img_goods);
        holder.goodsName=(TextView) convertView.findViewById(R.id.txt_goodsname);
        holder.goodsPrice=(TextView) convertView.findViewById(R.id.txt_goodsprice);
        holder.goodsNum=(TextView) convertView.findViewById(R.id.txt_goods_num);
        holder.goodsParams=(TextView) convertView.findViewById(R.id.txt_params);


        return holder;
    }



}

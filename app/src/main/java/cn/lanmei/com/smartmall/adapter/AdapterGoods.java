package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.myui.ImageGoodsView;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterGoods extends MyBaseAdapter<M_Goods> {
    boolean isList;
    String goodsInfo;
    public AdapterGoods(Context mContext, List<M_Goods> mLists,boolean isList) {
        super(mContext,mLists);
        this.isList=isList;
        goodsInfo=mContext.getResources().getString(R.string.goods_reviewnum);

    }

    public AdapterGoods(Context mContext, List<M_Goods> mLists) {
        super(mContext,mLists);
        this.isList=false;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(isList?R.layout.item_list_goods:R.layout.layout_item_goods,parent,false);
            holder.goodsImg=(ImageGoodsView) convertView.findViewById(R.id.goods_img);
            holder.goodsName=(TextView) convertView.findViewById(R.id.goods_name);
            holder.goodsPrice=(TextView) convertView.findViewById(R.id.goods_price);
            holder.goodsSaleNum=(TextView) convertView.findViewById(R.id.goods_sale_num);
            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        M_Goods goods=getItem(position);
        holder.goodsName.setText(goods.getGoodsName());
        if (isList){
            holder.goodsSaleNum.setText(String.format(goodsInfo,goods.getGoodsComments(),goods.getGoodsSale()));
        }else {
            holder.goodsSaleNum.setText("已购买"+goods.getGoodsSale());
        }


        setGoodsPrice(mContext,holder.goodsPrice,goods.getCost_price(),goods.getSell_price());

        imgLoader.displayImage(goods.getGoodsImg(), holder.goodsImg, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public ImageGoodsView goodsImg;
        public TextView goodsName;
        public TextView goodsPrice;
        public TextView goodsSaleNum;

    }

    public static void setGoodsPrice(Context mContext,TextView txt,double cost,double sell){
        String info="V¥" +cost+
                " \t¥ " +sell;
        SpannableString ss=new SpannableString(info);
        ss.setSpan(new AbsoluteSizeSpan(14,true), 0, info.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.txtColor_bar)),0,info.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(10,true), info.indexOf(" "), info.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.txtColor_txt)),info.indexOf(" "), info.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt.setText(ss);
    }
}

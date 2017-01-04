package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.listener.OnCheckSelectListener;
import cn.lanmei.com.smartmall.model.M_Goods;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterGoodsCollect extends MyBaseAdapter<M_Goods> {
    private boolean isEditStatus=false;
    private Map<Integer,Boolean> isCheck=new HashMap<>();

    public AdapterGoodsCollect(Context mContext, List<M_Goods> mLists) {
        super(mContext,mLists);
    }

    public void setIsEdit(boolean isEdit){
        if (isEditStatus^=isEdit){
            isEditStatus=isEdit;
            notifyDataSetChanged();
        }
    }

    public void clearCheck(){
        isCheck.clear();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_goods_collect,parent,false);
            holder.checkBox=(CheckBox) convertView.findViewById(R.id.box_check);
            holder.goodsImg=(ImageView) convertView.findViewById(R.id.img_goods);
            holder.goodsName=(TextView) convertView.findViewById(R.id.goods_name);
            holder.goodsPrice=(TextView) convertView.findViewById(R.id.goods_price);
            holder.goodsReviewNum=(TextView) convertView.findViewById(R.id.txt_goods_review_num);
            holder.goodsBuyNum=(TextView) convertView.findViewById(R.id.txt_goods_buy_num);
            holder.goodsShop=(TextView) convertView.findViewById(R.id.txt_goods_shop);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        if (isEditStatus){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else {
            holder.checkBox.setVisibility(View.GONE);
        }
        if (isCheck.containsKey(position)){
            holder.checkBox.setChecked(isCheck.get(position));
        }else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setTag(position);
        M_Goods goods=getItem(position);
        holder.goodsName.setText(goods.getGoodsName());
        holder.goodsPrice.setText("¥"+goods.getGoodsPrice());
        holder.goodsReviewNum.setText(goods.getGoodsComments()+"");
        holder.goodsBuyNum.setText("已有"+goods.getGoodsSale()+"人购买");
        holder.goodsShop.setText(goods.getGoodsBrand());
        imgLoader.displayImage(goods.getGoodsImg(), holder.goodsImg, options, animateFirstListener);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p= (int) v.getTag();

                if (isCheck.containsKey(p)){
                    ((CheckBox)v).setChecked(!isCheck.get(p));
                }else {
                    ((CheckBox)v).setChecked(true);
                }
                isCheck.put(p,((CheckBox)v).isChecked());

                List<Integer> selectedList=new ArrayList<Integer>();
                Set<Map.Entry<Integer, Boolean>> entrys = isCheck.entrySet();
                for(Map.Entry<Integer, Boolean> entry:entrys){
                    if (entry.getValue()){
                        selectedList.add(getItem(p).getRecordId());
                    }
                }
                if (onCheckSelectListener!=null)
                    onCheckSelectListener.onCheckSelectListener(selectedList);
            }
        });

        return convertView;
    }

    protected class ViewHolder{
        public CheckBox checkBox;
        public ImageView goodsImg;
        public TextView goodsName;
        public TextView goodsPrice;
        public TextView goodsReviewNum;
        public TextView goodsBuyNum;
        public TextView goodsShop;
    }

    private OnCheckSelectListener onCheckSelectListener;

    public void setOnCheckSelectListener(OnCheckSelectListener onCheckSelectListener) {
        this.onCheckSelectListener = onCheckSelectListener;
    }
}

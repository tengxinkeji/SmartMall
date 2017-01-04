package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_categroy;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterCategory extends MyBaseAdapter<M_categroy> {

    public AdapterCategory(Context mContext, List<M_categroy> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.item_category,parent,false);
            holder.goodsImg=(ImageView) convertView.findViewById(R.id.goods_img);
            holder.goodsName=(TextView) convertView.findViewById(R.id.goods_name);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        M_categroy categroy=getItem(position);
        holder.goodsName.setText(categroy.getName());


        imgLoader.displayImage(categroy.getImgUrl(), holder.goodsImg, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public ImageView goodsImg;
        public TextView goodsName;


    }
}

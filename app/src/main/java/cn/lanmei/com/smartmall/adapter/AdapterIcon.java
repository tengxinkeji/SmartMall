package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.myui.CircleImageView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_categroy;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterIcon extends MyBaseAdapter<M_categroy> {
    LinearLayout.LayoutParams lp;
    public AdapterIcon(Context mContext, List<M_categroy> mLists,int width) {
        super(mContext,mLists);
         lp = new LinearLayout.LayoutParams(width, width);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_icon,parent,false);
            holder.img=(CircleImageView) convertView.findViewById(R.id.img_icon);
            holder.title=(TextView) convertView.findViewById(R.id.txt_icon);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        M_categroy categroy=getItem(position);
        holder.title.setText(categroy.getName());
        holder.img.setLayoutParams(lp);
        imgLoader.displayImage(categroy.getImgUrl(), holder.img, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public CircleImageView img;
        public TextView title;


    }

}

package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_ring;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterIcon_ring extends MyBaseAdapter<M_ring> {
    LinearLayout.LayoutParams lp;
    public AdapterIcon_ring(Context mContext, List<M_ring> mLists, int width) {
        super(mContext,mLists);
         lp = new LinearLayout.LayoutParams(width, width);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_icon,parent,false);
            holder.img=(ImageView) convertView.findViewById(R.id.img_icon);
            holder.title=(TextView) convertView.findViewById(R.id.txt_icon);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        M_ring categroy=getItem(position);
        holder.title.setText(categroy.getName());
        holder.img.setLayoutParams(lp);
        imgLoader.displayImage(categroy.getUrl(), holder.img, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public ImageView img;
        public TextView title;


    }

}

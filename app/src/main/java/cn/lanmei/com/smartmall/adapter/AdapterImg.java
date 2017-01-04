package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.lanmei.com.smartmall.R;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterImg extends MyBaseAdapter<String> {

    public AdapterImg(Context mContext, List<String> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ImageView imageView;
        if (convertView == null){

            convertView=inflater.inflate(R.layout.layout_item_img,parent,false);
            imageView=(ImageView) convertView.findViewById(R.id.img);


            convertView.setTag(imageView);

        }else{
            imageView=(ImageView) convertView.getTag();
        }

        imgLoader.displayImage(getItem(position), imageView, options, animateFirstListener);

        return convertView;
    }

}

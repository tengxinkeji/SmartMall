package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.TopicImg;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterImg_2 extends MyBaseAdapter<TopicImg> {

    public AdapterImg_2(Context mContext, List<TopicImg> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ImageView imageView;
        if (convertView == null){

            convertView=inflater.inflate(R.layout.item_img_2,parent,false);
            imageView=(ImageView) convertView.findViewById(R.id.img);


            convertView.setTag(imageView);

        }else{
            imageView=(ImageView) convertView.getTag();
        }
        imgLoader.displayImage(getItem(position).url, imageView, options, animateFirstListener);


        return convertView;
    }

}

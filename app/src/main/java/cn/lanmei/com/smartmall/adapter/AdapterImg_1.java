package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import cn.lanmei.com.smartmall.R;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterImg_1 extends MyBaseAdapter<String> {
    LinearLayout.LayoutParams lp ;

    public AdapterImg_1(Context mContext, List<String> mLists) {
        super(mContext,mLists);

    }
    public AdapterImg_1(Context mContext, List<String> mLists,int imgWidth,int imgheight) {
        super(mContext,mLists);
        lp=new LinearLayout.LayoutParams(imgWidth,imgheight);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ImageView imageView;
        if (convertView == null){

            convertView=inflater.inflate(R.layout.layout_item_img_1,parent,false);
            imageView=(ImageView) convertView.findViewById(R.id.img);
            if (lp!=null)
                imageView.setLayoutParams(lp);

            convertView.setTag(imageView);

        }else{
            imageView=(ImageView) convertView.getTag();
        }


        imgLoader.displayImage(getItem(position)+"", imageView, options, animateFirstListener);

        return convertView;
    }

}

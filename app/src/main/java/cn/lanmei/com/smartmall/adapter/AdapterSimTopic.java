package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.app.Common;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.Activity_topic_detail;
import cn.lanmei.com.smartmall.model.M_topic;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterSimTopic extends MyBaseAdapter<M_topic> {

    public AdapterSimTopic(Context mContext, List<M_topic> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_txt,parent,false);
            holder.txt=(TextView) convertView.findViewById(R.id.txt);
            holder.txt.setLines(1);
            holder.txt.setEllipsize(TextUtils.TruncateAt.END);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.txt.getLayoutParams();
        lp.setMargins(5,10,0,10);
        lp.gravity= Gravity.CENTER_VERTICAL;
        holder.txt.setLayoutParams(lp);
        M_topic topic=getItem(position);
        holder.txt.setText(getTopic(mContext,topic.getRecommend(),topic.getTopic()));

        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toList=new Intent(mContext, Activity_topic_detail.class);
                toList.putExtra(Common.KEY_bundle,getItem(position));
                mContext.startActivity(toList);
            }
        });

        return convertView;
    }

    protected class ViewHolder{

        public TextView txt;


    }

    /***/
    public static SpannableStringBuilder getTopic(Context mContext,int reccomment,String str){
        SpannableStringBuilder spannableString = new SpannableStringBuilder("     "+str);
        switch (reccomment){
            case 1:
                spannableString.setSpan(new ImageSpan(mContext, R.drawable.icon_topic_esse), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE );
                break;
            case 2:
//                spannableString.setSpan(new ImageSpan(mContext, R.drawable.icon_topic_esse), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE );
                break;
            case 3:
                spannableString.setSpan(new ImageSpan(mContext, R.drawable.icon_topic_top), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE );
                break;
            case 4:
                spannableString.setSpan(new ImageSpan(mContext, R.drawable.icon_topic_rec), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE );
                break;
        }




        return spannableString;
    }
}

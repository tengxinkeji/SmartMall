package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_news;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterNews extends MyBaseAdapter<M_news> {

    public AdapterNews(Context mContext, List<M_news> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_newsinfo,parent,false);
            viewHolder.txtTag= (TextView) convertView.findViewById(R.id.txt_tag);
            viewHolder.txtInfo= (TextView) convertView.findViewById(R.id.txt_info);
            viewHolder.txtTime= (TextView) convertView.findViewById(R.id.txt_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.txtTag.setText(getItem(position).getKey());
        viewHolder.txtInfo.setText(Html.fromHtml(getItem(position).getValue()));
        viewHolder.txtTime.setText("发布于:"+getItem(position).getTime());
        return convertView;
    }

    protected class ViewHolder{
        public TextView txtTag;
        public TextView txtInfo;
        public TextView txtTime;

    }
}

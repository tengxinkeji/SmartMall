package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_device_info;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterDevInfo extends MyBaseAdapter<M_device_info> {

    public AdapterDevInfo(Context mContext, List<M_device_info> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_info,parent,false);
            viewHolder.txtTag= (TextView) convertView.findViewById(R.id.txt_tag);
            viewHolder.txtInfo= (TextView) convertView.findViewById(R.id.txt_info);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.txtTag.setText(getItem(position).getKey());
        viewHolder.txtInfo.setText(getItem(position).getValue());
        return convertView;
    }

    protected class ViewHolder{
        public TextView txtTag;
        public TextView txtInfo;


    }
}

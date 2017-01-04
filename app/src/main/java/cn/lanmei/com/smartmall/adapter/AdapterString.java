package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;

/**
 * Created by Administrator on 2016/8/16.
 */

public class AdapterString extends BaseAdapter {
    private List<String> lists;
    private LayoutInflater inflater;
    public AdapterString(Context mContext,List<String> lists) {
        super();
        inflater=LayoutInflater.from(mContext);
        this.lists=lists;
    }

    @Override
    public int getCount() {
        if (lists==null)
            return 0;
        return lists.size();
    }

    @Override
    public String getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txt;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.layout_item_txt,parent,false);
            txt= (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(txt);
        }else{
            txt= (TextView) convertView.getTag();
        }
        txt.setText(getItem(position));
        return convertView;
    }
}

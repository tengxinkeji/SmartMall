package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.smarthome.device.Dev;

import java.util.List;

import cn.lanmei.com.smartmall.R;


/**
 * 我的设备---列表
 * Created by Administrator on 2015/11/16.
 */
public class AdapterDevice extends MyBaseAdapter<Dev>{
    public AdapterDevice(Context mContext, List<Dev> lists) {
        super(mContext, lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_device,parent,false);
            convertView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.bg_device_item));
            holder.txtName=(TextView) convertView.findViewById(R.id.device_name);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        Dev mDevices=getItem(position);

        holder.txtName.setText(mDevices.getNickName());

        return convertView;
    }

    protected class ViewHolder{

        public TextView txtName;
    }
}

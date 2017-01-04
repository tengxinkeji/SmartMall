package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.espressif.iot.esptouch.EsptouchResult;

import java.util.List;

import cn.lanmei.com.smartmall.R;


/**
 * 我的设备---列表
 * Created by Administrator on 2015/11/16.
 */
public class AdapterWifiDevices extends MyBaseAdapter<EsptouchResult>{
    public AdapterWifiDevices(Context mContext, List<EsptouchResult> lists) {
        super(mContext, lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_device,parent,false);
            holder.boxCheck=(CheckBox) convertView.findViewById(R.id.device_check);
            holder.txtName=(TextView) convertView.findViewById(R.id.device_name);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        EsptouchResult mDevices=getItem(position);
        holder.boxCheck.setVisibility(View.GONE);
        holder.txtName.setText(mDevices.getBssid()+"\t\t"+mDevices.getInetAddress().getHostAddress());

        return convertView;
    }

    protected class ViewHolder{
        public CheckBox boxCheck;
        public TextView txtName;
    }
}

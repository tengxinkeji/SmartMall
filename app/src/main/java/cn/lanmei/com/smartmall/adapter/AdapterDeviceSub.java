package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.myui.RoundImageView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_DevSub;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterDeviceSub extends MyBaseAdapter<M_DevSub> {

    public AdapterDeviceSub(Context mContext, List<M_DevSub> devSubs) {
        super(mContext,devSubs);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_device_sub,parent,false);
            holder.img=(RoundImageView) convertView.findViewById(R.id.img_pic);
            holder.txtUserType=(TextView) convertView.findViewById(R.id.txt_user_type);
            holder.txtName=(TextView) convertView.findViewById(R.id.txt_username);
            holder.txtAddtime=(TextView) convertView.findViewById(R.id.txt_addtime);


            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        M_DevSub devSub=getItem(position);
        holder.txtName.setText(devSub.getNickname()+"");
        holder.txtAddtime.setText(devSub.getAddTime()+"");

        holder.txtUserType.setVisibility(devSub.getDev().getMaster()==1?View.VISIBLE:View.GONE);

        imgLoader.displayImage(devSub.getImg(), holder.img, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public RoundImageView img;
        public TextView txtName;
        public TextView txtUserType;
        public TextView txtAddtime;


    }
}

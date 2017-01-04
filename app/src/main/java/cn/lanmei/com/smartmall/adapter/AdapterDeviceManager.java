package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_DevSub;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterDeviceManager extends MyBaseAdapter<M_DevSub> {

    public AdapterDeviceManager(Context mContext, List<M_DevSub> devSubs) {
        super(mContext,devSubs);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_device_manager,parent,false);
            holder.img=(ImageView) convertView.findViewById(R.id.img_pic);
            holder.txtUserType=(TextView) convertView.findViewById(R.id.txt_user_type);
            holder.txtName=(TextView) convertView.findViewById(R.id.txt_username);
            holder.txtAddtime=(TextView) convertView.findViewById(R.id.txt_addtime);
            holder.txtFlag=(TextView) convertView.findViewById(R.id.txt_flag);
            holder.imgDel=(ImageView) convertView.findViewById(R.id.img_del);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        M_DevSub devSub=getItem(position);
        holder.txtName.setText(devSub.getDev().getNickName()
                +"\n昵称："+devSub.getNickname()
                +"\n账号："+devSub.getUsername());
        holder.txtAddtime.setText(devSub.getAddTime()+"");

        final boolean isMaster=devSub.getDev().getMaster()==1;
        holder.txtUserType.setVisibility(isMaster?View.VISIBLE:View.GONE);
        holder.imgDel.setVisibility(isMaster?View.GONE:View.VISIBLE);
        holder.txtFlag.setText(isMaster?"权限转让":"备注");

        final String recordDeviceId=devSub.getId()+"";
        final String mid = devSub.getUserId();

        holder.txtFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMaster){
                    if (deviceManagerListener!=null)
                        deviceManagerListener.deviceManagerAccess(recordDeviceId);
                }else{
                    if (deviceManagerListener!=null)
                        deviceManagerListener.deviceManagerRemark(recordDeviceId);
                }
            }
        });

        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceManagerListener!=null)
                    deviceManagerListener.deviceManagerDel(recordDeviceId,mid);
            }
        });

        imgLoader.displayImage(devSub.getImg(), holder.img, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public ImageView img;
        public TextView txtName;
        public TextView txtUserType;
        public TextView txtAddtime;
        public TextView txtFlag;
        public ImageView imgDel;

    }

    private DeviceManagerListener deviceManagerListener;

    public void setDeviceManagerListener(DeviceManagerListener deviceManagerListener) {
        this.deviceManagerListener = deviceManagerListener;
    }

    public interface DeviceManagerListener{
        public void deviceManagerDel(String rid,String mid);
        public void deviceManagerRemark(String rid);
        public void deviceManagerAccess(String rid);
    }
}

package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lanmei.com.smartmall.model.M_DevSub;
import cn.lanmei.com.smartmall.R;


/**
 * 我的设备---列表
 * Created by Administrator on 2015/11/16.
 */
public class AdapterDeviceAccessManager extends MyBaseAdapter<M_DevSub>{

    private Map<Integer, Boolean> isSelected;
    public AdapterDeviceAccessManager(Context mContext, List<M_DevSub> lists) {
        super(mContext, lists);
        isSelected=new HashMap<>();
        initSelected();
    }

    @Override
    public void refreshData(List<M_DevSub> lists) {
        this.lists=lists;
        initSelected();
        super.refreshData(lists);

    }

    private void initSelected(){
        for (int i=0;i<lists.size();i++){
            isSelected.put(i,false);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_device_access,parent,false);
            holder.img=(ImageView) convertView.findViewById(R.id.img_pic);
            holder.boxCheck=(CheckBox) convertView.findViewById(R.id.device_check);
            holder.txtName=(TextView) convertView.findViewById(R.id.txt_username);
            holder.txtAddtime=(TextView) convertView.findViewById(R.id.txt_addtime);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        M_DevSub mDevices=getItem(position);
        holder.boxCheck.setChecked(isSelected.get(position));
        holder.txtName.setText(mDevices.getNickname());
        holder.txtAddtime.setText(mDevices.getAddTime());
        imgLoader.displayImage(mDevices.getImg(), holder.img, options, animateFirstListener);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 当前点击的CB
                boolean cu = !isSelected.get(position);
                // 先将所有的置为FALSE
                for(Integer p : isSelected.keySet()) {
                    isSelected.put(p, false);
                }
                // 再将当前选择CB的实际状态
                isSelected.put(position, cu);
                AdapterDeviceAccessManager.this.notifyDataSetChanged();
                if (checkChangeListener!=null){
                    checkChangeListener.checkChangeListener(position,cu);
                }

            }
        });
        return convertView;
    }

    protected class ViewHolder{
        public ImageView img;
        public CheckBox boxCheck;
        public TextView txtName;
        public TextView txtAddtime;
    }
    private CheckChangeListener checkChangeListener;

    public void setCheckChangeListener(CheckChangeListener checkChangeListener) {
        this.checkChangeListener = checkChangeListener;
    }

    public interface CheckChangeListener{
        public void checkChangeListener(int position,boolean isCheck);
    }
}

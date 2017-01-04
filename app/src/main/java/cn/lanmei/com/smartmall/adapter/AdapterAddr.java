package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_address;


/**
 * 收货地址列表
 * Created by Administrator on 2015/11/16.
 */
public class AdapterAddr extends MyBaseAdapter<M_address>{
    public AdapterAddr(Context mContext, List<M_address> lists) {
        super(mContext, lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            convertView=inflater.inflate(R.layout.layout_item_addr_ddddd,parent,false);
            holder = new ViewHolder(convertView,position);

        }else{
            holder=(ViewHolder) convertView.getTag();
            holder.setPosition(position);
        }
        M_address mAddress=getItem(position);
        holder.boxCheck.setChecked(mAddress.getIsDefault()==1);
        holder.txtName.setText(mAddress.getAccept_name());
        holder.txtPhone.setText(mAddress.getMobile());
        holder.txtAddr.setText(mAddress.getAddress());

        return convertView;
    }

    protected class ViewHolder{

        public int position;
        public CheckBox boxCheck;
        public TextView txtName;
        public TextView txtPhone;
        public TextView txtAddr;
        public TextView txtCheck;
        public ImageView imgEdit;
        public ImageView imgDel;

        public ViewHolder(View convertView, final int position) {
            this.position=position;
            boxCheck=(CheckBox) convertView.findViewById(R.id.box_check);
            txtName=(TextView) convertView.findViewById(R.id.txt_username);
            txtPhone=(TextView) convertView.findViewById(R.id.txt_userphone);
            txtAddr=(TextView) convertView.findViewById(R.id.txt_addr);
            txtCheck=(TextView) convertView.findViewById(R.id.txt_check);
            imgEdit=(ImageView) convertView.findViewById(R.id.img_edit);
            imgDel=(ImageView) convertView.findViewById(R.id.img_del);

            boxCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addressListener!=null)
                        addressListener.addressDefaultListener(position,getItem(position).getId(),boxCheck.isChecked());
                    for (int i=0;i<getCount();i++){
                        getItem(i).setIsDefault(0);
                    }
                    getItem(position).setIsDefault(boxCheck.isChecked()?1:0);
                }
            });

            txtCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boxCheck.setChecked(!boxCheck.isChecked());
                    if (addressListener!=null)
                        addressListener.addressDefaultListener(position,getItem(position).getId(),boxCheck.isChecked());
                    for (int i=0;i<getCount();i++){
                        getItem(i).setIsDefault(0);
                    }
                    getItem(position).setIsDefault(boxCheck.isChecked()?1:0);
                }
            });
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addressListener!=null)
                        addressListener.addressEditListener(position,getItem(position).getId());
                }
            });
            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addressListener!=null)
                        addressListener.addressDelListener(position,getItem(position).getId());
                }
            });

            convertView.setTag(this);
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
    private AddressListener  addressListener;

    public void setAddressListener(AddressListener addressListener) {
        this.addressListener = addressListener;
    }

    public interface AddressListener{
        public void addressDefaultListener(int position,int id,boolean isDefault);
        public void addressEditListener(int position,int id);
        public void addressDelListener(int position,int id);
    }
}

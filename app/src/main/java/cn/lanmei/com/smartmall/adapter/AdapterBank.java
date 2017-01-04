package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_bank;


/**
 * 收货地址列表
 * Created by Administrator on 2015/11/16.
 */
public class AdapterBank extends MyBaseAdapter<M_bank>{
    public AdapterBank(Context mContext, List<M_bank> lists) {
        super(mContext, lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            convertView=inflater.inflate(R.layout.layout_item_bank,parent,false);
            holder = new ViewHolder();
            holder.bg= convertView.findViewById(R.id.layout_bank);
            holder.imgBank=(ImageView) convertView.findViewById(R.id.img_bank);
            holder.txtBankName=(TextView) convertView.findViewById(R.id.txt_bank_name);
            holder.txtBankNo=(TextView) convertView.findViewById(R.id.txt_bank_no);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();

        }
        M_bank mBank=getItem(position);

        holder.txtBankName.setText(mBank.getBanks_name());
        holder.txtBankNo.setText(mBank.getBanks_no());
        switch (mBank.getBanks_id()){
            case 1:
                holder.imgBank.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.icon_gs));
                break;
            case 2:
                holder.imgBank.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.icon_ny));
                break;
            case 3:
                holder.imgBank.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.icon_js));
                break;
            case 4:
                holder.imgBank.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.icon_cn));
                break;
        }

        if (position%2==0){
            holder.bg.setBackgroundResource(R.drawable.rectangel_select);
        }else {
            holder.bg.setBackgroundResource(R.drawable.rectangel_red);
        }

        return convertView;
    }

    protected class ViewHolder{
        public View bg;
        public ImageView imgBank;
        public TextView txtBankName;
        public TextView txtBankNo;


    }

}

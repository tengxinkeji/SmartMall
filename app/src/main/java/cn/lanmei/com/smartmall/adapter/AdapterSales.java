package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.myui.RoundImageView;

import java.util.List;

import cn.lanmei.com.smartmall.model.M_user;
import cn.lanmei.com.smartmall.R;


/**
 * 分销商列表
 */
public class AdapterSales extends MyBaseAdapter<M_user> {
    int rankNum;

    public AdapterSales(Context mContext, List<M_user> mLists,int rankNum) {
        super(mContext,mLists);
        this.rankNum=rankNum;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_sales,parent,false);
            holder.imgHead=(RoundImageView) convertView.findViewById(R.id.roundImageView);
            holder.txtName=(TextView) convertView.findViewById(R.id.txt_name);
            holder.imgRight=(ImageView) convertView.findViewById(R.id.img_right);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        M_user mUser=getItem(position);
        holder.imgRight.setVisibility(rankNum==1?View.VISIBLE:View.GONE);
        holder.txtName.setText(mUser.getName());

        imgLoader.displayImage(mUser.getUrlHead(), holder.imgHead, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public RoundImageView imgHead;
        public TextView txtName;
        public ImageView imgRight;


    }
}

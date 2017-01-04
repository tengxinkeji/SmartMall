package cn.lanmei.com.smartmall.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.app.StaticMethod;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_merchant;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterMerchant extends MyBaseAdapter<M_merchant> {

    public AdapterMerchant(Context mContext, List<M_merchant> mLists) {
        super(mContext, mLists);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_item_merchant, parent, false);
            holder.imgMerchant = (ImageView) convertView.findViewById(R.id.img_merchant);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtAddr = (TextView) convertView.findViewById(R.id.txt_addr);
            holder.txtDistance = (TextView) convertView.findViewById(R.id.txt_distance);
            holder.vCall = convertView.findViewById(R.id.layout_call);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final M_merchant mMerchant = getItem(position);
        holder.txtName.setText(mMerchant.getName() + "");
        holder.txtAddr.setText(mMerchant.getAddress());
        holder.txtDistance.setText(mMerchant.getDistance() + "M");

        if (mMerchant.getType().equals("service")) {
            holder.vCall.setVisibility(View.VISIBLE);
        } else {
            holder.vCall.setVisibility(View.GONE);
        }
        holder.vCall.setTag(mMerchant.getTel());
        imgLoader.displayImage(mMerchant.getPic(), holder.imgMerchant, options, animateFirstListener);

        holder.vCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + v.getTag().toString()));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    StaticMethod.showInfo(mContext,"请开启电话权限");
                    return;
                }
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    protected class ViewHolder{
        public ImageView imgMerchant;
        public TextView txtName;
        public TextView txtAddr;
        public TextView txtDistance;
        public View vCall;

    }
}

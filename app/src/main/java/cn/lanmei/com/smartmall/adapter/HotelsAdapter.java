package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import cn.lanmei.com.smartmall.model.StructorHotel;
import cn.lanmei.com.smartmall.R;


/**
 * Created by Administrator on 2016/5/16.
 */
public class HotelsAdapter extends MyBaseAdapter<StructorHotel> {

    public HotelsAdapter(Context mContext, List<StructorHotel> structorHotels) {
        super(mContext,structorHotels);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_ada_item_hotel,parent,false);
            holder.hotelImg=(ImageView) convertView.findViewById(R.id.hotel_img);
            holder.hotelName=(TextView) convertView.findViewById(R.id.hotel_name);
            holder.hotelPrice=(TextView) convertView.findViewById(R.id.hotel_price);
            holder.hotelDistance=(TextView) convertView.findViewById(R.id.hotel_distance);
            holder.hotelAddr=(TextView) convertView.findViewById(R.id.hotel_addr);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        StructorHotel product=getItem(position);
        holder.hotelName.setText(product.getHotelName());
        holder.hotelPrice.setText("¥"+product.getHotelPprice());
        int distance=product.getHotelDistance();
        if (distance>1000)
            holder.hotelDistance.setText("大约"+(distance/1000)+"千米");
        else
            holder.hotelDistance.setText("距离"+distance+"米");
        holder.hotelAddr.setText(product.getHotelAddr());

        imgLoader.displayImage(product.getHotelImg(), holder.hotelImg, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public ImageView hotelImg;
        public TextView hotelName;
        public TextView hotelPrice;
        public TextView hotelDistance;
        public TextView hotelAddr;

    }
}

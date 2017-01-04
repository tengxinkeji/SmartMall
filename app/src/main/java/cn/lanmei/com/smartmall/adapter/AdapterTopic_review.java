package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.myui.CircleImageView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_Review;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterTopic_review extends MyBaseAdapter<M_Review> {

    public AdapterTopic_review(Context mContext, List<M_Review> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            convertView=inflater.inflate(R.layout.item_topic_head,parent,false);
            holder = new ViewHolder(position,convertView);
            holder.initUi();
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
            holder.setPosition(position);
        }
        M_Review topic=getItem(position);
        holder.txtName.setText(topic.getName());
        holder.txtAddr.setText(topic.getArea());
        holder.txtTime.setText(topic.getTime());
        holder.txtTopic.setText(topic.getContent());



        imgLoader.displayImage(topic.getImg(), holder.imgHead, options, animateFirstListener);

        return convertView;
    }

    protected class ViewHolder{
        public CircleImageView imgHead;
        public TextView txtName;
        public TextView txtAddr;
        public TextView txtTime;
        public TextView txtTopic;
        public int position;
        public View convertView;


        public ViewHolder(int position, View convertView) {
            this.position = position;
            this.convertView = convertView;
        }

        public void initUi(){
            imgHead=(CircleImageView) convertView.findViewById(R.id.img_head);
            txtName=(TextView) convertView.findViewById(R.id.txt_name);
            txtAddr=(TextView) convertView.findViewById(R.id.txt_addr);
            txtTime=(TextView) convertView.findViewById(R.id.txt_time);

            txtTopic=(TextView) convertView.findViewById(R.id.txt_topic);

        }

        public void setPosition(int position) {
            this.position = position;

        }
    }
}

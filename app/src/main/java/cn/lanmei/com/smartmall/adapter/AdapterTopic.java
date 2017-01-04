package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.common.app.Common;
import com.common.myui.CircleImageView;
import com.common.myui.MyGridView;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.Activity_pic;
import cn.lanmei.com.smartmall.main.Activity_topic_detail;
import cn.lanmei.com.smartmall.model.M_topic;
import cn.lanmei.com.smartmall.model.TopicImg;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterTopic extends MyBaseAdapter<M_topic> {

    public AdapterTopic(Context mContext, List<M_topic> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            convertView=inflater.inflate(R.layout.layout_item_topic,parent,false);
            holder = new ViewHolder(position,convertView);
            holder.initUi();
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
            holder.setPosition(position);
        }
        M_topic topic=getItem(position);
        holder.txtName.setText(topic.getTopicName());
        holder.txtAddr.setText(topic.getTopicAddr());
        holder.txtTime.setText(topic.getTopicTime());

        holder.txtTopic.setText(AdapterSimTopic.getTopic(mContext,topic.getRecommend(),topic.getTopic()));

        holder.txtTopic_1.setText(topic.getTopicTypeName());
        holder.txtTopic_2.setText(topic.getTopic_2()+"");
        holder.txtTopic_3.setText(topic.getTopic_3()+"");

        imgLoader.displayImage(topic.getTopicUrl(), holder.imgHead, options, animateFirstListener);


        holder.convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toList=new Intent(mContext, Activity_topic_detail.class);
                toList.putExtra(Common.KEY_bundle,getItem(position));
                mContext.startActivity(toList);
            }
        });

        return convertView;
    }

    protected class ViewHolder{
        public CircleImageView imgHead;
        public TextView txtName;
        public TextView txtAddr;
        public TextView txtTime;

        public TextView txtTopic;
        public MyGridView myGridView;
        public TextView txtTopic_1;
        public TextView txtTopic_2;
        public TextView txtTopic_3;
        public int position;
        public View convertView;

        private AdapterImg_Topic adapterImg_topic;
        private ArrayList<String> imgShow;
        public ViewHolder(int position, View convertView) {
            this.position = position;
            this.convertView = convertView;
            imgShow = new ArrayList<>();
        }

        public void initUi(){
            imgHead=(CircleImageView) convertView.findViewById(R.id.img_head);
            txtName=(TextView) convertView.findViewById(R.id.txt_name);
            txtAddr=(TextView) convertView.findViewById(R.id.txt_addr);
            txtTime=(TextView) convertView.findViewById(R.id.txt_time);

            txtTopic=(TextView) convertView.findViewById(R.id.txt_topic);
            myGridView=(MyGridView) convertView.findViewById(R.id.gridview);
            txtTopic_1=(TextView) convertView.findViewById(R.id.txt_1);
            txtTopic_2=(TextView) convertView.findViewById(R.id.txt_2);
            txtTopic_3=(TextView) convertView.findViewById(R.id.txt_3);
            adapterImg_topic=new AdapterImg_Topic(mContext,getItem(position).imgs);
            imgShow.clear();
            for(TopicImg img:getItem(position).imgs){
                imgShow.add(img.url);
            }
            myGridView.setAdapter(adapterImg_topic);
            myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent toPic=new Intent(mContext, Activity_pic.class);
                    toPic.putStringArrayListExtra(Common.KEY_listString,imgShow);
                    toPic.putExtra(Common.KEY_position,position);
                    mContext.startActivity(toPic);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
            imgShow.clear();
            for(TopicImg img:getItem(position).imgs){
                imgShow.add(img.url);
            }
            adapterImg_topic= (AdapterImg_Topic) myGridView.getAdapter();
            adapterImg_topic.refreshData(getItem(position).imgs);
        }
    }
}

package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.myui.CircleImageView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_ring;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterRing extends MyBaseAdapter<M_ring> {
    boolean isAttention;
    public AdapterRing(Context mContext, List<M_ring> mLists,boolean isAttention) {
        super(mContext,mLists);
        this.isAttention=isAttention;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_ring,parent,false);
            viewHolder.imgHead=(CircleImageView) convertView.findViewById(R.id.img_ring);
            viewHolder.txtName=(TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.txtDetail=(TextView) convertView.findViewById(R.id.txt_detail);
            viewHolder.imgMore=(ImageView) convertView.findViewById(R.id.img_add);


            convertView.setTag(viewHolder);

        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }

        viewHolder.imgMore.setVisibility(isAttention?View.GONE:View.VISIBLE);

        M_ring item=getItem(position);
        viewHolder.txtName.setText(item.getName());
        viewHolder.txtDetail.setText(item.getDetail());
        imgLoader.displayImage(item.getUrl(), viewHolder.imgHead, options, animateFirstListener);
        viewHolder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onListenerAddAttention!=null){
                    onListenerAddAttention.onListenerAdd(position);
                }
            }
        });
        return convertView;
    }
    protected class ViewHolder{

        public CircleImageView imgHead;
        public TextView txtName;
        public TextView txtDetail;
        public ImageView imgMore;

    }

    OnListenerAddAttention onListenerAddAttention;

    public void setOnListenerAddAttention(OnListenerAddAttention onListenerAddAttention) {
        this.onListenerAddAttention = onListenerAddAttention;
    }

    public interface OnListenerAddAttention{
        public void onListenerAdd(int position);
    }
}

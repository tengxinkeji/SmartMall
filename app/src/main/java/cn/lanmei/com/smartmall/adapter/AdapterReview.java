package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.common.myui.MyGridView;

import java.util.List;

import cn.lanmei.com.smartmall.model.M_Review;
import cn.lanmei.com.smartmall.R;


/**
 *
 */
public class AdapterReview extends MyBaseAdapter<M_Review> {

    public AdapterReview(Context mContext, List<M_Review> mLists) {
        super(mContext,mLists);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_review,parent,false);
            viewHolder.img=(ImageView) convertView.findViewById(R.id.rev_img);
            viewHolder.name=(TextView) convertView.findViewById(R.id.rev_name);
            viewHolder.ratingBar=(RatingBar) convertView.findViewById(R.id.ratingbar);
            viewHolder.time=(TextView) convertView.findViewById(R.id.rev_time);
            viewHolder.content=(TextView) convertView.findViewById(R.id.rev_content);
            viewHolder.myGridView=(MyGridView) convertView.findViewById(R.id.grid_rev_img);
            viewHolder.goodsTime=(TextView) convertView.findViewById(R.id.rev_goods_time);
            viewHolder.like=(TextView) convertView.findViewById(R.id.rev_like);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        M_Review mReview=getItem(position);
        viewHolder.name.setText(mReview.getName());
        viewHolder.time.setText(mReview.getTime());
        viewHolder.content.setText(mReview.getContent());
        viewHolder.goodsTime.setText("价格：¥"+mReview.getGoodsTime());
        viewHolder.like.setText(""+mReview.getLike());
        viewHolder.ratingBar.setProgress(mReview.getHeart());
        imgLoader.displayImage(mReview.getImg(),viewHolder.img,options,animateFirstListener);
        if (mReview.getImgs()!=null&&mReview.getImgs().size()>0){
            AdapterImg adapterImg =new AdapterImg(mContext,mReview.getImgs());
            viewHolder.myGridView.setAdapter(adapterImg);
        }

        return convertView;
    }
    protected class ViewHolder{
        public ImageView img;
        public TextView name;
        public RatingBar ratingBar;
        public TextView time;
        public TextView content;
        public MyGridView myGridView;
        public TextView goodsTime;
        public TextView like;

    }
}

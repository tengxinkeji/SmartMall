package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.common.app.MyApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.nostra13.universalimageloader.core.display.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


import java.util.List;

import cn.lanmei.com.smartmall.main.BaseActivity;


/**
 * 分类列表
 * Created by Administrator on 2015/11/16.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter{
    public Context mContext;
    public List<T> lists;
    public LayoutInflater inflater;
    public ImageLoader imgLoader;
    public DisplayImageOptions options;
    public ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    public MyBaseAdapter(Context mContext, List<T> lists) {
        this.mContext = mContext;
        this.lists = lists;
        inflater=LayoutInflater.from(mContext);
        imgLoader =((BaseActivity)mContext).getImageLoader();
        int placeholder = MyApplication.getInstance().getPlaceholder();
        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeholder)
                .showImageForEmptyUri(placeholder)
                .showImageOnFail(placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .build();

    }

    public void refreshData(List<T> lists){
        if (lists!=null){
            this.lists=lists;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (lists==null)
            return 0;
        else
            return lists.size();
    }

    @Override
    public T getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_anchor_item,parent,false);
            holder.img=(ImageView) convertView.findViewById(R.id.anchor_img_head);
            holder.txt=(TextView) convertView.findViewById(R.id.anchor_txt_name);
            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }


        imgLoader .displayImage(anchorModel.getImgUrl(), holder.img, options, animateFirstListener);


        return convertView;
    }*/

    /*protected class ViewHolder{
        public ImageView img;
        public TextView txt;
    }*/
}

package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.lanmei.com.smartmall.R;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterImgUpload extends MyBaseAdapter<String> {

    public AdapterImgUpload(Context mContext, List<String> mLists) {
        super(mContext,mLists);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_img,parent,false);
            viewHolder.img=(ImageView) convertView.findViewById(R.id.img);
            viewHolder.imgDel=(ImageView) convertView.findViewById(R.id.img_del);
            viewHolder.img.setTag(position);
            viewHolder.imgDel.setTag(position);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
            viewHolder.img.setTag(position);
            viewHolder.imgDel.setTag(position);
        }

        imgLoader.displayImage(getItem(position), viewHolder.img, options, animateFirstListener);

        if (position==(lists.size()-1)){
            viewHolder.imgDel.setVisibility(View.GONE);
        }else{
            viewHolder.imgDel.setVisibility(View.VISIBLE);
        }
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadGridListener!=null&&((int)v.getTag()==(lists.size()-1)))
                    uploadGridListener.uploadImgAdd();
            }
        });

        viewHolder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadGridListener!=null&&((int)v.getTag()!=(lists.size()-1)))
                    uploadGridListener.uploadImgDel((int)v.getTag());
            }
        });
        return convertView;
    }

    private UploadGridListener uploadGridListener;

    public void setUploadGridListener(UploadGridListener uploadGridListener) {
        this.uploadGridListener = uploadGridListener;
    }

    public interface UploadGridListener{
        public void uploadImgAdd();
        public void uploadImgDel(int position);
    }

    protected class ViewHolder{

        public ImageView img;
        public ImageView imgDel;
    }
}

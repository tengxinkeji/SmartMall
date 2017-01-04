package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.presenter.TagInfo;

/**
 * Created by Administrator on 2016/8/16.
 */

public class AdapterCustomChild extends MyBaseAdapter<TagInfo> {
    private int curPosition;


    private List<TagInfo> lists;
    private LayoutInflater inflater;

    public AdapterCustomChild(Context mContext, List<TagInfo> lists) {
        super(mContext, lists);
        inflater=LayoutInflater.from(mContext);
        this.lists=lists;

    }


    @Override
    public int getCount() {
        if (lists==null)
            return 0;
        return lists.size();
    }

    @Override
    public TagInfo getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView txt;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_txt,parent,false);
            txt= (TextView) convertView.findViewById(R.id.txt);
            convertView.setTag(txt);
        }else{
            txt= (TextView) convertView.getTag();
        }

        txt.setTag(getItem(position));
        txt.setText(getItem(position).getText());
        if (getItem(position).isSelect()){
            txt.setSelected(true);
        }else {
            txt.setSelected(false);
        }

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curPosition=position;
                boolean cur=lists.get(curPosition).isSelect();
                for(int i=0;i<getCount();i++){
                    lists.get(i).setSelect(false);
                }
                lists.get(position).setSelect(!cur);
                notifyDataSetChanged();
                if (onClickListion!=null){
                    onClickListion.onPositon(position,getItem(position).isSelect());
                }
            }
        });
        return convertView;
    }
    private OnClickListion onClickListion;

    public void setOnClickListion(OnClickListion onClickListion) {
        this.onClickListion = onClickListion;
    }

    public interface OnClickListion{
        public void onPositon(int positon,boolean isSelect);
    }
}

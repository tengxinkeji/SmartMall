package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_categroy;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterGoodsCategory extends MyBaseAdapter<M_categroy> {
    private int curPosition;
    public HashMap<Integer, Boolean> isselected;
    private Drawable drawable;

    public AdapterGoodsCategory(Context mContext, List<M_categroy> mLists) {
        super(mContext,mLists);
        drawable = ContextCompat.getDrawable(mContext,R.drawable.icon_right_gray);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        curPosition=0;
        isselected = new HashMap<Integer, Boolean>();
        initData(mLists.size());
    }

    public void initData(int count) {
        for (int i = 0; i < count; i++) {
            if(i==curPosition)
                isselected.put(i, true);
            else
                isselected.put(i, false);
        }
    }

    @Override
    public void refreshData(List<M_categroy> list) {
        this.curPosition=0;
        initData(list.size());
        super.refreshData(list);
    }

    public void refreshCurPosition(int position){
        this.curPosition=position;
        initData(getCount());
        notifyDataSetChanged();
    }

    public int getCurPosition() {
        return curPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtName=null;
        if (convertView == null){
            convertView=inflater.inflate(R.layout.layout_item_txt,parent,false);
            txtName=(TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(txtName);
        }else{
            txtName=(TextView) convertView.getTag();
        }
        txtName.setLines(1);
        txtName.setEllipsize(TextUtils.TruncateAt.END);
        M_categroy goods=getItem(position);
        txtName.setText(goods.getName());
        txtName.setCompoundDrawables(null,null,drawable,null);

        if (isselected.get(position)){
            txtName.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            txtName.setBackgroundColor(ContextCompat.getColor(mContext,R.color.txtColor_bar));
        }else {
            txtName.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));
            txtName.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        }
        return convertView;
    }

}

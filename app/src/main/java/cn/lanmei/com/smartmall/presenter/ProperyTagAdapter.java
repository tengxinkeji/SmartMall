package cn.lanmei.com.smartmall.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.presenter.ui.OnInitSelectedPosition;



/**
 *商品属性
 */
public class ProperyTagAdapter extends BaseAdapter implements OnInitSelectedPosition {

    private Context mContext;
    private List<TagInfo> mDataList;

    public ProperyTagAdapter(Context mContext, List<TagInfo> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, null);

        TextView textView = (TextView) view.findViewById(R.id.tv_tag);
        TagInfo tagInfo = mDataList.get(position);
//        textView.setEnabled(false);
//        textView.setFocusable(false);
//        textView.setClickable(false);
        textView.setText(tagInfo.getText());
        view.setTag(tagInfo);
        return view;
    }

    public void isSelect(int positon) {
//        Observable.from(mDataList).subscribe(tagInfo -> {
//            tagInfo.setSelect(false);
//        });
        for(int i=0;i<getCount();i++){
            mDataList.get(i).setSelect(false);
        }
        mDataList.get(positon).setSelect(true);
    }

    @Override
    public boolean isSelectedPosition(int position) {
        if (position % 2 == 0) {
            return true;
        }
        return false;
    }
}

package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.app.degexce.L;

import java.util.List;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_goods_version;
import cn.lanmei.com.smartmall.presenter.ProperyTagAdapter;
import cn.lanmei.com.smartmall.presenter.TagInfo;
import cn.lanmei.com.smartmall.presenter.ui.FlowTagLayout;
import cn.lanmei.com.smartmall.presenter.ui.OnTagSelectListener;

public class AdapterGoodsVersion extends MyBaseAdapter<M_goods_version> {
        private Drawable drawableRight;
        private Drawable drawableUp;
        public AdapterGoodsVersion(Context mContext, List<M_goods_version> mLists) {
            super(mContext,mLists);
            drawableRight= ContextCompat.getDrawable(mContext, R.drawable.icon_right_gray);
            drawableRight.setBounds(0,0,drawableRight.getMinimumWidth(),drawableRight.getMinimumHeight());
            drawableUp= ContextCompat.getDrawable(mContext,R.drawable.icon_pagedown_gray);
            drawableUp.setBounds(0,0,drawableUp.getMinimumWidth(),drawableUp.getMinimumHeight());
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if (convertView == null){
                convertView=inflater.inflate(R.layout.layout_item_goodsversion,parent,false);
                holder = new ViewHolder(convertView,position);

            }else{
                holder=(ViewHolder) convertView.getTag();
                holder.refresh(position);
            }
            M_goods_version custom=getItem(position);
            holder.txtType.setText(custom.getName()+"");

            return convertView;
        }

        protected class ViewHolder{
            public TextView txtType;
            public FlowTagLayout myGridView;
            ProperyTagAdapter customChild;
            int positions;
            List<TagInfo> childList;
            public ViewHolder(View convertView, final int position) {
                this.positions=position;
                txtType=(TextView) convertView.findViewById(R.id.txt_type);
                myGridView=(FlowTagLayout) convertView.findViewById(R.id.gridview);
                childList = getItem(position).getValue();
                customChild=new ProperyTagAdapter(mContext,childList);
                myGridView.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
                myGridView.setOnTagSelectListener(new OnTagSelectListener() {
                    @Override
                    public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                        int idP=selectedList.get(0);
                        getItem(positions).setCurChild(idP);
                        L.MyLog("----------",idP+"");
                        if (goodsVersionListener!=null){
                            String key="";
                            for(int i=0;i<getCount();i++){
                                key+=getItem(i).getValue().get( getItem(i).getCurChild()).getText();
                            }
                            goodsVersionListener.goodsVersionListener(positions,idP,key);
                        }
                    }
                });
//                customChild.setOnClickListion(new AdapterCustomChild.OnClickListion() {
//                    @Override
//                    public void onPositon(int positon) {
//                        getItem(positions).setCurChild(positon);
//
//                        if (goodsVersionListener!=null){
//                            String key="";
//                            for(int i=0;i<getCount();i++){
//                                key+=getItem(i).getValue().get(getItem(i).getCurChild());
//                            }
//                            goodsVersionListener.goodsVersionListener(positions,positon,key);
//                        }
//
//                    }
//                });
                myGridView.setAdapter(customChild);
                customChild.notifyDataSetChanged();
                convertView.setTag(this);
            }

            public void refresh(int position){
                this.positions=position;
                childList = getItem(positions).getValue();
                customChild.notifyDataSetChanged();
//                customChild.refreshData(childList);
            }
        }

    private GoodsVersionListener goodsVersionListener;

    public void setGoodsVersionListener(GoodsVersionListener goodsVersionListener) {
        this.goodsVersionListener = goodsVersionListener;
    }

    public interface GoodsVersionListener{
        public void goodsVersionListener(int parent,int child,String key);
    }
}


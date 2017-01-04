package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.myui.MyGridView;

import java.util.List;

import cn.lanmei.com.smartmall.presenter.TagInfo;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_custom;

public class AdapterCustom extends MyBaseAdapter<M_custom> {
        private Drawable drawableRight;
        private Drawable drawableUp;
        public AdapterCustom(Context mContext, List<M_custom> mLists) {
            super(mContext,mLists);
            drawableRight= ContextCompat.getDrawable(mContext, R.drawable.icon_right_gray);
            drawableRight.setBounds(0,0,drawableRight.getMinimumWidth(),drawableRight.getMinimumHeight());
            drawableUp= ContextCompat.getDrawable(mContext,R.drawable.icon_pagedown_gray);
            drawableUp.setBounds(0,0,drawableUp.getMinimumWidth(),drawableUp.getMinimumHeight());
        }

    @Override
    public void refreshData(List<M_custom> lists) {
        super.refreshData(lists);
    }

    @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if (convertView == null){
                convertView=inflater.inflate(R.layout.layout_item_custom,parent,false);
                holder = new ViewHolder(convertView,position);

            }else{
                holder=(ViewHolder) convertView.getTag();
                holder.refresh(position);
            }
            M_custom custom=getItem(position);
            holder.txtType.setText(custom.getTypeName()+"");
//          holder.txtTypeCur.setText(holder.childList.get(custom.getCurChild()).getText());
            final MyGridView myGridView=holder.myGridView;
            holder.txtTypeCur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myGridView.setVisibility(myGridView.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
                    if (myGridView.getVisibility()==View.VISIBLE){
                        ((TextView)v).setCompoundDrawables(null,null,drawableUp,null);
                    }else{
                        ((TextView)v).setCompoundDrawables(null,null,drawableRight,null);
                    }

                }
            });

            return convertView;
        }

        protected class ViewHolder{
            public ImageView imgLogo;
            public TextView txtType;
            public TextView txtTypeCur;
            public MyGridView myGridView;

            AdapterCustomChild customChild=new AdapterCustomChild(mContext,null);;
            int positions;
            List<TagInfo> childList;
            public ViewHolder(View convertView,int position) {
                this.positions=position;
                imgLogo=(ImageView) convertView.findViewById(R.id.img_logo);
                txtType=(TextView) convertView.findViewById(R.id.txt_type);
                txtTypeCur=(TextView) convertView.findViewById(R.id.txt_type_cur);
                myGridView=(MyGridView) convertView.findViewById(R.id.gridview);

                childList = getItem(position).getChildList();
                customChild=new AdapterCustomChild(mContext,childList);
                myGridView.setAdapter(customChild);
                customChild.setOnClickListion(new AdapterCustomChild.OnClickListion() {
                    @Override
                    public void onPositon(int positon,boolean isSelect) {
                        if (isSelect){
                            lists.get(positions).setCurChild(positon);
                            txtTypeCur.setText(childList.get(positon).getText());
                        }else{
                            lists.get(positions).setCurChild(-1);
                            txtTypeCur.setText("");
                        }


                    }
                });

                convertView.setTag(this);
            }

            public void refresh(int position){
                this.positions=position;
                childList = getItem(position).getChildList();
                customChild=new AdapterCustomChild(mContext,childList);
                myGridView.setAdapter(customChild);
            }
        }
    }
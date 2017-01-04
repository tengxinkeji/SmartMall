package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.StaticMethod;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.goods.ActivityGoodsDetail_2;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.model.M_follow;
import cn.lanmei.com.smartmall.model.M_merchant;
import cn.lanmei.com.smartmall.shop.ActivityFixShop;
import cn.lanmei.com.smartmall.shop.Activity_shop;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterFollow extends MyBaseAdapter<M_follow> {

    /**是否是商店*/
    boolean isShop;

    public AdapterFollow(Context mContext,boolean isShop, List<M_follow> mLists) {
        super(mContext,mLists);
        this.isShop=isShop;

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){

            convertView=inflater.inflate(R.layout.layout_item_follow,parent,false);
            holder = new ViewHolder(position,convertView);

        }else{
            holder=(ViewHolder) convertView.getTag();
            holder.setPst(position);
        }
        if (isShop){
            holder.hsv.setVisibility(View.VISIBLE);
        }else {
            holder.hsv.setVisibility(View.GONE);
        }
        M_merchant mMerchant=getItem(position).getmMerchant();
        holder.setTxt(holder.txtStoreName,mMerchant.getName(),mMerchant.getGoodNum());
        imgLoader.displayImage(mMerchant.getPic(), holder.imgGoods, options, animateFirstListener);
        holder.txtGo.setTag(mMerchant.getUid());
        holder.txtGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShop){
                    Intent toShop=new Intent(mContext, Activity_shop.class);
                    toShop.putExtra(Common.KEY_id,(int)v.getTag());
                    mContext.startActivity(toShop);
                }else {
                    Intent toDetail=new Intent(mContext,ActivityFixShop.class);
                    toDetail.putExtra(Common.KEY_id,(int)v.getTag());
                    mContext.startActivity(toDetail);
                }


            }
        });
        holder.mGoodsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionChild, long id) {
                Intent toDetail=new Intent(mContext,ActivityGoodsDetail_2.class);
                toDetail.putExtra(Common.KEY_id,getItem(position).getmGoodsList().get(positionChild).getRecordId());
                mContext.startActivity(toDetail);
            }
        });



        return convertView;
    }

    protected class ViewHolder{
        public TextView txtStoreName;
        public ImageView imgGoods;
        public GridView mGoodsListView;
        public TextView txtGo;
        public View hsv;

        private int pst;
        private View convertView;
        private AdapterGoods_2 adapterGoods;

        public ViewHolder(int pst_a, View convertView) {
            this.pst = pst_a;
            this.convertView = convertView;
            txtStoreName=(TextView) convertView.findViewById(R.id.txt_detail);
            imgGoods=(ImageView) convertView.findViewById(R.id.img_head);
            mGoodsListView=(GridView) convertView.findViewById(R.id.grid);
            txtGo=(TextView) convertView.findViewById(R.id.txt_go);
            hsv=convertView.findViewById(R.id.hsv);
            if (isShop){
                setGridView(getItem(pst).getmGoodsList().size());
                adapterGoods=new AdapterGoods_2(mContext,getItem(pst).getmGoodsList());
                mGoodsListView.setAdapter(adapterGoods);
            }

            convertView.setTag(this);
        }

        public void setPst(int pst_b) {
            this.pst = pst_b;
            if (isShop){
                setGridView(getItem(pst).getmGoodsList().size());
                adapterGoods.refreshData(getItem(pst).getmGoodsList());
            }

        }

        public void setTxt(TextView txt,String name,int num){
            String m=name
                            +"\n好评率达"+num+"%";
            SpannableString msp = new SpannableString(m);
            int nameL=name.length();
            msp.setSpan(new AbsoluteSizeSpan(14,true),0,nameL, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.txtColor_bar))
                    ,0,nameL, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            int numAt=m.lastIndexOf(num+"");
            msp.setSpan(new AbsoluteSizeSpan(12,true),nameL,m.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.txtColor_bar))
                    ,numAt,m.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            txt.setText(msp);
        }

        /**设置GirdView参数，绑定数据*/
        private void setGridView(int size) {
            int length = 100;
            DisplayMetrics dm = new DisplayMetrics();
            ((BaseActivity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            int gridviewWidth = (int) (size * (length + 4) * density);
            int itemWidth = (int) (length * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    gridviewWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            mGoodsListView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
            mGoodsListView.setColumnWidth(itemWidth); // 设置列表项宽
            mGoodsListView.setHorizontalSpacing(StaticMethod.dip2px(mContext,5)); // 设置列表项水平间距
            mGoodsListView.setStretchMode(GridView.NO_STRETCH);
            mGoodsListView.setNumColumns(size); // 设置列数量=列表集合数


        }
    }

    private OnShopListener onShopListener;

    public void setOnShopListener(OnShopListener onShopListener) {
        this.onShopListener = onShopListener;
    }

    public interface OnShopListener{
        public void toShopIndex(String uid);
        public void toFixShopIndex(String uid);
    }
}

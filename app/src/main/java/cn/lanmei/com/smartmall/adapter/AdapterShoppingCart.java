package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.datadb.DBGoodsCartManager;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_Goods;


/**
 * Created by Administrator on 2016/5/16.
 */
public class AdapterShoppingCart extends MyBaseAdapter<M_Goods> {

    public AdapterShoppingCart(Context mContext, List<M_Goods> mLists) {
        super(mContext,mLists);

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        if (convertView == null){
            convertView=inflater.inflate(R.layout.layout_item_shopingcart,parent,false);
            holder = getHolder(convertView ,position);
            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
            holder.position=position;
        }
        M_Goods goods=getItem(position);
//        final int goodsCount=goods.getGoodsCount();
        holder.goodsName.setText(goods.getGoodsName()+"");
        holder.goodsPrice.setText("¥"+goods.getGoodsPrice());
        holder.goodsParams.setText(goods.getSpec()+"");
        holder.boxGoods.setChecked(goods.isSelect());

        holder.goodsCount.setText(goods.getGoodsCount()+"");
        imgLoader.displayImage(goods.getGoodsImg(), holder.goodsImg, options, animateFirstListener);
        holder.boxGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox= (CheckBox) v;
                getItem(position).setSelect(!getItem(position).isSelect());
                checkBox.setChecked(getItem(position).isSelect());

                DBGoodsCartManager.dbGoodsCartManager.updateGoodsSelect(getItem(position).getGoods_id(),checkBox.isChecked());
                DBGoodsCartManager.dbGoodsCartManager.queryUserCartGoods();
                if (shoppingCartGooodsListener!=null)
                    shoppingCartGooodsListener.goodsSelectChange();
            }
        });

        return convertView;
    }


    public class ViewHolder{
        public int position;
        public CheckBox boxGoods;
        public ImageView goodsImg;
        public TextView goodsName;
        public TextView goodsParams;
        public TextView goodsPrice;
        public TextView goodsSub;
        public TextView goodsAdd;
        public EditText goodsCount;
        public ImageView goodsDel;
    }
    private ShoppingCartGooodsListener shoppingCartGooodsListener;

    public void setShoppingCartGooodsListener(ShoppingCartGooodsListener shoppingCartGooodsListener) {
        this.shoppingCartGooodsListener = shoppingCartGooodsListener;
    }

    public interface ShoppingCartGooodsListener{
        public void goodsCount(int postion,int goodsId,int count);
        public void goodsDel(int postion,int goodsId);
        public void goodsSelectChange();
    }

    public ViewHolder getHolder(View convertView,int position){
        ViewHolder holder = new ViewHolder();
        holder.position=position;
        holder.boxGoods=(CheckBox) convertView.findViewById(R.id.box_goods);
        holder.goodsImg=(ImageView) convertView.findViewById(R.id.img_goods);
        holder.goodsName=(TextView) convertView.findViewById(R.id.txt_goodsname);
        holder.goodsPrice=(TextView) convertView.findViewById(R.id.txt_goodsprice);
        holder.goodsParams=(TextView) convertView.findViewById(R.id.txt_goodsparams);
        holder.goodsSub=(TextView) convertView.findViewById(R.id.txt_sub);
        holder.goodsCount=(EditText) convertView.findViewById(R.id.txt_goodscount);
        holder.goodsAdd=(TextView) convertView.findViewById(R.id.txt_add);
        holder.goodsDel=(ImageView) convertView.findViewById(R.id.img_goods_del);

        initView(holder);
        return holder;
    }
    private void initView(final ViewHolder holder){

        holder.goodsCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.position>=getCount())
                    return;
                if (lists.get(holder.position).getGoodsCount()==Integer.parseInt(s.toString()))
                    return;
                if (s!=null&&s.length()>0){
                    lists.get(holder.position).setGoodsCount(Integer.parseInt(s.toString()));
                    if (shoppingCartGooodsListener!=null){
                        shoppingCartGooodsListener.goodsCount(holder.position,lists.get(holder.position).getGoods_id(),Integer.parseInt(s.toString()));
                    }
                }
            }
        });
        holder.goodsSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int beforeC=getItem(holder.position).getGoodsCount();
                if (beforeC>1){
                    holder.goodsCount.setText((beforeC-1)+"");
                }

            }
        });
        holder.goodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int beforeC=getItem(holder.position).getGoodsCount();
                holder.goodsCount.setText((beforeC+1)+"");
            }
        });
        holder.goodsDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shoppingCartGooodsListener!=null){
                    shoppingCartGooodsListener.goodsDel(holder.position,lists.get(holder.position).getGoods_id());
                }
            }
        });
    }


}

package cn.lanmei.com.smartmall.goods;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.degexce.L;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.ui.ChatActivity;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.shop.Activity_shop;


/**
 *
 */
public class F_goods_details_menu extends Fragment {
    private String TAG="F_goods_details_menu";
    private int goodsId;
    private int shopUid;
    private boolean isCollect;

    private View view;
    private LinearLayout layoutMenu;
    private TextView txt_goods_store;
    private TextView txt_goods_attention;
    private TextView txt_goods_chat;
    private TextView txt_goods_add_cart;
    private TextView txt_goods_buy;


    private Drawable drawableYes;
    private Drawable drawableNo;

    public static F_goods_details_menu newInstance(int goodsId) {
        F_goods_details_menu fragment = new F_goods_details_menu();
        Bundle bundle = new Bundle();
        bundle.putInt("goodsId",goodsId);

        fragment.setArguments(bundle);
        return fragment;
    }

    public void setShopUid(int shopUid) {
        this.shopUid = shopUid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            goodsId=getArguments().getInt("goodsId",0);

        }

        drawableNo= ContextCompat.getDrawable(getActivity(), R.drawable.icon_collect);
        drawableNo.setBounds(0,0,drawableNo.getMinimumWidth(),drawableNo.getMinimumHeight());
        drawableYes= ContextCompat.getDrawable(getActivity(),R.drawable.icon_collect_01);
        drawableYes.setBounds(0,0,drawableYes.getMinimumWidth(),drawableYes.getMinimumHeight());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_menu_goods,container,false);
        initUi();
        return view;
    }


    public void initUi() {
        layoutMenu=(LinearLayout)view. findViewById(R.id.layout_menu);
        txt_goods_store= (TextView) layoutMenu.findViewById(R.id.menu_goods_store);
        txt_goods_attention= (TextView) layoutMenu.findViewById(R.id.menu_goods_attention);
        txt_goods_chat= (TextView) layoutMenu.findViewById(R.id.menu_goods_chat);
        txt_goods_add_cart= (TextView) layoutMenu.findViewById(R.id.menu_goods_add_cart);
        txt_goods_buy= (TextView) layoutMenu.findViewById(R.id.menu_goods_buy);
        txt_goods_store.setOnClickListener(onClickListener);
        txt_goods_attention.setOnClickListener(onClickListener);
        txt_goods_chat.setOnClickListener(onClickListener);
        txt_goods_add_cart.setOnClickListener(onClickListener);
        txt_goods_buy.setOnClickListener(onClickListener);

    }

    public void setGoodsAttention(boolean isCollect){
        this.isCollect=isCollect;
        if (isCollect){
            txt_goods_attention.setCompoundDrawables(null,drawableYes,null,null);
        }else{
            txt_goods_attention.setCompoundDrawables(null,drawableNo,null,null);
        }
    }

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.menu_goods_store:
                    L.MyLog(TAG,"menu_goods_store");
                    Intent toShop=new Intent(getActivity(), Activity_shop.class);

                    toShop.putExtra(Common.KEY_id,shopUid);

                    getActivity().startActivity(toShop);
                    break;
                case R.id.menu_goods_attention:
                    L.MyLog(TAG,"menu_goods_attention");
                    collectGoods();
                    break;
                case R.id.menu_goods_chat:
                    L.MyLog(TAG,"menu_goods_chat");
                    boolean islogin = SharePreferenceUtil.getBoolean(Common.KEY_is_login, false);
                    if (!islogin){
                        Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                        startActivity(toLogin);
                        break;
                    }
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    // it's single chat
                    Bundle data=new Bundle();
                    intent.putExtra(Constant.EXTRA_USER_name, F_goods_detail_2.mGoods.getGoodsStoreName());
                    intent.putExtra(Constant.EXTRA_USER_ID, MyApplication.hxServer);
                    startActivity(intent);
                    break;
                case R.id.menu_goods_add_cart:
                    L.MyLog(TAG,"menu_goods_add_cart");
                    addShoppingCart();
                    break;
                case R.id.menu_goods_buy:
                    L.MyLog(TAG,"menu_goods_buy");
                    buyShopping();
                    break;
            }

        }
    };

    private void addShoppingCart(){
        F_goods_detail_2 fGoodsDetail=(F_goods_detail_2) getActivity()
                .getSupportFragmentManager().findFragmentByTag("F_goods_detail_2");
        if (fGoodsDetail==null)
            return;
        fGoodsDetail.selectGoods();
//        if (fGoodsDetail.mGoods.getGoods_id()==0){
//            StaticMethod.showInfo(getActivity(),"请选择商品");
//            return;
//        }
////        fGoodsDetail.mGoods.setGoodsCount(1);
//        DBGoodsCartManager.dbGoodsCartManager.addGoods(fGoodsDetail.mGoods);
    }

    private void buyShopping(){
        F_goods_detail_2 fGoodsDetail=(F_goods_detail_2) getActivity()
                .getSupportFragmentManager().findFragmentByTag("F_goods_detail_2");
        if (fGoodsDetail==null)
            return;
        fGoodsDetail.selectGoods();
//        fGoodsDetail.toBuy();

    }

    private void collectGoods(){
        F_goods_detail_2 fGoodsDetail=(F_goods_detail_2) getActivity()
                .getSupportFragmentManager().findFragmentByTag("F_goods_detail_2");
        if (fGoodsDetail==null)
            return;

        fGoodsDetail.collectGoods();

    }



}

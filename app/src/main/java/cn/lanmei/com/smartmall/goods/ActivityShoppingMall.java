package cn.lanmei.com.smartmall.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.SharePreferenceUtil;
import com.common.datadb.DBGoodsCartManager;

import java.util.List;

import cn.lanmei.com.smartmall.customization.F_custom;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.main.BaseMainActionActivity;
import cn.lanmei.com.smartmall.main.F_index;
import cn.lanmei.com.smartmall.main.F_nearby_merchant;
import cn.lanmei.com.smartmall.main.F_shopping_cart;
import cn.lanmei.com.smartmall.my.Activity_chat;
import cn.lanmei.com.smartmall.my.F_my;
import cn.lanmei.com.smartmall.R;

/**
 * 商城
 */
public class ActivityShoppingMall extends BaseMainActionActivity implements View.OnClickListener{
    private String TAG="ActivityShoppingMall";

    private static TextView txtCount;
    private LinearLayout layoutMenu;
    private ImageView img_menu_1;
    private ImageView img_menu_2;
    private ImageView img_menu_3;
    private ImageView img_menu_4;
    private ImageView img_menu_5;


    private TextView txt_menu_1;
    private TextView txt_menu_2;
    private TextView txt_menu_3;
    private TextView txt_menu_4;
    private TextView txt_menu_5;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBGoodsCartManager.dbGoodsCartManager.queryUserCartGoods();
        if (getIntent()!=null){
            boolean isToCart=getIntent().getBooleanExtra("toCart",false);
            if (isToCart){
                setMenuFragment(3);
            }
        }
    }



    @Override
    public void loadViewLayout() {
        setMContentView(R.layout.activity_shoppingmall);
    }

    @Override
    public void mfindViewById() {
        txtLeft.setVisibility(View.GONE);
        initUI();
    }

    @Override
    protected void onHeadRightButton(View v) {
        super.onHeadRightButton(v);
        boolean islogin = SharePreferenceUtil.getBoolean(Common.KEY_is_login, false);
        if (!islogin){
            Intent toLogin=new Intent(ActivityShoppingMall.this,LoginActionActivity.class);
            startActivity(toLogin);

        }else {
            Intent toHx=new Intent(ActivityShoppingMall.this, Activity_chat.class);
            //toHx.putExtra("index",getActivity() instanceof MainActionActivity);
            ActivityShoppingMall.this.startActivity(toHx);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_0:
                setMenuFragment(0);
                break;
            case R.id.menu_1:
                setMenuFragment(1);
                break;
            case R.id.menu_2:
                setMenuFragment(2);
                break;
            case R.id.menu_3:
                setMenuFragment(3);
                break;
            case R.id.menu_4:
                setMenuFragment(4);
                break;

        }

    }

    @Override
    public void setTitle(String title) {
        setHeadCentertText(title);
    }

    @Override
    public void addFrament(Fragment fragment, String tag) {
        super.addFrament(fragment, tag);
        addFragmentShow(fragment,tag);
    }

    @Override
    public void showFrament(int casePositon, Bundle data) {
        super.showFrament(casePositon, data);
        setMenuFragment(casePositon,data);
    }

    @Override
    public void backFragment(String currentTag) {
        super.backFragment(currentTag);
        if (fm.getBackStackEntryCount()>0)
            fm.popBackStack();

    }

    public static void refreshMallCartNum(int gooodsCount, double money) {
        if (txtCount==null)
            return;

        txtCount.setText(gooodsCount+"");
        if (gooodsCount!=0){
            if (txtCount.getVisibility()==View.GONE)
                txtCount.setVisibility(View.VISIBLE);
        }else {
            if (txtCount.getVisibility()==View.VISIBLE)
                txtCount.setVisibility(View.GONE);
        }
    }


    private void initUI(){
        layoutMenu=(LinearLayout) findViewById(R.id.layout_menu);

        LinearLayout layoutMenu_1 = (LinearLayout)layoutMenu. findViewById(R.id.menu_0);
        img_menu_1=(ImageView) layoutMenu_1.findViewById(R.id.menu_item_img);
        txt_menu_1=(TextView) layoutMenu_1.findViewById(R.id.menu_item_name);
        txt_menu_1.setText(getResources().getString(R.string.menu_1));
        img_menu_1.setImageResource(R.mipmap.my_012);
        txt_menu_1.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
        layoutMenu_1.setOnClickListener(this);

        LinearLayout layoutMenu_2 = (LinearLayout) layoutMenu.findViewById(R.id.menu_1);
        img_menu_2=(ImageView) layoutMenu_2.findViewById(R.id.menu_item_img);
        txt_menu_2=(TextView) layoutMenu_2.findViewById(R.id.menu_item_name);
        img_menu_2.setImageResource(R.mipmap.my_13);
        txt_menu_2.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));
        txt_menu_2.setText(getResources().getString(R.string.menu_2));
        layoutMenu_2.setOnClickListener(this);

        LinearLayout layoutMenu_3 = (LinearLayout)layoutMenu. findViewById(R.id.menu_2);
        img_menu_3=(ImageView) layoutMenu_3.findViewById(R.id.menu_item_img);
        txt_menu_3=(TextView) layoutMenu_3.findViewById(R.id.menu_item_name);
        img_menu_3.setImageResource(R.mipmap.my_14);
        txt_menu_3.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));
        txt_menu_3.setText(getResources().getString(R.string.menu_3));
        layoutMenu_3.setOnClickListener(this);


        LinearLayout layoutMenu_4 = (LinearLayout)layoutMenu. findViewById(R.id.menu_3);
        img_menu_4=(ImageView) layoutMenu_4.findViewById(R.id.menu_item_img);
        txt_menu_4=(TextView) layoutMenu_4.findViewById(R.id.menu_item_name);
        txtCount=(TextView) layoutMenu_4.findViewById(R.id.txt_goods_count);
        img_menu_4.setImageResource(R.mipmap.my_15);
        txt_menu_4.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));
        txt_menu_4.setText(getResources().getString(R.string.menu_4));
        layoutMenu_4.setOnClickListener(this);

        LinearLayout layoutMenu_5 = (LinearLayout)layoutMenu. findViewById(R.id.menu_4);
        img_menu_5=(ImageView) layoutMenu_5.findViewById(R.id.menu_item_img);
        txt_menu_5=(TextView) layoutMenu_5.findViewById(R.id.menu_item_name);
        img_menu_5.setImageResource(R.mipmap.my_16);
        txt_menu_5.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));
        txt_menu_5.setText(getResources().getString(R.string.menu_5));
        layoutMenu_5.setOnClickListener(this);

        setMenuFragment(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        boolean isToCart=false;
        if (intent!=null){
            isToCart=intent.getBooleanExtra("toCart",false);

        }
        if (isToCart){
            setMenuFragment(3);
        }else {
            setMenuFragment(0);
        }

    }



    private void setMenuUI(int menuCase){
        img_menu_1.setImageResource(R.mipmap.my_12);
        txt_menu_1.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));

        img_menu_2.setImageResource(R.mipmap.my_13);
        txt_menu_2.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));

        img_menu_3.setImageResource(R.mipmap.my_14);
        txt_menu_3.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));

        img_menu_4.setImageResource(R.mipmap.my_15);
        txt_menu_4.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));

        img_menu_5.setImageResource(R.mipmap.my_16);
        txt_menu_5.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));


        switch (menuCase){
            case 0:
                img_menu_1.setImageResource(R.mipmap.my_012);
                txt_menu_1.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
                break;
            case 1:
                img_menu_2.setImageResource(R.mipmap.my_013);
                txt_menu_2.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
                break;
            case 2:
                img_menu_3.setImageResource(R.mipmap.my_014);
                txt_menu_3.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
                break;
            case 3:
                img_menu_4.setImageResource(R.mipmap.my_015);
                txt_menu_4.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
                break;
            case 4:
                img_menu_5.setImageResource(R.mipmap.my_016);
                txt_menu_5.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
                break;

        }
    }
    private void setMenuFragment(int menuCase){
        setMenuFragment(menuCase,null);
    };
    private void setMenuFragment(int menuCase,Bundle data){
        setMenuUI(menuCase);

        switch (menuCase){
            case 0://首页
                F_index f_index=(F_index)fm.findFragmentByTag("F_index");
                if (f_index==null){
                    f_index=new F_index();
                    addFragmentShow(f_index,"F_index");
                }else{
                    if (f_index.isHidden()){
                        showFragment(f_index);
                    }
                    setHeadCentertText("商城");
                }
                break;
            case 1://
                F_nearby_merchant f_nearby_merchant=(F_nearby_merchant)fm.findFragmentByTag("F_nearby_merchant");
                if (f_nearby_merchant==null){
                    f_nearby_merchant=new F_nearby_merchant();
                    addFragmentShow(f_nearby_merchant,"F_nearby_merchant");
                }else{
                    if (f_nearby_merchant.isHidden())
                        showFragment(f_nearby_merchant);
                    setHeadCentertText(getResources().getString(R.string.menu_2));
                }
                break;
            case 2://
                F_custom f_customization=(F_custom)fm.findFragmentByTag("F_custom");
                if (f_customization==null){
                    f_customization=new F_custom();
                    addFragmentShow(f_customization,"F_custom");
                }else{
                    if (f_customization.isHidden())
                        showFragment(f_customization);

                }
                setHeadCentertText(getResources().getString(R.string.menu_3));
                break;
            case 3://
                F_shopping_cart f_shopping_cart=(F_shopping_cart)fm.findFragmentByTag("F_shopping_cart");
                if (f_shopping_cart==null){
                    f_shopping_cart=new F_shopping_cart();
                    addFragmentShow(f_shopping_cart,"F_shopping_cart");
                }else{
                    f_shopping_cart.refreshDB();
                    if (f_shopping_cart.isHidden())
                        showFragment(f_shopping_cart);
                    setHeadCentertText(getResources().getString(R.string.menu_4));
                }
                break;

            case 4://
                boolean islogin = SharePreferenceUtil.getBoolean(Common.KEY_is_login, false);
                if (!islogin){
                    Intent toLogin=new Intent(this,LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                F_my f_my=(F_my)fm.findFragmentByTag("F_my");
                if (f_my==null){
                    f_my=new F_my();
                    addFragmentShow(f_my,"F_my");
                }else{
                    if (f_my.isHidden())
                        showFragment(f_my);
                    setHeadCentertText(getResources().getString(R.string.menu_5));
                    f_my.requestServerData();
                }
                break;


        }
    }


    public void hiddenMainBar(boolean isHidden){
        layoutHead.setVisibility(isHidden?View.GONE:View.VISIBLE);
    }

    private void addFragmentShow(Fragment f, String tag){

        hideFragment();
        fm.beginTransaction()
                .add(R.id.layout_frame_main_s,f,tag)
                .addToBackStack(tag)
                .commit();

    }

    private void showFragment(Fragment f){
        hideFragment();
        fm.beginTransaction().show(f).commit();

    }

    private void hideFragment(){
        List<Fragment> fgLists =fm.getFragments();
        FragmentTransaction tran = fm.beginTransaction();

        if (fgLists!=null){
            Fragment item;
            for (int i=0;i<fgLists.size();i++){
                item=fgLists.get(i);
                if (item==null)
                    continue;
//                BugLog.MyLog(TAG, "item.getTag():"+item.getTag()+"---f.getTag():"+f.getTag()+"---f---"+f.getClass().getName());
//                if (!f.getTag().equals(item.getTag()))
                tran.hide(item);

            }
        }
        tran.commit();
    }
}

package cn.lanmei.com.smartmall.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;

import java.util.List;

import cn.lanmei.com.smartmall.goods.F_goods_review_menu;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.goods.F_goods_review;
import cn.lanmei.com.smartmall.main.BaseActionActivity;

/**
 * 维修商
 */
public class ActivityFixShop extends BaseActionActivity implements View.OnClickListener{
    private String TAG="ActivityFixShop";
    private TextView txtBarBack;
    private TextView txtBarGoods;
    private TextView txtBarGoodsReview;
    public static TextView txtBarAttention;

    private View goodsView;

    private F_goods_review_menu goodsReviewMenu;

    private int fixShopId;
    F_fixshop_index fFixshopIndex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent()!=null){
            fixShopId=getIntent().getIntExtra(Common.KEY_id,0);
        }
    }

    @Override
    public void loadViewLayout() {
        if (getIntent()!=null){
            fixShopId=getIntent().getIntExtra(Common.KEY_id,0);
        }
        setMContentView(R.layout.activity_fixshop_detail);
    }

    @Override
    public void mfindViewById() {
        txtLeft.setVisibility(View.GONE);
        initUI();
    }


    @Override
    public void onClick(View v) {
        LinearLayout.LayoutParams lp;
        switch (v.getId()){
            case R.id.bar_goods_left:
                L.MyLog(TAG,"back_bar_left");
                finish();
                break;
            case R.id.bar_goods:
                lp = new LinearLayout.LayoutParams(txtBarGoods.getWidth(), StaticMethod.dip2px(this,2));
                lp.setMargins(txtBarGoods.getLeft(),0,0,0);
                goodsView.setLayoutParams(lp);
                setMenuFragment(0);
                break;

            case R.id.bar_goods_review:
                lp = new LinearLayout.LayoutParams(txtBarGoodsReview.getWidth(),StaticMethod.dip2px(this,2));
                lp.setMargins(txtBarGoodsReview.getLeft(),0,0,0);
                goodsView.setLayoutParams(lp);
                setMenuFragment(2);
                break;
            case R.id.txt_attention:
                fFixshopIndex.collectGoods();
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



    private void initUI(){
        setHeadShow(false);
        txtBarBack= (TextView) findViewById(R.id.bar_goods_left);
        txtBarGoods= (TextView) findViewById(R.id.bar_goods);
        txtBarGoodsReview= (TextView) findViewById(R.id.bar_goods_review);
        txtBarAttention= (TextView) findViewById(R.id.txt_attention);
        goodsView=  findViewById(R.id.view_goods);

        txtBarGoods.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(txtBarGoods.getWidth(),
                        StaticMethod.dip2px(ActivityFixShop.this,2));
                lp.setMargins(txtBarGoods.getLeft(),0,0,0);
                goodsView.setLayoutParams(lp);
            }
        });

        if (goodsReviewMenu==null){
            goodsReviewMenu=F_goods_review_menu.newInstance(fixShopId);
            fm.beginTransaction()
                    .add(R.id.layout_menu_goods,goodsReviewMenu)
                    .commit();
        }else {
            if (goodsReviewMenu.isHidden())
                fm.beginTransaction().show(goodsReviewMenu).commit();
        }

        txtBarBack.setOnClickListener(this);
        txtBarAttention.setOnClickListener(this);
        txtBarGoods.setOnClickListener(this);
        txtBarGoodsReview.setOnClickListener(this);

        setMenuFragment(0);
    }



    private void setMenuFragment(int menuCase){
        setMenuFragment(menuCase,null);
    };
    private void setMenuFragment(int menuCase,Bundle data){

        switch (menuCase){

            case 0://首页

                fFixshopIndex=(F_fixshop_index)fm.findFragmentByTag("F_fixshop_index");
                if (fFixshopIndex==null){
                    fFixshopIndex=F_fixshop_index.newInstance(fixShopId+"");
                    addFragmentShow(fFixshopIndex,"F_fixshop_index");
                }else{
                    if (fFixshopIndex.isHidden()){
                        showFragment(fFixshopIndex);
                    }
                }

                break;


            case 2://
                F_goods_review fGoodsReview=(F_goods_review)fm.findFragmentByTag("F_goods_review");
                if (fGoodsReview==null){
                    fGoodsReview=F_goods_review.newInstance(fixShopId);
                    addFragmentShow(fGoodsReview,"F_goods_review");
                }else{
                    if (fGoodsReview.isHidden()){
                        showFragment(fGoodsReview);
                    }
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

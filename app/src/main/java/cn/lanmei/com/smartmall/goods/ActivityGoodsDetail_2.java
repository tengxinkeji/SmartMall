package cn.lanmei.com.smartmall.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.datadb.DBGoodsCartManager;
import com.common.net.NetData;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.main.BaseActionActivity;
import cn.lanmei.com.smartmall.main.MainActionActivity;
import cn.lanmei.com.smartmall.myui.DragLayout;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 商品详情
 */
public class ActivityGoodsDetail_2 extends BaseActionActivity implements View.OnClickListener{
    private String TAG="ActivityGoodsDetail";
    private TextView txtBarBack;
    private TextView txtBarGoods;
    private TextView txtBarGoodsDetail;
    private TextView txtBarGoodsReview;
    private TextView txtBarCart;
    private ImageView imgShare;
    private static TextView txtBarGoodsCount;
    private View goodsView;

    private DragLayout draglayout;

    private F_goods_params_2 f_goods_params_2;
    private  F_goods_detail_2 fGoodsDetail;
    public F_goods_details_menu goodsDetailsMenu;
    private F_goods_review_menu goodsReviewMenu;

    private int goodsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (getIntent()!=null){
            goodsId=getIntent().getIntExtra(Common.KEY_id,0);
        }
        super.onCreate(savedInstanceState);

        DBGoodsCartManager.dbGoodsCartManager.queryUserCartGoods();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent()!=null){
            goodsId=getIntent().getIntExtra(Common.KEY_id,0);
        }
    }

    @Override
    public void loadViewLayout() {
        setMContentView(R.layout.fragment_f_goods_detail_2);
    }

    @Override
    public void mfindViewById() {
        txtLeft.setVisibility(View.GONE);
        initUI();
    }

    /**
     * 初始化View
     *//**/
    private void initView() {
        fGoodsDetail= F_goods_detail_2.newInstance(goodsId);
        f_goods_params_2=F_goods_params_2.newInstance(goodsId);
//        verticalFragment3=new VerticalFragment3();


        DragLayout.ShowNextPageNotifier nextIntf = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
//                fragment3.initView();
            }
        };
        draglayout = (DragLayout) findViewById(R.id.draglayout);
        draglayout.setNextPageListener(nextIntf);
    }


    @Override
    public void onClick(View v) {
        LinearLayout.LayoutParams lp;
        switch (v.getId()){
            case R.id.bar_goods_left:
                L.MyLog(TAG,"back_bar_left");
                finish();
                break;
            case R.id.img_share:
                showShare();
                break;
            case R.id.bar_goods:
                lp = new LinearLayout.LayoutParams(txtBarGoods.getWidth(), StaticMethod.dip2px(this,2));
                lp.setMargins(txtBarGoods.getLeft(),0,0,0);
                goodsView.setLayoutParams(lp);
                setMenuFragment(0);
                break;
            case R.id.bar_goods_detail:
                lp = new LinearLayout.LayoutParams(txtBarGoodsDetail.getWidth(),StaticMethod.dip2px(this,2));
                lp.setMargins(txtBarGoodsDetail.getLeft(),0,0,0);
                goodsView.setLayoutParams(lp);
                setMenuFragment(1);
                break;
            case R.id.bar_goods_review:
                lp = new LinearLayout.LayoutParams(txtBarGoodsReview.getWidth(),StaticMethod.dip2px(this,2));
                lp.setMargins(txtBarGoodsReview.getLeft(),0,0,0);
                goodsView.setLayoutParams(lp);
                setMenuFragment(2);
                break;
            case R.id.bar_goods_right:
                boolean islogin = SharePreferenceUtil.getBoolean(Common.KEY_is_login, false);
                if (!islogin){
                    Intent toLogin=new Intent(this,LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                Intent toCart=new Intent(ActivityGoodsDetail_2.this,MainActionActivity.class);
                toCart.putExtra(Common.KEY_position,3);
                ActivityGoodsDetail_2.this.startActivity(toCart);
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

    @Override
    public void dbShoppingCartListener(int gooodsCount, double money) {
        super.dbShoppingCartListener(gooodsCount, money);
        refreshShopGoodsCount(gooodsCount);
    }

    private void initUI(){
        setHeadShow(false);
        txtBarBack= (TextView) findViewById(R.id.bar_goods_left);
        txtBarGoods= (TextView) findViewById(R.id.bar_goods);
        txtBarGoodsDetail= (TextView) findViewById(R.id.bar_goods_detail);
        txtBarGoodsReview= (TextView) findViewById(R.id.bar_goods_review);
        txtBarGoodsDetail.setVisibility(View.GONE);
        txtBarGoodsReview.setVisibility(View.GONE);
        txtBarCart= (TextView) findViewById(R.id.bar_goods_right);
        imgShare= (ImageView) findViewById(R.id.img_share);
        txtBarGoodsCount= (TextView) findViewById(R.id.txt_goods_count);
        goodsView=  findViewById(R.id.view_goods);
        goodsView.setVisibility(View.GONE);
        txtBarGoods.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(txtBarGoods.getWidth(),
                        StaticMethod.dip2px(ActivityGoodsDetail_2.this,2));
                lp.setMargins(txtBarGoods.getLeft(),0,0,0);
                goodsView.setLayoutParams(lp);
            }
        });



        txtBarBack.setOnClickListener(this);
        txtBarCart.setOnClickListener(this);
        txtBarGoods.setOnClickListener(this);
        txtBarGoodsDetail.setOnClickListener(this);
        txtBarGoodsReview.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        initView();
        setMenuFragment(0);
    }


    public static void refreshShopGoodsCount(int count){
        if (txtBarGoodsCount==null)
            return;
        if (count>0){
            txtBarGoodsCount.setVisibility(View.VISIBLE);
        }else {
            txtBarGoodsCount.setVisibility(View.GONE);
        }
        txtBarGoodsCount.setText(count+"");
    }

    private void setMenuFragment(int menuCase){
        setMenuFragment(menuCase,null);
    };
    private void setMenuFragment(int menuCase,Bundle data){

        switch (menuCase){

            case 0://首页
                if (goodsReviewMenu!=null){
                    fm.beginTransaction().hide(goodsReviewMenu).commit();
                }

                if (goodsDetailsMenu==null){
                    goodsDetailsMenu=F_goods_details_menu.newInstance(goodsId);
                    fm.beginTransaction()
                            .add(R.id.layout_menu_goods,goodsDetailsMenu)
                            .commit();
                }
                if (!fGoodsDetail.isAdded()){
                    fm.beginTransaction()
                            .add(R.id.first, fGoodsDetail,"F_goods_detail_2")
                            .add(R.id.second, f_goods_params_2,"F_goods_params_2")
                            .commit();
                }


               /* fGoodsDetail=(F_goods_detail_2) fm.findFragmentByTag("F_goods_detail_2");
                if (fGoodsDetail==null){
                    fGoodsDetail=F_goods_detail_2.newInstance(goodsId);
                    addFragmentShow(fGoodsDetail,"F_goods_detail_2");
                }else{
                    if (fGoodsDetail.isHidden()){
                        showFragment(fGoodsDetail);
                    }

                }*/
                if (goodsDetailsMenu.isHidden())
                    fm.beginTransaction().show(goodsDetailsMenu).commit();
                break;
            case 1://
                F_goods_params fGoodsParams=(F_goods_params)fm.findFragmentByTag("F_goods_params");
                if (fGoodsParams==null){
                    fGoodsParams=F_goods_params.newInstance(goodsId);
                    addFragmentShow(fGoodsParams,"F_goods_params");
                }else{
                    if (fGoodsParams.isHidden()){
                        showFragment(fGoodsParams);
                    }
                }
                if (goodsDetailsMenu!=null){
                    fm.beginTransaction().hide(goodsDetailsMenu).commit();
                }
                if (goodsReviewMenu!=null){
                    fm.beginTransaction().hide(goodsReviewMenu).commit();
                }
                break;
            case 2://
                F_goods_review fGoodsReview=(F_goods_review)fm.findFragmentByTag("F_goods_review");
                if (fGoodsReview==null){
                    fGoodsReview=F_goods_review.newInstance(goodsId);
                    addFragmentShow(fGoodsReview,"F_goods_review");
                }else{
                    if (fGoodsReview.isHidden()){
                        showFragment(fGoodsReview);
                    }
                }

                if (goodsDetailsMenu!=null){
                    fm.beginTransaction().hide(goodsDetailsMenu).commit();
                }
                if (goodsReviewMenu==null){
                    goodsReviewMenu=F_goods_review_menu.newInstance(goodsId);
                    fm.beginTransaction()
                            .add(R.id.layout_menu_goods,goodsReviewMenu)
                            .commit();
                }else {
                    if (goodsReviewMenu.isHidden())
                        fm.beginTransaction().show(goodsReviewMenu).commit();
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
                .add(R.id.first,f,tag)
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


    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        String url=NetData.HOST+"/goods/details?id="+fGoodsDetail.mGoods.getGoods_id();
    L.MyLog("分享：",url+"");
// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getResources().getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(fGoodsDetail.mGoods.getGoodsName());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(fGoodsDetail.mGoods.getGoodsImg());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(fGoodsDetail.mGoods.getGoodsName());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getResources().getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

// 启动分享GUI
        oks.show(this);
    }
}

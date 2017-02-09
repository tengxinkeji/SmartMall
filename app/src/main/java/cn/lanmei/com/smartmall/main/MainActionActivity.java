package cn.lanmei.com.smartmall.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.datadb.DBGoodsCartManager;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.categorygoods.ActionActivity_Goods_Category;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.my.F_my;
import cn.lanmei.com.smartmall.search.Activity_list_goods;
import cn.lanmei.com.smartmall.search.F_list_goods;

/**
 * Created by Administrator on 2016/7/25.
 */
public class MainActionActivity extends BaseMainActionActivity implements View.OnClickListener{
    private String TAG="MainActionActivity";

    private static TextView txtCount;

    private LinearLayout[] layoutMenu=new LinearLayout[5];
    private ImageView[] img_menu=new ImageView[5];
    private TextView[] txt_menu=new TextView[5];
    private Drawable[] selectDraw=null;
    private Drawable[] normalDraw=null;
    private String[] menuStr=null;

    F_index f_index_recommend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);

        DBGoodsCartManager.dbGoodsCartManager.queryUserCartGoods();

    }

    private void init(){
        selectDraw=new Drawable[]{
                ContextCompat.getDrawable(this,R.drawable.tab_set_home_pre),
                ContextCompat.getDrawable(this,R.drawable.tab_set_yh_pre),
                ContextCompat.getDrawable(this,R.drawable.tab_set_qz_pre),
                ContextCompat.getDrawable(this,R.drawable.tab_set_shop_pre),
                ContextCompat.getDrawable(this,R.drawable.tab_set_my_pre)};

        normalDraw=new Drawable[]{
                ContextCompat.getDrawable(this,R.drawable.tab_set_home),
                ContextCompat.getDrawable(this,R.drawable.tab_set_yh),
                ContextCompat.getDrawable(this,R.drawable.tab_set_qz),
                ContextCompat.getDrawable(this,R.drawable.tab_set_shop),
                ContextCompat.getDrawable(this,R.drawable.tab_set_my)};

        menuStr=new String[]{
                getResources().getString(R.string.menu_1),
                getResources().getString(R.string.menu_2),
                getResources().getString(R.string.menu_3),
                getResources().getString(R.string.menu_4),
                getResources().getString(R.string.menu_5),};
    }

    @Override
    public void loadViewLayout() {
        setMContentView(R.layout.activity_main);
    }

    @Override
    public void mfindViewById() {
        setRingBar();
        initUI();
    }

    @Override
    protected void onHeadLeftButton(View v) {
//        super.onHeadLeftButton(v);
        setMenuFragment(0);
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

                setMenuFragment(5);
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
    public void chatListRefresh(int count) {
        super.chatListRefresh(count);
        if (f_index_recommend!=null)
            f_index_recommend.refreshChatCount(count);
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

    public static void refreshCartNum(int gooodsCount, double money) {
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
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_menu);
        layoutMenu[0] = (LinearLayout)layout. findViewById(R.id.menu_0);
        layoutMenu[1] = (LinearLayout)layout. findViewById(R.id.menu_1);
        layoutMenu[2] = (LinearLayout)layout. findViewById(R.id.menu_2);
        layoutMenu[3] = (LinearLayout)layout. findViewById(R.id.menu_3);
        layoutMenu[4] = (LinearLayout)layout. findViewById(R.id.menu_4);
        for (int i=0;i<layoutMenu.length;i++){
            img_menu[i]=(ImageView) layoutMenu[i].findViewById(R.id.menu_item_img);
            txt_menu[i]=(TextView) layoutMenu[i].findViewById(R.id.menu_item_name);
            img_menu[i].setImageDrawable(normalDraw[i]);
            txt_menu[i].setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));
            txt_menu[i].setText(menuStr[i]);
            layoutMenu[i].setOnClickListener(this);
        }


        toIntentFragment(getIntent());
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        toIntentFragment(intent);

    }

    private void toIntentFragment(Intent intent){
        if (intent!=null){
            int position=intent.getIntExtra(Common.KEY_position,0);
            setMenuFragment(position);
            return;
        }
        setMenuFragment(0);
    }

    private void setMenuUI(int menuCase){
        for (int i=0;i<layoutMenu.length;i++){
            if (menuCase==i){
                img_menu[i].setImageDrawable(selectDraw[i]);
                txt_menu[i].setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
            }else {
                img_menu[i].setImageDrawable(normalDraw[i]);
                txt_menu[i].setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));
            }

        }

    }
    private void setMenuFragment(int menuCase){
        setMenuFragment(menuCase,null);
    };
    private void setMenuFragment(int menuCase,Bundle data){


        switch (menuCase){
            case 0://首页
                setMenuUI(0);
                setHeadCentertText(menuStr[0]);
                layoutHead.setVisibility(View.GONE);
                viewRingBar.setVisibility(View.GONE);
                f_index_recommend=(F_index)fm.findFragmentByTag("F_index");
                if (f_index_recommend==null){
                    f_index_recommend=new F_index();
                    addFragmentShow(f_index_recommend,"F_index");
                }else{
                    if (f_index_recommend.isHidden()){
                        showFragment(f_index_recommend);
                    }
                }

                break;

            case 1://促销
                setMenuUI(1);
                setHeadCentertText(menuStr[1]);
                layoutHead.setVisibility(View.VISIBLE);
                viewRingBar.setVisibility(View.GONE);
                F_list_goods f_list_goods=(F_list_goods)fm.findFragmentByTag("F_list_goods");
                Bundle dataList=new Bundle();
                dataList.putInt(F_list_goods.KEY_type, Activity_list_goods.TYPE_sale);
                if (f_list_goods==null){
                    f_list_goods=F_list_goods.newInstance(dataList);
                    addFragmentShow(f_list_goods,"F_list_goods");
                }else{
                    if (f_list_goods.isHidden()){
                        showFragment(f_list_goods);
                    }
                }
                txtLeft.setVisibility(View.VISIBLE);
                txtRight.setVisibility(View.VISIBLE);
                setHeadRightImg(R.drawable.icon_menu_w);

                txtRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActionActivity.this.startActivity(new Intent(MainActionActivity.this,
                                ActionActivity_Goods_Category.class));
                    }
                });
                break;
            case 2://圈子
                setMenuUI(2);
                setHeadCentertText(menuStr[2]);
                layoutHead.setVisibility(View.GONE);
                viewRingBar.setVisibility(View.VISIBLE);
                TextView txtRing=(TextView)viewRingBar.findViewById(R.id.bar_ring_ring);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(txtRing.getWidth(), StaticMethod.dip2px(MainActionActivity.this, 2));
                lp.setMargins(txtRing.getLeft(),0,0,0);
                viewIndicator.setLayoutParams(lp);
                viewIndicator.setVisibility(View.VISIBLE);
                F_ring  f_ring=(F_ring)fm.findFragmentByTag("F_ring");
                if (f_ring==null){
                    f_ring=F_ring.newInstance();
                    addFragmentShow(f_ring,"F_ring");
                }else{
                    if (f_ring.isHidden()){
                        showFragment(f_ring);
                    }
                }

                break;
            case 3://
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(this,LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                setMenuUI(3);
                setHeadCentertText(menuStr[3]);
                layoutHead.setVisibility(View.VISIBLE);
                viewRingBar.setVisibility(View.GONE);
                F_shopping_cart f_shopping_cart=(F_shopping_cart)fm.findFragmentByTag("F_shopping_cart");
                if (f_shopping_cart==null){
                    f_shopping_cart=new F_shopping_cart();
                    addFragmentShow(f_shopping_cart,"F_shopping_cart");
                }else{
                    f_shopping_cart.refreshDB();
                    if (f_shopping_cart.isHidden())
                        showFragment(f_shopping_cart);
                }

                break;
            case 4://
                setMenuUI(4);
                setHeadCentertText(menuStr[4]);
                layoutHead.setVisibility(View.GONE);
                viewRingBar.setVisibility(View.GONE);
                F_my f_my=(F_my)fm.findFragmentByTag("F_my");
                if (f_my==null){
                    f_my=new F_my();
                    addFragmentShow(f_my,"F_my");
                }else{
                    if (f_my.isHidden())
                        showFragment(f_my);
                    f_my.requestServerData();
                }
                break;
            case 5://圈子 推荐
                setMenuUI(2);
                setHeadCentertText(menuStr[2]);
                layoutHead.setVisibility(View.GONE);
                viewRingBar.setVisibility(View.VISIBLE);
                TextView txtRingre=(TextView)viewRingBar.findViewById(R.id.bar_ring_rec);
                LinearLayout.LayoutParams lpRe = new LinearLayout.LayoutParams(txtRingre.getWidth(), StaticMethod.dip2px(MainActionActivity.this, 2));
                lpRe.setMargins(txtRingre.getLeft(),0,0,0);
                viewIndicator.setLayoutParams(lpRe);
                viewIndicator.setVisibility(View.VISIBLE);
                F_ring_re  f_ring_re=(F_ring_re)fm.findFragmentByTag("F_ring_re");
                if (f_ring_re==null){
                    f_ring_re=F_ring_re.newInstance();
                    addFragmentShow(f_ring_re,"F_ring_re");
                }else{
                    if (f_ring_re.isHidden()){
                        showFragment(f_ring_re);
                    }
                }

                break;
            case 6://
                Intent toLogin=new Intent(this,LoginActionActivity.class);
                startActivity(toLogin);
                break;



        }
    }
    /**圈子bar*/
    private View viewRingBar;
    private View viewIndicator;
    private void setRingBar(){
        viewRingBar = LayoutInflater.from(this).inflate(R.layout.layout_bar_ring, frameLayoutBar,false);
        TextView txtBack= (TextView) viewRingBar.findViewById(R.id.bar_ring_left);
        TextView txtRec=(TextView)viewRingBar.findViewById(R.id.bar_ring_rec);
        TextView txtRing=(TextView)viewRingBar.findViewById(R.id.bar_ring_ring);
        TextView txtMenu=(TextView)viewRingBar.findViewById(R.id.bar_ring_right);
        viewIndicator=viewRingBar.findViewById(R.id.view_indicator);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMenuFragment(0);
            }
        });
        txtRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setMenuFragment(5);
            }
        });
        txtRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setMenuFragment(2);
            }
        });
        txtMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTopic();
            }
        });
//        viewRingBar.setVisibility(View.GONE);
//        viewIndicator.setVisibility(View.GONE);
        frameLayoutBar.addView(viewRingBar);
    }

    private void toTopic(){
        if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
            Intent toLogin=new Intent(MainActionActivity.this,LoginActionActivity.class);
            startActivity(toLogin);
            return;
        }
        MainActionActivity.this.startActivity(
                new Intent(MainActionActivity.this, Activity_topic_send.class));
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

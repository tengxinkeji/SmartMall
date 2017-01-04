package cn.lanmei.com.smartmall.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_list_goods extends BaseActionActivity {
    String TAG="Activity_search";

    public static final int TYPE_category=1;
    public static final int TYPE_search=2;
    public static final int TYPE_sale=3;
    public static final int TYPE_Vgoods=4;

    private ImageView imgLeft;
    private EditText eSearch;
    private TextView txtCancle;
    private TextView[] txtTabs=new TextView[4];

    private  Bundle data=null;
    private String searchStr;
    F_list_goods f_goods_grid;
    F_list_goods_2 f_goods_list;
    private boolean ascNum=false;
    private boolean ascPrice=false;
    private boolean isList=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeadShow(false);

    }

    @Override
    public void loadViewLayout() {
        Intent intent=getIntent();

        if (getIntent()!=null){
            data = intent.getBundleExtra(Common.KEY_bundle);
            searchStr=data.getString(F_list_goods.KEY_key);
        }else {
            L.MyLog(TAG,data+"");
            finish();
        }

        isList= SharePreferenceUtil.getBoolean("list_isList",false);

        setMContentView(R.layout.activity_list_search);
        showList();
//
    }

    private void showList(){
       FragmentTransaction bt=fm.beginTransaction();
        if (isList){
            if (f_goods_grid!=null)
                bt.hide(f_goods_grid);
            if (f_goods_list==null){
                f_goods_list=F_list_goods_2.newInstance(data);
                bt.add(R.id.layout_search, f_goods_list,"F_list_goods_2")
                        .addToBackStack("F_list_goods_2");
                bt.commit();

            }else{
                if (f_goods_list.isHidden())
                    bt.show(f_goods_list).commit();
                if (f_goods_grid!=null){
                    f_goods_list.parentId=f_goods_grid.parentId;
                    f_goods_list.category=f_goods_grid.category;
                    f_goods_list.search=f_goods_grid.search;
                    f_goods_list.order=f_goods_grid.order;
                    f_goods_list.isVipgoods=f_goods_grid.isVipgoods;
                    f_goods_list.typeGoods=f_goods_grid.typeGoods;
                    f_goods_list.brand=f_goods_grid.brand;
                    f_goods_list.p=1;
                    f_goods_list.startProgressDialog();
                    f_goods_list.refresh(f_goods_list.category);
                }

            }

        }else {
            if (f_goods_list!=null)
                bt.hide(f_goods_list);
            if (f_goods_grid==null){
                f_goods_grid=F_list_goods.newInstance(data);
                bt.add(R.id.layout_search, f_goods_grid,"F_list_goods")
                        .addToBackStack("F_list_goods");
                bt.commit();

            }else{
                if (f_goods_grid.isHidden())
                    bt.show(f_goods_grid).commit();

                if (f_goods_grid!=null){
                    f_goods_grid.parentId=f_goods_list.parentId;
                    f_goods_grid.category=f_goods_list.category;
                    f_goods_grid.search=f_goods_list.search;
                    f_goods_grid.order=f_goods_list.order;
                    f_goods_grid.isVipgoods=f_goods_list.isVipgoods;
                    f_goods_grid.typeGoods=f_goods_list.typeGoods;
                    f_goods_grid.brand=f_goods_list.brand;
                    f_goods_grid.p=1;
                    f_goods_grid.startProgressDialog();
                    f_goods_grid.refresh(f_goods_grid.category);
                }
            }
        }

    }

    @Override
    public void mfindViewById() {
        imgLeft= (ImageView) findViewById(R.id.back);
        imgLeft.setImageResource(isList?R.drawable.icon_list:R.drawable.icon_grid);

        eSearch= (EditText) findViewById(R.id.search);
        eSearch.setText(searchStr);

        txtCancle= (TextView) findViewById(R.id.cancle);
        txtTabs[0] = (TextView) findViewById(R.id.txt_1);
        txtTabs[1] = (TextView) findViewById(R.id.txt_2);
        txtTabs[2] = (TextView) findViewById(R.id.txt_3);
        txtTabs[3] = (TextView) findViewById(R.id.txt_4);


        txtTabs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isList){
                    f_goods_list.showPopDistance(v);
                }else {
                    f_goods_grid.showPopDistance(v);
                }

            }
        });
        txtTabs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ascNum=!ascNum;
                if (ascNum){
                    txtTabs[1].setText("销量▲");
                    if (isList){
                        f_goods_list.order=1;
                    }else {
                        f_goods_grid.order=1;
                    }

                }else{
                    txtTabs[1].setText("销量▼");
                    if (isList){
                        f_goods_list.order=2;
                    }else {
                        f_goods_grid.order=2;
                    }

                }
                if (isList){
                    f_goods_list.p=1;
                    f_goods_list.refresh(f_goods_list.category);
                }else {
                    f_goods_grid.p=1;
                    f_goods_grid.refresh(f_goods_grid.category);
                }

            }
        });
        txtTabs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ascPrice=!ascPrice;
                if (ascPrice){
                    txtTabs[2].setText("价格▲");
                    if (isList){
                        f_goods_list.order=3;
                    }else {
                        f_goods_grid.order=3;
                    }

                }else{
                    txtTabs[2].setText("价格▼");
                    if (isList){
                        f_goods_list.order=4;
                    }else {
                        f_goods_grid.order=4;
                    }
                }

                if (isList){
                    f_goods_list.p=1;
                    f_goods_list.refresh(f_goods_list.category);
                }else {
                    f_goods_grid.p=1;
                    f_goods_grid.refresh(f_goods_grid.category);
                }
            }
        });
        txtTabs[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isList){
                    f_goods_list.showPopType(v);
                }else {
                    f_goods_grid.showPopType(v);
                }

            }
        });

        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isList=!isList;
                SharePreferenceUtil.putBoolean("list_isList",isList);
                imgLeft.setImageResource(isList?R.drawable.icon_list:R.drawable.icon_grid);
                showList();
            }
        });

        eSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    if (event.getAction()==KeyEvent.ACTION_UP){
                        L.MyLog(TAG,"搜索:"+v.getText()+"---");
                        if (!TextUtils.isEmpty(v.getText())){
                            searchStr=v.getText().toString();
                            if (isList){

                                f_goods_list.search=searchStr;
                                f_goods_list.p=1;
                                f_goods_list.startProgressDialog();
                                f_goods_list.refresh(f_goods_list.category);
                            }else {

                                f_goods_grid.search=searchStr;
                                f_goods_grid.p=1;
                                f_goods_grid.startProgressDialog();
                                f_goods_grid.refresh(f_goods_grid.category);
                            }

                        }else {
                            StaticMethod.showInfo(Activity_list_goods.this,"请输入搜索关键字");
                        }

                    }
                    StaticMethod.hideSoft(Activity_list_goods.this,v);
                    //do something;
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void setTitle(String title) {
        setHeadCentertText(title);
    }

    @Override
    public void showFrament(int casePositon) {
        showFrament(casePositon,null);
    }

    @Override
    public void showFrament(int casePositon, Bundle data) {

    }

    @Override
    public void addFrament(Fragment fragment, String tag) {

    }

    @Override
    public void backFragment(String currentTag) {
        this.finish();
    }
}

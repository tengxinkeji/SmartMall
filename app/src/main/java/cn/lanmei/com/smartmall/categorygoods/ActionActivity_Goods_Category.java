package cn.lanmei.com.smartmall.categorygoods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.datadb.DBSearchManager;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.search.Activity_list_goods;


public class ActionActivity_Goods_Category extends BaseActivity {
    private String TAG="ActionActivity_Goods_Category";
    private ImageView btnBack;
    private EditText editSearch;
    private TextView txtCancle;

    private F_category_child f_category_child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_category);
        btnBack=(ImageView) findViewById(R.id.back);
        editSearch=(EditText) findViewById(R.id.search);
        txtCancle = (TextView) findViewById(R.id.cancle);
        f_category_child= F_category_child.newInstance(0);
        fm.beginTransaction()
                .add(R.id.layout_category_child,f_category_child,"F_category_child")
                .addToBackStack("F_category_child")
                .commit();

        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    if (event.getAction()==KeyEvent.ACTION_UP){
                        String key=v.getText().toString();
                        DBSearchManager dbSearchManager = new DBSearchManager(ActionActivity_Goods_Category.this);
                        dbSearchManager.addSearch(key);
                        L.MyLog(TAG,"搜索:"+v.getText()+"---");
                        Intent toSearchResult=new Intent(ActionActivity_Goods_Category.this,Activity_list_goods.class);
                        toSearchResult.putExtra(Common.KEY_bundle,key);
                        startActivity(toSearchResult);

                    }
                    StaticMethod.hideSoft(ActionActivity_Goods_Category.this   ,v);
                    //do something;
                    return true;
                }
                return false;
            }
        });

        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void showFrament(int casePositon) {
        showFrament(casePositon,null);
    }

    @Override
    public void showFrament(int casePositon, Bundle data) {
        f_category_child.refreshCategory(casePositon);
    }

    @Override
    public void addFrament(Fragment fragment, String tag) {

    }

    @Override
    public void backFragment(String currentTag) {
        this.finish();
    }
}

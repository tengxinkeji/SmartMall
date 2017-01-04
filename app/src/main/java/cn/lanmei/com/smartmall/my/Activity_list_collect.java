package cn.lanmei.com.smartmall.my;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.categorygoods.F_tabs_select;
import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_list_collect extends BaseActionActivity {
    String TAG = "Activity_list_collect";

    private F_tabs_select f_tabs_select;
    private F_list_collect f_list_collect;
    private Button btnDel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void loadViewLayout() {
        setMContentView(R.layout.activity_list);
    }

    @Override
    public void mfindViewById() {
//        f_tabs_select = F_tabs_select.newInstance();
//        fm.beginTransaction().add(R.id.f_goods_tabs, f_tabs_select, "F_tabs_select")
//                .addToBackStack("F_tabs_select")
//                .commit();
        txtRight.setTag(true);
        setHeadRightText("编辑");
        findViewById(R.id.f_goods_tabs).setVisibility(View.GONE);
        btnDel= (Button) findViewById(R.id.del);

        f_list_collect = F_list_collect.newInstance();
        fm.beginTransaction().add(R.id.f_goods_list, f_list_collect, "F_list_collect")
                .addToBackStack("F_list_collect")
                .commit();

        btnDel.setVisibility(View.GONE);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f_list_collect.collectDel();
            }
        });
    }

    @Override
    protected void onHeadRightButton(View v) {
        super.onHeadRightButton(v);
        boolean isEditStatus = (boolean) txtRight.getTag();
        if (isEditStatus){
            setHeadRightText("完成");
            txtRight.setTag(false);
            btnDel.setVisibility(View.VISIBLE);
            f_list_collect.adapterGoodsCollect.setIsEdit(true);
        }else {
            setHeadRightText("编辑");
            txtRight.setTag(true);
            btnDel.setVisibility(View.GONE);
            f_list_collect.adapterGoodsCollect.setIsEdit(false);
        }

    }

    @Override
    public void setTitle(String title) {
        setHeadCentertText(title);

    }

    @Override
    public void showFrament(int casePositon) {
        showFrament(casePositon, null);
    }

    @Override
    public void showFrament(int casePositon, Bundle data) {

        if (f_list_collect != null) {
            f_list_collect.requestRefreshCollect(casePositon,data);
        }
    }

    @Override
    public void addFrament(Fragment fragment, String tag) {

    }

    @Override
    public void backFragment(String currentTag) {
        this.finish();
    }
}

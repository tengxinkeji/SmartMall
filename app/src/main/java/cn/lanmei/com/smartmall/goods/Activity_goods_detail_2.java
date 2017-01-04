package cn.lanmei.com.smartmall.goods;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.myui.DragLayout;


public class Activity_goods_detail_2 extends BaseActivity {
    private DragLayout draglayout;
    private F_goods_detail_2 fragment1;
    private F_goods_params_2 verticalFragment3;
    private int goodsId=211;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_f_goods_detail_2);
        initView();
    }


    /**
     * 初始化View
     *//**/
    private void initView() {
        fragment1= F_goods_detail_2.newInstance(goodsId);
        verticalFragment3=F_goods_params_2.newInstance(goodsId);
//        verticalFragment3=new VerticalFragment3();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.first, fragment1)
                .add(R.id.second, verticalFragment3)
                .commit();

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
    public void setTitle(String title) {

    }

    @Override
    public void showFrament(int casePositon) {

    }

    @Override
    public void showFrament(int casePositon, Bundle data) {

    }

    @Override
    public void addFrament(Fragment fragment, String tag) {

    }

    @Override
    public void backFragment(String currentTag) {

    }
}

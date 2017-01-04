package cn.lanmei.com.smartmall.categorygoods;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.datadb.DBManagerCategory;
import com.common.myui.MyListView;
import com.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterGoodsCategory;
import cn.lanmei.com.smartmall.model.M_categroy;


/**
 *商品分类
 */
public class F_goods_category extends BaseScrollFragment {
    private String TAG="F_goods_category";

    private MyListView mListview;

    private DBManagerCategory dbManagerCategory;
    private List<M_categroy> goodsCategories;
    private AdapterGoodsCategory adapterGoodsCategory;

    public static F_goods_category newInstance() {
        F_goods_category fragment = new F_goods_category();

        return fragment;
    }


    @Override
    public void init() {
        dbManagerCategory=new DBManagerCategory(mContext);
        goodsCategories=new ArrayList<>();
        adapterGoodsCategory=new AdapterGoodsCategory(mContext,goodsCategories);
    }

    private void refreshCategorys(){
        goodsCategories.clear();
        M_categroy category=new M_categroy();
        category.setId(0);
        category.setName("全部");
        goodsCategories.add(category);
        goodsCategories.addAll(dbManagerCategory.getCategroys(0));
        adapterGoodsCategory.refreshData(goodsCategories);
    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_goods_category);
    }

    @Override
    public void findViewById() {
        setScrollViewMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mListview=(MyListView) findViewById(R.id.listview);
        mListview.setAdapter(adapterGoodsCategory);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterGoodsCategory.refreshCurPosition(position);
                mOnFragmentInteractionListener.showFrament(goodsCategories.get(position).getId());
            }
        });

    }

    @Override
    public void requestServerData() {
        refreshCategorys();
    }



    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        refreshCategorys();
        mPullRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }
}

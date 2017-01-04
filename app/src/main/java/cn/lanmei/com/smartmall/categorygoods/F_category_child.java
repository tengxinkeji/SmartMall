package cn.lanmei.com.smartmall.categorygoods;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.datadb.DBManagerCategory;
import com.common.myui.MyGridView;
import com.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterCategory;
import cn.lanmei.com.smartmall.model.M_categroy;
import cn.lanmei.com.smartmall.search.Activity_list_goods;
import cn.lanmei.com.smartmall.search.F_list_goods;


/**
 *
 */
public class F_category_child extends BaseScrollFragment {
    private String TAG="F_category_goods";


    private MyGridView myGridView;

    private DBManagerCategory dbManagerCategory;
    private List<M_categroy> categroys;
    private AdapterCategory adapterCategory;
    private int parentId=0;
    private int p=1;


    public static F_category_child newInstance(int parentId) {
        F_category_child fragment = new F_category_child();
        Bundle data=new Bundle();
        data.putInt("parentId",parentId);
        fragment.setArguments(data);
        return fragment;
    }






    @Override
    public void init(){
        if (getArguments()!=null)
            parentId=getArguments().getInt("parentId");
        dbManagerCategory=new DBManagerCategory(mContext);

        categroys=new ArrayList<>();
        categroys.addAll(dbManagerCategory.getCategroys(parentId));
        adapterCategory=new AdapterCategory(mContext,categroys);
    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_gridview);
    }

    @Override
    public void findViewById() {

        setScrollViewMode(PullToRefreshBase.Mode.PULL_FROM_START);
        myGridView= (MyGridView) findViewById(R.id.gridview);
        myGridView.setAdapter(adapterCategory);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toDetail=new Intent(getActivity(),Activity_list_goods.class);
                Bundle data=new Bundle();
                data.putInt(F_list_goods.KEY_type,Activity_list_goods.TYPE_category);
                data.putInt(F_list_goods.KEY_parentId,categroys.get(position).getId());
                data.putString(F_list_goods.KEY_key,categroys.get(position).getName());
                toDetail.putExtra(F_list_goods.KEY_key,categroys.get(position).getName());
                toDetail.putExtra(Common.KEY_bundle,data);
                getActivity().startActivity(toDetail);
            }
        });
    }

    @Override
    public void requestServerData() {

    }

    public void refreshCategory(int parentId){
        this.parentId=parentId;
        categroys.clear();
        categroys.addAll(dbManagerCategory.getCategroys(parentId));
        adapterCategory.refreshData(categroys);
    }


    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        refreshCategory(parentId);
        mPullRefreshScrollView.onRefreshComplete();
    }


}

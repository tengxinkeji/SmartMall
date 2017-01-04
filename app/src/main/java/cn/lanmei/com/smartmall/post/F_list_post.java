package cn.lanmei.com.smartmall.post;


import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterRing;
import cn.lanmei.com.smartmall.model.M_ring;
import cn.lanmei.com.smartmall.parse.ParserRing;


/**
 *
 */
public class F_list_post extends BaseScrollFragment {
    private String TAG="F_list_post";

    private MyListView myListView;



    private List<M_ring> rings;
    private AdapterRing adapterRing;


    private int p=1;
    private Resources res;

    public static F_list_post newInstance() {
        F_list_post fragment = new F_list_post();


        return fragment;
    }



    @Override
    public void init(){

        res=getResources();
        tag=res.getString(R.string.menu_3);
        rings=new ArrayList<>();
        adapterRing=new AdapterRing(mContext,rings,true);
    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_listview);
    }

    @Override
    public void findViewById() {
        setScrollViewMode(PullToRefreshBase.Mode.BOTH);

        myListView= (MyListView) findViewById(R.id.listview);
        myListView.setAdapter(adapterRing);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra(Common.KEY_id,rings.get(position).getId());
                intent.putExtra("name",rings.get(position).getName());
                getActivity().setResult(Common.resultCode_post,intent);
                getActivity().finish();
            }
        });

    }

    @Override
    public void requestServerData() {
        requestCategoryGoods();

    }

    private void requestCategoryGoods(){

        RequestParams requestParams = new RequestParams(NetData.ACTION_post_list);

        requestParams.addParam("p",p);
        requestParams.setBaseParser(new ParserRing());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_ring>,Integer>(p){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_ring> parserData) {
                super.processData(obj, parserData);
                if (obj==1)
                    rings.clear();
                if (parserData.size()>0)
                    p++;

                rings.addAll(parserData);
                adapterRing.refreshData(rings);

            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                mPullRefreshScrollView.onRefreshComplete();
            }
        });


    }



    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        p=1;
        requestCategoryGoods();


    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        p++;
        requestCategoryGoods();
    }
}

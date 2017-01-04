package cn.lanmei.com.smartmall.categorygoods;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyGridView;
import com.common.net.NetData;
import com.common.net.ParserJsonManager;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterGoods;
import cn.lanmei.com.smartmall.goods.ActivityGoodsDetail_2;
import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.parse.ParserGoods;


/**
 *
 */
public class F_category_goods extends BaseScrollFragment {
    private String TAG="F_category_goods";
    private MyGridView myGridView;

    private ParserJsonManager parserJsonManager;
    private List<M_Goods> categoryGoodses;
    private AdapterGoods adapterGoods;
    private int category;
    private int p=1;

    public static F_category_goods newInstance() {
        F_category_goods fragment = new F_category_goods();

        return fragment;
    }

    @Override
    public void init(){
        parserJsonManager=new ParserJsonManager(mContext);
        categoryGoodses=new ArrayList<>();
        adapterGoods=new AdapterGoods(mContext,categoryGoodses);
    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_gridview);
    }

    @Override
    public void findViewById() {
        setScrollViewMode(PullToRefreshBase.Mode.BOTH);
        myGridView= (MyGridView) findViewById(R.id.gridview);
        myGridView.setAdapter(adapterGoods);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toDetail=new Intent(getActivity(),ActivityGoodsDetail_2.class);
                toDetail.putExtra(Common.KEY_id,categoryGoodses.get(position).getRecordId());
                getActivity().startActivity(toDetail);
            }
        });
    }

    @Override
    public void requestServerData() {
        requestCategoryGoods(category);
    }
    public void requestCategoryGoods(int categoryId,int p){
        this.p=p;
        requestCategoryGoods(categoryId);
    }
    private void requestCategoryGoods(int categoryId){
        this.category=categoryId;
        RequestParams requestParams = new RequestParams(NetData.ACTION_Shop_goods_list);
        requestParams.addParam("category",category);
        requestParams.addParam("p",p);
        requestParams.setBaseParser(new ParserGoods());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_Goods>,Integer>(p){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_Goods> parserData) {
                super.processData(obj, parserData);
                if (obj==1)
                    categoryGoodses.clear();
                if (parserData!=null&&parserData.size()>0){
                    categoryGoodses.addAll(parserData);
                    p++;
                }
                adapterGoods.refreshData(categoryGoodses);

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
        requestCategoryGoods(category);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        requestCategoryGoods(category);
    }
}

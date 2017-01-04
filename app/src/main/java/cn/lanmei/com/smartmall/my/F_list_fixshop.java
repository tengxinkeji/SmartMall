package cn.lanmei.com.smartmall.my;


import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterFollow;
import cn.lanmei.com.smartmall.main.BaseActionActivity;
import cn.lanmei.com.smartmall.model.M_follow;
import cn.lanmei.com.smartmall.parse.ParserFollow;


/**
 *维修商列表
 *
 */
public class F_list_fixshop extends BaseScrollFragment {

    private String TAG="F_list_fixshop";

    private MyListView myListView;

    private Resources res;

    private int p=1;
    private List<M_follow> mFollows;
    private AdapterFollow adapterFollow;


    public static F_list_fixshop newInstance() {
        F_list_fixshop f=new F_list_fixshop();
        return f;
    }

    @Override
    public void init(){

        res = mContext.getResources();
        tag = "我的关注";
        mFollows=new ArrayList<>();
        adapterFollow=new AdapterFollow(mContext,false,mFollows);

    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_listview);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            initHead();
        }
    }
    private void initHead(){
        ((BaseActionActivity)getActivity()).txtRight.setVisibility(View.GONE);

    }

    @Override
    public void findViewById() {
        initHead();


        myListView= (MyListView)findViewById(R.id.listview);
        myListView.setDivider(ContextCompat.getDrawable(mContext,R.color.transparent));
        myListView.setDividerHeight(StaticMethod.dip2px(mContext,20));
        myListView.setAdapter(adapterFollow);

    }

    @Override
    public void requestServerData() {
        p=1;
        requestList();
    }


    /**刷新*/
    public void requestList() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_Member_follow);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("act",0);
        requestParams.addParam("type",1);
        requestParams.setBaseParser(new ParserFollow());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_follow>,Integer>(p){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_follow> parserData) {
                super.processData(obj, parserData);
                if (obj==1)
                    mFollows.clear();
                if (parserData.size()>0)
                    p++;
                mFollows.addAll(parserData);
                adapterFollow.refreshData(mFollows);

            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                mPullRefreshScrollView.onRefreshComplete();
            }
        });

    }



    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        p=1;
        requestList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        requestList();
    }


}

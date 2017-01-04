package cn.lanmei.com.smartmall.news;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.adapter.AdapterNews;
import cn.lanmei.com.smartmall.parse.ParserNews;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_news;


/**
 *
 */
public class F_news_left extends BaseScrollFragment {
    private String TAG="F_news_left";
    private int goodsId;
    private MyListView mListView;


    private List<M_news> newsList;
    private AdapterNews adapterNews;

    public static F_news_left newInstance(int goodsId) {
        F_news_left fragment = new F_news_left();
        Bundle bundle = new Bundle();
        bundle.putInt("goodsId",goodsId);
//        bundle.putInt("goodsId",6);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void init() {
        if (getArguments()!=null){
            goodsId=getArguments().getInt("goodsId");
        }
        newsList=new ArrayList<>();

        adapterNews=new AdapterNews(mContext,newsList);
    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_listview);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void findViewById() {
        mListView= (MyListView) findViewById(R.id.listview);
        mListView.setDivider(ContextCompat.getDrawable(mContext,R.drawable.divider_t));
        mListView.setDividerHeight(StaticMethod.dip2px(mContext,10));
        mListView.setAdapter(adapterNews);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toDetail=new Intent(getActivity(),Activity_news_detail.class);
                toDetail.putExtra(Common.KEY_bundle,newsList.get(position));
                getActivity().startActivity(toDetail);
            }
        });


    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        requset();
    }

    @Override
    public void requestServerData() {
        requset();
    }

    private void requset(){
        RequestParams requestParams=new RequestParams(NetData.ACTION_News);
        requestParams.addParam("key", "news");
        requestParams.setBaseParser(new ParserNews());
        getDataFromServer(requestParams, new DataCallBack<List<M_news>>() {

            @Override
            public void onPre() {

            }

            @Override
            public void processData(List<M_news> parserData) {
                newsList.clear();
                newsList.addAll(parserData);
                adapterNews.refreshData(newsList);
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();

            }
        });
    }

    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        if (result!=null){
            try {
                if (result.getInt("status")==1){

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            StaticMethod.showInfo(mContext,"绑定失败");
        }
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }



}

package cn.lanmei.com.smartmall.sales;


import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterSales;
import cn.lanmei.com.smartmall.main.BaseActionActivity;
import cn.lanmei.com.smartmall.model.M_user;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.parse.ParserUser;


/**
 *维修商列表
 *
 */
public class F_list_sales_one extends BaseScrollFragment {

    private String TAG="F_list_sales_one";

    private MyListView myListView;

    private Resources res;

    private int p=1;
    private List<M_user> mUsers;
    private AdapterSales adapterSales;


    public static F_list_sales_one newInstance() {
        F_list_sales_one f=new F_list_sales_one();
        return f;
    }

    @Override
    public void init(){

        res = mContext.getResources();
        tag = "分销";
        mUsers=new ArrayList<>();
        adapterSales=new AdapterSales(mContext,mUsers,1);

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
        myListView.setDivider(ContextCompat.getDrawable(mContext,R.color.divider));
        myListView.setDividerHeight(StaticMethod.dip2px(mContext,1));
        myListView.setAdapter(adapterSales);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toInfo=new Intent(getActivity(),Activity_salesshop_info.class);
                toInfo.putExtra(Common.KEY_id,mUsers.get(position).getId());
                getActivity().startActivity(toInfo);
            }
        });
    }

    @Override
    public void requestServerData() {
        p=1;
        requestList();
    }


    /**刷新*/
    public void requestList() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_sales_team);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("sub",1);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new SimpleDataCallBack<JSONObject,Integer>(p){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, JSONObject parserData) {
                super.processData(obj, parserData);
                try {
                    if (parserData.getInt("status")==1){
                        int count1 = parserData.getInt("count1");
                        int count2 = parserData.getInt("count2");
                        ((Activity_list_sales)getActivity()).refreshNum(count1,count2);
                        mUsers.clear();
                        mUsers.addAll(new ParserUser().parserJson(parserData.toString()));
                        adapterSales.refreshData(mUsers);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

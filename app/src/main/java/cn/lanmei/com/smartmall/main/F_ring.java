package cn.lanmei.com.smartmall.main;


import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

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
import cn.lanmei.com.smartmall.adapter.AdapterRing;
import cn.lanmei.com.smartmall.model.M_ring;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.parse.ParserRing;


/**
 *
 */
public class F_ring extends BaseScrollFragment {
    private String TAG="F_ring";
    private TextView txtMore;
    private MyListView myGridView;
    private MyListView myGridView_Re;


    private List<M_ring> rings;
    private AdapterRing adapterRing;

    private List<M_ring> rings_Re;
    private AdapterRing adapterRing_Re;

    private int p=1;
    private int recommend=1;
    private boolean isFirstMore=true;
    private Resources res;

    public static F_ring newInstance() {
        F_ring fragment = new F_ring();


        return fragment;
    }



    @Override
    public void init(){

        res=getResources();
        tag=res.getString(R.string.menu_3);
        rings=new ArrayList<>();
        rings_Re=new ArrayList<>();
        adapterRing=new AdapterRing(mContext,rings,true);
        adapterRing_Re=new AdapterRing(mContext,rings_Re,false);



    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_ring);
    }

    @Override
    public void findViewById() {
        setScrollViewMode(PullToRefreshBase.Mode.BOTH);
        txtMore= (TextView) findViewById(R.id.txt_add);
        myGridView= (MyListView) findViewById(R.id.gridview);
        myGridView_Re= (MyListView) findViewById(R.id.grid_re);
        myGridView.setAdapter(adapterRing);
        myGridView_Re.setAdapter(adapterRing_Re);

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toList=new Intent(getActivity(), Activity_ring_detail.class);
                toList.putExtra(Common.KEY_bundle,rings.get(position));

                getActivity().startActivity(toList);
            }
        });
        myGridView_Re.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toList=new Intent(getActivity(), Activity_ring_detail.class);
                toList.putExtra(Common.KEY_bundle,rings_Re.get(position));
                getActivity().startActivity(toList);
            }
        });

        adapterRing_Re.setOnListenerAddAttention(new AdapterRing.OnListenerAddAttention() {
            @Override
            public void onListenerAdd(int position) {
                attentionPost(rings_Re.get(position).getId(),position);

            }
        });

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommend=0;
                if (isFirstMore){
                    p=1;
                    isFirstMore=false;
                }else {
                    p++;
                }


                requestCategoryGoods();
            }
        });
    }

    @Override
    public void requestServerData() {
        requestCategoryGoods();
        requestAttePost();
    }

    private void requestCategoryGoods(){

        RequestParams requestParams = new RequestParams(NetData.ACTION_post_list);
        requestParams.addParam("recommend",recommend);
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
                    rings_Re.clear();
                if (parserData.size()>0)
                    p++;

                rings_Re.addAll(parserData);
                adapterRing_Re.refreshData(rings_Re);

            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                mPullRefreshScrollView.onRefreshComplete();
            }
        });


    }

    private void requestAttePost(){

        RequestParams requestParams = new RequestParams(NetData.ACTION_post_follow);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
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

    private void attentionPost(int id,int position){

        RequestParams requestParams = new RequestParams(NetData.ACTION_post_do_follow);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("cid",id);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new SimpleDataCallBack<JSONObject,Integer>(position) {
            @Override
            public void processData(Integer obj, JSONObject parserData) {
                super.processData(obj, parserData);
                if (parserData!=null){
                    try {
                        StaticMethod.showInfo(mContext,parserData.getString("info"));
                        if (parserData.getInt("status")==1){
                            rings.add(rings_Re.get((int)obj));
                            adapterRing.refreshData(rings);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
        requestAttePost();

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        p++;
        requestCategoryGoods();
    }
}

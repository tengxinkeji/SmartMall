package cn.lanmei.com.smartmall.myuser;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.common.app.BaseFragment;
import com.common.app.MyApplication;
import com.common.app.degexce.L;
import com.common.myinterface.SimpleDataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_log;
import cn.lanmei.com.smartmall.parse.ParserLog;
import se.emilsjolander.stickylistheaders.RefreshLayout;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


/**
 *我的收藏列表
 *
 */
public class F_list_moneylog extends BaseFragment
        implements AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {

    private String TAG="F_list_moneylog";

//    AdapterBill adapterBill;
    private TestBaseAdapter mAdapter;
    private StickyListHeadersListView stickyList;
    private RefreshLayout refreshLayout;

    private int p=1;
    private List<M_log> mLogs;


    public static F_list_moneylog newInstance() {
        F_list_moneylog f=new F_list_moneylog();
        return f;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }


    private void init(){

        tag = "我的账户";
        mLogs=new ArrayList<>();

//        adapterBill=new AdapterBill(mContext,mLogs);
    }

    @Override
    public void loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.layout_list_log,container,false);
    }

    @Override
    public void findViewById() {

        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        //上拉加载
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setLoading(false);
                    }
                }, 1000);
            }
        });

        mAdapter = new TestBaseAdapter(mContext);

        stickyList = (StickyListHeadersListView) findViewById(R.id.list);
        stickyList.setOnItemClickListener(this);
        stickyList.setOnHeaderClickListener(this);
        stickyList.setOnStickyHeaderChangedListener(this);
        stickyList.setOnStickyHeaderOffsetChangedListener(this);

//        stickyList.addHeaderView(getLayoutInflater().inflate(R.layout.list_header, null));
//        stickyList.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
//        stickyList.setEmptyView(findViewById(R.id.empty));

        stickyList.setDrawingListUnderStickyHeader(true);
        stickyList.setAreHeadersSticky(true);
        stickyList.setAdapter(mAdapter);
//        stickyList.setStickyHeaderTopOffset(-10);


    }



    @Override
    public void requestServerData() {
        p=1;
        requestList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        L.MyLog(TAG,"Item " + position + " clicked!");

    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
        L.MyLog(TAG,"Header " + headerId + " currentlySticky ? " + currentlySticky);

    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        header.setAlpha(1);
    }




    /**刷新*/
    private void requestList() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_money_log);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
//        requestParams.addParam("uid",29);
        requestParams.setBaseParser(new ParserLog());
        getDataFromServer(requestParams, new SimpleDataCallBack<M_log[],Integer>(p){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, M_log[] parserData) {
                super.processData(obj, parserData);
                if (obj==1)
                    mLogs.clear();
                if (parserData.length>0)
                    p++;

                mAdapter.refreshData(parserData);


            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);

            }
        });

    }


}

package cn.lanmei.com.smartmall.my;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
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
import cn.lanmei.com.smartmall.adapter.AdapterGoodsCollect;
import cn.lanmei.com.smartmall.goods.ActivityGoodsDetail_2;
import cn.lanmei.com.smartmall.listener.OnCheckSelectListener;
import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.parse.ParserGoods;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *我的收藏列表
 *
 */
public class F_list_collect extends BaseScrollFragment implements OnCheckSelectListener {

    private String TAG="F_list_collect";

    private MyListView myListView;


    private int p=1;
    private List<M_Goods> goodses;
    public AdapterGoodsCollect adapterGoodsCollect;
    private int type=0;
    private String delIds;

    public static F_list_collect newInstance() {
        F_list_collect f=new F_list_collect();
        return f;
    }

    @Override
    public  void init(){

        tag = "我的收藏";
        goodses=new ArrayList<>();
        adapterGoodsCollect=new AdapterGoodsCollect(mContext,goodses);
        adapterGoodsCollect.setOnCheckSelectListener(this);

    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_listview);

    }



    @Override
    public void findViewById() {
        myListView= (MyListView)findViewById(R.id.listview);
        myListView.setDivider(ContextCompat.getDrawable(mContext,R.color.transparent));
        myListView.setDividerHeight(StaticMethod.dip2px(mContext,20));
        myListView.setAdapter(adapterGoodsCollect);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toDetail=new Intent(mContext,ActivityGoodsDetail_2.class);
                toDetail.putExtra(Common.KEY_id,goodses.get(position).getRecordId());
                mContext.startActivity(toDetail);
            }
        });
    }



    @Override
    public void requestServerData() {
        p=1;
        requestList();
    }

    public void collectDel(){
        if (delIds!=null){
            delIds(delIds);
        }
    }

    public void requestRefreshCollect(int casePositon, Bundle data){
        switch (casePositon){
            case 1://排序
                int type=data.getInt("sort");
                L.MyLog(TAG,"排序:"+type);
                break;
            case 2://是否销量升序
                boolean ascNum=data.getBoolean("ascNum");
                L.MyLog(TAG,"ascNum:"+ascNum);
                break;
            case 3://是否价格升序
                boolean ascPrice=data.getBoolean("ascPrice");
                L.MyLog(TAG,"ascPrice:"+ascPrice);
                break;
            case 4://筛选
                String select=data.getString("select");
                L.MyLog(TAG,"select:"+select);
                break;
        }

    }

    /**刷新*/
    private void requestList() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_goods_favorite);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("act",0);
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
                    goodses.clear();
                if (parserData.size()>0)
                    p++;
                goodses.addAll(parserData);
                adapterGoodsCollect.refreshData(goodses);

            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                mPullRefreshScrollView.onRefreshComplete();
            }
        });

    }

    /**
     * 取消收藏
     * @param delIds 删除时，可批量 id=1,2,3
     * */
    public void delIds(String delIds) {
        RequestParams requestParams = new RequestParams(NetData.ACTION_goods_favorite);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("act",2);
        requestParams.addParam("id",delIds);

        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>(){

            @Override
            public void onPre() {
                startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                parserInfo(parserData);
            }

            @Override
            public void onComplete() {
                stopProgressDialog();
            }
        });

    }


    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        p=1;
        if (result!=null){
            try {
                StaticMethod.showInfo(mContext,result.getString("info"));
                if (result.getInt("status")==1){
                    p=1;
                    requestList();
                    adapterGoodsCollect.clearCheck();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            StaticMethod.showInfo(mContext,"请求失败");
        }
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


    @Override
    public void onCheckSelectListener(List<Integer> selectedList) {
        if (selectedList.size()>0){
            delIds="";
            for (Integer id:selectedList){
                delIds+=id+",";
            }
            delIds=delIds.substring(0,delIds.length()-1);
        }else {
            delIds=null;
        }
    }
}

package cn.lanmei.com.smartmall.myuser;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
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

import cn.lanmei.com.smartmall.adapter.AdapterBank;
import cn.lanmei.com.smartmall.model.M_bank;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.goods.Activity_address_addormodify;
import cn.lanmei.com.smartmall.parse.ParserBank;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *银行卡列表
 */
public class F_list_bank extends BaseScrollFragment {


    private String TAG="F_list_bank";
    private MyListView myListView;

    private List<M_bank> mBanks;
    private AdapterBank adapterBank;

    private int p=1;

    public static F_list_bank newInstance() {
        F_list_bank fragment = new F_list_bank();

        return fragment;
    }




    @Override
    public void init() {
        mBanks=new ArrayList<>();
        adapterBank=new AdapterBank(mContext,mBanks);
    }

    @Override
    public void loadViewLayout() {
        tag = "银行卡";
        setContentView(R.layout.layout_listview);


    }

    @Override
    public void findViewById() {
        myListView=(MyListView) findViewById(R.id.listview);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) myListView.getLayoutParams();
        int div=StaticMethod.dip2px(mContext,10);
        lp.setMargins(div,div,div,div);
        myListView.setLayoutParams(lp);
        myListView.setDivider(ContextCompat.getDrawable(mContext,R.color.divider));
        myListView.setDividerHeight(div);
        myListView.setAdapter(adapterBank);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent backOrderOk=new Intent();
                Bundle data=new Bundle();
                data.putSerializable(Common.KEY_bundle,mBanks.get(position));
                backOrderOk.putExtra(Common.KEY_bundle,data);
                getActivity().setResult(Common.resultCode_bank_select,backOrderOk);
                getActivity().finish();
//                mOnFragmentInteractionListener.addFrament(new F_device_detail(mDevices.get(position)),"F_device_detail");
            }
        });
        isFirstRequest=true;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.MyLog(TAG,"requestCode:"+requestCode+"---resultCode:"+resultCode);
        if (requestCode==Common.requestCode_bank_add&&resultCode==Common.resultCode_bank_add){//添加返回
            p=1;
            requestServerData();
        }
    }

    private boolean isFirstRequest;

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        isFirstRequest=false;
        requestServerData();
    }

    @Override
    public void requestServerData() {
        RequestParams requestParams=new RequestParams(NetData.ACTION_bank_card);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());

        requestParams.setBaseParser(new ParserBank());
        getDataFromServer(requestParams, new DataCallBack<List<M_bank>>() {

            @Override
            public void onPre() {
                   if (isFirstRequest)
                       startProgressDialog();
            }

            @Override
            public void processData(List<M_bank> parserData) {
                mBanks.clear();
                mBanks.addAll(parserData);
                adapterBank.refreshData(mBanks);
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
                stopProgressDialog();
            }
        });
    }

    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        if (result!=null){
            try {
                if (result.getInt("status")==1){

                }
                stopProgressDialog();
                requestServerData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            StaticMethod.showInfo(mContext,"修改失败");
        }
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }

    private void modifyDefaultAddr(int id,boolean isDefault){
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_address);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("act", 1);
        requestParams.addParam("id", id);
        requestParams.addParam("default", isDefault?1:0);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {

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

    private void addrDel(int id){
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_address);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("act", -1);
        requestParams.addParam("id", id);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {

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

    /**
     * @param id  id==0 添加 ，else 修改
     * */
    public void addrAddorModify(int id){
        Intent toAddAddr=new Intent(getActivity(),Activity_address_addormodify.class);
        toAddAddr.putExtra(Common.KEY_id,id);
        getActivity().startActivityForResult(toAddAddr,Common.requestCode_addr_add);
    }




}

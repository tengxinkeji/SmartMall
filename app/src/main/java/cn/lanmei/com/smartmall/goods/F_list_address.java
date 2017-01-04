package cn.lanmei.com.smartmall.goods;


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
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterAddr;
import cn.lanmei.com.smartmall.model.M_address;
import cn.lanmei.com.smartmall.parse.ParserAddress;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *收货地址列表
 */
public class F_list_address extends BaseScrollFragment implements AdapterAddr.AddressListener{


    private String TAG="F_list_address";
    private MyListView myListView;

    private List<M_address> mAddresses;
    private AdapterAddr adapterAddr;


    public static F_list_address newInstance() {
        F_list_address fragment = new F_list_address();

        return fragment;
    }


    @Override
    public void init() {
        mAddresses=new ArrayList<>();
        adapterAddr=new AdapterAddr(mContext,mAddresses);
        adapterAddr.setAddressListener(this);
    }

    @Override
    public void loadViewLayout() {
        tag = "地址管理";
        setContentView(R.layout.layout_listview);


    }

    @Override
    public void findViewById() {
        myListView=(MyListView) findViewById(R.id.listview);
        myListView.setDivider(ContextCompat.getDrawable(mContext,R.color.divider));
        myListView.setDividerHeight(1);
        myListView.setAdapter(adapterAddr);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent backOrderOk=new Intent();
                Bundle data=new Bundle();
                data.putSerializable(Common.KEY_bundle,mAddresses.get(position));
                backOrderOk.putExtra(Common.KEY_bundle,data);
                getActivity().setResult(Common.resultCode_addr_select,backOrderOk);
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
        if (requestCode==Common.requestCode_addr_add){
            if (resultCode==Common.resultCode_addr_add){
                requestServerData();
            }
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
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_address);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("act", 0);
        requestParams.setBaseParser(new ParserAddress());
        getDataFromServer(requestParams, new DataCallBack<List<M_address>>() {

            @Override
            public void onPre() {
                   if (isFirstRequest)
                       startProgressDialog();
            }

            @Override
            public void processData(List<M_address> parserData) {
                mAddresses.clear();
                mAddresses.addAll(parserData);
                adapterAddr.refreshData(mAddresses);
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


    @Override
    public void addressDefaultListener(int position,int id, boolean isDefault) {
        modifyDefaultAddr(id,isDefault);
    }

    @Override
    public void addressEditListener(int position, int id) {
        addrAddorModify(id);
    }

    @Override
    public void addressDelListener(int position, int id) {
        addrDel(id);
    }

}

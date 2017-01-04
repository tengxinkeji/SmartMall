package cn.lanmei.com.smartmall.device;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.parse.ParserDeviceSub;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterDeviceManager;
import cn.lanmei.com.smartmall.model.M_DevSub;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *设备管理列表
 */
@SuppressLint("ValidFragment")
public class F_device_manager_list extends BaseScrollFragment {
    private String TAG="F_device_manager_list";

    private MyListView listDeviceSub;

    private String id;
    private List<M_DevSub> devSubs;
    private AdapterDeviceManager adapterDeviceManager;

    public F_device_manager_list(String id) {
        this.id = id;
    }



    @Override
    public void init() {
        devSubs=new ArrayList<>();
        adapterDeviceManager=new AdapterDeviceManager(mContext,devSubs);
    }

    @Override
    public void loadViewLayout() {
        tag = "设备控制成员";
        setContentView(R.layout.layout_listview);

    }

    @Override
    public void findViewById() {

        listDeviceSub=(MyListView) findViewById(R.id.listview);
        listDeviceSub.setDivider(new ColorDrawable(ContextCompat.getColor(mContext,R.color.txtColor_hint)));
        listDeviceSub.setDividerHeight(1);
        int padding=StaticMethod.dip2px(mContext,10);
        LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT) ;
        layoutParams.setMargins(padding,padding,padding,padding);
        listDeviceSub.setLayoutParams(layoutParams);
        listDeviceSub.setPadding(padding,padding,padding,padding);
        listDeviceSub.setBackgroundResource(R.drawable.bg_corners_w);
        listDeviceSub.setAdapter(adapterDeviceManager);

        adapterDeviceManager.setDeviceManagerListener(new AdapterDeviceManager.DeviceManagerListener() {
            @Override
            public void deviceManagerDel(String rid,String mid) {
                deviceUserManageDel(id,mid);
            }

            @Override
            public void deviceManagerRemark(String rid) {

            }

            @Override
            public void deviceManagerAccess(String rid) {
                Intent toIntent=new Intent(getActivity(),Activity_device_access_manager_list.class);
                Bundle bundle = new Bundle();
                bundle.putString(Common.KEY_id,id);
                bundle.putSerializable(Common.KEY_bundle, (Serializable) devSubs);
                toIntent.putExtra(Common.KEY_bundle,bundle);
                getActivity().startActivity(toIntent);
            }
        });

        listDeviceSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }



    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        deviceUserManageList();
    }

    @Override
    public void requestServerData() {
        deviceUserManageList();
    }

    private void deviceUserManageList(){
//      http://120.25.132.245/App/Member/device_sub?appkey=33829f311e7d2c2940cbb365c6844d33&uid=6&id=47
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device_sub);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("id", id);
        requestParams.setBaseParser(new ParserDeviceSub());
        getDataFromServer(requestParams, new DataCallBack<List<M_DevSub>>(){

            @Override
            public void onPre() {

            }

            @Override
            public void processData(List<M_DevSub> devSub) {
                devSubs.clear();
                devSubs.addAll(devSub);
                adapterDeviceManager.refreshData(devSubs);

            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
            }
        });
    }


    private void deviceUserManageDel(String id,String mid){
//      http://120.25.132.245/App/Member/device_sub?appkey=33829f311e7d2c2940cbb365c6844d33&uid=6&id=47
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device_sub);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("id", id);
        requestParams.addParam("del", 1);
        requestParams.addParam("mid", mid);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>(){

            @Override
            public void onPre() {
                startProgressDialog();
            }

            @Override
            public void processData(JSONObject jsonObject) {
                parserInfo(jsonObject);
            }

            @Override
            public void onComplete() {
                stopProgressDialog();
            }
        });
    }



    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();

    }

    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        if (result!=null){
            try {
                StaticMethod.showInfo(mContext,result.getString("info"));
                if (result.getInt("status")==1){
                    deviceUserManageList();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                StaticMethod.showInfo(mContext,"删除失败");
            }
        }else{
            StaticMethod.showInfo(mContext,"删除失败");
        }
    }
}

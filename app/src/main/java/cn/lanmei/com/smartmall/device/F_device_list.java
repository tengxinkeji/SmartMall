package cn.lanmei.com.smartmall.device;


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
import com.demo.smarthome.device.Dev;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterDevice;
import cn.lanmei.com.smartmall.parse.ParserDevice;


/**
 *设备列表
 */
public class F_device_list extends BaseScrollFragment {
    private String TAG="F_device_list";
    private MyListView myListView;

    private List<Dev> mDevices;
    private AdapterDevice adapterDevice;

    public static F_device_list newInstance() {
        F_device_list fragment = new F_device_list();

        return fragment;
    }





    @Override
    public void init() {

    }

    @Override
    public void loadViewLayout() {
        tag = getResources().getString(R.string.menu_device_2);
        setContentView(R.layout.layout_listview);
        setBg(R.mipmap.bg_device);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.MyLog(TAG,"requestCode:"+requestCode+"resultCode:"+resultCode);
        if (requestCode==Common.requestCode_devdetail&&resultCode==Common.resultCode_devdetail){
            requestServerData();
        }
    }

    @Override
    public void findViewById() {
        myListView=(MyListView) findViewById(R.id.listview);
        int top=StaticMethod.dip2px(mContext,10);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0,top,0,top);
        myListView.setLayoutParams(lp);
        myListView.setDivider(ContextCompat.getDrawable(mContext,R.color.transparent));
        myListView.setDividerHeight(top);
        mDevices=new ArrayList<Dev>();
        adapterDevice=new AdapterDevice(mContext,mDevices);
        myListView.setAdapter(adapterDevice);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toDevDetail=new Intent(getActivity(),Activity_device_detail.class);
                Bundle data=new Bundle();
                data.putSerializable(Common.KEY_bundle,mDevices.get(position));
                toDevDetail.putExtra(Common.KEY_bundle,data);
                getActivity().startActivityForResult(toDevDetail,Common.requestCode_devdetail);
//                mOnFragmentInteractionListener.addFrament(new F_device_detail(mDevices.get(position)),"F_device_detail");
            }
        });
        isFirstRequest=true;

    }
    private boolean isFirstRequest;

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        isFirstRequest=false;
        requestServerData();
    }

    @Override
    public void requestServerData() {
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.setBaseParser(new ParserDevice());
        getDataFromServer(requestParams, new DataCallBack<List<Dev>>() {

            @Override
            public void onPre() {
                   if (isFirstRequest)
                       startProgressDialog();
            }

            @Override
            public void processData(List<Dev> parserData) {
                mDevices=parserData;
                adapterDevice.refreshData(parserData);
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

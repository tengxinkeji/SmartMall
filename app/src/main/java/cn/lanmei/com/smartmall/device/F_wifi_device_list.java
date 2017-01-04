package cn.lanmei.com.smartmall.device;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.myui.MyListView;
import com.espressif.iot.esptouch.EsptouchResult;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterWifiDevices;


/**
 *wifi设备列表
 */
@SuppressLint("ValidFragment")
public class F_wifi_device_list extends BaseScrollFragment {
    private String TAG="F_wifi_device_list";
    private MyListView myListView;

    private List<EsptouchResult> mDevices;
    private AdapterWifiDevices adapterDevice;

    public F_wifi_device_list(List<EsptouchResult> mDevices) {
        this.mDevices = mDevices;
    }



    @Override
    public void init() {

    }

    @Override
    public void loadViewLayout() {
        tag = getResources().getString(R.string.menu_device_2);
        setContentView(R.layout.layout_listview);


    }

    @Override
    public void findViewById() {
        myListView=(MyListView) findViewById(R.id.listview);
        if (mDevices!=null){
            adapterDevice=new AdapterWifiDevices(mContext,mDevices);
            myListView.setAdapter(adapterDevice);
        }

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toDevDetail=new Intent(getActivity(),Activity_device_scan.class);
                Bundle data=new Bundle();
                data.putSerializable(Common.KEY_bundle,mDevices.get(position));
                toDevDetail.putExtra(Common.KEY_bundle,data);
                getActivity().startActivity(toDevDetail);
//                mOnFragmentInteractionListener.addFrament(new F_device_detail(mDevices.get(position)),"F_device_detail");
            }
        });

    }


    @Override
    public void requestServerData() {

    }


    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }
}

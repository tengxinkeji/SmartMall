package cn.lanmei.com.smartmall.device;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.datadb.DBDeviceErrManager;
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
import cn.lanmei.com.smartmall.adapter.AdapterDevInfo;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.model.M_Dev_Err;
import cn.lanmei.com.smartmall.model.M_device_info;
import cn.lanmei.com.smartmall.myui.MyInputEdit;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.socket.SocketAction;
import cn.lanmei.com.smartmall.socket.WebSocketDevService;
import smartmall.socket.IBackService;


/**
 *设备异常
 */
public class F_device_err extends BaseScrollFragment {
    private String TAG="F_device_err";
    private ImageView imgDevErr;
    private MyInputEdit uiDeviceName;
    private MyInputEdit uiDeviceId;
    private MyInputEdit uiDeviceErrcode;
    private TextView uiDeviceErr;
    private MyInputEdit uiDeviceErrtime;
    private TextView txtRefer;
    private TextView txtOk;
    private MyListView myListView;

    DBDeviceErrManager dbDeviceErrManager;

    private String devErrDevNo;
    private String faultmsg;
    M_Dev_Err devErr;

    private List<M_device_info> deviceInfos;
    private AdapterDevInfo adapterDevInfo;

    public static F_device_err newInstance(String devErrDevNo,String devCode) {
        F_device_err fragment = new F_device_err();
        Bundle bundle = new Bundle();
        bundle.putString(Common.DEV_device_no,devErrDevNo);
        bundle.putString(Common.DEV_device_fault,devCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    private IBackService iBackService;
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBackService = null;

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBackService = IBackService.Stub.asInterface(service);
            L.MyLog(TAG,"onServiceConnected:"+iBackService);
            if (iBackService!=null){
                try {
                    iBackService.initServer(devErrDevNo);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    boolean isOnline=false;
    class MessageBackReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case SocketAction.ACTION_online://
                    isOnline=true;

                    break;
                case SocketAction.ACTION_offline://
                    isOnline=false;

                    break;
                case SocketAction.LOGIN_ACTION://
                    L.MyLog(TAG,"登录:"+devErrDevNo);
                    if (!TextUtils.isEmpty(devErrDevNo)){
                        try {
                            if (iBackService!=null){
                                iBackService.startSendHeartMessage(devErrDevNo);
                            }

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case SocketAction.MESSAGE_ACTION://
                    String message = intent.getStringExtra("message");
                    parserReciverMsg(message);
                    break;


            }
        };
    }
    private Intent mServiceIntent;
    private MessageBackReciver mReciver;
    private IntentFilter mIntentFilter;
    private LocalBroadcastManager mLocalBroadcastManager;

    private void parserReciverMsg(String msg){
//        L.MyLog(TAG,msg+"");
        String deviceAll=null;
        if (!msg.startsWith("{"))
            return;
        try {
            JSONObject msgJson = new JSONObject(msg);
            if (msgJson.has("ALL")){
                deviceAll=msgJson.getString("ALL");
                if (TextUtils.isEmpty(deviceAll))
                    return;
                L.MyLog(TAG,deviceAll+"");
                deviceAll = deviceAll.replaceAll("\\|"," ");
                String[] devInfo = deviceAll.split(" ");
                refreshData(devInfo);
//
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }


    }

    private void refreshData(String[] devInfo){
        if (devInfo.length==64){
            int offset=32;
            //出水
            deviceInfos.get(0).setValue((Integer.parseInt(devInfo[50],16)-offset)+"");
            //水箱
            deviceInfos.get(1).setValue((Integer.parseInt(devInfo[51],16)-offset)+"");
            //环境
            deviceInfos.get(2).setValue((Integer.parseInt(devInfo[52],16)-offset)+"");
            //盘管
            deviceInfos.get(3).setValue((Integer.parseInt(devInfo[53],16)-offset)+"");
            //回气
            deviceInfos.get(4).setValue((Integer.parseInt(devInfo[54],16)-offset)+"");
            //排气
            deviceInfos.get(5).setValue((Integer.parseInt(devInfo[55],16)-offset)+"");
            //wifi
            deviceInfos.get(6).setValue((Integer.parseInt(devInfo[29],16)-offset)+"");
            adapterDevInfo.refreshData(deviceInfos);
        }

    }

    private void initSocket(){

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        mReciver = new  MessageBackReciver();

        mServiceIntent = new Intent(getActivity(), WebSocketDevService.class);
//        mServiceIntent = new Intent(getActivity(), BackService.class);
        getActivity().startService(mServiceIntent);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(SocketAction.ACTION_online);
        mIntentFilter.addAction(SocketAction.ACTION_offline);
        mIntentFilter.addAction(SocketAction.LOGIN_ACTION);
        mIntentFilter.addAction(SocketAction.MESSAGE_ACTION);
        mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
        getActivity().bindService(mServiceIntent, conn, Context.BIND_AUTO_CREATE);
    }



    @Override
    public void init() {
        tag = "故障详情";
        if (getArguments()!=null){
            devErrDevNo=getArguments().getString(Common.DEV_device_no);
            faultmsg=getArguments().getString(Common.DEV_device_fault);
        }
        L.MyLog(TAG,"devErrDevNo:"+devErrDevNo+"---faultmsg:"+faultmsg);
        dbDeviceErrManager=new DBDeviceErrManager(mContext);
        devErr=dbDeviceErrManager.getErr(devErrDevNo,faultmsg);

        if (faultmsg.contains("|")){
            String[] faults = faultmsg.split("\\|");
        }

        deviceInfos=new ArrayList<>();
        deviceInfos.add(new M_device_info("出水温度","--"));
        deviceInfos.add(new M_device_info("水箱温度","--"));
        deviceInfos.add(new M_device_info("环境温度","--"));
        deviceInfos.add(new M_device_info("盘管温度","--"));
        deviceInfos.add(new M_device_info("压缩机回气温度","--"));
        deviceInfos.add(new M_device_info("压缩机排气温度","--"));
        deviceInfos.add(new M_device_info("WIFI信号温度","--"));
        adapterDevInfo=new AdapterDevInfo(mContext,deviceInfos);

        initSocket();
    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_device_err);
    }

    @Override
    public void findViewById() {
        imgDevErr=(ImageView) findViewById(R.id.img_dev_err);
        uiDeviceName=(MyInputEdit) findViewById(R.id.ui_device_name);
        uiDeviceId=(MyInputEdit) findViewById(R.id.ui_device_id);
        uiDeviceErrcode=(MyInputEdit) findViewById(R.id.ui_device_errcode);
        uiDeviceErr=(TextView) findViewById(R.id.txt_device_err);
        uiDeviceErrtime=(MyInputEdit) findViewById(R.id.ui_device_errtime);
        myListView=(MyListView) findViewById(R.id.listview);
        myListView.setAdapter(adapterDevInfo);

        txtRefer=(TextView) findViewById(R.id.refer);
        txtOk=(TextView) findViewById(R.id.ok);

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(faultmsg)){
                    StaticMethod.showInfo(mContext,"故障处理中...");
                }else{
                    M_Dev_Err devErr = new M_Dev_Err();

                    devErr.setErrDoneTime(System.currentTimeMillis());
                    devErr.setErrDevName(uiDeviceName.getText().toString());
                    devErr.setErr(uiDeviceErr.getText().toString());

                    int u=dbDeviceErrManager.updateErrTime(devErrDevNo,faultmsg,devErr);
                    L.MyLog(TAG,"设备故障：更新："+u);
                    if (u<1){
                        long insN = dbDeviceErrManager.addErr(devErr);
                        L.MyLog(TAG,"设备故障：更新：addErr:"+insN);
                    }


                    getActivity().finish();
                }

            }
        });
        if (devErr!=null){
            uiDeviceName.setText(devErr.getErrDevName());
            uiDeviceId.setText(devErr.getErrDevId());
            uiDeviceErrcode.setText(devErr.getErrCode());
            uiDeviceErr.setText(devErr.getErr());
            uiDeviceErrtime.setText(StaticMethod.formatterTime(devErr.getErrTime()));

            ((BaseActivity)getActivity()).getImageLoader()
                    .displayImage(devErr.getPic(),imgDevErr);
        }


    }

    @Override
    public void requestServerData() {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);

    }

    /**绑定设备*/
    private void requestErr(){
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device_fault);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("device_no",devErrDevNo);

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
                mPullRefreshScrollView.onRefreshComplete();
            }
        });
    }

    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        if (result!=null){
            try {
                if (result.getInt("status")==1){
                    result=result.optJSONObject("data");
                    if (result==null)
                        return;
                    uiDeviceName.setText(result.getString("device_name"));
                    uiDeviceId.setText(result.getString("device_id"));
                    faultmsg=result.getString("fault");
                    uiDeviceErrcode.setText(faultmsg);
                    uiDeviceErr.setText(result.getString("fault_format"));
                    uiDeviceErrtime.setText(result.getString("addtime"));

                    ((BaseActivity)getActivity()).getImageLoader()
                            .displayImage(result.getString("pic"),imgDevErr);
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
       mOnFragmentInteractionListener.backFragment(TAG);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (iBackService==null)
            return;
        try {
            L.MyLog(TAG,"onHiddenChanged:"+hidden+"---hasHeartBeat:"+iBackService.hasHeartBeat());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (hidden){
            try {
                if (iBackService.hasHeartBeat())
                    iBackService.removeSendHeartMessage();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            try {
                if (!iBackService.hasHeartBeat())
                    iBackService.startSendHeartMessage(devErrDevNo);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        L.MyLog(TAG,"onResume：isHidden："+isHidden());
        if (iBackService==null||isHidden())
            return;
        try {
            L.MyLog(TAG,"onResume:"+"---hasHeartBeat:"+iBackService.hasHeartBeat());
            if (!iBackService.hasHeartBeat())
                iBackService.startSendHeartMessage(devErrDevNo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        L.MyLog(TAG,"onPause");
        if (iBackService==null)
            return;
        try {
            L.MyLog(TAG,"onPause:"+"---hasHeartBeat:"+iBackService.hasHeartBeat());
            if (iBackService.hasHeartBeat())
                iBackService.removeSendHeartMessage();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceIntent!=null){
            getActivity().unbindService(conn);
            getActivity().stopService(mServiceIntent);
            mLocalBroadcastManager.unregisterReceiver(mReciver);
        }

    }

}

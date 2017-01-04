package cn.lanmei.com.smartmall.device;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.datadb.DBDeviceErrManager;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.demo.smarthome.device.Dev;
import com.pulltorefresh.library.PullToRefreshBase;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.zcw.togglebutton.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterString;
import cn.lanmei.com.smartmall.dialog.PopWindow_List;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.model.M_Dev_Err;
import cn.lanmei.com.smartmall.myui.circleprogress.SeekCircle;
import cn.lanmei.com.smartmall.parse.ParserDevice;
import cn.lanmei.com.smartmall.socket.SocketAction;
import cn.lanmei.com.smartmall.socket.WebSocketDevService;
import smartmall.socket.IBackService;


/**
 *我的设备
 */
public class F_my_device extends BaseScrollFragment {
    private String TAG="F_my_device";

    private RelativeLayout layoutErr;
    private SeekCircle mSeekCircle;
    private ImageView deviceOnOff;
    private TextView txtDeviceName;
    private ImageView imgDeviceState;
    private TextView txtCurT;
    private TextView txtSettingT;
    private LinearLayout layoutTimer[];
    private ToggleButton tbTimeOpen[];
    private ToggleButton tbTimeStop[];
    private LinearLayout layoutTimeOpen[];
    private LinearLayout layoutTimeStop[];
    private TextView txtTimerTagOpen[];
    private TextView txtTimerTagStop[];

    private TextView txtTimerStart[];
    private TextView txtTimerEnd[];
    private Map<String,Integer> timerStartMap;
    private Map<String,Integer> timerEndMap;

    private DBDeviceErrManager dbDeviceErrManager;
    private boolean isInitUI;
    private boolean devSWITCH;
    private String devHexId;
    private String faultmsg;
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
                    iBackService.initServer(devHexId);
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
//                    L.MyLog(TAG,"在线");
                    isOnline=true;
                    if (isInitUI){
                        if (!deviceOnOff.isEnabled()){
                            imgDeviceState.setImageResource(R.drawable.xh_on);
                            deviceOnOff.setEnabled(true);
                            for(int i=0;i<layoutTimeOpen.length;i++){
                                layoutTimeOpen[i].setEnabled(true);
                                layoutTimeStop[i].setEnabled(true);
                                tbTimeOpen[i].setEnabled(true);
                                tbTimeStop[i].setEnabled(true);
                            }
                        }


                    }
                    break;
                case SocketAction.ACTION_offline://
//                    L.MyLog(TAG,"不在线");
                    isOnline=false;
                    if (isInitUI){
                        if (deviceOnOff.isEnabled()){
                            imgDeviceState.setImageResource(R.drawable.xh_off);
                            deviceOnOff.setEnabled(false);
                            for(int i=0;i<layoutTimeOpen.length;i++){
                                layoutTimeOpen[i].setEnabled(false);
                                layoutTimeStop[i].setEnabled(false);
                                tbTimeOpen[i].setEnabled(false);
                                tbTimeStop[i].setEnabled(false);
                            }
                        }


                    }

                    break;
                case SocketAction.LOGIN_ACTION://
                    L.MyLog(TAG,"登录");
                    devHexId = SharePreferenceUtil.getString(Common.DEV_bind_idhex, "");
                    if (!TextUtils.isEmpty(devHexId)){
                        try {
                            if (iBackService!=null){

                                iBackService.startSendHeartMessage(devHexId);
                            }

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case SocketAction.MESSAGE_ACTION://
                    String message = intent.getStringExtra("message");
                    if (isInitUI)
                        parserReciverMsg(message);
                    break;

            }
        };
    }
    private Intent mServiceIntent;
    private MessageBackReciver mReciver;
    private IntentFilter mIntentFilter;
    private LocalBroadcastManager mLocalBroadcastManager;

    public static F_my_device newInstance() {
        F_my_device fragment = new F_my_device();
        return fragment;
    }


    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;



    @Override
    public void init() {
        powerManager = (PowerManager)getActivity().getSystemService(getActivity().POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        isInitUI=false;
        dbDeviceErrManager=new DBDeviceErrManager(mContext);
        initSocket();
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
                    iBackService.startSendHeartMessage(devHexId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        L.MyLog(TAG,"onResume：isHidden："+isHidden());
        if (iBackService==null||isHidden())
            return;
        try {
            L.MyLog(TAG,"onResume:"+"---hasHeartBeat:"+iBackService.hasHeartBeat());
            if (!iBackService.hasHeartBeat())
                iBackService.startSendHeartMessage(devHexId);
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
        wakeLock.release();
        if (mServiceIntent!=null){
            getActivity().unbindService(conn);
            getActivity().stopService(mServiceIntent);
            mLocalBroadcastManager.unregisterReceiver(mReciver);
        }

    }

    private void initSocket(){

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        mReciver = new MessageBackReciver();

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
    public void loadViewLayout() {
        tag = getResources().getString(R.string.menu_device_1);
        setContentView(R.layout.layout_my_device);
        devHexId= SharePreferenceUtil.getString(Common.DEV_bind_idhex,"") ;

        timerStartMap=new HashMap<String, Integer>();
        timerEndMap=new HashMap<String, Integer>();
        setBg(R.mipmap.bg_device);
    }

    @Override
    public void findViewById() {
        layoutErr = (RelativeLayout) findViewById(R.id.layout_err);
        mSeekCircle = (SeekCircle)findViewById(R.id.seekCircle);
        mSeekCircle.setStart(false);
        deviceOnOff = (ImageView) findViewById(R.id.device_on_off);
        txtDeviceName = (TextView) findViewById(R.id.device_name);
        imgDeviceState = (ImageView) findViewById(R.id.device_state);
        txtCurT = (TextView) findViewById(R.id.device_current_t);
        txtSettingT = (TextView) findViewById(R.id.device_setting_t);
        int count=2;
        layoutTimer=new LinearLayout[count*2];
        tbTimeOpen=new ToggleButton[count];
        tbTimeStop=new ToggleButton[count];
        layoutTimeOpen=new LinearLayout[count];
        layoutTimeStop=new LinearLayout[count];
        txtTimerTagOpen=new TextView[count];
        txtTimerTagStop=new TextView[count];
        txtTimerStart=new TextView[count];
        txtTimerEnd=new TextView[count];
        layoutTimer[0]=(LinearLayout) findViewById(R.id.timer_1);
        layoutTimer[1]=(LinearLayout) findViewById(R.id.timer_2);
        layoutTimer[2]=(LinearLayout) findViewById(R.id.timer_3);
        layoutTimer[3]=(LinearLayout) findViewById(R.id.timer_4);
        int positionLayout=0;
        for(int i=0;i<count;i++){
            tbTimeOpen[i]=(ToggleButton)layoutTimer[positionLayout].findViewById(R.id.tb_timer);
            tbTimeStop[i]=(ToggleButton)layoutTimer[positionLayout+1].findViewById(R.id.tb_timer);

            layoutTimeOpen[i]=(LinearLayout) layoutTimer[positionLayout].findViewById(R.id.layout_timer);
            layoutTimeStop[i]=(LinearLayout)layoutTimer[positionLayout+1].findViewById(R.id.layout_timer);

            txtTimerTagOpen[i] =  (TextView)layoutTimeOpen[i].findViewById(R.id.txt_timer);
            txtTimerTagStop[i] =  (TextView)layoutTimeStop[i].findViewById(R.id.txt_timer);
            txtTimerStart[i] =  (TextView)layoutTimeOpen[i].findViewById(R.id.txt_timer_time);
            txtTimerEnd[i] =  (TextView)layoutTimeStop[i].findViewById(R.id.txt_timer_time);
            ((TextView)layoutTimeOpen[i].findViewById(R.id.txt_timer_stauts)).setText("开");
            ((TextView)layoutTimeStop[i].findViewById(R.id.txt_timer_stauts)).setText("关");
            positionLayout+=2;

            txtTimerTagOpen[i].setText(i+1+"");
            txtTimerTagStop[i].setText(i+1+"");
            final int position=i;

            layoutTimeOpen[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                            txtTimerStart[position].setText((hourOfDay<10?"0"+hourOfDay:hourOfDay)+":"+(minute<10?"0"+minute:minute));
                            timerStartMap.put(position+"hh",hourOfDay);
                            timerStartMap.put(position+"mm",minute);
                            String msg="timer"+(position+1)+":1."+(tbTimeOpen[position].isToggleOn()?1:0)
                                    +"."+hourOfDay+"."+minute;
                            sendMsg(msg);
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true, false);
                    timePickerDialog.setVibrate(true);
                    timePickerDialog.setCloseOnSingleTapMinute(false);
                    timePickerDialog.show(getActivity().getSupportFragmentManager(),"DF_select_time");
                    /*new DF_select_time(new DF_select_time.DiolagTimeListener() {
                        @Override
                        public void onDialogTimeDismiss(int hour, int minute) {
                            txtTimerStart[position].setText(hour+":"+minute);
                            timerStartMap.put(position+"hh",hour);
                            timerStartMap.put(position+"mm",minute);
                            String msg="timer"+(position+1)+":1."+(tbTimeOpen[position].isToggleOn()?1:0)
                                    +"."+hour+"."+minute;
                            sendMsg(msg);
                        }
                    }).show(getActivity().getSupportFragmentManager(),"DF_select_time");*/
                }
            });

            layoutTimeStop[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                            txtTimerEnd[position].setText((hourOfDay<10?"0"+hourOfDay:hourOfDay)+":"+(minute<10?"0"+minute:minute));
                            timerEndMap.put(position+"hh",hourOfDay);
                            timerEndMap.put(position+"mm",minute);
                            String msg="timer"+(position+1)+":0."+(tbTimeStop[position].isToggleOn()?1:0)
                                    +"."+hourOfDay+"."+minute;
                            sendMsg(msg);
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true, false);
                    timePickerDialog.setVibrate(true);
                    timePickerDialog.setCloseOnSingleTapMinute(false);
                    timePickerDialog.show(getActivity().getSupportFragmentManager(),"DF_select_time");
                   /* new DF_select_time(new DF_select_time.DiolagTimeListener() {
                        @Override
                        public void onDialogTimeDismiss(int hour, int minute) {
                            txtTimerEnd[position].setText(hour+":"+minute);
                            timerEndMap.put(position+"hh",hour);
                            timerEndMap.put(position+"mm",minute);
                            String msg="timer"+(position+1)+":0."+(tbTimeStop[position].isToggleOn()?1:0)
                                    +"."+hour+"."+minute;
                            sendMsg(msg);
                        }
                    }).show(getActivity().getSupportFragmentManager(),"DF_select_time");*/
                }
            });

            tbTimeOpen[position].setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    L.MyLog(TAG,"ToggleButton:tbTimeOpen："+position+on);
                    settingTimer(true,position);
                }
            });
            tbTimeStop[position].setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    L.MyLog(TAG,"ToggleButton:tbTimeStop："+position+on);
                    settingTimer(false,position);
                }
            });
            /*tbTimeOpen[position].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    settingTimer(true,position);
                }
            });
            tbTimeStop[position].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settingTimer(false,position);
                }
            });*/



        }


        setTextMsg("25℃\n当前温度",txtCurT);
        setTextMsg("0℃\n设置温度",txtSettingT);

        mSeekCircle.setOnSeekCircleChangeListener(new SeekCircle.OnSeekCircleChangeListener() {

            @Override
            public boolean onStopTrackingTouch(SeekCircle seekCircle,boolean updateProgress)
            {

                int setT=seekCircle.getProgress();
                if (setT<25){
                    setT=25;
                    seekCircle.setProgress(setT);
                    updateProgress=false;
                }
                L.MyLog("stop",setT+"---"+mSeekCircle.getProgress());
                setTextMsg(setT+"℃\n设置温度",txtSettingT);
                String msg="temp:"+setT;
                sendMsg(msg);
                return updateProgress;
            }

            @Override
            public void onStartTrackingTouch(SeekCircle seekCircle){

            }

            @Override
            public void onProgressChanged(SeekCircle seekCircle, int progress, boolean fromUser)
            {
//                updateText();
            }
        });
        deviceOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceId = SharePreferenceUtil.getString(Common.DEV_bind_idhex, "");
                if (TextUtils.isEmpty(deviceId)){
                    showPopDevices(v);
                }else{
                    String msg="switch:"+(mSeekCircle.isStart()?"0":"1");
                    sendMsg(msg);
                    return;
                }


            }
        });

        txtDeviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopDevices(v);
            }
        });

        layoutErr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutErr.setVisibility(View.GONE);
                ((BaseActivity)getActivity()).toDevErrDetail(devHexId,faultmsg);
            }
        });

        isInitUI=true;
        isFirstRequest=true;
    }

    /**计时器设置*/
    private void settingTimer(boolean timerType,int timerFlag){

        if (timerType){//计时器开
            if (timerStartMap.containsKey(timerFlag+"hh")){
                String msg="timer"+(timerFlag+1)+":1."+(tbTimeOpen[timerFlag].isToggleOn()?1:0)
                        +"."+timerStartMap.get(timerFlag+"hh")+"."+timerStartMap.get(timerFlag+"mm");
                sendMsg(msg);
            }
        }else{//计时器关
            if (timerEndMap.containsKey(timerFlag+"hh")){
                String msg="timer"+(timerFlag+1)+":0."+(tbTimeStop[timerFlag].isToggleOn()?1:0)
                        +"."+timerEndMap.get(timerFlag+"hh")+"."+timerEndMap.get(timerFlag+"mm");
                sendMsg(msg);
            }
        }


    }

    public void sendMsg(String msg){
        if (iBackService!=null){
            try {
                iBackService.sendMessage(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            L.MyLog(TAG,"iBackService==null");
        }
    }

    private void parserReciverMsg(String msg){
        /**
         *SWITCH:0,
         SETTEMP:0,
         TIME:00.00.00,
         RSSI:0,
         TIMER1OPEN:0.00.00,
         TIMER1CLOSE:0.00.00,
         TIMER2OPEN:0.00.00,
         TIMER2CLOSE:0.00.00,
         CURRENTTEMP:0,
         FAULTMSG:E00",
         * */
        String deviceMsg=null;

        if (!msg.startsWith("{"))
            return;
        try {
            deviceMsg=new JSONObject(msg).getString("MSG");

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        if (deviceMsg==null)
            return;
        if (deviceMsg.equals("logout"))
            return;
        if (deviceMsg.startsWith("upgrade"))
            return;
        if (!deviceMsg.startsWith("SWITCH"))
            return;

        String[] deviceInfoT = deviceMsg.split(",");
        Map<String,String> deviceInfo=new HashMap<>();
        String key="";
        String value="";
        for(int i=0;i<deviceInfoT.length;i++){
            key=deviceInfoT[i].substring(0,deviceInfoT[i].lastIndexOf(":"));
            value=deviceInfoT[i].substring(deviceInfoT[i].lastIndexOf(":") + 1);
            deviceInfo.put(key,value);
        }

        devSWITCH=deviceInfo.get("SWITCH").equals("1");
        if (devSWITCH!=mSeekCircle.isStart()){
            refreshDeviceImg(devSWITCH);
        }
        setTextMsg(deviceInfo.get("CURRENTTEMP")+"℃\n当前温度",txtCurT);
        setTextMsg(deviceInfo.get("SETTEMP")+"℃\n设置温度",txtSettingT);
        mSeekCircle.setProgress(Integer.parseInt(deviceInfo.get("CURRENTTEMP")));

        String[] timeStart_1=deviceInfo.get("TIMER1OPEN").split("\\.");
        txtTimerStart[0].setText(timeStart_1[1]+":"+timeStart_1[2]);
        timerStartMap.put("0hh",Integer.parseInt(timeStart_1[1]));
        timerStartMap.put("0mm",Integer.parseInt(timeStart_1[2]));

        if (timeStart_1[0].equals("1")){
            if (!tbTimeOpen[0].isToggleOn())
                tbTimeOpen[0].setToggleOn();
        }else {
            if (tbTimeOpen[0].isToggleOn())
                tbTimeOpen[0].setToggleOff();
        }

        String[] timeStart_2=deviceInfo.get("TIMER2OPEN").split("\\.");
        txtTimerStart[1].setText(timeStart_2[1]+":"+timeStart_2[2]);
        timerStartMap.put("1hh",Integer.parseInt(timeStart_2[1]));
        timerStartMap.put("1mm",Integer.parseInt(timeStart_2[2]));
        if (timeStart_2[0].equals("1")){
            if (!tbTimeOpen[1].isToggleOn())
                tbTimeOpen[1].setToggleOn();
        }else {
            if (tbTimeOpen[1].isToggleOn())
                tbTimeOpen[1].setToggleOff();
        }

        String[] timeEnd_1=deviceInfo.get("TIMER1CLOSE").split("\\.");
        txtTimerEnd[0].setText(timeEnd_1[1]+":"+timeEnd_1[2]);
        timerEndMap.put("0hh",Integer.parseInt(timeEnd_1[1]));
        timerEndMap.put("0mm",Integer.parseInt(timeEnd_1[2]));
        if (timeEnd_1[0].equals("1")){
            if (!tbTimeStop[0].isToggleOn())
                tbTimeStop[0].setToggleOn();
        }else {
            if (tbTimeStop[0].isToggleOn())
                tbTimeStop[0].setToggleOff();
        }

        String[] timeEnd_2=deviceInfo.get("TIMER2CLOSE").split("\\.");
        txtTimerEnd[1].setText(timeEnd_2[1]+":"+timeEnd_2[2]);
        timerEndMap.put("1hh",Integer.parseInt(timeEnd_2[1]));
        timerEndMap.put("1mm",Integer.parseInt(timeEnd_2[2]));
        if (timeEnd_2[0].equals("1")){
            if (!tbTimeStop[1].isToggleOn())
                tbTimeStop[1].setToggleOn();
        }else {
            if (tbTimeStop[1].isToggleOn())
                tbTimeStop[1].setToggleOff();
        }
        faultmsg= deviceInfo.get("FAULTMSG");
        if (faultmsg.equals("E00")||faultmsg.equals("E00|E00")){//正常
            layoutErr.setVisibility(View.INVISIBLE);
        }else {
            if (faultmsg.contains("|")){
                String[] faults = faultmsg.split("\\|");
//                L.MyLog(TAG,"设备故障：faults:"+faults[0]+"-----"+faults[1]);
                if (faults[0].equals("E00")){
                    faultmsg=faults[1];
                }
                if (faults[1].equals("E00")){
                    faultmsg=faults[0];
                }
            }

            M_Dev_Err dererr = dbDeviceErrManager.getErr(devHexId, faultmsg);
            long t=0;
            if (dererr!=null)
                t=(System.currentTimeMillis()-dererr.getErrDoneTime())/1000;
            L.MyLog(TAG,"设备故障："+t+"----"+faultmsg);
            if (dererr==null){
                dererr = new M_Dev_Err();
                dererr.setErrCode(faultmsg);
                dererr.setErrTime(System.currentTimeMillis());
                dererr.setErrDoneTime(0);
                dererr.setErrDevIdHex(devHexId);
                dbDeviceErrManager.addErr(dererr);
                layoutErr.setVisibility(View.VISIBLE);
            }else if (t>30*30){
                layoutErr.setVisibility(View.VISIBLE);
            }else {
                layoutErr.setVisibility(View.INVISIBLE);
            }

        }

    }

    private boolean isFirstRequest;
    private List<Dev> mDevices;

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
//                String deviceId = SharePreferenceUtil.getString(Common.DEV_bind_idhex, "");
                if (!TextUtils.isEmpty(devHexId)){
                    for(int i=0;i<mDevices.size();i++){
                        if (mDevices.get(i).getIdHex().equals(devHexId)) {
                            txtDeviceName.setText(mDevices.get(i).getNickName());
                            tag=mDevices.get(i).getNickName()+"";
                            mOnFragmentInteractionListener.setTitle(tag);
                            break;
                        }

                    }
                }
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
                stopProgressDialog();
            }
        });
    }

    public void showPopDevices(View v){

        if (mDevices==null||mDevices.size()<1){
            StaticMethod.showInfo(mContext,"没有获取到绑定的设备，请刷新或绑定");
            return;
        }
        List<String> lists=new ArrayList<String>();
        for(int i=0;i<mDevices.size();i++){
            lists.add(mDevices.get(i).getNickName());
        }

        new PopWindow_List<AdapterString>(mContext,
                new AdapterString(mContext, lists),
                new PopWindow_List.PopWindowItemClick() {
                    @Override
                    public void onPopWindowItemClick(int position) {
                            try {
                                devHexId=mDevices.get(position).getIdHex();
                                SharePreferenceUtil.putString(Common.DEV_bind_idhex,devHexId) ;
                                iBackService.startSendHeartMessage(devHexId);
                                tag=mDevices.get(position).getNickName()+"";
                                mOnFragmentInteractionListener.setTitle(tag);
                                txtDeviceName.setText(mDevices.get(position).getNickName()+"");
                                String msg="switch:1";
                                sendMsg(msg);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        String reSet="{\"TYPE\":2,\"ID\":\"" +devHexId+"\",\"TO\":\"5\",\"UARTREFLAG\":1," +
                                "\"MSG\":\"SWITCH:0,SETTEMP:0,TIME:16.38.37,RSSI:10,TIMER1OPEN:0.00.00,TIMER1CLOSE:0.00.00,TIMER2OPEN:0.00.00,TIMER2CLOSE:0.00.00,CURRENTTEMP:25,FAULTMSG:E00|E00\"," +
                                "\"ALL\":\"81 00 a0 ec ee a0 a0 00 00 00 00 00 00 00 56 0e|81 01 21 4c 30 46 45 ff ff ff ff ff ff 2a 2f 60|81 02 2c 2a 20 20 36 46 23 48 20 21 20 21 6f 6b|81 03 00 38 01 37 01 75 43 2a 21 20 20 38 38 0d\"}";
                        parserReciverMsg(reSet);


                    }
                },
        StaticMethod.dip2px(mContext,160)).showPopupWindow(v);
    }
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        isFirstRequest=false;
        requestServerData();
    }


    private void setTextMsg(String msg,TextView txt){
        SpannableString ss=new SpannableString(msg);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE),0,msg.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(StaticMethod.dip2px(mContext,20));
        ss.setSpan(span,0,msg.indexOf("\n"),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span = new AbsoluteSizeSpan(StaticMethod.dip2px(mContext,14));
        ss.setSpan(span,msg.indexOf("\n")+1,msg.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new ForegroundColorSpan(Color.WHITE),msg.indexOf(":")+1,msg.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        ss.setSpan(new UnderlineSpan(),msg.indexOf(":")+1,msg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt.setText(ss);
    }

    private void refreshDeviceImg(boolean isStart){
        mSeekCircle.setStart(isStart);
        deviceOnOff.setTag(mSeekCircle.isStart());
        if (mSeekCircle.isStart())
            deviceOnOff.setImageResource(R.mipmap.icon_device_start);
        else
            deviceOnOff.setImageResource(R.mipmap.icon_device_stop);
    }


    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }
}

package cn.lanmei.com.smartmall.socket;


import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.common.app.SendMsgLinkedMap;
import com.common.app.degexce.L;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;
import smartmall.socket.IBackService;


public class WebSocketRepairService extends Service {
    private static final String TAG = WebSocketRepairService.class.getSimpleName();
    private static final long HEART_BEAT_RATE = 3 * 1000;

//    public static final int PORT = 8001;
    public static final String WSuri  = "ws://wlyg.itlanmei.cn:8088";// "192.168.1.21";//

    private WeakReference<WebSocket> mConnection;

    private LocalBroadcastManager mLocalBroadcastManager;

    private SendMsgLinkedMap sendMsgMap;

    // For heart Beat
    private boolean hasHeartBeat;
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {

        @Override
        public void run() {
            if (System.currentTimeMillis() - timeReciver >= HEART_BEAT_RATE*3){
                Intent intent=new Intent(SocketAction.ACTION_offline);
                mLocalBroadcastManager.sendBroadcast(intent);
            }else {
                Intent intent=new Intent(SocketAction.ACTION_online);
                mLocalBroadcastManager.sendBroadcast(intent);
            }
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                boolean isSuccess = sendMsg(sendMsgMap.getHeartMap());//就发送一个\r\n过去 如果发送失败，就重新初始化一个socket
                if (!isSuccess) {
                    mHandler.removeCallbacks(heartBeatRunnable);
                    setHasHeartBeat(false);
                    releaseLastSocket(mConnection);
                    new InitSocketThread().start();
                }
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
            setHasHeartBeat(true);
        }
    };



    private void setHasHeartBeat(boolean hasHeartBeat) {
        this.hasHeartBeat = hasHeartBeat;
    }

    private long sendTime = 0L;
    private long timeReciver = 0L;
    private long timeLastReciver = 0L;

    private IBackService.Stub iBackService = new IBackService.Stub() {


        @Override
        public void initServer(String deviceIdHex) throws RemoteException {
            initServerCreate(deviceIdHex);
        }

        @Override
        public boolean sendMessage(String message) throws RemoteException {
            return sendMsg(message);
        }

        @Override
        public boolean startSendHeartMessage(String deviceIdHex) throws RemoteException {

            if (hasHeartBeat){
                mHandler.removeCallbacks(heartBeatRunnable);
                setHasHeartBeat(false);
            }
            sendMsgMap.refreshDeviceId(deviceIdHex);
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
            setHasHeartBeat(true);
            return false;
        }

        @Override
        public boolean removeSendHeartMessage() throws RemoteException {
            mHandler.removeCallbacks(heartBeatRunnable);
            setHasHeartBeat(false);
            return false;
        }

        @Override
        public boolean hasHeartBeat() throws RemoteException {
            return hasHeartBeat;
        }


    };


    @Override
    public IBinder onBind(Intent arg0) {
        return iBackService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.MyLog(TAG,"onCreate");
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(this);
    }

    private void initServerCreate(String devNo){
        sendMsgMap=new SendMsgLinkedMap(devNo);
        new InitSocketThread().start();
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        L.MyLog(TAG,"unbindService");
        mHandler.removeCallbacks(heartBeatRunnable);
        setHasHeartBeat(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.MyLog(TAG,"onDestroy");
        mHandler.removeCallbacks(heartBeatRunnable);
        setHasHeartBeat(false);
        releaseLastSocket(mConnection);
    }


    public boolean sendMsg(LinkedHashMap<String,Object> linkedHashMap) {
        if (null == mConnection || null == mConnection.get()) {
            return false;
        }
        WebSocket soc = mConnection.get();
        if (soc.isConnected()) {
            String message = sendMsgMap.toString(linkedHashMap);
            L.MyLog(TAG,"sendMsg:"+message);
            soc.sendTextMessage(message);
            sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
        } else {
            return false;
        }
        return true;
    }

    public boolean sendMsg(String msg) {
        if (null == mConnection || null == mConnection.get()) {
            return false;
        }
        WebSocket soc = mConnection.get();
        if (soc.isConnected()) {
            String message = sendMsgMap.toString(sendMsgMap.getSendMap(msg));
            L.MyLog(TAG,"sendMsg:"+message);
            soc.sendTextMessage(message);
            sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
        } else {
            return false;
        }
        return true;
    }
    boolean connect;
    private boolean initSocket() {//初始化Socket
        connect=false;
        try {
            final WebSocket so = new WebSocketConnection();
            so.connect(WSuri, new WebSocketConnectionHandler() {

                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " );
                    connect=true;
                    mConnection = new WeakReference<WebSocket>(so);
                    Intent intent=new Intent(SocketAction.ACTION_online);
                    mLocalBroadcastManager.sendBroadcast(intent);
                    sendMsg(sendMsgMap.getLoginMap());
                }

                @Override
                public void onTextMessage(String payload) {
                    timeReciver=System.currentTimeMillis();
                    L.MyLog(TAG,":"+payload);
                    String login="hi!\n";
                    if(payload.equals(login)){//登录
                        Intent intent=new Intent(SocketAction.LOGIN_ACTION);
                        mLocalBroadcastManager.sendBroadcast(intent);
                    }else{//处理回复
                        Intent intent=new Intent(SocketAction.MESSAGE_ACTION);
                        intent.putExtra("message", payload);
                        mLocalBroadcastManager.sendBroadcast(intent);
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "Connection lost.");
                    releaseLastSocket(mConnection);
                    connect=false;
                }
            } );



//            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
        }catch (WebSocketException e) {
            e.printStackTrace();
            releaseLastSocket(mConnection);
            connect=false;
        }
        return connect;
    }

    private void releaseLastSocket(WeakReference<WebSocket> mSocket) {
        if (null != mSocket) {
            WebSocket sk = mSocket.get();
            if (sk!=null&&sk.isConnected()) {
                sk.disconnect();
            }
            sk = null;
            mSocket = null;
            Intent intent=new Intent(SocketAction.ACTION_offline);
            mLocalBroadcastManager.sendBroadcast(intent);
        }
    }

    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            initSocket();
           /* boolean con = false;
            while (!con){
                con = initSocket();
                if (!con){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }*/

        }
    }


}

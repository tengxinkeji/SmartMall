package cn.lanmei.com.smartmall.socket;


import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;

import com.common.app.SendMsgLinkedMap;
import com.common.app.degexce.L;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedHashMap;

import smartmall.socket.IBackService;


public class BackService extends Service {
    private static final String TAG = "BackService";
    private static final long HEART_BEAT_RATE = 3 * 1000;

    public static final String HOST = "wlyg.itlanmei.cn";// "192.168.1.21";//
    public static final int PORT = 8001;

    private ReadThread mReadThread;

    private LocalBroadcastManager mLocalBroadcastManager;

    private WeakReference<Socket> mSocket;

    private SendMsgLinkedMap sendMsgMap;

    public void setHasHeartBeat(boolean hasHeartBeat) {
        this.hasHeartBeat = hasHeartBeat;
    }

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
                    mReadThread.release();
                    releaseLastSocket(mSocket);
                    new InitSocketThread().start();
                }
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
            setHasHeartBeat(true);
        }
    };

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
            mHandler.removeCallbacks(heartBeatRunnable);
            setHasHeartBeat(false);
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

    private void initServerCreate(String deviceIdHex) {
        sendMsgMap=new SendMsgLinkedMap(deviceIdHex);
        new InitSocketThread().start();
    }

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

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        L.MyLog(TAG,"unbindService");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.MyLog(TAG,"onDestroy");
        mHandler.removeCallbacks(heartBeatRunnable);
        setHasHeartBeat(false);
        mReadThread.release();
        releaseLastSocket(mSocket);
    }

    public boolean sendMsg(LinkedHashMap<String,Object> linkedHashMap) {
        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        Socket soc = mSocket.get();
        try {
            if (!soc.isClosed() && !soc.isOutputShutdown()) {
                OutputStream os = soc.getOutputStream();
                String message = sendMsgMap.toString(linkedHashMap) + "\r\n";
                L.MyLog(TAG,"sendMsg:"+message);
                os.write(message.getBytes());
                os.flush();
                sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间

            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean sendMsg(String msg) {
        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        Socket soc = mSocket.get();
        try {
            if (!soc.isClosed() && !soc.isOutputShutdown()) {
                OutputStream os = soc.getOutputStream();
                String message = sendMsgMap.toString(sendMsgMap.getSendMap(msg)) + "\r\n";
                L.MyLog(TAG,"sendMsg:"+message);
                os.write(message.getBytes());
                os.flush();
                sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间

            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void initSocket() {//初始化Socket
        try {
            Socket so = new Socket(HOST, PORT);
            mSocket = new WeakReference<Socket>(so);
            mReadThread = new ReadThread(so);
            mReadThread.start();
            Intent intent=new Intent(SocketAction.ACTION_online);
            mLocalBroadcastManager.sendBroadcast(intent);
            sendMsg(sendMsgMap.getLoginMap());
//            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Intent intent=new Intent(SocketAction.ACTION_offline);
            mLocalBroadcastManager.sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
            Intent intent=new Intent(SocketAction.ACTION_offline);
            mLocalBroadcastManager.sendBroadcast(intent);
        }
    }

    private void releaseLastSocket(WeakReference<Socket> mSocket) {
        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (sk!=null&&!sk.isClosed()) {
                    sk.close();
                }
                sk = null;
                mSocket = null;
                Intent intent=new Intent(SocketAction.ACTION_offline);
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            initSocket();
        }
    }

    // Thread to read content from Socket
    class ReadThread extends Thread {
        private WeakReference<Socket> mWeakSocket;
        private boolean isStart = true;

        public ReadThread(Socket socket) {
            mWeakSocket = new WeakReference<Socket>(socket);
        }

        public void release() {
            isStart = false;
            releaseLastSocket(mWeakSocket);
        }

        @Override
        public void run() {
            super.run();
            Socket socket = mWeakSocket.get();
            if (null != socket) {
                try {
                    InputStream is = socket.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int length = 0;
                    while (!socket.isClosed() && !socket.isInputShutdown()
                            && isStart && ((length = is.read(buffer)) != -1)) {
                        if (length > 0) {
                            timeReciver=System.currentTimeMillis();
                            String message = new String(Arrays.copyOf(buffer,
                                    length)).trim();
                            L.MyLog(TAG,"socket接收："+message);
                            //收到服务器过来的消息，就通过Broadcast发送出去
                            if(message.equals("hi!")){//登录
                                Intent intent=new Intent(SocketAction.LOGIN_ACTION);
                                mLocalBroadcastManager.sendBroadcast(intent);
                            }else{//处理回复
                                Intent intent=new Intent(SocketAction.MESSAGE_ACTION);
                                intent.putExtra("message", message);
                                mLocalBroadcastManager.sendBroadcast(intent);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

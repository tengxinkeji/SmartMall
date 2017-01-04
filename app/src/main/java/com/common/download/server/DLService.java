package com.common.download.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.mydownloader.cn.interfaces.IDListener;
import com.mydownloader.cn.tools.FileDownloader;


import java.io.File;
import java.util.List;


/**
 * 执行下载的Service
 *
 * @author AigeStudio 2015-05-18
 */
public class DLService extends Service {
    private static final String TAG="DLService";

    private Context mContext;

   

    private String  downloadUrl;
    private  int downId;
    FileDownloader loader;

//    //自定义通知栏类
//    MyNotification myNotification;
//    //通知栏跳转Intent
//    private Intent updateIntent = null;
//    private PendingIntent updatePendingIntent = null;

    /**
     * 由于用户的输入事件(点击button, 触摸屏幕....)是由主线程负责处理的，如果主线程处于工作状态，
     * 此时用户产生的输入事件如果没能在5秒内得到处理，系统就会报“应用无响应”错误。
     * 所以在主线程里不能执行一件比较耗时的工作，否则会因主线程阻塞而无法处理用户的输入事件，
     * 导致“应用无响应”错误的出现。耗时的工作应该在子线程里执行。
     */


    private final IBinder mBinder = new DownloadBinder();

    public synchronized   void stopDownload() {

            if (curDLinfo!=null){
                curDLinfo.setStop(true);
                loader.setStopDownload(true);
            }

    }

    public synchronized void setDownIDListener(IDListener idListener) {
        if (curDLinfo!=null)
            curDLinfo.setMyIdIDListener(idListener);
    }


    public class DownloadBinder extends Binder {
        /**
         * 获取当前Service的实例
         * @return
         */
        public DLService getService(){
            return DLService.this;
        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub

        //      IBinder myIBinder = null;
//      if ( null == myIBinder )
//          myIBinder = new LocalBinder() ;
//      return myIBinder;
        return mBinder;     //也可以像上面几个语句那样重新new一个IBinder
        //如果这边不返回一个IBinder的接口实例，那么ServiceConnection中的onServiceConnected就不会被调用
        //那么bind所具有的传递数据的功能也就体现不出来~\(≧▽≦)/
        // ~啦啦啦（这个返回值是被作为onServiceConnected中的第二个参数的）

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;

//        updateIntent = new Intent(mContext, MainFragmentActivity.class);
//        PendingIntent   updatePendingIntent = PendingIntent.getActivity(mContext,0,updateIntent,0);
//        myNotification=new MyNotification(mContext, updatePendingIntent, 1);
////		myNotification.showDefaultNotification(R.drawable.ic_launcher, "测试", "开始下载");
//        myNotification.showCustomizeNotification(R.mipmap.logo,
//                getResources().getString(R.string.app_name), R.layout.layout_notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }



    public DLinfo curDLinfo;


    /**
     * @param downStart  是否立即开始下载
     * */
    public synchronized void download(DLinfo dLinfo,boolean downStart){

        dLinfo.setStop(false);
        curDLinfo=dLinfo;
        File saveDir =new File(dLinfo.getDir());
        if (!saveDir.exists())
            saveDir.mkdirs();
        if (loader!=null&&!loader.isStopFinish){
            loader.setStopDownload(true);
        }
        download();



    }

    public synchronized boolean isDownloading(){
        return loader!=null&&!loader.isStopFinish;
    }


    /**
     * 主线程(UI线程)
     * 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
     *
     */

    private void download() {
        downId=curDLinfo.getDownId();
        curDLinfo.setStop(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loader=new FileDownloader(mContext);
//                        loader.init(curDLinfo.getDownLoadUrl(),new File(curDLinfo.getDir()), 3,dlmIDListener);
                        loader.init(curDLinfo.getDownLoadUrl(),new File(curDLinfo.getDir()), 3,new MyDownloadListener(mContext,curDLinfo));
                        loader.download();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    public int getDownId() {

        return downId;
    }
}

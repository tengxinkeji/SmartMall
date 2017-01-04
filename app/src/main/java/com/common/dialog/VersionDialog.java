package com.common.dialog;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.app.StaticMethod;
import com.common.app.VersionModel;
import com.common.app.degexce.CustomExce;
import com.common.app.degexce.L;
import com.common.app.sd.Enum_Dir;
import com.common.app.sd.SDCardUtils;
import com.common.download.server.DLService;
import com.common.download.server.DLinfo;
import com.common.net.NetWorkUtil;
import com.mydownloader.cn.interfaces.IDListener;
import java.io.File;

import cn.lanmei.com.smartmall.R;


/**
 * 版本升级
 * Created by Administrator on 2015/10/16.
 */

public class VersionDialog extends DialogFragment {
    private View view;
    private TextView titleTxt;
    private TextView infoTxt;
    private ProgressBar downProgress;
    private TextView txtPercentage;
    private TextView ignore;
    private TextView ok;
    private TextView cancle;

    private Context mContext;
    private String id;
    private VersionModel versionModel;


    private boolean isIgnore;
    private Drawable yes;
    private Drawable no;
    private File dir;
    private int downlaodCase;
    private int apkId=-1;



    /**类似于MediaPlayer mPlayer = new
     *MediaPlayer();只不过这边的服务是自定义的，不是系统提供好了的 */
    public DLService myService;
    public ServiceConnection sc;

    public void refreshDialog(VersionModel versionModel, boolean isInstall,
                              CancleDismissListener cancleDismissListener){
        this.versionModel=versionModel;
       if (isInstall)
           downlaodCase=5;
        this.cancleDismissListener = cancleDismissListener;
        id=versionModel.getApkDownId();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        initDownService();
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);


        try {
            dir = SDCardUtils.getDirFile(Enum_Dir.DIR_apk);
        } catch (CustomExce customExce) {
            customExce.printStackTrace();
            dismiss();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.layout_dialog_version,container, false);
        initUi();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setLayout(StaticMethod.wmWidthHeight(getActivity())[0]-50,
                getDialog().getWindow().getAttributes().height);

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
       mContext.unbindService(sc);
    }

    private void initUi(){
        titleTxt=(TextView) view.findViewById(R.id.setting_version_title);
        infoTxt=(TextView) view.findViewById(R.id.setting_version_info);
        downProgress=(ProgressBar) view.findViewById(R.id.down_progress);
        txtPercentage=(TextView) view.findViewById(R.id.percentage);
        ignore=(TextView) view.findViewById(R.id.setting_version_ignore);
        cancle=(TextView) view.findViewById(R.id.version_dialog_cancle);
        ok=(TextView) view.findViewById(R.id.version_dialog_ok);

        infoTxt=(TextView) view.findViewById(R.id.setting_version_info);

        if (NetWorkUtil.isWifi()){
            Drawable drawable= ContextCompat.getDrawable(mContext,R.mipmap.wifi_yes);
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            titleTxt.setCompoundDrawables(drawable,null,null,null);
        }
        yes= ContextCompat.getDrawable(mContext,R.mipmap.check_yes);
        yes.setBounds(0,0,yes.getIntrinsicWidth(),yes.getIntrinsicHeight());
        no= ContextCompat.getDrawable(mContext,R.mipmap.check_no);
        no.setBounds(0, 0, no.getIntrinsicWidth(), no.getIntrinsicHeight());

        if (versionModel.isIgnore(versionModel.getNetVersion())){
            ignore.setCompoundDrawables(yes,null,null,null);
        }else{
            ignore.setCompoundDrawables(no,null,null,null);
        }


        infoTxt.setText(versionModel.getNetDesc());
        if (downlaodCase==5){//安装
            ok.setText(mContext.getResources().getString(R.string.install));
            downProgress.setProgress(100);
            txtPercentage.setText("100%");
        }


        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isIgnore = !isIgnore;
                if (isIgnore) {
                    ignore.setCompoundDrawables(yes, null, null, null);
                    versionModel.setIsIgnore(true,versionModel.getNetVersion());
                } else {
                    ignore.setCompoundDrawables(no, null, null, null);
                    versionModel.setIsIgnore(false,versionModel.getNetVersion());
                }

            }
        });


        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mainFragmentActivity.sc!=null)
//                    mContext.unbindService(mainFragmentActivity.sc);
                if (cancleDismissListener!=null)
                    cancleDismissListener.onCancleDismissListener(true);
                getDialog().dismiss();

            }
        });



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=versionModel.getApkFile();
                DLinfo dLinfo=new DLinfo();
                dLinfo.setDownId(apkId);
                dLinfo.setName("wlyg.apk");
                dLinfo.setStop(false);
                dLinfo.setDir(dir.getAbsolutePath());
                dLinfo.setDownLoadUrl(versionModel.getNetUrl());
                dLinfo.setMyIdIDListener(idListener);
                    switch (downlaodCase){
                        case 0://下载
                            myService.download(dLinfo,true);
                            break;
                        case 1://下载准备

                        case 2://下载开始

                        case 3://下载进度
                            ok.setText(mContext.getResources().getString(R.string.start));
                            myService.stopDownload();
                            break;
                        case 4://下载停止
                            ok.setText(mContext.getResources().getString(R.string.pause));
                            myService.download(dLinfo,true);
                            break;
                        case 5://下载完成
                            StaticMethod.installApK(mContext,file);
                            getDialog().dismiss();
                            break;
                        case 6://下载失败
                            ok.setText(mContext.getResources().getString(R.string.pause));
                            myService.download(dLinfo,true);
                            break;
                    }

            }
        });

    }

    private IDListener idListener=new IDListener() {


        @Override
        public void onPrepare() {
            downlaodHandler.sendEmptyMessage(1);

        }

        @Override
        public void onStart(String fileName, String realUrl, int fileLength) {
            Message msg=new Message();
            msg.what=2;
            msg.arg1=fileLength;
            downlaodHandler.sendMessage(msg);

        }

        @Override
        public void onProgress(int progress, int speed, int percentage) {
            Message msg=new Message();
            msg.what=3;
            msg.arg1=progress;
            msg.arg2=speed;
            msg.obj=percentage;
            downlaodHandler.sendMessage(msg);
        }

        @Override
        public void onStop(int progress) {
            downlaodHandler.sendEmptyMessage(4);

        }

        @Override
        public void onFinish(File file) {
            Message msg=new Message();
            msg.what=5;
            msg.obj=file;
            downlaodHandler.sendMessage(msg);

        }

        @Override
        public void onError(int status, String error) {
            downlaodHandler.sendEmptyMessage(6);
        }
    };

    private Handler downlaodHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            downlaodCase=msg.what;
            Bundle data;
            switch (msg.what){
                case 1://下载准备
                    ok.setText("准备中");
                    break;
                case 2://下载开始
                    downProgress.setProgress(0);
                    downProgress.setMax(msg.arg1);
                    ok.setText(mContext.getResources().getString(R.string.pause));
                    break;
                case 3://下载进度
//                    msg.arg1=progress;
//                    msg.arg2=speed;
//                    msg.obj=percentage;
                    downProgress.setProgress(msg.arg1);
                    txtPercentage.setText(msg.obj+"%");
                    break;
                case 4://下载停止
                    ok.setText(mContext.getResources().getString(R.string.start));
                    break;
                case 5://下载完成
                    File file=(File) msg.obj;
                    if (file==null||!file.exists()){
                        Message msg1=new Message();
                        msg1.what=6;
                        downlaodHandler.sendMessage(msg);
                    }else{
                        ok.setText(mContext.getResources().getString(R.string.install));
                        StaticMethod.installApK(mContext,file);
                        getDialog().dismiss();
                    }

                    break;
                case 6://下载失败
                    ok.setText("下载失败，点击重新下载");

                    break;
            }

        }
    };

    private CancleDismissListener cancleDismissListener;
    public interface CancleDismissListener{
        public void onCancleDismissListener(boolean isDismiss);
    }



    private void initDownService(){

        sc=new ServiceConnection() {
            /*
             * 只有在MyService中的onBind方法中返回一个IBinder实例才会在Bind的时候
             * 调用onServiceConnection回调方法
             * 第二个参数service就是MyService中onBind方法return的那个IBinder实例，可以利用这个来传递数据
             */
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // TODO Auto-generated method stub
                myService = ((DLService.DownloadBinder) service).getService();
//                String recStr = ((DownloadService.DownloadBinder) service).stringToSend;
                //利用IBinder对象传递过来的字符串数据（其他数据也可以啦，哪怕是一个对象也OK~~）
//                Log.i("TAG","The String is : " + recStr);
                L.MyLog("", "Bind DownloadService");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                /* SDK上是这么说的：
                 * This is called when the connection with the service has been unexpectedly disconnected
                 * that is, its process crashed. Because it is running in our same process, we should never see this happen.
                 * 所以说，只有在service因异常而断开连接的时候，这个方法才会用到*/
                // TODO Auto-generated method stub
                sc = null;
                L.MyLog("", "onServiceDisconnected : ServiceConnection --->"
                        + sc);
            }
        };

        Intent downApk=new Intent(mContext,DLService.class);
        mContext.startService(downApk);

//     Intent intent = new Intent();
//     intent.setAction("tools.download.download.DownloadService");
        mContext.bindService(downApk, sc, Context.BIND_AUTO_CREATE);//bind多次也只会调用一次onBind方法



    }


}


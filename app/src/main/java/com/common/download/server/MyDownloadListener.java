package com.common.download.server;

import android.content.ContentValues;
import android.content.Context;

import com.common.app.degexce.L;
import com.mydownloader.cn.interfaces.IDListener;


import java.io.File;

/**
 * Created by Administrator on 2016/7/4.
 */
public class MyDownloadListener implements IDListener {
    private String TAG="MyDownloadListener";
    private DLinfo dLinfo;



    public MyDownloadListener(Context mContext, DLinfo dLinfo) {
        this.dLinfo = dLinfo;


    }

    @Override
    public void onPrepare() {
        L.MyLog(TAG, "--onPrepare");
        if (dLinfo.getMyIdIDListener() != null)
            dLinfo.getMyIdIDListener().onPrepare();
    }

    @Override
    public void onStart(String fileName, String realUrl, int fileLength) {
        dLinfo.setName(fileName);
        dLinfo.setFileLenght(fileLength);

        L.MyLog(TAG + "--onStart:", "fileName:" + fileName + "---fileLength:" + fileLength);



        if (dLinfo.getMyIdIDListener() != null)
            dLinfo.getMyIdIDListener().onStart(fileName, realUrl, fileLength);
    }

    @Override
    public void onProgress(int progress, int speed, int percentage) {
//            BugLog.MyLog(TAG+"--onProgress:","progress:"+progress+"---speed:"+speed+"---percentage:"+percentage);

        if (dLinfo.getMyIdIDListener() != null)
            dLinfo.getMyIdIDListener().onProgress(progress, speed, percentage);
    }

    @Override
    public void onStop(int progress) {
        L.MyLog(TAG, "--onStop");


        if (dLinfo.getMyIdIDListener() != null)
            dLinfo.getMyIdIDListener().onStop(progress);

    }

    @Override
    public void onFinish(File file) {
        L.MyLog(TAG, "--onFinish");

        if (dLinfo.getMyIdIDListener() != null)
            dLinfo.getMyIdIDListener().onFinish(file);

    }

    @Override
    public void onError(int status, String error) {
        L.MyLog(TAG, "--onError：" + error);


        if (dLinfo.getMyIdIDListener() != null)
            dLinfo.getMyIdIDListener().onError(status, error);

    }
}

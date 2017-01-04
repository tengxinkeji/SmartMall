package com.common.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.common.app.degexce.CustomExce;
import com.common.app.degexce.L;
import com.common.app.sd.Enum_Dir;
import com.common.app.sd.SDCardUtils;

import java.io.File;


public class AppInstallReceiver extends BroadcastReceiver {
    public static final String TAG="AppInstallReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        L.MyLog("广播：", "getAction:" + intent.getAction().toString());
        String packageName,pkName;
        packageName = intent.getData().getSchemeSpecificPart();
        pkName = context.getPackageName();
        switch (intent.getAction()){

            case Intent.ACTION_PACKAGE_ADDED://安装成功
                L.MyLog(pkName+"广播：", "安装成功" + packageName);
                break;
            case Intent.ACTION_PACKAGE_REMOVED://卸载成功
                L.MyLog(pkName+"广播：", "卸载成功" + packageName);
                break;
            case Intent.ACTION_PACKAGE_REPLACED://替换成功
                L.MyLog(pkName+"广播：", "替换成功" + packageName);
                if (!pkName.equals(packageName))
                    return;
                File file= null;
                try {
                    file = SDCardUtils.getDirFile(Enum_Dir.DIR_apk);
                } catch (CustomExce customExce) {
                    customExce.printStackTrace();
                }
                if (file!=null)
                    SDCardUtils.deleteDirFile(file);
                break;
        }

        

    }



}
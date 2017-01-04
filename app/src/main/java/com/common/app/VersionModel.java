package com.common.app;

import android.text.TextUtils;

import com.common.app.degexce.L;

import java.io.File;
import java.util.Calendar;


/**
 * Created by Administrator on 2015/11/18.
 */
public class VersionModel {
    private String KEY_netVersion="version_netVersion";
    private String KEY_netUrl="version_netUrl";
    private String KEY_netDesc="version_netDesc";
    private String KEY_request_time="version_request_time";
    private String KEY_isNoUpdate="version_isNoUpdate";
    private String KEY_NoUpdateVersion="NoUpdateVersion";
    private String KEY_fileapkpath="version_fileapk_path";
    private String KEY_MobileNet="version_MobileNet";

    public String getApkDownId() {
        return "0";
    }
    public int getNetVersion() {
        return SharePreferenceUtil.getInt(KEY_netVersion,0);
    }

    public void setNetVersion(int netVersion) {
        SharePreferenceUtil.putInt(KEY_netVersion,netVersion);
    }

    public String getNetUrl() {
        return SharePreferenceUtil.getString(KEY_netUrl,null);
    }

    public void setNetUrl(String netUrl) {
        SharePreferenceUtil.putString(KEY_netUrl,netUrl);
    }

    public String getNetDesc() {
        return SharePreferenceUtil.getString(KEY_netDesc,null);
    }

    public void setNetDesc(String netDesc) {
        SharePreferenceUtil.putString(KEY_netDesc,netDesc);
    }

    public File getApkFile() {
        String path=SharePreferenceUtil.getString(KEY_fileapkpath,null);
        if (!TextUtils.isEmpty(path)){
            File file=new File(path);
            if (file.exists()&&file.isFile())
                return file;
        }
        return null;
    }

    public void setApkPath(String apkPath) {
        SharePreferenceUtil.putString(KEY_fileapkpath,apkPath);
    }

    public int getCurrentVersion() {
        return StaticMethod.getVersion(MyApplication.getInstance().getApplicationContext());
    }

    public int getCurrentDay() {
        Calendar mCalendar= Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        int day=mCalendar.get(Calendar.DAY_OF_YEAR);
        return day;
    }

    public int getRequestTime() {
        return SharePreferenceUtil.getInt(KEY_request_time,0);
    }

    public void setRequestTime() {
        SharePreferenceUtil.putInt(KEY_request_time,getCurrentDay());
    }

    public void setIsIgnore(boolean isIgnore,int versionCode) {
        SharePreferenceUtil.putBoolean(KEY_isNoUpdate,isIgnore);
        SharePreferenceUtil.putInt(KEY_NoUpdateVersion,versionCode);
    }
    public boolean isIgnore(int versionCode) {
        if (versionCode!=0&&
                versionCode==SharePreferenceUtil.getInt(KEY_NoUpdateVersion,0)){
            return SharePreferenceUtil.getBoolean(KEY_isNoUpdate,false);
        }
        return false;

    }

    public boolean isMobileNetDownload(){
        return  SharePreferenceUtil.getBoolean(KEY_MobileNet,false);
    }

    public void setMobileNetDownload(boolean isDownload){
        SharePreferenceUtil.putBoolean(KEY_MobileNet,isDownload);
    }

    /**
     * @return 是否请求
     * */
    public boolean isUpdateRequest() {
        if (getRequestTime()==getCurrentDay()){
           return false;
        }
        return true;
    }

    /**
     * @param updateVersionCode 要更新的版本号
     * */
    public boolean isUpdateApk(int updateVersionCode) {
        if (updateVersionCode>getCurrentVersion()){
            boolean isIgnore=isIgnore(updateVersionCode);
            if (isIgnore) {//忽略此版本
                L.MyLog("忽略此版本",updateVersionCode+"");
                return false;
            }
            return true;
        }else {
            return false;
        }

    }


}

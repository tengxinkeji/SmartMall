package com.common.datadb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.common.app.MyApplication;
import com.common.app.degexce.L;
import com.demo.smarthome.device.Dev;

import cn.lanmei.com.smartmall.model.M_Dev_Err;

/**
 * Created by Administrator on 2016/9/2.
 */

public class DBDeviceErrManager {

    private Context mContext;
    private DBhelper dBhelper;
    private  SQLiteDatabase db;

    public static final String DEV_ERR="deverr";

    public static final String DEV_ERR_id="_id";
    public static final String DEV_user_id="uid";
    public static final String DEV_ERR_code="code";
    public static final String DEV_ERR_time="time";
    public static final String DEV_ERR_time_done="timedone";
    public static final String DEV_ERR_devId="devId";
    public static final String DEV_ERR_devname="devname";
    public static final String DEV_ERR_err="err";


    public static final String createTable="CREATE TABLE " +DEV_ERR+
            "(" +DEV_ERR_id+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DEV_user_id+" INTEGER, " +
            DEV_ERR_devname+" TEXT, " +
            DEV_ERR_devId+" TEXT, " +
            DEV_ERR_code+" TEXT, " +
            DEV_ERR_err+" TEXT, " +
            DEV_ERR_time_done+" TEXT, " +
            DEV_ERR_time+" TEXT)";

    public static final String createTable_notexists="CREATE TABLE if not exists " +DEV_ERR+
            "(" +DEV_ERR_id+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DEV_user_id+" INTEGER, " +
            DEV_ERR_devname+" TEXT, " +
            DEV_ERR_devId+" TEXT, " +
            DEV_ERR_code+" TEXT, " +
            DEV_ERR_err+" TEXT, " +
            DEV_ERR_time_done+" TEXT, " +
            DEV_ERR_time+" TEXT)";

    private String uid;
    DBDeviceListManager dbDeviceListManager;
    DBErrManager dbErrManager;
    public DBDeviceErrManager(Context mContext) {
        this.mContext = mContext;
        uid = MyApplication.getInstance().getUid();
        dBhelper=DBhelper.newInstance(mContext);
        db=dBhelper.getWritableDatabase();
        dbDeviceListManager=new DBDeviceListManager(mContext);
        dbErrManager=new DBErrManager(mContext);

    }

    public long addErr(M_Dev_Err devErr){

        String select=DEV_user_id+" = ? and "
                +DEV_ERR_devId + " = ? and "
                +DEV_ERR_code + " = ?";

        ContentValues values=new ContentValues();
        values.put(DEV_user_id,uid);
        values.put(DEV_ERR_code,devErr.getErrCode());
        values.put(DEV_ERR_time,devErr.getErrTime());
        values.put(DEV_ERR_devId,devErr.getErrDevIdHex());
        values.put(DEV_ERR_devname,devErr.getErrDevName());
        values.put(DEV_ERR_err,devErr.getErr());
        values.put(DEV_ERR_time_done,devErr.getErrDoneTime());
        long insN = db.insert(DEV_ERR, DEV_ERR_code, values);
        L.MyLog(DEV_ERR,"insert:"+insN);
        return insN;

    }

    public M_Dev_Err getErr(String devNo){

        String select=DEV_user_id+" = ? and "
                +DEV_ERR_devId + " = ?";
        Cursor c = db.query(DEV_ERR, null,select , new String[]{uid,devNo}, null, null, null);
        if (c.getCount()>0){
            c.moveToFirst();
            M_Dev_Err err = setErr(c);;
            c.close();
            return err;
        }
        c.close();
        return null;
    }

    public M_Dev_Err getErr(String devNo,String errCode){

        String select=DEV_user_id+" = ? and "
                +DEV_ERR_code+" = ? and "
                +DEV_ERR_devId + " = ?";
        Cursor c = db.query(DEV_ERR, null,select , new String[]{uid,errCode,devNo}, null, null, null);
        if (c.getCount()>0){
            c.moveToFirst();
            M_Dev_Err err=setErr(c);
            c.close();
            return err;
        }
        c.close();
        return null;
    }

    private M_Dev_Err setErr(Cursor c){
        M_Dev_Err err = new M_Dev_Err();
        err.setErrCode(c.getString(c.getColumnIndex(DEV_ERR_code)));
        err.setErrDevIdHex(c.getString(c.getColumnIndex(DEV_ERR_devId)));
        err.setErrTime(c.getLong(c.getColumnIndex(DEV_ERR_time)));
        err.setErrDoneTime(c.getLong(c.getColumnIndex(DEV_ERR_time_done)));
        Dev dev = dbDeviceListManager.getDev(err.getErrDevIdHex());
        err.setErrDevId(dev.getId());
        err.setErrDevName(dev.getNickName());
        err.setPic(dev.getDevPic());
        if (err.getErrCode().contains("|")){
            String[] faults = err.getErrCode().split("\\|");
            String errStr=dbErrManager.getErr(faults[0]);
            errStr+="\n"+dbErrManager.getErr(faults[1]);
            err.setErr(errStr);
        }else {
            err.setErr(dbErrManager.getErr(err.getErrCode()));
        }

        return err;
    }


    public int  updateErrTime(String devNo,String errCode,M_Dev_Err devErr){

        String select=DEV_user_id+" = ? and "
                +DEV_ERR_devId + " = ? and "
                +DEV_ERR_code + " = ?";
        L.MyLog("",select);
        L.MyLog("",uid+"---"+devNo+"---"+errCode);
        ContentValues values=new ContentValues();
        values.put(DEV_ERR_time_done,devErr.getErrDoneTime());
        values.put(DEV_ERR_devname,devErr.getErrDevName());
        values.put(DEV_ERR_err,devErr.getErr());
        values.put(DEV_ERR_time,devErr.getErrTime());
        return  db.update(DEV_ERR, values, select, new String[]{uid, devNo, errCode});

    }
}

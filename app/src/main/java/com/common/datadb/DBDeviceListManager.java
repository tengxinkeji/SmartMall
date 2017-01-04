package com.common.datadb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.common.app.MyApplication;
import com.common.app.degexce.L;
import com.demo.smarthome.device.Dev;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */

public class DBDeviceListManager {

    private Context mContext;
    private DBhelper dBhelper;
    private  SQLiteDatabase db;

    public static final String DEV_List="devlist";

    public static final String DEV_id="_id";
    public static final String DEV_user_id="uid";
    public static final String DEV_device_id="device_id";
    public static final String DEV_device_name="device_name";
    public static final String DEV_device_no="device_no";
    public static final String DEV_device_type="device_type";
    public static final String DEV_device_type_name="device_type_name";
    public static final String DEV_record_id="record_id";
    public static final String DEV_master="master";
    public static final String DEV_password="password";
    public static final String DEV_pic="pic";
    public static final String DEV_version="version";
    public static final String DEV_addtime="addtime";


    public static final String createTable="CREATE TABLE " +DEV_List+
            "(" +DEV_id+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DEV_user_id+" INTEGER, " +
            DEV_device_id+" TEXT, " +
            DEV_device_name+" TEXT, " +
            DEV_device_no+" TEXT, " +
            DEV_device_type+" TEXT, " +
            DEV_device_type_name+" TEXT, " +
            DEV_record_id+" INTEGER, " +
            DEV_master+" INTEGER, " +
            DEV_password+" TEXT, " +
            DEV_pic+" TEXT, " +
            DEV_version+" TEXT, " +
            DEV_addtime+" TEXT)";

    private String uid;
    public DBDeviceListManager(Context mContext) {
        this.mContext = mContext;
        uid = MyApplication.getInstance().getUid();
        dBhelper=DBhelper.newInstance(mContext);
        db=dBhelper.getWritableDatabase();
    }

    public long addDev(Dev dev){


        ContentValues values=new ContentValues();
        values.put(DEV_user_id,uid);
        values.put(DEV_device_id,dev.getId());
        values.put(DEV_device_name,dev.getNickName());
        values.put(DEV_device_no,dev.getIdHex());
        values.put(DEV_device_type,dev.getDevType());
        values.put(DEV_device_type_name,dev.getDevTypeName());
        values.put(DEV_record_id,dev.getRecordId());
        values.put(DEV_master,dev.getMaster());
        values.put(DEV_password,dev.getPass());
        values.put(DEV_pic,dev.getDevPic());
        values.put(DEV_version,dev.getDevVersion());
        values.put(DEV_addtime,dev.getAddtime());
        long insN = db.insert(DEV_List, DEV_user_id, values);
        L.MyLog(DEV_List,"insert:"+insN);
        return insN;

    }

    public Dev getDev(String devNo){

        String select=DEV_user_id+" = ? and "
                +DEV_device_no + " = ?";
        Cursor c = db.query(DEV_List, null,select , new String[]{uid,devNo}, null, null, null);
        if (c.getCount()>0){
            c.moveToFirst();
            Dev dev = setDev(c);
            c.close();
            return dev;
        }
        c.close();
        return null;
    }

    public Dev getDevById(String devId){

        String select=DEV_user_id+" = ? and "
                +DEV_device_id + " = ?";
        Cursor c = db.query(DEV_List, null,select , new String[]{uid,devId}, null, null, null);
        if (c.getCount()>0){
            c.moveToFirst();
            Dev dev = setDev(c);
            c.close();
            return dev;
        }
        c.close();
        return null;
    }

    public List<Dev> selectDevs(){
        List<Dev> devs = new ArrayList<>();
        String select=DEV_user_id+" = ? ";
        Cursor c = db.query(DEV_List, null,select , new String[]{uid}, null, null, null);
        if (c.getCount()>0){
            while (c.moveToNext()){
                devs.add(setDev(c));
            }

        }
        c.close();
        return devs;
    }

    public int delUid(){
        String select=DEV_user_id+" = ? ";
        return db.delete(DEV_List,select,new String[]{uid});
    }

    public int delDevRecId(String id){
        String select=DEV_user_id+" = ? and "+DEV_record_id+" = ? ";
        return db.delete(DEV_List,select,new String[]{uid,id});
    }

    private Dev setDev( Cursor c ){
        Dev dev = new Dev();
        dev.setId(c.getString(c.getColumnIndex(DEV_device_id)));
        dev.setNickName(c.getString(c.getColumnIndex(DEV_device_name)));
        dev.setIdHex(c.getString(c.getColumnIndex(DEV_device_no)));
        dev.setDevType(c.getString(c.getColumnIndex(DEV_device_type)));
        dev.setDevTypeName(c.getString(c.getColumnIndex(DEV_device_type_name)));
        dev.setRecordId(c.getString(c.getColumnIndex(DEV_record_id)));
        dev.setMaster(c.getInt(c.getColumnIndex(DEV_master)));
        dev.setPass(c.getString(c.getColumnIndex(DEV_password)));
        dev.setDevPic(c.getString(c.getColumnIndex(DEV_pic)));
        dev.setDevVersion(c.getString(c.getColumnIndex(DEV_version)));
        dev.setAddtime(c.getString(c.getColumnIndex(DEV_addtime)));
        return dev;
    }


}

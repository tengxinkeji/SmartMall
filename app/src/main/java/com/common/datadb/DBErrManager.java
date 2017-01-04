package com.common.datadb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.common.app.degexce.L;

/**
 * Created by Administrator on 2016/9/2.
 */

public class DBErrManager {

    private Context mContext;
    private DBhelper dBhelper;
    private  SQLiteDatabase db;

    public static final String ERR="err";

    public static final String ERR_id="_id";
    public static final String ERR_code="code";
    public static final String ERR_code_msg="code_msg";



    public static final String createTable="CREATE TABLE " +ERR+
            "(" +ERR_id+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ERR_code+" TEXT, " +
            ERR_code_msg+" TEXT)";


    public DBErrManager(Context mContext) {
        this.mContext = mContext;

        dBhelper=DBhelper.newInstance(mContext);
        db=dBhelper.getWritableDatabase();
    }

    public long addErr(String code,String codeMsg){
        ContentValues values=new ContentValues();
        values.put(ERR_code,code);
        values.put(ERR_code_msg,codeMsg);
        long insN = db.insert(ERR, ERR_code, values);
        L.MyLog(ERR,"insert:"+insN);
        return insN;

    }

    public String getErr(String code){

        String select=ERR_code+" = ?  ";
        Cursor c = db.query(ERR, null,select , new String[]{code}, null, null, null);
        if (c.getCount()>0){
            c.moveToFirst();
            String codeMsg = c.getString(c.getColumnIndex(ERR_code_msg));
            c.close();
            return codeMsg;
        }
        c.close();
        return null;
    }

   public int delAll(){
       return db.delete(ERR,null,null);
   }
}

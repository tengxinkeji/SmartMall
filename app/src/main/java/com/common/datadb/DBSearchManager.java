package com.common.datadb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.common.app.degexce.L;

import java.util.ArrayList;
import java.util.List;

import static com.common.datadb.DBErrManager.ERR_code;

/**
 * Created by Administrator on 2016/9/2.
 */

public class DBSearchManager {

    private Context mContext;
    private DBhelper dBhelper;
    private  SQLiteDatabase db;

    public static final String SEARCH="search";

    public static final String SEARCH_name="name";

    public static final String createTable="CREATE TABLE " +SEARCH+
            "(" +SEARCH_name+" TEXT PRIMARY KEY ); ";


    public DBSearchManager(Context mContext) {
        this.mContext = mContext;

        dBhelper=DBhelper.newInstance(mContext);
        db=dBhelper.getWritableDatabase();
    }

    public long addSearch(String name){
        ContentValues values=new ContentValues();
        values.put(SEARCH_name,name);
        long insN = db.insert(SEARCH, SEARCH_name, values);
        L.MyLog(SEARCH,"insert:"+insN);
        return insN;

    }

    public ArrayList<String> getSearc(){
        ArrayList<String> list=new ArrayList<>();
        Cursor c = db.query(SEARCH, null,null , null, null, null, null);
        if (c.getCount()>0){
            c.moveToFirst();
            for (int i=0;i<c.getCount();i++){
                list.add(c.getString(c.getColumnIndex(SEARCH_name)));
                c.moveToNext();
            }

            c.close();

        }
        c.close();
        return list;
    }
    public void delAll(){
        int  del = db.delete(SEARCH,null,null);
        L.MyLog(SEARCH,"delete:"+del);


    }
}

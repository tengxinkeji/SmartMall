package com.common.datadb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.common.app.degexce.L;

/**
 * Created by Administrator on 2016/9/2.
 */

public class DBhelper extends SQLiteOpenHelper {
    private String TAG="DBhelper";

    private static String dbName="smartmall.db";
    private static int dbVersion=1;
    private static DBhelper dBhelper;

    public static DBhelper newInstance(Context context){
        if (dBhelper==null)
            dBhelper=new DBhelper(context);
        return dBhelper;
    }

    public DBhelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        L.MyLog(TAG,"onCreate__dbVersion:"+dbVersion);
        update(0,db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        L.MyLog(TAG,"onUpgrade_oldVersion:"+oldVersion+"newVersion:"+newVersion);
        update(oldVersion,db);

    }

    private void update(int oldVersion,SQLiteDatabase db){
        switch (oldVersion){
            case 0:
                db.execSQL(DBGoodsCartManager.createTable);
                db.execSQL(DBManagerCategory.createTable);
                db.execSQL(DBSearchManager.createTable);
                db.execSQL(DBDeviceListManager.createTable);
                db.execSQL(DBDeviceErrManager.createTable);
                db.execSQL(DBErrManager.createTable);
//            case 3:
//                db.execSQL("alter table " +DBGoodsCartManager.Cart+
//                        " ADD " +DBGoodsCartManager.Cart_goods_record_id+" INTEGER ");
//            case 4:
//                db.execSQL(DBDeviceErrManager.createTable_notexists);
//            case 5:
//                db.execSQL(DBSearchManager.createTable);
//
//                break;

        }
    }
}

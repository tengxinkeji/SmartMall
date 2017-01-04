package com.common.datadb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_categroy;

/**
 * Created by Administrator on 2016/9/2.
 */

public class DBManagerCategory {

    private Context mContext;
    private DBhelper dBhelper;
    private  SQLiteDatabase db;

    public static final String TAGLE="category";

    public static final String TAGLE_id="_id";
    public static final String TAGLE_name="name";
    public static final String TAGLE_pic="pic";
    public static final String TAGLE_parent_id="parent_id";
    public static final String TAGLE_model_id="model_id";
    public static final String TAGLE_recommend="recommend";
    public static final String TAGLE_sort="sort";
    public static final String TAGLE_visibility="visibility";


    public static final String createTable="CREATE TABLE " +TAGLE+
            "(" +TAGLE_id+" INTEGER PRIMARY KEY , " +
            TAGLE_name+" TEXT, " +
            TAGLE_parent_id+" INTEGER, " +
            TAGLE_model_id+" INTEGER, " +
            TAGLE_recommend+" INTEGER, " +
            TAGLE_sort+" INTEGER, " +
            TAGLE_visibility+" INTEGER, " +
            TAGLE_pic+" TEXT)";


    public DBManagerCategory(Context mContext) {
        this.mContext = mContext;

        dBhelper=DBhelper.newInstance(mContext);
        db=dBhelper.getWritableDatabase();
    }

    /**更新全部分类*/
    public void updateData(List<ContentValues> categrys){
        db.delete(TAGLE,null,null);
        for (ContentValues values:categrys){
            db.insert(TAGLE, TAGLE_name, values);
        }
    }
    /**
     * 获取子分类
     * @param parentId 父id
     * */
    public List<M_categroy> getCategroys(int parentId){
        List<M_categroy> categroys = new ArrayList<>();
        String select=TAGLE_parent_id+" = ?  ";
        Cursor c = db.query(TAGLE, null,select , new String[]{parentId+""}, null, null, TAGLE_sort+" asc");
        while (c.moveToNext()){
            categroys.add(getCate(c));
        }
        c.close();
        return categroys;
    }

    /**
     * 获取推荐分类
     *
     * */
    public List<M_categroy> getCategroysRec(){
        List<M_categroy> categroys = new ArrayList<>();
        String select=TAGLE_recommend+" = 1  ";
        Cursor c = db.query(TAGLE, null,select , null, null, null, TAGLE_sort+" asc");
        while (c.moveToNext()){
            categroys.add(getCate(c));
        }
        c.close();
        return categroys;
    }

    private M_categroy getCate( Cursor c){
        M_categroy categroy=new M_categroy();
        categroy.setId(c.getInt(c.getColumnIndex(TAGLE_id)));
        categroy.setParent_id(c.getInt(c.getColumnIndex(TAGLE_parent_id)));
        categroy.setImgUrl(c.getString(c.getColumnIndex(TAGLE_pic)));
        categroy.setName(c.getString(c.getColumnIndex(TAGLE_name)));
        return categroy;
    }

}

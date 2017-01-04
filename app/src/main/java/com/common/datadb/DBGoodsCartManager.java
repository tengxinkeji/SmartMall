package com.common.datadb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.model.M_cart_goods;

/**
 * Created by Administrator on 2016/9/2.
 */

public class DBGoodsCartManager {

    private Context mContext;
    private DBhelper dBhelper;
    private  SQLiteDatabase db;

    public static final String Cart="goodscart";

    public static final String Cart_id="_id";
    public static final String Cart_user_id="uid";
    public static final String Cart_goods_record_id="goods_record_id";
    public static final String Cart_goodsid="goodsid";
    public static final String Cart_goodsStore="goodsStore";
    public static final String Cart_goodsName="goodsName";
    public static final String Cart_goodsImg="goodsImg";
    public static final String Cart_goodsPrice="goodsPrice";
    public static final String Cart_goodsparams="goodsparams";
    public static final String Cart_goodsCount="goodsCount";

    public static final String Cart_goodsParentId="goodsParentId";
    public static final String Cart_goodsParentName="goodsParentName";

    public static final String Cart_goodsIsCheck="goodsIsCheck";

    public static final String createTable="CREATE TABLE " +Cart+
            "(" +Cart_id+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Cart_user_id+" INTEGER, " +
            Cart_goodsid+" INTEGER, " +
            Cart_goodsParentId+" INTEGER, " +
            Cart_goods_record_id+" INTEGER, " +
            Cart_goodsParentName+" TEXT, " +
            Cart_goodsStore+" TEXT, " +
            Cart_goodsName+" TEXT, " +
            Cart_goodsImg+" TEXT, " +
            Cart_goodsPrice+" TEXT, " +
            Cart_goodsparams+" TEXT, " +
            Cart_goodsIsCheck+" INTEGER, " +
            Cart_goodsCount+" INTEGER)";

    public static String uid;
    public static double momey=0;
    public static int goodsCount=0;
    public static DBGoodsCartManager dbGoodsCartManager;

    public DBGoodsCartManager(Context mContext) {
        this.mContext = mContext;
        uid = MyApplication.getInstance().getUid();
        dBhelper=DBhelper.newInstance(mContext);
        db=dBhelper.getWritableDatabase();
    }


    private long insertGoods(M_Goods mGoods){
        ContentValues values = new ContentValues();
        values.put(Cart_user_id,uid);
        values.put(Cart_goods_record_id,mGoods.getRecordId());
        values.put(Cart_goodsid,mGoods.getGoods_id());
        values.put(Cart_goodsStore,mGoods.getGoodsStoreName());
        values.put(Cart_goodsName,mGoods.getGoodsName());
        values.put(Cart_goodsImg,TextUtils.isEmpty(mGoods.getProducts_img())?mGoods.getGoodsImg():mGoods.getProducts_img());
        values.put(Cart_goodsPrice,mGoods.getGoodsPrice());
        values.put(Cart_goodsparams,mGoods.getSpec());
        values.put(Cart_goodsCount,mGoods.getGoodsCount());
        values.put(Cart_goodsParentId,mGoods.getGoodsStoreId());
        values.put(Cart_goodsParentName,mGoods.getGoodsStoreName());
        values.put(Cart_goodsIsCheck,mGoods.isSelect()?1:0);
        long insC=db.insert(Cart, Cart_goodsid, values);
        L.MyLog(Cart,":加入购物车"+mGoods.getGoods_id()+"---insC:"+insC);
        if (insC>0){
            StaticMethod.showInfo(mContext,"加入购物车");
            queryUserCartGoods();
        }

        return insC;

    }

    /**
     *更新商品价格
     */
    public void updateGoodsPrice(M_Goods mGoods){
        ContentValues values = new ContentValues();
        values.put(Cart_goodsPrice,mGoods.getGoodsPrice());
        String whereClause = Cart_user_id+" = "+uid
                +" and "+Cart_goodsid+" = "+mGoods.getGoods_id();
        int re = db.update(Cart, values, whereClause, null);
        if (re>0){
            queryUserCartGoods();
        }
        L.MyLog(Cart,uid+"商品价格更新：rows"+re+"---Goods_id:"+mGoods.getGoods_id()+":price:"+mGoods.getGoodsPrice());
    }

    /**
    *@param goodsCount
    */
    public int updateGoodsCount(int goodsId,int goodsCount){
        ContentValues values = new ContentValues();
        values.put(Cart_goodsCount,goodsCount);
        String whereClause = Cart_user_id+" = "+uid
                +" and "+Cart_goodsid+" = "+goodsId;
        int updateC = db.update(Cart, values, whereClause, null);
        if (updateC>0)
            queryUserCartGoods();
        return updateC;

    }

    public void delGoods(int goodsId){
        String whereClause = Cart_user_id+" = "+uid
                +" and "+Cart_goodsid+" = "+goodsId;
        int del = db.delete(Cart,whereClause,null);
        if (del>0){
            queryUserCartGoods();
            StaticMethod.showInfo(mContext,"删除成功");
        }
    }

    public void delCartGoods(){
        String whereClause = Cart_user_id+" = "+uid;
        int del = db.delete(Cart,whereClause,null);
        if (del>0){
            queryUserCartGoods();
            StaticMethod.showInfo(mContext,"删除成功");
        }
    }

    public void addGoods(M_Goods mGoods){
        String selection=Cart_user_id+" = "+uid
                +" and "+Cart_goodsid+" = "+mGoods.getGoods_id();
        Cursor cur = db.query(Cart, null, selection, null, null, null, null);
        long insC=0;
        if (cur.getCount()==1){
//            updateGoods(mGoods);
            StaticMethod.showInfo(mContext,"已加入购物车");
        }else if (cur.getCount()>1){
            int count=0;
            while(cur.moveToNext()){
                count+= cur.getInt(cur.getColumnIndex(Cart_goodsCount));
                int goodsId=cur.getInt(cur.getColumnIndex(Cart_goodsid));
                String whereClause = Cart_user_id+" = "+uid
                        +" and "+Cart_goodsid+" = "+goodsId;
                db.delete(Cart,whereClause,null);
            }
            mGoods.setGoodsCount(count);
            insC=insertGoods(mGoods);
        }else{
            insC = insertGoods(mGoods);
        }
        cur.close();

    }

    /**
     *获取当前账户的购物车的所有商品是否选择
     *  @param isSelect
     */
    public void updateAllGoodsSelect(boolean isSelect){
        String selection=Cart_user_id+" = "+uid;
        ContentValues values = new ContentValues();
        values.put(Cart_goodsIsCheck,isSelect?1:0);
        db.update(Cart,values,selection,null);
    }

    /**
     *获取当前账户的购物车的指定商品是否选择
     *  @param goodsId
     */
    public void updateGoodsSelect(int goodsId,boolean isSelect){
        String selection=Cart_user_id+" = "+uid+" and "+Cart_goodsid+" = "+goodsId;
        ContentValues values = new ContentValues();
        values.put(Cart_goodsIsCheck,isSelect?1:0);
        db.update(Cart,values,selection,null);
    }

    /**
     *获取当前账户的购物车的商店是否选择
     *  @param storeId
     */
    public void updateGoodsStoreSelect(int storeId,boolean isSelect){
        String selection=Cart_user_id+" = "+uid+" and "+Cart_goodsParentId+" = "+storeId;
        ContentValues values = new ContentValues();
        values.put(Cart_goodsIsCheck,isSelect?1:0);
        db.update(Cart,values,selection,null);
    }

    /**
     *获取当前账户的购物车
     *  @param storeId
     */
    public List<M_Goods> queryStoreGoods(int storeId){

        List<M_Goods> goodses=new ArrayList<>();

        String selection=Cart_user_id+" = "+uid+" and "+Cart_goodsParentId+" = "+storeId;
        Cursor cur = db.query(Cart, null, selection, null, null, null, null);

        if (cur.getCount()>0){
            M_Goods goods;
            while (cur.moveToNext()){
                goods=getGoods(cur);
                if (goods.isSelect()){
                    goodsCount+=goods.getGoodsCount();
                    momey+=goods.getGoodsCount()*goods.getGoodsPrice();
                }

                goodses.add(goods);
            }
        }

        return goodses;
    }

    /**
     *获取当前账户的购物车
     */
    public List<M_cart_goods> queryUserCartGoods(){
        List<M_cart_goods> cartGoodses=new ArrayList<>();
        String selection=Cart_user_id+" = "+uid;
        Cursor c= db.query(Cart,null,selection,null,Cart_goodsParentId,null,null);
        momey=0;
        goodsCount=0;
        if (c.getCount()>0){
            M_cart_goods cartGoods;
            List<M_Goods> goodses;
            while (c.moveToNext()){
                cartGoods=new M_cart_goods();
                cartGoods.setStoreId(c.getInt(c.getColumnIndex(Cart_goodsParentId)));
                cartGoods.setStoreName(c.getString(c.getColumnIndex(Cart_goodsParentName)));

                goodses=queryStoreGoods(cartGoods.getStoreId());

                int storeGoodsNum=0;
                double storeGoodsMoney=0.0d;
                int storeGoodsSelectCount=0;
                if (goodses.size()>0){
                    for (M_Goods goods:goodses){
                        if (goods.isSelect()){
                            storeGoodsNum+=goods.getGoodsCount();
                            storeGoodsMoney+=goods.getGoodsCount()*goods.getGoodsPrice();
                            storeGoodsSelectCount++;
                        }
                    }

                }
                cartGoods.setGoodsList(goodses);
                cartGoods.setMoney(storeGoodsMoney);
                cartGoods.setGoodsNum(storeGoodsNum);
                cartGoods.setSelect(goodses.size()==storeGoodsSelectCount);
                cartGoodses.add(cartGoods);

            }
            if (dbShoppingCartListener!=null)
                dbShoppingCartListener.dbShoppingCartListener(goodsCount,momey);
        }

        c.close();
        L.MyLog(Cart+"---goodsList()","goodsCount:"+goodsCount+"---momey:"+momey+"---dbShoppingCartListener:"+dbShoppingCartListener);
        if (dbShoppingCartListener!=null)
            dbShoppingCartListener.dbShoppingCartListener(goodsCount,momey);
        return cartGoodses;
    }

    private M_Goods getGoods(Cursor cur){
        M_Goods goods = new M_Goods();
        goods.setRecordId(cur.getInt(cur.getColumnIndex(Cart_goods_record_id)));
        goods.setGoods_id(cur.getInt(cur.getColumnIndex(Cart_goodsid)));
        goods.setGoodsName(cur.getString(cur.getColumnIndex(Cart_goodsName)));
        goods.setGoodsStoreId(cur.getInt(cur.getColumnIndex(Cart_goodsParentId)));
        goods.setGoodsStoreName(cur.getString(cur.getColumnIndex(Cart_goodsParentName)));
        goods.setSpec(cur.getString(cur.getColumnIndex(Cart_goodsparams)));
        goods.setGoodsImg(cur.getString(cur.getColumnIndex(Cart_goodsImg)));
        goods.setGoodsPrice(cur.getDouble(cur.getColumnIndex(Cart_goodsPrice)));
        goods.setGoodsCount(cur.getInt(cur.getColumnIndex(Cart_goodsCount)));
        goods.setSelect(cur.getInt(cur.getColumnIndex(Cart_goodsIsCheck))==1);
        return goods;
    }

    private static DBShoppingCartListener dbShoppingCartListener;

    public void setShoppingCartListener(DBShoppingCartListener shoppingCartListener) {
        L.MyLog(Cart,"shoppingCartListener:"+shoppingCartListener);
        this.dbShoppingCartListener = shoppingCartListener;
    }

    public interface DBShoppingCartListener{
        public void dbShoppingCartListener(int gooodsCount,double money);
    }
}

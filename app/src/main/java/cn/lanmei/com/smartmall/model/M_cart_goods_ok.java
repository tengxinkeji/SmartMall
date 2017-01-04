package cn.lanmei.com.smartmall.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */

public class M_cart_goods_ok {
    private String storeId;
    private String storeName;
    private List<M_Goods> goodsList;


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<M_Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<M_Goods> goodsList) {
        this.goodsList = goodsList;
    }




}

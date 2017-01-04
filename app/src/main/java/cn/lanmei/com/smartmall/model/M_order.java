package cn.lanmei.com.smartmall.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */

public class M_order {
    private int id;
    private String orderNo;
    private String storeName;
    private int storeId;
    private int orderStauts;
    private int orderPayStauts;
    private int payType;
    private String goodsImg;
    private String goodsName;
    private String goodsParams;
    private double goodsMoney;
    private int goodsNum;
    private double orderMoney;
    private int orderNum;
    private String mobile;

    private List<M_Goods> product;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    /**
     *  0、全部 1, 待支付  5，已完成
    * */
    public int getOrderStauts() {
        return orderStauts;
    }

    public void setOrderStauts(int orderStauts) {
        this.orderStauts = orderStauts;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsParams() {
        return goodsParams;
    }

    public void setGoodsParams(String goodsParams) {
        this.goodsParams = goodsParams;
    }

    public double getGoodsMoney() {
        return goodsMoney;
    }

    public void setGoodsMoney(double goodsMoney) {
        this.goodsMoney = goodsMoney;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public double getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(double orderMoney) {
        this.orderMoney = orderMoney;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<M_Goods> getProduct() {
        return product;
    }

    public void setProduct(List<M_Goods> product) {
        this.product = product;
    }

    public int getOrderPayStauts() {
        return orderPayStauts;
    }

    public void setOrderPayStauts(int orderPayStauts) {
        this.orderPayStauts = orderPayStauts;
    }
}

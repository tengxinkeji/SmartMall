package cn.lanmei.com.smartmall.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 */

public class M_Category_goods {
    private int categoryId;
    private String categoryName;
    private List<M_Goods> goodsList;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<M_Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<M_Goods> goodsList) {
        this.goodsList = goodsList;
    }
}

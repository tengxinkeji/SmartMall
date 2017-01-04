package cn.lanmei.com.smartmall.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/12.
 */

public class M_categroy implements Serializable{
    private static final long serialVersionUID = 1509208511643253396L;
    private int id;
    private int parent_id;
    private int recommend;
    private String imgUrl;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

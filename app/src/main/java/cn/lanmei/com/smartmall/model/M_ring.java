package cn.lanmei.com.smartmall.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/14.
 */

public class M_ring implements Serializable {
    private static final long serialVersionUID = 3967720437574613931L;
    private int id;
    private String name;
    private String detail;
    private String url;
    private int post_count;
    private int reviews_count;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPost_count() {
        return post_count;
    }

    public void setPost_count(int post_count) {
        this.post_count = post_count;
    }

    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }
}

package cn.lanmei.com.smartmall.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/20.
 */

public class M_news implements Serializable {
    private static final long serialVersionUID = 8798993681754138963L;
    private int id;
    private String key;
    private String value;
    private String time;

    public M_news(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

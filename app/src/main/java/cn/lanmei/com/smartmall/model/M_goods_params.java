package cn.lanmei.com.smartmall.model;

/**
 * Created by Administrator on 2016/9/23.
 */

public class M_goods_params {
    private String key;
    private String value;

    public M_goods_params(String key, String value) {
        this.key = key;
        this.value = value;
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
}

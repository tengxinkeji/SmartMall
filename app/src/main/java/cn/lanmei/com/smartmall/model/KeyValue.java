package cn.lanmei.com.smartmall.model;

/**
 * Created by Administrator on 2016/9/26.
 */

public class KeyValue {
    private String key;
    private String Value;

    public KeyValue(String key, String value) {
        this.key = key;
        Value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}

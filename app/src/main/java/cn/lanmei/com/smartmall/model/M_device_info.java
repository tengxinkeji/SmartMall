package cn.lanmei.com.smartmall.model;

/**
 * Created by Administrator on 2016/9/20.
 */

public class M_device_info {
    private String key;
    private String value;

    public M_device_info(String key, String value) {
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

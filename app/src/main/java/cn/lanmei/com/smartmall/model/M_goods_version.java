package cn.lanmei.com.smartmall.model;

import java.util.List;

import cn.lanmei.com.smartmall.presenter.TagInfo;

/**
 * Created by Administrator on 2016/9/21.
 */

public class M_goods_version {
    private int id;
    private int type;
    private int curChild;
    private List<TagInfo> value;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<TagInfo> getValue() {
        return value;
    }

    public void setValue(List<TagInfo> value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurChild() {
        return curChild;
    }

    public void setCurChild(int curChild) {
        this.curChild = curChild;
    }
}

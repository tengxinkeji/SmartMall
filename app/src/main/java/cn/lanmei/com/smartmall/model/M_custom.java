package cn.lanmei.com.smartmall.model;

import java.util.List;

import cn.lanmei.com.smartmall.presenter.TagInfo;

/**
 * Created by Administrator on 2016/9/18.
 */

public class M_custom {
    private String typeName;
    private int drawable;
    private int curChild=-1;
    private List<TagInfo> childList;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getCurChild() {
        return curChild;
    }

    public void setCurChild(int curChild) {
        this.curChild = curChild;
    }

    public List<TagInfo> getChildList() {
        return childList;
    }

    public void setChildList(List<TagInfo> childList) {
        this.childList = childList;
    }


}

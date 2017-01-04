package cn.lanmei.com.smartmall.model;

/**
 * Created by Administrator on 2015/12/28.
 */
public class MenuModel {
    private boolean isDividerShow;
    private boolean isDividerLarge;
    private boolean isShowTop;
    private int id;
    private String title;
    private int drawable;


    public boolean isShowTop() {
        return isShowTop;
    }

    public void setShowTop(boolean showTop) {
        isShowTop = showTop;
    }

    public boolean isDividerShow() {
        return isDividerShow;
    }

    public void setDividerShow(boolean dividerShow) {
        isDividerShow = dividerShow;
    }

    public void setDividerLarge(boolean dividerLarge) {
        isDividerLarge = dividerLarge;
    }

    public boolean isDividerLarge() {
        return isDividerLarge;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




}

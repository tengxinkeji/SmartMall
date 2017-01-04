package cn.lanmei.com.smartmall.presenter;

/**
 * <p>作者：江俊超 on 2016/6/25 17:11</p>
 * <p>邮箱：aohanyao@gmail.com</p>
 * <p>标签</p>
 */
public class TagInfo {

    private String text;
    private boolean isSelect;
    private int positon=0;
    private int id;

    public TagInfo(boolean isChecked, String text) {

        this.isSelect = isChecked;
        this.text = text;
    }

    public TagInfo(boolean isChecked, String text,int id) {

        this.isSelect = isChecked;
        this.text = text;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public TagInfo(String text) {

        this.text = text;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

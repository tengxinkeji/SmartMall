package cn.lanmei.com.smartmall.model;

import com.common.app.StaticMethod;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */

public class M_Review {
    private int id;
    private String img;
    private String name;
    private String area;
    private int heart;
    private long time;
    private String content;
    private String goodsTime;
    private int like;
    private List<String> imgs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public  String getTime() {
        return StaticMethod.formatterTime1(time);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGoodsTime() {
        return goodsTime;
    }

    public void setGoodsTime(String goodsTime) {
        this.goodsTime = goodsTime;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

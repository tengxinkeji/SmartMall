package cn.lanmei.com.smartmall.model;

import com.common.app.StaticMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */

public class M_topic implements Serializable {
    private static final long serialVersionUID = 2737119443918781404L;
    private int id;
    public List<TopicImg> imgs ;
    private String topicUrl;
    private String topicName;
    private String topicAddr;
    private long topicTime;
    private int recommend;
    private String topic;
    private String topicTypeName;
    private int topic_2;
    private int topic_3;

    public M_topic() {
        this.imgs = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopicUrl() {
        return topicUrl;
    }

    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicAddr() {
        return topicAddr;
    }

    public void setTopicAddr(String topicAddr) {
        this.topicAddr = topicAddr;
    }

    public String getTopicTime() {
        return StaticMethod.formatterTime1(topicTime);
    }

    public void setTopicTime(long topicTime) {
        this.topicTime = topicTime*1000;
    }

    /**
     * 1精华,2最热,3置顶,4推荐
     * */
    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopicTypeName() {
        return topicTypeName;
    }

    public void setTopicTypeName(String topicTypeName) {
        this.topicTypeName = topicTypeName;
    }

    public int getTopic_2() {
        return topic_2;
    }

    public void setTopic_2(int topic_2) {
        this.topic_2 = topic_2;
    }

    public int getTopic_3() {
        return topic_3;
    }

    public void setTopic_3(int topic_3) {
        this.topic_3 = topic_3;
    }

    public void addImg(int type, String url){
        imgs.add(new TopicImg(type,url));
    }


}

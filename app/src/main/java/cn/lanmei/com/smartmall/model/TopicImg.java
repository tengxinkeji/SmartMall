package cn.lanmei.com.smartmall.model;

import java.io.Serializable;

public class TopicImg implements Serializable{
    private static final long serialVersionUID = 833770247371485L;
    public int type;
        public String url;
        public String videoUrl;

        public TopicImg(int type, String url) {
            this.type = type;
            this.url = url;

        }
    }
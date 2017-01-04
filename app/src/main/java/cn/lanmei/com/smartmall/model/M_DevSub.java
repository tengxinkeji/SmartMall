package cn.lanmei.com.smartmall.model;

import com.demo.smarthome.device.Dev;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/22.
 */

public class M_DevSub implements Serializable {
    private static final long serialVersionUID = -6156673608833924285L;
    private int id;

    private String nickname;
    private String addTime;
    private String img;
    private String userId;
    private String username;
    private Dev dev;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Dev getDev() {
        return dev;
    }

    public void setDev(String device_id,String device_no,String device_name,int device_type,String password,int master) {
        Dev dev=new Dev();
        dev.setId(device_id);
        dev.setIdHex(device_no);
        dev.setNickName(device_name);
        dev.setDevType(device_type+"");
        dev.setMaster(master);
        dev.setPass(password);
        this.dev = dev;
    }
}

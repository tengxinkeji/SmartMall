package cn.lanmei.com.smartmall.model;

import android.content.res.Resources;

import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.tools.FormatTime;

import cn.lanmei.com.smartmall.R;

/**
 * Created by Administrator on 2016/11/7.
 */

public class M_log {
    private int id;
    private String title;
    private Double money;
    private Double balance;
    private String userImg;
    public FormatTime formatTime;

    public long time;


    public  int type;
    public int sectionPosition;
    public int listPosition;

    private Resources res;

    public M_log() {
        this.res = MyApplication.applicationContext.getResources();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        formatTime=new FormatTime(this.time);
        /*String timeStr = StaticMethod.formatterTime(time);
        year = Integer.parseInt(timeStr.substring(0,4));
        month = Integer.parseInt(timeStr.substring(5,7));
        day = Integer.parseInt(timeStr.substring(8,10));
        hour = Integer.parseInt(timeStr.substring(11,13));
        minute = Integer.parseInt(timeStr.substring(14,16));*/
    }

    public String getTimeStr(){
       return StaticMethod.formatterTime(getTime());
    }

    public String getFormatTime(){
        String timeStr=getTimeStr();

        if ((System.currentTimeMillis()/1000-getTime()/1000)<24*60*60){
            timeStr=res.getString(R.string.day)
                    +formatTime.getHour()+":"+formatTime.getMinute();
        }else if ((System.currentTimeMillis()/1000-getTime()/1000)<2*24*60*60){
            timeStr=res.getString(R.string.yesterday)+formatTime.getHour()+":"+formatTime.getMinute();
        }
        return timeStr;
    }

}

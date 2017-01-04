package com.common.tools;

import android.content.res.Resources;

import java.util.Calendar;

import cn.lanmei.com.smartmall.R;

/**
 * Created by Administrator on 2016/12/8.
 */

public class FormatTime {
    private long time;
    private boolean is12Hour;
    private Calendar calendar = Calendar.getInstance();

    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;

    public int week;

    public FormatTime() {
        this.time = System.currentTimeMillis();
    }

    /**
     * @param time  毫秒
     * */
    public FormatTime(long time) {
        this.time = time;
        calendar.setTimeInMillis(this.time);
    }

    /**
     * @param is12Hour 是否12小时
     * */
    public void setIs12Hour(boolean is12Hour) {
        this.is12Hour = is12Hour;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH)+1;
    }

    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour() {
        return calendar.get(is12Hour?Calendar.HOUR:Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public int getWeek() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public String getWeek(Resources res) {
        String week="";
        switch (getWeek()){
            case 1:
                week=res.getString(R.string.week_0);

                break;
            case 2:
                week=res.getString(R.string.week_1);

                break;
            case 3:
                week=res.getString(R.string.week_2);

                break;
            case 4:
                week=res.getString(R.string.week_3);

                break;
            case 5:
                week=res.getString(R.string.week_4);

                break;
            case 6:
                week=res.getString(R.string.week_5);

                break;
            case 7:
                week=res.getString(R.string.week_6);
                break;

        }
        return week;
    }

    public boolean isAM(){
        //0-上午；1-下午
        int am=calendar.get(Calendar.AM_PM);
        return am==0;
    }
}

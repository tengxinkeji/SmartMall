package cn.lanmei.com.smartmall.model;

/**
 * Created by Administrator on 2016/9/2.
 */

public class M_Dev_Err {
    private String err;
    private String errCode;
    private String errDevId;
    private String errDevIdHex;
    private String errDevName;
    private long errTime;
    private long errDoneTime;
    private String pic;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrDevId() {
        return errDevId;
    }

    public void setErrDevId(String errDevId) {
        this.errDevId = errDevId;
    }

    public String getErrDevName() {
        return errDevName;
    }

    public void setErrDevName(String errDevName) {
        this.errDevName = errDevName;
    }

    public long getErrTime() {
        return errTime;
    }

    public void setErrTime(long errTime) {
        this.errTime = errTime;
    }

    public long getErrDoneTime() {
        return errDoneTime;
    }

    public void setErrDoneTime(long errDoneTime) {
        this.errDoneTime = errDoneTime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getErrDevIdHex() {
        return errDevIdHex;
    }

    public void setErrDevIdHex(String errDevIdHex) {
        this.errDevIdHex = errDevIdHex;
    }
}

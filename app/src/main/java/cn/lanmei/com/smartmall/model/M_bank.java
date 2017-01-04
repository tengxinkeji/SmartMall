package cn.lanmei.com.smartmall.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/23.
 */

public class M_bank implements Serializable {

    private static final long serialVersionUID = -1310945827652289341L;
    private int id;
    private String realname;
    private String banks_no;
    private String banks_name;
    private int banks_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getBanks_no() {
        return banks_no;
    }

    public void setBanks_no(String banks_no) {
        this.banks_no = banks_no;
    }

    public String getBanks_name() {
        return banks_name;
    }

    public void setBanks_name(String banks_name) {
        this.banks_name = banks_name;
    }

    public int getBanks_id() {
        return banks_id;
    }

    public void setBanks_id(int banks_id) {
        this.banks_id = banks_id;
    }
}

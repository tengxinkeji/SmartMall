package cn.lanmei.com.smartmall.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/23.
 */

public class M_address implements Serializable {
    private static final long serialVersionUID = -6501840354096094849L;
    private int id;
    private String accept_name;
    private String zip;
    private String telphone;
    private String country;
    private String province;
    private String city;
    private String area;
    private String address;
    private String mobile;
    private int isDefault;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccept_name() {
        return accept_name;
    }

    public void setAccept_name(String accept_name) {
        this.accept_name = accept_name;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}

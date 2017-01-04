package cn.lanmei.com.smartmall.model;

/**
 * Created by Administrator on 2016/5/16.
 */
public class StructorHotel {
    private int userId;
    private int hotelId;
    private String hotelImg;
    private String hotelName;
    private float hotelPprice;
    private int hotelDistance;
    private String hotelAddr;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelImg() {
        return hotelImg;
    }

    public void setHotelImg(String hotelImg) {
        this.hotelImg = hotelImg;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public float getHotelPprice() {
        return hotelPprice;
    }

    public void setHotelPprice(float hotelPprice) {
        this.hotelPprice = hotelPprice;
    }

    public int getHotelDistance() {
        return hotelDistance;
    }

    public void setHotelDistance(int hotelDistance) {
        this.hotelDistance = hotelDistance;
    }

    public String getHotelAddr() {
        return hotelAddr;
    }

    public void setHotelAddr(String hotelAddr) {
        this.hotelAddr = hotelAddr;
    }
}

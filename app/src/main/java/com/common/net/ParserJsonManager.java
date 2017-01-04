package com.common.net;

import android.content.Context;
import android.text.TextUtils;

import com.common.app.ADModel;
import com.common.app.StaticMethod;
import com.common.app.VersionModel;
import com.common.app.degexce.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.StructorHotel;


/**
 * Created by Administrator on 2015/11/18.
 */
public class ParserJsonManager {
    private Context mContext;

    public ParserJsonManager(Context mContext) {
        this.mContext = mContext;
    }



    public JSONObject parserDataJsonObject(JSONObject jsonObject){
        if (jsonObject==null)
            return null;
        try {
            if (!jsonObject.isNull("info")&&!TextUtils.isEmpty(jsonObject.getString("info")))
                StaticMethod.showInfo(mContext, jsonObject.getString("info"));
            if (jsonObject.getInt("status")==1){
                return jsonObject.getJSONObject("data");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray parserDataJSONArray(JSONObject jsonObject){
        if (jsonObject==null)
            return null;
        try {
            if (!jsonObject.isNull("info")&&!TextUtils.isEmpty(jsonObject.getString("info")))
                StaticMethod.showInfo(mContext, jsonObject.getString("info"));
            if (jsonObject.getInt("status")==1&&
                    !(TextUtils.isEmpty(jsonObject.getString("data"))||
                            "null".equals(jsonObject.getString("data")))){
                    return jsonObject.getJSONArray("data");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    /**广告图
     *
     * @param type type=1 为首页的，2为专辑页面的.
     * */
    public ADModel parserAD(JSONArray jsonArray, int type){
        List<String> imgUrls=new ArrayList<String>();
        List<String> imgIds=new ArrayList<String>();
        ADModel adModel=new ADModel();
        adModel.setImgIds(imgIds);
        adModel.setImgUrls(imgUrls);
        if (jsonArray==null)
            return adModel;
        /**
         * {
         "id": "1",
         "type": "2",
         "pic": "http://lmpic.img-cn-shanghai.aliyuncs.com/160121/160121161423_rkgtpj.jpg",
         "href": "#"
         }*/
        JSONObject item;
        try {
            for (int i=0;i<jsonArray.length();i++){
                item=jsonArray.getJSONObject(i);
                if (item.getInt("type")==type){
                    imgUrls.add(item.getString("pic"));
                    imgIds.add(item.getString("id"));
                }
            }
            adModel.setImgIds(imgIds);
            adModel.setImgUrls(imgUrls);
        } catch (JSONException e) {
            e.printStackTrace();
            return adModel;
        }

        return  adModel;
    }

    /**版本更新*/
    public boolean parserVersionJsonObject(JSONObject jsonObject){
        VersionModel versionModel=new VersionModel();

        if (jsonObject==null)
            return false;
        L.MyLog("版本：", jsonObject.toString() + "");
        /**
         * {"version":"12","description":"bug fix","url":"http:\/\/www.baidu.com"}*/
        try {
            if (jsonObject.getInt("status")!=1)
                return false;
            jsonObject=jsonObject.getJSONObject("data");
            int versor = jsonObject.getInt("version");
            String apk_url = jsonObject.getString("url");
//            apk_url="http://filelx.liqucn.com/upload/2014/yizhi/ms.ttxxl_daiji_lq_20140804.apk";
            String info = jsonObject.getString("description");
            versionModel.setNetDesc(info);
            versionModel.setNetUrl(apk_url);
            versionModel.setNetVersion(versor);
            L.MyLog("当前版本：", versionModel.getCurrentVersion() + "");

            return true;

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


    }


    /**商品*/
    public List<StructorHotel> parserHotels(JSONArray hotelsData){
        if (hotelsData==null||hotelsData.length()<1)
            return null;
        List<StructorHotel> hotels=new ArrayList<StructorHotel>();
        StructorHotel hotel;
        JSONObject item;
        for (int i=0;i<hotelsData.length();i++){
            try {
                item=hotelsData.getJSONObject(i);
                hotel=new StructorHotel();
                hotel.setHotelImg(item.getString("img"));
                hotel.setUserId(item.getInt("uid"));
                hotel.setHotelId(item.getInt("id"));
                hotel.setHotelName(item.getString("name"));
                hotel.setHotelPprice((float) item.getDouble("price"));
                hotel.setHotelAddr(item.getString("address"));
                if (!item.isNull("distance"))
                    hotel.setHotelDistance(item.getInt("distance"));
                hotels.add(hotel);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return hotels;
    }

}

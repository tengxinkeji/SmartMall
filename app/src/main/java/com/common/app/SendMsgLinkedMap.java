package com.common.app;

import com.common.app.degexce.L;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/8/12.
 */

public class SendMsgLinkedMap {
    private String uid=MyApplication.getInstance().getUid();
    private String deviceId;

    public SendMsgLinkedMap(String deviceId) {
        this.deviceId = deviceId;
    }

    public synchronized void refreshDeviceId(String deviceIdHex) {
        deviceId=deviceIdHex;
//        SharePreferenceUtil.putString(Common.DEV_bind_idhex,deviceIdHex) ;
        L.MyLog("设备id:",deviceId+"");
    }

    /**登录*/
    public LinkedHashMap<String,Object> getLoginMap(){
        LinkedHashMap<String,Object> mapLogin=new LinkedHashMap<>();
        mapLogin.put(Common.KEY_type, Common.VALUE_type_regster);
        mapLogin.put(Common.KEY_sendId, uid);
        mapLogin.put(Common.KEY_psw, SharePreferenceUtil.getString( Common.User_psw ,""));

        return mapLogin;
    }

    /**心跳包*/
    public LinkedHashMap<String,Object> getHeartMap()  {
        LinkedHashMap<String,Object> heartMap=new LinkedHashMap<>();
        heartMap.put(Common.KEY_type, Common.VALUE_type_send);
        heartMap.put(Common.KEY_sendId, uid);
        heartMap.put(Common.KEY_toId, deviceId);
        heartMap.put(Common.KEY_sendMsg, "hello.world!");

        return heartMap;
    }
    /***/
    public LinkedHashMap<String,Object> getSendMap(String msg){
        LinkedHashMap<String,Object>  sendMap=getHeartMap();
        sendMap.put(Common.KEY_sendMsg, msg);

        return sendMap;
    }

    public String toString(LinkedHashMap<String,Object> linkedHashMap) {
        try {
            JSONStringer stringer = new JSONStringer();
            stringer.object();
            Iterator it = linkedHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                stringer.key(String.valueOf(e.getKey())).value(e.getValue());
            }

            stringer.endObject();
            return stringer.toString();
        } catch (JSONException e) {
            return null;
        }
    }
}

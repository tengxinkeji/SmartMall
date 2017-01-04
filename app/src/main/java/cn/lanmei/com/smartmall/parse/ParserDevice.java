package cn.lanmei.com.smartmall.parse;

import com.common.app.MyApplication;
import com.common.datadb.DBDeviceListManager;
import com.common.net.BaseParser;
import com.demo.smarthome.device.Dev;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserDevice extends BaseParser<List<Dev>> {
    @Override
    public List<Dev> parserJson(String responseString) {
        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<Dev> devList=new ArrayList<>();
            if (jsonObject.getInt("status")==1){

                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return null;
                JSONArray data= (JSONArray) obj;
                Dev dev;
                JSONObject temp;
                MyApplication.devBindClean();
                DBDeviceListManager deviceListManager = new DBDeviceListManager(MyApplication.getInstance());
                deviceListManager.delUid();
                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    dev=new Dev();
                    dev.setRecordId(temp.getInt("id")+"");
                    dev.setId(temp.getString("device_id"));
                    dev.setIdHex(temp.getString("device_no"));
                    dev.setNickName(temp.getString("device_name"));
                    dev.setDevType(temp.getString("device_type"));
                    dev.setDevTypeName(temp.getString("device_type_name"));
                    dev.setMaster(temp.getInt("master"));
                    dev.setDevPic(temp.getString("pic"));
                    dev.setDevVersion(temp.getString("version"));
                    dev.setPass(temp.getString("password"));
                    String addtime=temp.getString("addtime");
                    dev.setAddtime(addtime.substring(0,addtime.indexOf(" ")));

                    deviceListManager.addDev(dev);
                    if (dev.getMaster()==0){
                        devList.add(dev);
                    }else{
                        devList.add(0,dev);
                    }

                }
                MyApplication.putDevsBind(devList);
            }
            return devList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_DevSub;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserDeviceSub extends BaseParser<List<M_DevSub>> {
    @Override
    public List<M_DevSub> parserJson(String responseString) {
        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<M_DevSub> devList=new ArrayList<>();
            if (jsonObject.getInt("status")==1){

                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return null;
                JSONArray data= (JSONArray) obj;
                M_DevSub dev;
                JSONObject temp;

                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    dev=new M_DevSub();
                    dev.setId(temp.getInt("id"));
                    dev.setNickname(temp.getString("nickname"));
                    dev.setImg(temp.getString("pic"));
                    dev.setUsername(temp.getString("username"));
                    String addtime=temp.getString("addtime");
                    if (addtime.length()>=10)
                        dev.setAddTime(addtime.substring(0,10));
                    dev.setUserId(temp.getString("uid"));

                    dev.setDev(temp.getString("device_id"),
                            temp.getString("device_no"),
                            temp.getString("device_name"),
                            temp.getInt("device_type"),
                            temp.getString("password"),
                            temp.getInt("master"));
                    if (dev.getDev().getMaster()==1)
                        devList.add(0,dev);
                    else
                        devList.add(dev);
                }
            }
            return devList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

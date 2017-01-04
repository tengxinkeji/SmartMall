package cn.lanmei.com.smartmall.parse;

import com.common.app.MyApplication;
import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserDeviceType extends BaseParser<Map<Integer,String>> {
    @Override
    public Map<Integer,String> parserJson(String responseString) {
        try {
            JSONObject jsonObject=new JSONObject(responseString);
            Map<Integer,String> map=new HashMap<>();
            if (jsonObject.getInt("status")==1){

                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return null;
                JSONArray data= (JSONArray) obj;
                JSONObject temp;
                MyApplication.devTypeClean();
                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);

                    MyApplication.putDevType(temp.getInt("id"),temp.getString("name"));

                }
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

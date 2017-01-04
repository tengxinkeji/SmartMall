package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_address;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserAddress extends BaseParser<List<M_address>> {
    @Override
    public List<M_address> parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<M_address> list=new ArrayList<>();
            if (jsonObject.getInt("status")==1){
                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return list;
                JSONArray data= (JSONArray) obj;
                M_address mAddress;
                JSONObject temp;

                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    mAddress=new M_address();
                    mAddress.setId(temp.getInt("id"));
                    mAddress.setIsDefault(temp.getInt("default"));
                    mAddress.setAccept_name(temp.getString("accept_name"));
                    mAddress.setAddress(temp.getString("address"));
                    mAddress.setProvince(temp.getString("province"));
                    mAddress.setCity(temp.getString("city"));
                    mAddress.setArea(temp.getString("area"));
                    mAddress.setMobile(temp.getString("mobile"));
                    mAddress.setTelphone(temp.getString("telphone"));
                    mAddress.setZip(temp.getString("zip"));
                    list.add(mAddress);
                }

            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

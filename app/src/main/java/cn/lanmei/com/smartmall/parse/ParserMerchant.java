package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_merchant;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserMerchant extends BaseParser<List<M_merchant>> {
    @Override
    public List<M_merchant> parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<M_merchant> list=new ArrayList<>();
            if (jsonObject.getInt("status")==1){
                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return list;
                JSONArray data= (JSONArray) obj;
                M_merchant mMerchant;
                JSONObject temp;

                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    mMerchant=new M_merchant();
                    mMerchant.setId(temp.getInt("id"));
                    mMerchant.setUid(temp.getInt("uid"));
                    mMerchant.setName(temp.getString("name"));
                    mMerchant.setPic(temp.getString("pic"));
                    mMerchant.setAddress(temp.getString("addr"));
                    mMerchant.setDistance(temp.getString("distance"));
                    mMerchant.setType(temp.getString("type"));
                    mMerchant.setTel(temp.getString("tel"));
                    list.add(mMerchant);
                }

            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

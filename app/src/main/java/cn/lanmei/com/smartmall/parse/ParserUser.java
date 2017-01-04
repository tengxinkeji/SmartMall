package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_user;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserUser extends BaseParser<List<M_user>> {
    @Override
    public List<M_user> parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<M_user> list=new ArrayList<>();
            if (jsonObject.getInt("status")==1){
                JSONArray obj = jsonObject.optJSONArray("data");
                if (obj==null)
                    return list;

                M_user user;
                JSONObject temp;
                for(int i=0;i<obj.length();i++){
                    temp=obj.getJSONObject(i);
                    user=new M_user();
                    user.setId(temp.getInt("id"));
                    user.setName(temp.getString("nickname"));
                    user.setUrlHead(temp.getString("pic"));
                    list.add(user);
                }

            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

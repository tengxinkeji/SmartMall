package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lanmei.com.smartmall.model.M_ServerHxUser;


/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserHXserver extends BaseParser<M_ServerHxUser> {
    @Override
    public M_ServerHxUser parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);

            if (jsonObject.getInt("status")==1) {
                jsonObject=jsonObject.optJSONObject("data");
                if (jsonObject!=null){
                    M_ServerHxUser mServerHxUser = new M_ServerHxUser();
                    mServerHxUser.setUid(jsonObject.getString("hx_user"));
                    mServerHxUser.setNickName(jsonObject.getString("nickname"));
                    mServerHxUser.setPicUrl(jsonObject.getString("pic"));
                    return mServerHxUser;
                }
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

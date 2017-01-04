package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserJson extends BaseParser<JSONObject> {
    @Override
    public JSONObject parserJson(String responseString) {
        try {
            return new JSONObject(responseString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

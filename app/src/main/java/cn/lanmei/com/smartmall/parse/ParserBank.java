package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_bank;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserBank extends BaseParser<List<M_bank>> {
    @Override
    public List<M_bank> parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<M_bank> list=new ArrayList<>();
            if (jsonObject.getInt("status")==1){
                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return list;
                JSONArray data= (JSONArray) obj;
                M_bank mLog;
                JSONObject temp;

                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    mLog=new M_bank();
                    mLog.setId(temp.getInt("id"));
                    mLog.setBanks_name(temp.getString("banks_name"));
                    mLog.setBanks_id(temp.getInt("banks_id"));
                    mLog.setBanks_no(temp.getString("banks_no"));
                    mLog.setRealname(temp.getString("realname"));
                    list.add(mLog);
                }

            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.lanmei.com.smartmall.model.M_log;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserLog extends BaseParser<M_log[]> {
    @Override
    public M_log[] parserJson(String responseString) {


        try {
            JSONObject jsonObject=new JSONObject(responseString);
            M_log[] mLogs=new M_log[0];
            if (jsonObject.getInt("status")==1){
                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return mLogs;
                JSONArray data= (JSONArray) obj;

                JSONObject temp;

                int count=data.length();
                mLogs=new M_log[count];
                for(int i=0;i<count;i++){
                    temp=data.getJSONObject(i);
                    M_log mLog=new M_log();
                    mLog.setId(temp.getInt("id"));
                    mLog.setTime(temp.getLong("addtime")*1000);
                    mLog.setBalance(temp.getDouble("balance"));
                    mLog.setMoney(temp.getDouble("money"));
                    mLog.setTitle(temp.getString("recode_info"));
                    mLog.setUserImg(temp.getString("pic"));
                    mLogs[i]=mLog;

                }

            }
            return mLogs;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

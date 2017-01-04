package cn.lanmei.com.smartmall.parse;

import com.common.app.MyApplication;
import com.common.datadb.DBErrManager;
import com.common.net.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserErrCode extends BaseParser<String> {
    @Override
    public String parserJson(String responseString) {
        try {
            JSONObject jsonObject=new JSONObject(responseString);
            Object obj = jsonObject.get("fault");
            boolean o=  obj instanceof JSONObject;
            if (!o)
                return null;
            DBErrManager errManager=new DBErrManager(MyApplication.getInstance());
            errManager.delAll();
            JSONObject fault= (JSONObject) obj;
            Iterator<String> it =  fault.keys();

            while (it.hasNext()) {
                String myKey = it.next().toString();
                errManager.addErr(myKey,fault.optString(myKey));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package cn.lanmei.com.smartmall.parse;

import android.content.ContentValues;

import com.common.datadb.DBManagerCategory;
import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserGoodsCategory extends BaseParser<List<ContentValues>> {
    @Override
    public List<ContentValues> parserJson(String responseString) {
        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<ContentValues> goodsCategories=new ArrayList<>();
            if (jsonObject.getInt("status")==1){

                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return null;
                JSONArray data= (JSONArray) obj;
                ContentValues values;
                JSONObject temp;

                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    values=new ContentValues();
                    values.put(DBManagerCategory.TAGLE_id,temp.getInt("id"));
                    values.put(DBManagerCategory.TAGLE_name,temp.getString("name"));
                    values.put(DBManagerCategory.TAGLE_pic,temp.getString("pic"));
                    values.put(DBManagerCategory.TAGLE_parent_id,temp.getInt("parent_id"));
                    values.put(DBManagerCategory.TAGLE_model_id,temp.getInt("model_id"));
                    values.put(DBManagerCategory.TAGLE_recommend,temp.getInt("recommend"));
                    values.put(DBManagerCategory.TAGLE_sort,temp.getInt("sort"));
                    values.put(DBManagerCategory.TAGLE_visibility,temp.getInt("visibility"));

                    goodsCategories.add(values);

                }

            }
            return goodsCategories;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

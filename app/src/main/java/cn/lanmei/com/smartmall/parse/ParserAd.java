package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_ad;
import cn.lanmei.com.smartmall.model.M_ad_item;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserAd extends BaseParser<M_ad> {
    @Override
    public M_ad parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);

            if (jsonObject.getInt("status")==1){
                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return null;
                JSONArray data= (JSONArray) obj;
                JSONObject temp;
                List<Integer> imgIds=new ArrayList<>();
                List<String> imgUrls=new ArrayList<>();
                List<M_ad_item> adItems=new ArrayList<>();
                M_ad_item item;
                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    imgIds.add(temp.getInt("id"));
                    imgUrls.add(temp.getString("pic"));
                    item = new M_ad_item();
                    item.setId(temp.getInt("id"));
                    item.setCategory_id(temp.getInt("category_id"));
                    item.setClassid(temp.getInt("classid"));
                    item.setLink(temp.getString("link"));
                    item.setPic(temp.getString("pic"));
                    adItems.add(item);
                }
                return new M_ad(imgIds,imgUrls,adItems);
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

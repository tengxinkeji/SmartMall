package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_ring;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserRing extends BaseParser<List<M_ring>> {
    @Override
    public List<M_ring> parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<M_ring> list=new ArrayList<>();
            if (jsonObject.getInt("status")==1){
                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return list;
                JSONArray data= (JSONArray) obj;
                M_ring ring;
                JSONObject temp;

                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    ring=new M_ring();
                    ring.setId(temp.getInt("id"));
                    ring.setName(temp.getString("name"));
                    ring.setUrl(temp.getString("pic"));
                    ring.setDetail("");
                    ring.setPost_count(temp.getInt("post_count"));
                    ring.setReviews_count(temp.getInt("reviews_count"));

                    list.add(ring);
                }

            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

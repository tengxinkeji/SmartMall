package cn.lanmei.com.smartmall.parse;

import com.common.app.MyApplication;
import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_Review;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserReview extends BaseParser<List<M_Review>> {
    public int count = 0 ;
    @Override
    public List<M_Review> parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<M_Review> list=new ArrayList<>();
            if (jsonObject.getInt("status")==1){
                count=jsonObject.getInt("count");
                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return list;
                JSONArray data= (JSONArray) obj;
                M_Review review;
                JSONObject temp;
                MyApplication.devBindClean();
                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    review=new M_Review();
                    review.setId(temp.getInt("id"));
                    review.setContent(temp.getString("contents"));
                    review.setGoodsTime(temp.getString("time"));
                    if (!temp.isNull("point"))
                        review.setHeart(temp.getInt("point"));

                    review.setImg(temp.getString("pic"));
                    review.setName(temp.getString("nickname"));
//                    review.setLike(temp.getInt(""));
                    list.add(review);
                }

            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

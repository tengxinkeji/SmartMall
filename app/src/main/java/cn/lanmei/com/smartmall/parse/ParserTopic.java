package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_topic;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserTopic extends BaseParser<List<M_topic>> {
    @Override
    public List<M_topic> parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<M_topic> list=new ArrayList<>();
            if (jsonObject.getInt("status")==1){
                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return list;
                JSONArray data= (JSONArray) obj;
                M_topic topic;
                JSONObject temp;

                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    topic=new M_topic();
                    topic.setId(temp.getInt("id"));
                    topic.setTopic(temp.getString("title"));
                    topic.setTopicName(temp.getString("nickname"));
                    topic.setTopicUrl(temp.getString("pic"));
                    topic.setTopicAddr("");
                    topic.setTopicTime(temp.getLong("addtime"));


                    topic.setRecommend(temp.getInt("recommend"));

                    topic.setTopicTypeName(temp.getString("cname"));
                    topic.setTopic_2(temp.getInt("reviews"));
                    topic.setTopic_3(temp.getInt("view"));
                    switch (temp.getInt("type")){
                        case 2:
                            JSONArray file=temp.optJSONArray("file");
                            if (file!=null){
                                for (int m=0;m<file.length();m++){
                                    topic.addImg(2,file.getString(m));
                                }
                            }

                            break;
                    }

                    list.add(topic);
                }

            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_Goods;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserGoods extends BaseParser<List<M_Goods>> {
    @Override
    public List<M_Goods> parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<M_Goods> list=new ArrayList<>();
            if (jsonObject.getInt("status")==1){
                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return list;
                JSONArray data= (JSONArray) obj;
                M_Goods goods;
                JSONObject temp;

                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    goods=new M_Goods();
                    goods.setRecordId(temp.getInt("id"));
                    goods.setGoodsStoreId(temp.getInt("uid"));
                    goods.setGoodsName(temp.getString("name"));
                    goods.setCost_price(temp.getDouble("cost_price"));
//                    goods.setMarket_price(temp.getDouble("market_price"));
                    goods.setSell_price(temp.getDouble("sell_price"));
                    goods.setGoodsSale(temp.getInt("sale"));
                    goods.setGoodsComments(temp.getInt("comments"));
                    goods.setGoodsImg(temp.getString("img"));
                    if (!temp.isNull("brand_name"))
                        goods.setGoodsBrand(temp.getString("brand_name"));


                    list.add(goods);
                }

            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.model.M_follow;
import cn.lanmei.com.smartmall.model.M_merchant;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserFollow extends BaseParser<List<M_follow>> {
    @Override
    public List<M_follow> parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<M_follow> list=new ArrayList<>();
            if (jsonObject.getInt("status")==1){
                JSONArray obj = jsonObject.optJSONArray("data");
                if (obj==null)
                    return list;
                JSONObject fItem;
                for (int m=0;m<obj.length();m++){
                    fItem = obj.getJSONObject(m);
                    M_follow m_follow=new M_follow();
                    M_merchant  mMerchant=new M_merchant();
                    mMerchant.setId(fItem.getInt("id"));
                    mMerchant.setUid(fItem.getInt("uid"));
                    mMerchant.setName(fItem.getString("name"));
                    mMerchant.setPic(fItem.getString("pic"));
                    mMerchant.setAddress(fItem.getString("addr"));
                    mMerchant.setTel(fItem.getString("tel"));
                    m_follow.setmMerchant(mMerchant);

                    if (fItem.isNull("goods")){
                        list.add(m_follow);
                        continue;
                    }
                    JSONArray goodsArr=  fItem.getJSONArray("goods");
                    M_Goods goods;
                    JSONObject temp;
                    List<M_Goods> listgoods=new ArrayList<>();
                    for(int i=0;i<goodsArr.length();i++){
                        temp=goodsArr.getJSONObject(i);
                        goods=new M_Goods();
                        goods.setRecordId(temp.getInt("id"));
                        goods.setGoodsStoreId(temp.getInt("uid"));
                        goods.setGoodsName(temp.getString("name"));
                        goods.setCost_price(temp.getDouble("cost_price"));
                        goods.setMarket_price(temp.getDouble("market_price"));
                        goods.setSell_price(temp.getDouble("sell_price"));
                        goods.setGoodsImg(temp.getString("img"));
                        if (!temp.isNull("brand_name"))
                            goods.setGoodsBrand(temp.getString("brand_name"));
                        listgoods.add(goods);
                    }
                    m_follow.setmGoodsList(listgoods);
                    list.add(m_follow);
                }


            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

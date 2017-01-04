package cn.lanmei.com.smartmall.parse;

import com.common.net.BaseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.model.M_order;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ParserOrder extends BaseParser<List<M_order>> {
    @Override
    public List<M_order> parserJson(String responseString) {

        try {
            JSONObject jsonObject=new JSONObject(responseString);
            List<M_order> list=new ArrayList<>();
            if (jsonObject.getInt("status")==1){
                Object obj = jsonObject.get("data");
                boolean o=  obj instanceof JSONArray;
                if (!o)
                    return list;
                JSONArray data= (JSONArray) obj;
                M_order mOrder;
                JSONObject temp;

                for(int i=0;i<data.length();i++){
                    temp=data.getJSONObject(i);
                    mOrder=new M_order();
                    mOrder.setId(temp.getInt("id"));
                    mOrder.setStoreId(temp.getInt("mid"));
                    mOrder.setPayType(temp.getInt("pay_type"));
                    mOrder.setStoreName(temp.getString("name"));
                    mOrder.setOrderNo(temp.getString("order_no"));
                    mOrder.setOrderMoney(temp.getDouble("order_amount"));
                    mOrder.setOrderStauts(temp.getInt("status"));
                    mOrder.setOrderPayStauts(temp.getInt("pay_status"));
                    mOrder.setMobile(temp.getString("mobile"));
                    JSONArray product = temp.optJSONArray("product");
                    List<M_Goods> m_goodsList=new ArrayList<>();
                    M_Goods mGoods;
                    JSONObject mJson;
                    double money=0.0;
                    if (product!=null){
                        for(int m=0;m<product.length();m++){
                            mJson=product.optJSONObject(m);
                            if (mJson==null)
                                continue;
                            mGoods=new M_Goods();
                            mGoods.setRecordId(mJson.getInt("goods_id"));
                            mGoods.setGoods_id(mJson.getInt("id"));
                            mGoods.setCost_price(mJson.getDouble("real_price"));
                            mGoods.setMarket_price(mJson.getDouble("real_price"));
                            mGoods.setSell_price(mJson.getDouble("real_price"));
                            mGoods.setGoodsImg(mJson.getString("img"));
                            mGoods.setProducts_img(mJson.getString("img"));
                            mGoods.setGoodsName("name");
                            mGoods.setGoodsStoreName(mOrder.getStoreName());
                            mGoods.setGoodsStoreId(mOrder.getStoreId());
                            money+=mGoods.getGoodsPrice();
                            JSONArray spec_array = mJson.optJSONArray("spec_array");
                            if (spec_array!=null){
                                StringBuffer buf=new StringBuffer();
                                JSONObject spec;
                                for (int n=0;n<spec_array.length();n++){
                                    spec = spec_array.optJSONObject(n);
                                    if (spec==null)
                                        continue;
                                    buf.append(spec.getString("name")+":"+spec.getString("value")+"\n");
                                }
                                mGoods.setSpec(buf.toString());
                            }

                            m_goodsList.add(mGoods);
                        }
                    }

                    mOrder.setOrderNum(m_goodsList.size());
                    mOrder.setOrderMoney(money);
                    mOrder.setProduct(m_goodsList);
                    list.add(mOrder);
                }

            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

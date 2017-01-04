package cn.lanmei.com.smartmall.goods;


import android.os.Bundle;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.StaticMethod;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lanmei.com.smartmall.myui.htmltext.OnHtmlImgListener;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.myui.htmltext.HtmlTextView;


/**
 *
 */
public class F_goods_params_server extends BaseScrollFragment implements OnHtmlImgListener{
    private String TAG="F_goods_params_detail";
    private int goodsRecordId;
    private HtmlTextView txtGoodsDetail;


    public static F_goods_params_server newInstance(int goodsRecordId) {
        F_goods_params_server fragment = new F_goods_params_server();
        Bundle bundle = new Bundle();
        bundle.putInt("goodsRecordId",goodsRecordId);
//        bundle.putInt("goodsId",6);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void init() {
        if (getArguments()!=null){
            goodsRecordId=getArguments().getInt("goodsRecordId");
        }
    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_goodsparams_server);


    }

    @Override
    public void findViewById() {
        txtGoodsDetail= (HtmlTextView) findViewById(R.id.txt_info);

    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

        requestServerData();
    }

    @Override
    public void requestServerData() {
        RequestParams requestParams=new RequestParams(NetData.ACTION_Shop_goods_details);
        requestParams.addParam("id",goodsRecordId);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {

            }

            @Override
            public void processData(JSONObject parserData) {
                parserInfo(parserData);
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();

            }
        });
    }

    private void parserInfo(JSONObject result) {

        if (result!=null){
            try {
                if (result.getInt("status")==1){
                    result=result.optJSONObject("data");
                    if (result==null)
                        return;
                    String contentStr = result.getString("service");
                    txtGoodsDetail.setHtmlFromString(contentStr,false,this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            StaticMethod.showInfo(mContext,"刷新失败");
        }
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }

    @Override
    public void OnHtmlImgListener(int position) {

    }
}

package cn.lanmei.com.smartmall.goods;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ScrollView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.Activity_pic;
import cn.lanmei.com.smartmall.myui.htmltext.HtmlTextView;
import cn.lanmei.com.smartmall.myui.htmltext.OnHtmlImgListener;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *
 */
public class F_goods_params_detail extends BaseScrollFragment implements OnHtmlImgListener{
    private String TAG="F_goods_params_detail";
    private int goodsRecordId;
    private HtmlTextView txtGoodsDetail;


    public static F_goods_params_detail newInstance(int goodsRecordId) {
        F_goods_params_detail fragment = new F_goods_params_detail();
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
        setContentView(R.layout.layout_goodsparams_detail);


    }

    @Override
    public void findViewById() {
        txtGoodsDetail= (HtmlTextView) findViewById(R.id.txt_info);
       /* String html = "<html><head><title>TextView使用HTML</title></head><body><p><strong>强调</strong></p><p><em>斜体</em></p>"
                +"<p><a href=\"http://www.dreamdu.com/xhtml/\">超链接HTML入门</a>学习HTML!</p><p><font color=\"#aabb00\">颜色1"
                +"</p><p><font color=\"#00bbaa\">颜色2</p><h1>标题1</h1><h3>标题2</h3><h6>标题3</h6><p>大于>小于<</p><p>" +
                "下面是网络图片</p><img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/></body></html>";
        txtGoodsDetail.setHtmlFromString(html,false);*/
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
                    String contentStr = result.getString("content");
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
        L.MyLog(TAG,"图片点击："+position);
        ArrayList<String> list=new ArrayList<>();
        for(int i=0;i<txtGoodsDetail.getImgs().size();i++){
            list.add(txtGoodsDetail.getImgs().get(i));


        }
        Intent toPic=new Intent(getActivity(), Activity_pic.class);
        toPic.putStringArrayListExtra(Common.KEY_listString,list);
        toPic.putExtra(Common.KEY_position,position);
        getActivity().startActivity(toPic);
    }
}

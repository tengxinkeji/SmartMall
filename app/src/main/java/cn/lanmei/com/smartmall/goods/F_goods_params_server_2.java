package cn.lanmei.com.smartmall.goods;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.app.Common;
import com.common.app.degexce.L;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.common.net.URLRequest;

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
public class F_goods_params_server_2 extends Fragment implements OnHtmlImgListener{
    private String TAG="F_goods_params_detail";
    private int goodsRecordId;
    private HtmlTextView txtGoodsDetail;


    public static F_goods_params_server_2 newInstance(int goodsRecordId) {
        F_goods_params_server_2 fragment = new F_goods_params_server_2();
        Bundle bundle = new Bundle();
        bundle.putInt("goodsRecordId",goodsRecordId);
//        bundle.putInt("goodsId",6);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            goodsRecordId=getArguments().getInt("goodsRecordId");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_goodsparams_detail_2,container,false);
        txtGoodsDetail= (HtmlTextView)view. findViewById(R.id.txt_info);
        requestServerData();
        return view;
    }




    public void requestServerData() {
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... params) {
                RequestParams requestParams=new RequestParams(NetData.ACTION_Shop_goods_details);
                requestParams.addParam("id",goodsRecordId);
                requestParams.setBaseParser(new ParserJson());
                JSONObject result= (JSONObject) URLRequest.requestByGet(requestParams);
                if (result!=null){
                    try {
                        if (result.getInt("status")==1){
                            result=result.optJSONObject("data");
                            if (result==null)
                                return null;
                            String contentStr = result.getString("service");
                            return contentStr;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                parserInfo(s);
            }
        }.execute();

    }

    private void parserInfo(String html) {

        if (html==null)
            return;
        txtGoodsDetail.setHtmlFromString(html,false,this);
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

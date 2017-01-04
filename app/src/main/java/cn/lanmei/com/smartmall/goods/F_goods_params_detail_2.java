package cn.lanmei.com.smartmall.goods;


import android.content.Context;
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
public class F_goods_params_detail_2 extends Fragment implements OnHtmlImgListener{
    private String TAG="F_goods_params_detail";
    private Context mContext;
    private int goodsRecordId;
    private HtmlTextView txtGoodsDetail;


    public static F_goods_params_detail_2 newInstance(int goodsRecordId) {
        F_goods_params_detail_2 fragment = new F_goods_params_detail_2();
        Bundle bundle = new Bundle();
        bundle.putInt("goodsRecordId",goodsRecordId);
//        bundle.putInt("goodsId",6);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        if (getArguments()!=null){
            goodsRecordId=getArguments().getInt("goodsRecordId");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_goodsparams_detail_2,null);
        txtGoodsDetail= (HtmlTextView)view. findViewById(R.id.txt_info);
        refresh();
       /* String html = "<html>\n" +
                "<head>\n" +
                "<title>TextView使用HTML</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p><strong>强调</strong></p>\n" +
                "<p><em>斜体</em></p>\n" +
                "<p><a href=\"http://www.dreamdu.com/xhtml/\">超链接HTML入门</a>学习HTML!</p>\n" +
                "<p><font color=\"#aabb00\">颜色1</p><p><font color=\"#00bbaa\">颜色2</p>\n" +
                "<h1>标题1</h1>\n" +
                "<h3>标题2</h3>\n" +
                "<h6>标题3</h6><p>大于>小于<</p>\n" +
                "<p>下面是网络图片</p>\n" +
                "<img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/>\n" +
                "<p>下面是网络图片" +"<img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/></p>\n" +
                "<p>下面是网络图片" +"<img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/></p>\n" +
                "<p>下面是网络图片" +"<img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/></p>\n" +
                "<p>下面是网络图片" +"<img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/></p>\n" +
                "<p>下面是网络图片" +"<img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/></p>\n" +
                "</body>\n" +
                "</html> "; ;
                txtGoodsDetail.setHtmlFromString(html,false,this);*/
        return view;
    }



    private void refresh(){
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
                            String contentStr = result.getString("content");
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

package cn.lanmei.com.smartmall.goods;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.StaticMethod;
import com.common.myinterface.DataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.adapter.MyBaseAdapter;
import cn.lanmei.com.smartmall.model.M_goods_params;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.R;


/**
 *
 */
public class F_goods_params_params extends BaseScrollFragment {
    private String TAG="F_goods_params_detail";
    private int goodsRecordId;
    private MyListView myListView;
    private AdapterParams adapterParams;


    public static F_goods_params_params newInstance(int goodsRecordId) {
        F_goods_params_params fragment = new F_goods_params_params();
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
        adapterParams=new AdapterParams(mContext,new ArrayList<M_goods_params>());
    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_goodsparams_params);


    }

    @Override
    public void findViewById() {
        myListView= (MyListView) findViewById(R.id.listview);
        myListView.setAdapter(adapterParams);
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
                    JSONArray attr = result.optJSONArray("attr");
                    if (attr==null)
                        return;
                    List<M_goods_params> goodsParamses=new ArrayList<>();
                    JSONObject jsonObject;
                    for(int i=0;i<attr.length();i++){
                        jsonObject=attr.optJSONObject(i);
                        goodsParamses.add(new M_goods_params(jsonObject.getString("name"),jsonObject.getString("value")));
                    }
                    adapterParams.refreshData(goodsParamses);
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

    public class AdapterParams extends MyBaseAdapter<M_goods_params> {

        public AdapterParams(Context mContext, List<M_goods_params> mLists) {
            super(mContext,mLists);

        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView=inflater.inflate(R.layout.layout_item_params,parent,false);
                viewHolder.txtTag= (TextView) convertView.findViewById(R.id.txt_key);
                viewHolder.txtInfo= (TextView) convertView.findViewById(R.id.txt_value);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.txtTag.setText(getItem(position).getKey());
            viewHolder.txtInfo.setText(getItem(position).getValue());
            return convertView;
        }

        protected class ViewHolder{
            public TextView txtTag;
            public TextView txtInfo;


        }
    }
}

package cn.lanmei.com.smartmall.goods;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.common.net.URLRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.MyBaseAdapter;
import cn.lanmei.com.smartmall.model.M_goods_params;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *
 */
public class F_goods_params_params_2 extends Fragment {
    private String TAG="F_goods_params_detail";
    private Context mContext;
    private int goodsRecordId;
    private MyListView myListView;
    private AdapterParams adapterParams;


    public static F_goods_params_params_2 newInstance(int goodsRecordId) {
        F_goods_params_params_2 fragment = new F_goods_params_params_2();
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
        adapterParams=new AdapterParams(mContext,new ArrayList<M_goods_params>());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_goodsparams_params_2,container,false);
        myListView= (MyListView)view.findViewById(R.id.listview);
        myListView.setAdapter(adapterParams);
        requestServerData();
        return view;
    }




    public void requestServerData() {
        new AsyncTask<Void,Void,List<M_goods_params>>(){
            @Override
            protected List<M_goods_params> doInBackground(Void... params) {
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
                            JSONArray attr = result.optJSONArray("attr");
                            if (attr==null)
                                return null;
                            List<M_goods_params> goodsParamses=new ArrayList<>();
                            JSONObject jsonObject;
                            for(int i=0;i<attr.length();i++){
                                jsonObject=attr.optJSONObject(i);
                                goodsParamses.add(new M_goods_params(jsonObject.getString("name"),jsonObject.getString("value")));
                            }

                            return goodsParamses;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<M_goods_params> s) {
                super.onPostExecute(s);
                if (s!=null)
                    adapterParams.refreshData(s);
            }
        }.execute();

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

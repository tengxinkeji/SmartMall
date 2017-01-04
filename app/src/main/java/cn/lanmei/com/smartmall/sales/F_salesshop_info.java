package cn.lanmei.com.smartmall.sales;


import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.MyApplication;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.RoundImageView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterSales;
import cn.lanmei.com.smartmall.main.BaseActionActivity;
import cn.lanmei.com.smartmall.model.M_user;


/**
 *维修商列表
 *
 */
public class F_salesshop_info extends BaseScrollFragment {

    private String TAG="F_list_sales_one";

    private RoundImageView imgHead;
    private TextView txtName;
    private TextView txtAddTime;
    private TextView txtNum;
    private TextView txtCall;
    private TextView txtAddr;

    private Resources res;

    private int id;
    private int p=1;
    private List<M_user> mUsers;
    private AdapterSales adapterSales;


    public static F_salesshop_info newInstance(int id) {
        F_salesshop_info f=new F_salesshop_info();
        Bundle data=new Bundle();
        data.putInt("id",id);
        f.setArguments(data);
        return f;
    }

    @Override
    public  void init(){

        res = mContext.getResources();
        tag = "分销";
        mUsers=new ArrayList<>();
        adapterSales=new AdapterSales(mContext,mUsers,1);
        if (getArguments()!=null){
            id=getArguments().getInt("id");
        }
    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_salesshop_info);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            initHead();
        }
    }
    private void initHead(){
        ((BaseActionActivity)getActivity()).txtRight.setVisibility(View.GONE);

    }

    @Override
    public void findViewById() {
        initHead();
        imgHead= (RoundImageView) findViewById(R.id.img_head);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtAddTime = (TextView) findViewById(R.id.txt_addtime);
        txtNum = (TextView) findViewById(R.id.txt_num);
        txtCall = (TextView) findViewById(R.id.txt_call);
        txtAddr = (TextView) findViewById(R.id.txt_addr);

    }

    @Override
    public void requestServerData() {
        p=1;
        requestList();
    }


    /**刷新*/
    public void requestList() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_Member_index);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("mid",id);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new SimpleDataCallBack<JSONObject,Integer>(p){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, JSONObject parserData) {
                super.processData(obj, parserData);
                try {
                    if (parserData.getInt("status")==1){
                        JSONObject data=parserData.optJSONObject("data");
                        if (data==null)
                            return;
                        refreshImg(data.getString("pic"));
                        txtName.setText(data.getString("realname"));
                        txtName.setSelected(data.getInt("sex")==1);
                        txtAddTime.setText(data.getString("distributor_time"));
                        txtNum.setText(data.getString("sub_count"));
                        txtCall.setText(data.getString("phone"));
                        txtAddr.setText(data.getString("address"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                mPullRefreshScrollView.onRefreshComplete();
            }
        });

    }



    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        p=1;
        requestList();
    }

    public void refreshImg(String result) {

        BaseActivity baseActivity;
        if (!TextUtils.isEmpty(result)){
            baseActivity =(BaseActivity) getActivity();
            baseActivity.getImageLoader().displayImage(
                    result, imgHead, baseActivity.options, new SimpleImageLoadingListener());
        }

    }


}

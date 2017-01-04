package cn.lanmei.com.smartmall.my;


import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.common.app.BaseScrollFragment;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.location.BaiduLocation;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.myui.MyInputEdit;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *完善资料
 */
public class F_complete_info extends BaseScrollFragment {
    private String TAG="F_complete_info";
    private MyInputEdit uiLocation;
    private ImageView imgLocation;
    private MyInputEdit uiCount;
    private MyInputEdit uiIncomming;

    private LocationClient mBdLocationClient;
    private BDLocation bdLocation;
    public static F_complete_info newInstance() {
        F_complete_info fragment = new F_complete_info();

        return fragment;
    }


    @Override
    public void init() {

    }

    @Override
    public void loadViewLayout() {
        tag = "完善资料";
        setContentView(R.layout.layout_complete_info);

    }

    @Override
    public void findViewById() {
        uiLocation= (MyInputEdit) findViewById(R.id.txt_info_location);
        imgLocation= (ImageView) findViewById(R.id.img_location);
        uiCount= (MyInputEdit) findViewById(R.id.txt_info_count);
        uiIncomming= (MyInputEdit) findViewById(R.id.txt_info_incomming);

        initLocation();
    }

    private void initLocation(){
        new BaiduLocation((BaseActivity) getActivity(), new BaiduLocation.WHbdLocaionListener() {
            @Override
            public void bdLocaionListener(LocationClient mLocationClient, BDLocation location) {
                mBdLocationClient=mLocationClient;
                bdLocation=location;
                if (location!=null){
                    uiLocation.setText(location.getProvince()+location.getCity());
                    mBdLocationClient.stop();
                }
            }
        });
        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBdLocationClient!=null)
                    mBdLocationClient.start();
            }
        });
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        requestServerData();
    }

    @Override
    public void requestServerData() {
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_index);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {

            @Override
            public void onPre() {
                   startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                if (parserData!=null){
                    try {

                        if (parserData.getInt("status")==1){
                            JSONObject data=parserData.optJSONObject("data");
                            if (data!=null){
                                uiCount.setText(data.getString("family_size"));
                                uiIncomming.setText(data.getString("household_income"));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    StaticMethod.showInfo(mContext,"请求失败");
                }
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
                stopProgressDialog();
            }
        });
    }

    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        if (result!=null){
            try {
                StaticMethod.showInfo(mContext,result.getString("info"));
                if (result.getInt("status")==1){

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            StaticMethod.showInfo(mContext,"请求失败");
        }
    }

    public void save(){
        if (bdLocation==null){
            StaticMethod.showInfo(mContext,"百度定位失败");
            return;
        }
        String family_size=uiCount.getText().toString();
        if (TextUtils.isEmpty(family_size)){
            StaticMethod.showInfo(mContext,"请输入家庭人数");
            return;
        }

        String household_income=uiIncomming.getText().toString();
        if (TextUtils.isEmpty(household_income)){
            StaticMethod.showInfo(mContext,"请输入家庭年收入");
            return;
        }
        RequestParams requestParams=new RequestParams(NetData.ACTION_user_update);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("address", bdLocation.getAddrStr());
        requestParams.addParam("family_size", family_size);
        requestParams.addParam("household_income", household_income);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {

            @Override
            public void onPre() {
                startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                    parserInfo(parserData);
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
                stopProgressDialog();
            }
        });
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }
}

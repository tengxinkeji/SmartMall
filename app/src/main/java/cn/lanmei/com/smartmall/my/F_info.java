package cn.lanmei.com.smartmall.my;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterString;
import cn.lanmei.com.smartmall.dialog.PopWindow_List;
import cn.lanmei.com.smartmall.goods.Activity_manager_address;
import cn.lanmei.com.smartmall.location.BaiduLocation;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.myui.MyInputEdit;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *完善资料
 */
public class F_info extends BaseScrollFragment {
    private String TAG="F_info";
    private MyInputEdit uiName;
    private MyInputEdit uiSex;
    private MyInputEdit uiCall;
    private TextView txtBindCall;
    private EditText uiLocation;
    private ImageView imgLocation;

    private TextView txtAddrManage;


    public static F_info newInstance() {
        F_info fragment = new F_info();

        return fragment;
    }


    @Override
    public void init() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Common.requestCode_bind_phone&&resultCode==Common.resultCode_bind_phone){
            uiCall.setText(SharePreferenceUtil.getString(Common.User_phone,""));
        }
    }

    @Override
    public void loadViewLayout() {
        tag = "个人资料";
        setContentView(R.layout.layout_info);

    }

    @Override
    public void findViewById() {
        uiLocation= (EditText) findViewById(R.id.txt_info_location);
        imgLocation= (ImageView) findViewById(R.id.img_location);
        uiName= (MyInputEdit) findViewById(R.id.txt_info_name);
        uiSex= (MyInputEdit) findViewById(R.id.txt_info_sex);
        uiCall= (MyInputEdit) findViewById(R.id.txt_info_call);
        txtBindCall= (TextView) findViewById(R.id.txt_bind);
        txtAddrManage= (TextView) findViewById(R.id.txt_addr_manage);
        uiSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopSex(uiSex.input);
            }
        });

        txtBindCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new DF_bind_phone(new DF_bind_phone.ResultBindListener() {
//                    @Override
//                    public void onResultBind(String phone) {
//                        uiCall.setText(phone);
//                    }
//                }).show(getChildFragmentManager(),"DF_bind_phone");
                Intent toBindPhone=new Intent(getActivity(),Activity_modify_phone.class);
                getActivity().startActivityForResult(toBindPhone, Common.requestCode_bind_phone);
            }
        });

        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLocation();
            }
        });

        txtAddrManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddress=new Intent(getActivity(),Activity_manager_address.class);
                getActivity().startActivity(toAddress);
            }
        });
    }

    private void initLocation(){
        new BaiduLocation((BaseActivity) getActivity(), new BaiduLocation.WHbdLocaionListener() {
            @Override
            public void bdLocaionListener(LocationClient mLocationClient, BDLocation location) {
                if (location!=null){
                    uiLocation.setText(location.getProvince()+location.getCity());
                    mLocationClient.stop();
                }
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
                                uiName.setText(data.getString("nickname"));
                                uiSex.setText(data.getInt("sex")==1?"男":"女");
                                uiCall.setText(data.getString("phone"));
                                uiLocation.setText(data.getString("address"));
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




        RequestParams requestParams=new RequestParams(NetData.ACTION_user_update);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        if (!TextUtils.isEmpty(uiName.getText().toString()))
            requestParams.addParam("nickname", uiName.getText().toString());
        if (!TextUtils.isEmpty(uiLocation.getText().toString()))
            requestParams.addParam("address", uiLocation.getText().toString());
        boolean isMan=TextUtils.equals(uiSex.getText().toString(),"男");
        requestParams.addParam("sex", isMan?1:2);

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

    /**
     * 类型
     * */
    public void showPopSex(View v){
        List<String> lists=new ArrayList<String>();
        lists.add("男");
        lists.add("女");

        new PopWindow_List<AdapterString>(mContext,
                new AdapterString(mContext, lists),
                new PopWindow_List.PopWindowItemClick() {
                    @Override
                    public void onPopWindowItemClick(int position) {

                        if (position==0){
                            uiSex.setText("男");
                        }else {
                            uiSex.setText("女");
                        }

                    }
                },
                StaticMethod.dip2px(mContext,160)).showPopupWindow(v);
    }
}

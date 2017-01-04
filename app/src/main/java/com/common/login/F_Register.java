package com.common.login;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.common.tools.AuthCode;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 * 注册
 */
public class F_Register extends BaseScrollFragment {
    private String TAG="F_Register";

    private EditText uiUser;
    private EditText uiPsw;
    private EditText uiPswSure;
    private EditText uiAuth;
    private EditText uiRePhone;

    private TextView txtAuth;
    private TextView txtRegister;

    private String userStr,pswStr;
    private String authStr;
    private Resources res;
    private Thread threadTimer;


    public static F_Register newInstance() {
        F_Register fragment = new F_Register();

        return fragment;
    }



    @Override
    public void init(){
        res=getResources();
        tag = res.getString(R.string.register);

    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_login_register);
    }

    @Override
    public void findViewById() {
        uiUser = (EditText) findViewById(R.id.rgt_phone);
        uiPsw = (EditText) findViewById(R.id.rgt_psw);
        uiPswSure = (EditText) findViewById(R.id.rgt_psw_sure);
        uiAuth = (EditText) findViewById(R.id.rgt_auth);
        uiRePhone = (EditText) findViewById(R.id.rgt_rephone);


        txtAuth = (TextView) findViewById(R.id.rgt_txt_auth);
        txtRegister = (TextView) findViewById(R.id.rgt_register);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

            }
        });


        txtAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAuth();
            }
        });

    }

    @Override
    public void requestServerData() {

    }



    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
       mOnFragmentInteractionListener.backFragment("F_Register");
    }

    /** 获取验证码*/
    private void refreshAuth(){
        userStr = uiUser.getText().toString();

        if (TextUtils.isEmpty(userStr)){
            StaticMethod.showInfo(mContext, res.getString(R.string.err_null_user));
            return;
        }

        RequestParams requestParams = new RequestParams(NetData.ACTION_user_search);
        requestParams.addParam("phone",userStr);

        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {

            }

            @Override
            public void processData(JSONObject parserData) {
                try {
                    if (parserData.getInt("status")==1){
                        StaticMethod.showInfo(mContext,res.getString(R.string.hint_mobile_has));
                        userStr="";
                        uiUser.setText("");
                    }else {
                        requestAuth();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    StaticMethod.showInfo(mContext,res.getString(R.string.err_net));
                }

            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**请求验证码*/
    private void requestAuth(){
        authStr=AuthCode.getInstance().createCode();

        RequestParams requestParams = new RequestParams(NetData.ACTION_send_sms);
        requestParams.addParam("phone",userStr);
        requestParams.addParam("content",authStr);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {
                authTimerStart();
            }

            @Override
            public void processData(JSONObject parserData) {
                paserAuth(parserData);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    txtAuth.setText(msg.arg1 + res.getString(R.string.second));
                    break;
                case 2:
                    txtAuth.setEnabled(true);
                    txtAuth.setText(res.getString(R.string.auth_get));
                    break;
                case 3:
                    txtAuth.setEnabled(false);
                    txtAuth.setText(90 + res.getString(R.string.second));
                    break;
            }
        }
    };

    private void authTimerStart(){
        if (threadTimer!=null){
            threadTimer.interrupt();
        }
        threadTimer=new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(3);
                for(int i=90;i>=0;i--){
                    try {
                        Message msg = mHandler.obtainMessage();
                        msg.what=1;
                        msg.arg1=i;
                        mHandler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        mHandler.sendEmptyMessage(2);
                    }
                }
                mHandler.sendEmptyMessage(2);
            }
        });
        threadTimer.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (threadTimer!=null){
            threadTimer.interrupt();
            threadTimer=null;
        }
    }

    private void register(){
        if (TextUtils.isEmpty(userStr)){
            StaticMethod.showInfo(mContext, res.getString(R.string.hint_mobile));
            return;
        }

        String inputAauthStr = uiAuth.getText().toString();
        if (TextUtils.isEmpty(inputAauthStr)){
            StaticMethod.showInfo(mContext, res.getString(R.string.hint_auth));
            return;
        }
        if (!authStr.equals(inputAauthStr)){
            StaticMethod.showInfo(mContext, res.getString(R.string.err_auth_sure));
            return;
        }

        pswStr = uiPsw.getText().toString();

        if (TextUtils.isEmpty(pswStr)){
            StaticMethod.showInfo(mContext, res.getString(R.string.err_null_psw));
            return;
        }
        if (pswStr.length()<6){
            String err=res.getString(R.string.err_psw);
            StaticMethod.showInfo(mContext,
                    String.format(err,6,pswStr.length()));
            return;
        }
        String temp=uiPswSure.getText().toString();
        if (TextUtils.isEmpty(temp)){
            StaticMethod.showInfo(mContext, res.getString(R.string.hint_psw_sure));
            return;
        }

        if (!temp.equals(pswStr)){
            StaticMethod.showInfo(mContext, res.getString(R.string.err_psw_sure));
            return;
        }

        String rephone = uiRePhone.getText().toString();
        RequestParams requestParams = new RequestParams(NetData.ACTION_regist);
        requestParams.addParam("phone",userStr);
        requestParams.addParam("password",pswStr);
        requestParams.addParam("rephone",rephone);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {
                txtRegister.setEnabled(false);
                startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                paserRegister(parserData);
            }

            @Override
            public void onComplete() {
                stopProgressDialog();
                txtRegister.setEnabled(true);
            }
        });


   }



    private void paserRegister( JSONObject jsonObject ){
        if (jsonObject==null){
            StaticMethod.showInfo(mContext,res.getString(R.string.err_rgs));
            return;
        }
        try {
            StaticMethod.showInfo(mContext,jsonObject.getString("info"));
            if (jsonObject.getInt("status")==1){
                SharePreferenceUtil.putBoolean(Common.KEY_is_login,false);
                SharePreferenceUtil.putString( Common.User_user ,userStr);
                SharePreferenceUtil.putString( Common.User_psw ,"");
                getActivity().startActivity(new Intent(getActivity(), LoginActionActivity.class));
                getActivity().finish();
            }
            L.MyLog("注册--Register",jsonObject.toString()+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void paserAuth(JSONObject jsonObject){
        L.MyLog(TAG,"发短信："+jsonObject.toString());
    }


}

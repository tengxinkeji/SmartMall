package cn.lanmei.com.smartmall.my;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
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
import cn.lanmei.com.smartmall.myui.MyInputEdit;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 * 修改密码
 */
public class F_modify_psw extends BaseScrollFragment {
    private String TAG="F_modify_psw";

    private MyInputEdit uiPhone;
    private MyInputEdit uiAuth;
    private MyInputEdit uiPsw;
    private MyInputEdit uiPswSure;

    private TextView txtAuth;
    private TextView txtRegister;

    private String userStr,pswStr;
    private String authStr;



    public static F_modify_psw newInstance() {
        F_modify_psw fragment = new F_modify_psw();

        return fragment;
    }



    @Override
    public void init(){
        tag = "密码修改";

    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_psw_modify);
    }

    @Override
    public void findViewById() {
        uiPhone = (MyInputEdit) findViewById(R.id.rgt_phone);
        uiPhone.setIsInput(false);
        userStr = SharePreferenceUtil.getString(Common.User_phone,null);
        if (!TextUtils.isEmpty(userStr)){
            uiPhone.setText(userStr);
        }
        uiAuth = (MyInputEdit) findViewById(R.id.rgt_auth);
        uiPsw = (MyInputEdit) findViewById(R.id.rgt_psw);
        uiPswSure = (MyInputEdit) findViewById(R.id.rgt_psw_sure);


        txtAuth = (TextView) findViewById(R.id.rgt_txt_auth);
        txtRegister = (TextView) findViewById(R.id.rgt_register);
        txtRegister.setText("提交");
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
//        userStr = uiPhone.getText().toString();

        if (TextUtils.isEmpty(userStr)){
            StaticMethod.showInfo(mContext, "请先完善个人资料");
            return;
        }
        requestAuth();

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


    private void authTimerStart(){
       new AsyncTask<Void, Integer, Boolean>() {
           @Override
            protected void onPreExecute() {
                super.onPreExecute();
                txtAuth.setEnabled(false);
                txtAuth.setText(90 + "秒");
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                for (int i = 90; i > 0; i--) {
                    publishProgress(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return true;
                    }
                }
                return true;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                txtAuth.setText(values[0] + "秒");
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                txtAuth.setEnabled(true);
                txtAuth.setText("获取验证码");
            }
        }.execute();
    }



    private void register(){
        if (TextUtils.isEmpty(userStr)){
            StaticMethod.showInfo(mContext, "请输入手机号");
            return;
        }

        String inputAauthStr = uiAuth.getText().toString();
        if (TextUtils.isEmpty(inputAauthStr)){
            StaticMethod.showInfo(mContext, "请输入验证码");
            return;
        }
        if (!authStr.equals(inputAauthStr)){
            StaticMethod.showInfo(mContext, "验证码有误，请重新输入");
            return;
        }

        pswStr = uiPsw.getText().toString();
        Resources res = getActivity().getResources();
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

        RequestParams requestParams = new RequestParams(NetData.ACTION_user_update);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("password",pswStr);
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
            StaticMethod.showInfo(mContext,"注册失败");
            return;
        }
        try {
            StaticMethod.showInfo(mContext,jsonObject.getString("info"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void paserAuth(JSONObject jsonObject){
        L.MyLog(TAG,"发短信："+jsonObject.toString());
    }


}

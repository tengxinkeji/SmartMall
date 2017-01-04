package com.common.login;


import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.datadb.DBGoodsCartManager;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.ParserJsonManager;
import com.common.net.RequestParams;
import com.demo.smarthome.device.Dev;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.parse.ParserDevice;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


/**
 * 登录
 */
public class F_Login extends BaseScrollFragment implements View.OnClickListener {
    private static final String TAG="F_Login";

    private EditText uiInputUser;
    private EditText uiInputPsw;
    private TextView login_forget;
    private TextView txtLogin;
    private TextView txtRegister;
    private ImageView imgLoginQQ;
    private ImageView imgLoginWX;


    private Resources res;
    private String userStr,pswStr;
    private boolean nullUser,nullPsw;
    private int loginType;

    private ParserJsonManager parserJsonManager;


    public static F_Login newInstance() {
        F_Login fragment = new F_Login();
        return fragment;
    }



    @Override
    public  void init(){

        res = mContext.getResources();
        tag = res.getString(R.string.login);
        parserJsonManager=new ParserJsonManager(mContext);
        ShareSDK.initSDK(getContext());

    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_login);
    }

    @Override
    public void findViewById() {

        uiInputUser = (EditText) findViewById(R.id.input_user);
        uiInputPsw = (EditText) findViewById(R.id.input_psw);

        login_forget = (TextView) findViewById(R.id.login_forget);
        txtLogin = (TextView) findViewById(R.id.login_login);
        txtRegister = (TextView) findViewById(R.id.login_register);
        imgLoginQQ = (ImageView) findViewById(R.id.login_qq);
        imgLoginWX = (ImageView) findViewById(R.id.login_wx);

        login_forget.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        imgLoginQQ.setOnClickListener(this);
        imgLoginWX.setOnClickListener(this);

        nullUser=true;
        nullPsw=true;

        uiInputUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0)
                    nullUser=false;
                else
                    nullUser=true;
                if (!nullUser&&!nullPsw){
                    txtLogin.setEnabled(true);
                }else {
                    txtLogin.setEnabled(false);
                }
            }
        });

        uiInputPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0)
                    nullPsw=false;
                else
                    nullPsw=true;
                if (!nullUser&&!nullPsw){
                    txtLogin.setEnabled(true);
                }else {
                    txtLogin.setEnabled(false);
                }
            }
        });


    }

    @Override
    public void requestServerData() {
        refresh();
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment("F_Login");
    }

    @Override
    public void setOnBarRight() {//注册
//        super.setOnBarRight();
        mOnFragmentInteractionListener.showFrament(LoginActionActivity.ToIntent_Register);
    }

    public void refresh(){
        userStr= SharePreferenceUtil.getString( Common.User_user,"");
        pswStr= SharePreferenceUtil.getString( Common.User_psw ,"");
        uiInputUser.setText(userStr);
        uiInputPsw.setText(pswStr);
    }
    private void login(){
        userStr = uiInputUser.getText().toString();
        pswStr = uiInputPsw.getText().toString();
        if (TextUtils.isEmpty(userStr)){
            StaticMethod.showInfo(mContext, res.getString(R.string.err_null_user));
            return;
        }
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

        RequestParams requestParams = new RequestParams(NetData.ACTION_login);
        requestParams.addParam("phone",userStr);
        requestParams.addParam("password",pswStr);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {
                txtLogin.setEnabled(false);
                startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                paserLogin(parserData);
            }

            @Override
            public void onComplete() {
                txtLogin.setEnabled(true);

            }
        });


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_login://登录

                login();
                break;
            case R.id.login_qq://登录

                qqLogin();
                break;
            case R.id.login_wx://登录

                wxLogin();
                break;
            case R.id.login_register://登录
                mOnFragmentInteractionListener.showFrament(LoginActionActivity.ToIntent_Register);
                break;

            case R.id.login_forget://忘记密码?
                mOnFragmentInteractionListener.showFrament(LoginActionActivity.ToIntent_find_psw);
                break;

        }
    }


    private void paserLogin( JSONObject jsonObject){
        if (jsonObject==null){
            StaticMethod.showInfo(mContext,res.getString(R.string.err_login));
            stopProgressDialog();
            return;
        }
        try {
            StaticMethod.showInfo(mContext, jsonObject.getString("info"));
            if (jsonObject.getInt("status")==1){
                SharePreferenceUtil.putString( Common.User_user ,userStr);
                SharePreferenceUtil.putString( Common.User_psw ,pswStr);
                JSONObject data = jsonObject.getJSONObject("data");
                MyApplication.getInstance().setUid(data.getString("id"));
                DBGoodsCartManager.uid=data.getString("id");
                int user_type=data.getInt("user_type");


                SharePreferenceUtil.putInt(Common.User_Type,user_type);
                SharePreferenceUtil.putString(Common.User_phone,data.getString("phone"));
                SharePreferenceUtil.putString(Common.User_name,data.getString("nickname"));
                SharePreferenceUtil.putString(Common.User_email,data.getString("email"));
                SharePreferenceUtil.putInt(Common.User_sex,data.getInt("sex"));
                SharePreferenceUtil.putString(Common.User_pic,data.getString("pic"));
                referRegistrationID();
                initMyDev();

            }else {
                stopProgressDialog();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            stopProgressDialog();
        }
    }

    /**提交极光id*/
    private void referRegistrationID(){
        final String rid = JPushInterface.getRegistrationID(mContext);
        if (TextUtils.isEmpty(rid)){
            L.MyLog(TAG,"极光id获取失败");
        }else{
            RequestParams requestParams=new RequestParams(NetData.ACTION_user_update);
            requestParams.addParam("uid", MyApplication.getInstance().getUid());
            requestParams.addParam("jpush_regid", rid);
            requestParams.setBaseParser(new ParserJson());
            getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
                @Override
                public void onPre() {

                }

                @Override
                public void processData(JSONObject info) {
                    L.MyLog(TAG,"极光id:"+rid+":---"+info.toString());
                }

                @Override
                public void onComplete() {
                }
            });
        }



    }

    private void initMyDev(){

        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.setBaseParser(new ParserDevice());
        getDataFromServer(requestParams, new DataCallBack<List<Dev>>() {
            @Override
            public void onPre() {

            }

            @Override
            public void processData(List<Dev> devices) {

            }

            @Override
            public void onComplete() {
                txtLogin.setEnabled(true);
                stopProgressDialog();
                mOnFragmentInteractionListener.showFrament(LoginActionActivity.ToIntent_Client);
            }
        });


    }

    private void thirdLogin(String open_id){
        RequestParams requestParams = new RequestParams(NetData.ACTION_login);
        requestParams.addParam("open_id",open_id);
        requestParams.addParam("open_type",loginType);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {
                txtLogin.setEnabled(false);
                startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                paserLogin(parserData);
            }

            @Override
            public void onComplete() {
                txtLogin.setEnabled(true);

            }
        });
    }

    /**QQ登录*/
    private void qqLogin(){
        loginType=2;
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        platform.setPlatformActionListener(platformActionListener);
        platform.showUser(null);
    }

    /**微信登录*/
    private void wxLogin(){
        loginType=1;
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.setPlatformActionListener(platformActionListener);
        platform.showUser(null);
    }

    PlatformActionListener platformActionListener=new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            //遍历Map
            Iterator ite =hashMap.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry entry = (Map.Entry)ite.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                L.MyLog(key.toString(),value.toString());
            }

            //用户资源都保存到res
            //通过打印res数据看看有哪些数据是你想要的i
            L.MyLog("action",""+i);
            if ( i== Platform.ACTION_USER_INFOR) {
                PlatformDb platDB = platform.getDb();//获取数平台数据DB
                //通过DB获取各种数据
                L.MyLog("getToken:",platDB.getToken());
                L.MyLog("getUserGender:",platDB.getUserGender());
                L.MyLog("getUserIcon:",platDB.getUserIcon());
                L.MyLog("getUserId:",platDB.getUserId());
                L.MyLog("getUserName:",platDB.getUserName());
                Message obt = mHandler.obtainMessage();
                obt.what=1;
                obt.obj=platDB;
                mHandler.sendMessage(obt);

            }
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            StaticMethod.showInfo(mContext,res.getString(R.string.err_login));
            throwable.printStackTrace();
        }

        @Override
        public void onCancel(Platform platform, int i) {
            StaticMethod.showInfo(mContext,res.getString(R.string.err_login_cancle));

        }
    };

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                thirdLogin(((PlatformDb)msg.obj).getUserId());
            }
        }
    };

}

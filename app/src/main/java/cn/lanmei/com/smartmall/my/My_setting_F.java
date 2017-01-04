package cn.lanmei.com.smartmall.my;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.VersionModel;
import com.common.app.degexce.L;
import com.common.app.sd.FileUtils;
import com.common.dialog.VersionDialog;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.ParserJsonManager;
import com.common.net.RequestParams;
import com.hyphenate.EMCallBack;
import com.hyphenate.chatuidemo.DemoHelper;

import org.json.JSONObject;

import java.io.File;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.MainActionActivity;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;


/**
 *设置
 */
public class My_setting_F extends BaseScrollFragment {
    private String TAG="My_setting_F";
    private TextView txtHelp;
    private LinearLayout layoutVersion;
    private TextView txtVersion;
    private TextView txtIdea;
    private TextView txtQuit;

    private ParserJsonManager parserJsonManager;
    private Resources res;
    private Bundle data;

    private VersionModel versionModel;

    public static My_setting_F newInstance() {
        My_setting_F fragment = new My_setting_F();

        return fragment;
    }



    @Override
    public void init(){
        parserJsonManager=new ParserJsonManager(mContext);
        res = mContext.getResources();
        tag = "设置";
        ShareSDK.initSDK(mContext);
        data = getArguments();
        if (data!=null) {

        }
        versionModel=new VersionModel();
    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_setting);
    }

    @Override
    public void findViewById() {
        txtHelp=(TextView) findViewById(R.id.txt_help);
        layoutVersion=(LinearLayout) findViewById(R.id.my_setting_version);
        txtVersion=(TextView) findViewById(R.id.my_setting_currentV);
        txtIdea=(TextView) findViewById(R.id.txt_idea);
        txtQuit=(TextView) findViewById(R.id.quit);


        txtQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitRegistrationID();
//                Platform platform = ShareSDK.getPlatform(QQ.NAME);
//                platform.removeAccount(true);
                Platform platform = ShareSDK.getPlatform(Wechat.NAME);
                platform.removeAccount(true);
                logout();
                MyApplication.getInstance().setUid(null);
                SharePreferenceUtil.putBoolean(Common.KEY_is_login,false);
                Intent toHome=new Intent(getActivity(), MainActionActivity.class);
                getActivity().startActivity(toHome);
                getActivity().finish();
            }
        });
        txtVersion.setText(StaticMethod.getVersionName(mContext)+"");

        layoutVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestVersion();
            }
        });
    }

    @Override
    public void requestServerData() {

    }

    public void requestVersion(){
        RequestParams requestParams = new RequestParams(NetData.ACTION_version);
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

    private void parserInfo(JSONObject parserData) {
        boolean isSuccess=parserJsonManager.parserVersionJsonObject(parserData);
        if (isSuccess)//请求成功
            versionModel.setRequestTime();

        if (versionModel.getCurrentVersion()>=versionModel.getNetVersion()){
            StaticMethod.showInfo(mContext,"已是最新版本！");
           return;
        }

        if(versionModel.isUpdateApk(versionModel.getNetVersion())){
            File apk=versionModel.getApkFile();
            VersionDialog versionDialog=new VersionDialog();
            PackageInfo apkInfo = FileUtils.getAPKinfo(apk);
            if (apk!=null&&apkInfo!=null&&
                    apkInfo.versionCode>=versionModel.getNetVersion()){   //本地安装
                versionDialog.refreshDialog(versionModel, true, null);
            }else{//网络下载
                versionDialog.refreshDialog(versionModel, false, null);
            }
            versionDialog.show(getActivity().getSupportFragmentManager(),"VersionDialog");

        }
    }



    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }

    /**退出极光id*/
    private void quitRegistrationID(){
            RequestParams requestParams=new RequestParams(NetData.ACTION_user_update);
            requestParams.addParam("uid", MyApplication.getInstance().getUid());
            requestParams.addParam("jpush_regid", 0);
            requestParams.setBaseParser(new ParserJson());
            getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
                @Override
                public void onPre() {

                }

                @Override
                public void processData(JSONObject info) {
                    L.MyLog(TAG,"账号:"+MyApplication.getInstance().getUid()+"退出极光:---"+info.toString());
                }

                @Override
                public void onComplete() {
                }
            });




    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(false,new EMCallBack() {

            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // show login screen
                       StaticMethod.showInfo(mContext,"退出成功");

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(getActivity(), "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

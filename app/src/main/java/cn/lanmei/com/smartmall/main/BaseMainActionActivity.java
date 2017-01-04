package cn.lanmei.com.smartmall.main;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.common.app.Common;
import com.common.app.StaticMethod;
import com.common.app.VersionModel;
import com.common.app.sd.FileUtils;
import com.common.dialog.VersionDialog;
import com.common.net.NetData;
import com.common.net.NetWorkUtil;
import com.common.net.ParserJsonManager;
import com.common.net.RequestParams;
import com.common.net.URLRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.lanmei.com.smartmall.my.F_my;
import cn.lanmei.com.smartmall.R;

/**
 * Created by Administrator on 2016/7/25.
 */
public abstract class BaseMainActionActivity extends BaseActionActivity {
    private VersionModel versionModel;
    public ParserJsonManager parserJsonManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parserJsonManager=new ParserJsonManager(BaseMainActionActivity.this);
        initUpdateAPK();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Common.NONE:
            case Common.PHOTO_PICK:
            case Common.PHOTO_GRAPH:
            case Common.PHOTO_RESOULT:
                F_my f_my=(F_my)fm.findFragmentByTag("F_my");
                if (f_my!=null){
                    f_my.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }



    }

    private void initUpdateAPK(){
        if (NetWorkUtil.netWorkConnection()){

            new AsyncTask<Void,Void,Boolean>(){
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    versionModel =new VersionModel();
                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    if (versionModel.isUpdateRequest()){
                        //  String update="http://app1.seacc.net/Index/update?key=abb&l=zh";
                        //  new Thread(new URLRequest(update,mHandler, 1)).start();
                        RequestParams urlModel = new RequestParams(NetData.ACTION_version);//
                        String result = URLRequest.requestByGet(urlModel.getRequestUrl());

                        try {
                            if (result==null)
                                return false;
                            JSONObject jsonObject=new JSONObject(result.toString());
                            boolean isSuccess=parserJsonManager.parserVersionJsonObject(jsonObject);
                            if (isSuccess)//请求成功
                                versionModel.setRequestTime();

                            boolean b=versionModel.isUpdateApk(versionModel.getNetVersion());
                            return b;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if(aBoolean){
                        File apk=versionModel.getApkFile();
                        VersionDialog versionDialog=new VersionDialog();
                        PackageInfo apkInfo = FileUtils.getAPKinfo(apk);
                        if (apk!=null&&apkInfo!=null&&
                                apkInfo.versionCode>=versionModel.getNetVersion()){   //本地安装
                            versionDialog.refreshDialog(versionModel, true, null);
                        }else{//网络下载
                            versionDialog.refreshDialog(versionModel, false, null);
                        }
                        versionDialog.show(fm,"VersionDialog");

                    }
                }
            }.execute();





        } else {
            StaticMethod.showInfo(BaseMainActionActivity.this, getResources().getString(R.string.net_err));
        }
    }



    @Override
    public void showFrament(int casePositon) {
        showFrament(casePositon,null);
    }

}

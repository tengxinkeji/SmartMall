package com.common.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.common.net.URLRequest;
import com.demo.smarthome.device.Dev;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.MainActionActivity;
import cn.lanmei.com.smartmall.parse.ParserDevice;
import cn.lanmei.com.smartmall.parse.ParserDeviceType;

import static android.content.ContentValues.TAG;


/**
 * @desc 功能：使用ViewPager实现初次进入应用时的引导页
 * 
 * (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * (2)是，则进入引导activity；否，则进入MainActivity
 * (3)5s后执行(2)操作
 * 
 * @author
 *
 */
public class SplashActivity extends Activity {
	//****************************************************************
    // 判断应用是否初次加载，
    //****************************************************************
	private static final int PERMISSION_SETTING_REQ_CODE=2;
    private static final int sleepTime =5000;
	private long start;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_splash);

		start = System.currentTimeMillis();
		toMain();


    }

	private void getMyDevs(){
		String uid = MyApplication.getInstance().getUid();
		if (TextUtils.isEmpty(uid)){
			toMain();
		}else{
			new AsyncTask<Void,Void,Boolean>(){
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					RequestParams requestParams=new RequestParams(NetData.ACTION_device_type);
					requestParams.setBaseParser(new ParserDeviceType());
					URLRequest.requestByGet(requestParams);

					requestParams=new RequestParams(NetData.ACTION_Member_device);
					requestParams.addParam("uid", MyApplication.getInstance().getUid());
					requestParams.setBaseParser(new ParserDevice());
					List<Dev> devices = (List<Dev>) URLRequest.requestByGet(requestParams);
					if (devices==null||devices.size()<1){
						return false;
					}else{
						return true;
					}

				}

				@Override
				protected void onPostExecute(Boolean aBoolean) {
					super.onPostExecute(aBoolean);
					toMain();
				}
			}.execute();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		toMainNext();
	}

	public void toMain(){
    	new Thread(new Runnable() {
			public void run() {
				long costTime = System.currentTimeMillis() - start;
				//等待sleeptime时长
				if (sleepTime - costTime > 0) {
					try {
						Thread.sleep(sleepTime - costTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				mHander.sendEmptyMessage(1);

			}
		}).start();
    	
    	
    }

	private Handler mHander=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==1){
				if (checkSettingAlertPermission(SplashActivity.this,PERMISSION_SETTING_REQ_CODE)){
					toMainNext();
				};

			}
		}
	};


	private void toMainNext(){
		boolean launch_nostart = SharePreferenceUtil.getBoolean(Common.KEY_launch_nostart, false);
		if (!launch_nostart){
			startActivity(new Intent(SplashActivity.this, LaunchActivity.class));
		}else{
			startActivity(new Intent(SplashActivity.this, MainActionActivity.class));
			/*boolean isLogin= SharePreferenceUtil.getBoolean(Common.KEY_is_login,false);
			if (isLogin){
				startActivity(new Intent(SplashActivity.this, MainActionActivity.class));
			}else{//登录
				startActivity(new Intent(SplashActivity.this, LoginActionActivity.class));
			}*/
		}
		finish();
	}

	/**
	 * 检测系统弹出权限
	 * @param cxt
	 * @param req
	 * @return
	 */
	@TargetApi(23)
	public static boolean checkSettingAlertPermission(Object cxt, int req) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
			return true;
		}
		if (cxt instanceof Activity) {
			Activity activity = (Activity) cxt;
			if (!Settings.canDrawOverlays(activity.getBaseContext())) {
				Log.i(TAG, "Setting not permission");

				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
						Uri.parse("package:" + activity.getPackageName()));
				activity.startActivityForResult(intent, req);
				return false;
			}
		} else if (cxt instanceof Fragment) {
			Fragment fragment = (Fragment) cxt;
			if (!Settings.canDrawOverlays(fragment.getActivity())) {
				Log.i(TAG, "Setting not permission");

				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
						Uri.parse("package:" + fragment.getActivity().getPackageName()));
				fragment.startActivityForResult(intent, req);
				return false;
			}
		} else {
			throw new RuntimeException("cxt is net a activity or fragment");
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PERMISSION_SETTING_REQ_CODE) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (Settings.canDrawOverlays(this)) {
					toMainNext();
				} else {
					StaticMethod.showInfo(this, "请开启\"在其他应用上层显示\"权限");
					toMainNext();
				}
			}
		}
	}
   
  
}
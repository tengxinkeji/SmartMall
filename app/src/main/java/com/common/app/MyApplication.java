/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.common.app;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.FragmentActivity;

import com.common.app.degexce.L;
import com.demo.smarthome.device.Dev;
import com.easemob.redpacketsdk.RedPacket;
import com.espressif.iot.esptouch.EsptouchResult;
import com.hyphenate.chatuidemo.DemoHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.lanmei.com.smartmall.R;


/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class MyApplication extends MultiDexApplication {
	private String TAG="MyApplication";

	public static Context applicationContext;
	private static MyApplication instance;

	// login user name
	public final String PREF_USERNAME = "username";

	/**
	 * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
	 */
	public static String currentUserNick = "";
	public static String hxServer = "";

	public static final boolean DEVELOPER_MODE = true;



	private int placeholder= R.drawable.img_logo;
	private List<FragmentActivity> recordsFA = new ArrayList<>();

	public final static int  DEV_UDP_SEND_PORT=8943;//2468
	public final static int  DEV_UDP_SEND_DELAY=100;
	public final static int  DEV_UDP_READ_DELAY=15; //udp 扫描等待15秒

	private static List<Dev> listDevScan=new  ArrayList<Dev>();//扫描的设备
	private static List<Dev> listDevBind=new  ArrayList<Dev>();//绑定的设备
	private static Map<String,EsptouchResult> wifiDevMap=new HashMap<String,EsptouchResult>();//扫描的wifi
	private static String currentWifiKey="";

	private static Map<Integer,String> mDeviceType=new HashMap<>();



	@Override
	public void onCreate() {

		super.onCreate();
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
		MultiDex.install(this);
		//init demo helper
		applicationContext=this;
		instance=this;

		//init demo helper
		DemoHelper.getInstance().init(applicationContext);
		//red packet code : 初始化红包上下文，开启日志输出开关
		RedPacket.getInstance().initContext(applicationContext);
		RedPacket.getInstance().setDebugMode(true);
		//end of red packet code


		initImageLoader(applicationContext);

		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);     		// 初始化 JPush
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	public static MyApplication getInstance(){
		if (instance!=null)
			return instance;
		return null;
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}


	public int getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(int placeholder) {
		this.placeholder = placeholder;
	}

	public void addActvity(FragmentActivity activity) {
		recordsFA.add(activity);
		L.MyLog(TAG, activity.getClass().getName()+"---Current Acitvity Size :" + getCurrentActivitySize());
	}

	public void removeActvity(FragmentActivity activity) {
		recordsFA.remove(activity);
		L.MyLog(TAG, activity.getClass().getName()+"---Current Acitvity Size :" + getCurrentActivitySize());
	}

	public void exit() {
		for (FragmentActivity activity : recordsFA) {
			activity.finish();
		}
	}

	public int getCurrentActivitySize() {
		return recordsFA.size();
	}


	public String getUid() {
		return SharePreferenceUtil.getString("myUid","0");
	}

	public void setUid(String uid) {
		SharePreferenceUtil.putString("myUid",uid);
	}


	public static Dev getDevBindByIdHex(String id){
		Dev dev = null;
		L.MyLog("listDevScan:",listDevBind.size()+"");
		for (Dev d : listDevBind)
		{
			if (d.getIdHex().equals(id))
			{
				dev = d;
				break;
			}
		}
		return dev;
	}

	public static Dev getDevScanByIdHex(String id){
		Dev dev = null;
		L.MyLog("已绑定设备:",listDevBind.size()+"");
		for (Dev d : listDevBind)
		{
			if (d.getIdHex().equals(id))
			{
				dev = d;
				break;
			}
		}
		L.MyLog("已扫描设备:",listDevScan.size()+"");
		for (Dev d : listDevScan)
		{
			if (d.getIdHex().equals(id))
			{
				dev = d;
				break;
			}
		}
		return dev;
	}

	public static List<Dev> getListDevBind() {
		return listDevBind;
	}

	public static void putDevsBind(List<Dev> devList)
	{
		devBindClean();
		listDevBind.addAll(devList);

		return ;
	}
	public static void putDevBind(Dev dev)
	{
		if(dev == null)
		{
			return;
		}

		for (Dev d : listDevBind)
		{
			if (d.getId().equals(dev.getId()))
			{
				return;
			}
		}
		listDevBind.add(dev);

		return ;
	}

	public static void putDevScan(Dev dev)
	{
		if(dev == null)
		{
			return;
		}

		for (Dev d : listDevScan)
		{
			if (d.getId().equals(dev.getId()))
			{
				return;
			}
		}
		listDevScan.add(dev);

		return ;
	}

	public static void devBindClean()
	{
		listDevBind.clear();
		return ;
	}

	public static void devScanClean()
	{
		listDevScan.clear();
		return ;
	}

	public static void devTypeClean()
	{
		mDeviceType.clear();
		return ;
	}
	public static void putDevType(Integer id,String name)
	{
		mDeviceType.put(id,name);
		return ;
	}
	public static String getDevType(Integer id)
	{
		if (mDeviceType.containsKey(id))
		 return mDeviceType.get(id);
		return "";
	}

	public static int getScanWifiCount(){
		return wifiDevMap.size();
	}
	public static boolean wifiBssidcontainsKey(String bssid){
		return wifiDevMap.containsKey(bssid);
	}
	public static void putWifiDevScan(String bssid,EsptouchResult esptouchResult){
		wifiDevMap.put(bssid,esptouchResult);
	}

	public static Map<String, EsptouchResult> getWifiDevScan(){
		return wifiDevMap;
	}

	public static void wifiDevScanClean()
	{
		wifiDevMap.clear();
		return ;
	}

	public static String getCurrentWifiKey() {
		return currentWifiKey;
	}

	public static void setCurrentWifiKey(String currentWifiKey) {
		MyApplication.currentWifiKey = currentWifiKey;
	}
}
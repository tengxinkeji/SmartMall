package com.common.net;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.common.app.MyApplication;

public class NetWorkUtil {
    /**
     * 判断有无网络 true有网 反正 返回false
     * @return
     */
	public static boolean netWorkConnection()
	{
    	ConnectivityManager manager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || (null != networkinfo && !networkinfo.isAvailable())) 
		{
			return false;
		}else{
			return true;
		}
    }
	
	/**
	 * 网络类型
	 *
	 * @return
	 */
	public static boolean isWifi()
	{
		boolean isWifi = false;
		ConnectivityManager manager = (ConnectivityManager)  MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo != null && networkinfo.isAvailable()) 
		{
			if(networkinfo.getType() == ConnectivityManager.TYPE_WIFI)
			{
				isWifi = true;
			}
		}
		return isWifi;
	}



}

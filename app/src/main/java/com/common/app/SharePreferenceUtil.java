package com.common.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharePreferenceUtil {
    public static final int mode= Context.MODE_PRIVATE;
    public static final String FILE_INFO="info_YK_video" ;


	
	public static void putString(String key, String value)
	{
		SharedPreferences pref = MyApplication.getInstance().getSharedPreferences(FILE_INFO, mode);
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getString(String key, String defaultValue)
	{
		SharedPreferences pref = MyApplication.getInstance().getSharedPreferences(FILE_INFO, mode);
		return pref.getString(key, defaultValue);
	}
	
	public static void putLong(String key, long value)
	{
		SharedPreferences pref = MyApplication.getInstance().getSharedPreferences(FILE_INFO, mode);
		Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public static long getLong(String key, long defaultValue)
	{
		SharedPreferences pref = MyApplication.getInstance().getSharedPreferences(FILE_INFO, mode);
		return pref.getLong(key, defaultValue);
	}
	
	public static boolean putBoolean(String key, boolean value)
	{
		SharedPreferences pref = MyApplication.getInstance().getSharedPreferences(FILE_INFO, mode);
		Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
		return value;
	}
	
	public static boolean getBoolean(String key, boolean defaultValue)
	{
		SharedPreferences pref = MyApplication.getInstance().getSharedPreferences(FILE_INFO, mode);
		return pref.getBoolean(key, defaultValue);
	}
	
	public static int getInt(String key, int defaultValue)
	{
		SharedPreferences pref = MyApplication.getInstance().getSharedPreferences(FILE_INFO, mode);
		return pref.getInt(key, defaultValue);
	}
	public static void putInt(String key, int value)
	{
		SharedPreferences pref = MyApplication.getInstance().getSharedPreferences(FILE_INFO, mode);
		Editor editor = pref.edit();
		editor.putInt(key, value);
		
		editor.commit();
	}
}

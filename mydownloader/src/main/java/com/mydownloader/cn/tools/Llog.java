package com.mydownloader.cn.tools;

import android.util.Log;

/**
 * Created by Administrator on 2016/6/30.
 */
public class Llog {
    public static boolean isPrint=true;

    public static void print(String tag,String msg){
        if (isPrint)
            Log.i(tag, msg);
    }

}

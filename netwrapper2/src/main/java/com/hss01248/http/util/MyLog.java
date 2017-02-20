package com.hss01248.http.util;

import android.util.Log;

/**
 * Created by Administrator on 2017/1/7 0007.
 */
public class MyLog {
    private static  final String TAG = "httputil";

   /* private static Logger logger = init();

    private static Logger init() {
        Logger logger =  Logger.getLogger(MyLog.class);
       // PropertyConfigurator.configure("I:\\dev\\Spark\\lib\\log4j.properties");
        PropertyConfigurator.configure("D:\\progects\\java\\jsoup\\config\\log4j.properties");
        return logger;
    }*/



    public static void d(Object obj){
        if(HttpUtil.isDebug)
        Log.d(TAG,obj.toString());
    }
    public static void e(Object obj){
        if(HttpUtil.isDebug)
        Log.e(TAG,obj.toString());
    }
    public static void i(Object obj){
        if(HttpUtil.isDebug)
        Log.i(TAG,obj.toString());
    }
    public static void json(Object obj){
        if(HttpUtil.isDebug)
        Log.d(TAG,MyJson.toJsonStr(obj));
    }

}

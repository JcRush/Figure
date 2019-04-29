package com.example.figurechatcommunity.util;

import android.util.Log;


public class LogUtils {
    
    
    private static  String TAG = "@@";
    
    public  static void i(String text){
        Log.e(TAG,text);
    }
    public  static void d(String text){
        Log.e(TAG,text);
    }


    public  static void d(String tag,String text){
        Log.e(TAG,text);
    }

    public  static void w(String text){
        Log.e(TAG,text);
    }

    public  static void e(String text){
        Log.e(TAG,text);
    }
}

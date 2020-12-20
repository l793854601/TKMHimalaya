package com.tkm.himalaya.utils;

import android.util.Log;

/**
 * 日志打印工具类
 */
public class LogUtil {

    /**
     * LogUtil内部Tag
     */
    private static String sTAG = "LogUtil";

    /**
     * 是否要输出日志
     */
    private static boolean sIsRelease = false;

    /**
     * 初始化LogUtil
     * @param tag
     * @param isRelease
     */
    public static void init(String tag, boolean isRelease) {
        sTAG = tag;
        sIsRelease = isRelease;
    }

    public static void d(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + TAG, content);
        }
    }

    public static void v(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + TAG, content);
        }
    }

    public static void i(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + TAG, content);
        }
    }

    public static void w(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + TAG, content);
        }
    }

    public static void e(String TAG, String content) {
        if (!sIsRelease) {
            Log.d("[" + sTAG + "]" + TAG, content);
        }
    }
}

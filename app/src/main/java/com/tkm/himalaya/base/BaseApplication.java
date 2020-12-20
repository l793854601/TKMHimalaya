package com.tkm.himalaya.base;

import android.app.Application;

import com.tkm.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //  初始化喜马拉雅SDK
        CommonRequest ximalaya = CommonRequest.getInstanse();
        ximalaya.setAppkey("1ae9b7ce1572d71a2fc4011498c4cb2d");
        ximalaya.setPackid(getPackageName());
        ximalaya.init(this ,"2fb7fb1aa47b7eac2fdcfe2c1458e3fa");

        //  初始化LogUtil
        LogUtil.init(getPackageName(), false);
    }
}

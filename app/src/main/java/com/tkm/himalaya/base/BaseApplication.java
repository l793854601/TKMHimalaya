package com.tkm.himalaya.base;

import android.app.Application;

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //  初始化喜马拉雅SDK

        CommonRequest mXimalaya = CommonRequest.getInstanse();
        mXimalaya.setAppkey("1ae9b7ce1572d71a2fc4011498c4cb2d");
        mXimalaya.setPackid("com.tkm.himalaya");
        mXimalaya.init(this ,"2fb7fb1aa47b7eac2fdcfe2c1458e3fa");
    }
}

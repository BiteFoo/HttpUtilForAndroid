package com.hss01248.netdemo;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;
import com.hss01248.net.wrapper.HttpUtil;

/**
 * Created by Administrator on 2016/9/4.
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        FreelineCore.init(this);//要第一行
        super.onCreate();
        HttpUtil.context = this;
       // MyRetrofitUtil.init(getApplicationContext());

    }


}

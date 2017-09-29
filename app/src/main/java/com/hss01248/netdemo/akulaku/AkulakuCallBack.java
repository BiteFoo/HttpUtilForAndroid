package com.hss01248.netdemo.akulaku;

import com.hss01248.net.wrapper.MyNetListener;

/**
 * Created by huangshuisheng on 2017/9/29.
 */

public abstract class AkulakuCallBack<T> extends MyNetListener<T> {

    @Override
    public void onCodeError(String msgCanShow, String hiddenMsg, int code) {
        onError(code+"",msgCanShow,hiddenMsg);
    }

    @Override
    public void onError(String msgCanShow) {
        //super.onError(msgCanShow);
        onError("",msgCanShow,msgCanShow);
    }

    @Override
    public abstract void onError(String code, String serverMsg, String exceptionMsg) ;
}

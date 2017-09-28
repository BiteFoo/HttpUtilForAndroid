package com.hss01248.net.akulaku;

import com.hss01248.net.interfaces.HttpCallback;

import java.util.List;

/**
 * Created by huangshuisheng on 2017/9/28.
 */

public interface AkulakuCallback<T> extends HttpCallback{

    void onSuccessObj(T obj,boolean isFromCache,long sysTime);

    void onSuccessArr(List<T> arr, boolean isFromCache,long sysTime);



}

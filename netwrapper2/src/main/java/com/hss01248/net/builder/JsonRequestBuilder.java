package com.hss01248.net.builder;

import android.app.Activity;
import android.app.Dialog;

import com.hss01248.net.wrapper.MyNetListener;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class JsonRequestBuilder<T> extends StringRequestBuilder{

    public Class<T> clazz;
    public JsonRequestBuilder<T> setJsonClazz(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }


    //TODO 预期的响应是否为arr
    private boolean isResponseJsonArray = false;
    public JsonRequestBuilder<T> setResponseJsonArray() {
        isResponseJsonArray = true;
        return this;
    }


    //todo 以下的都是复写基类的方法,强转成子类

    @Override
    public JsonRequestBuilder url(String url) {
        return (JsonRequestBuilder) super.url(url);
    }

    @Override
    public JsonRequestBuilder addHeader(String key, String value) {
        return (JsonRequestBuilder) super.addHeader(key, value);
    }

    @Override
    public JsonRequestBuilder addParams(String key, String value) {
        return (JsonRequestBuilder) super.addParams(key, value);
    }

    @Override
    public JsonRequestBuilder callback(MyNetListener listener) {
        return (JsonRequestBuilder) super.callback(listener);
    }



    @Override
    public JsonRequestBuilder showLoadingDialog(Activity activity, String loadingMsg) {
        return (JsonRequestBuilder) super.showLoadingDialog(activity, loadingMsg);
    }

    @Override
    public JsonRequestBuilder showLoadingDialog(Dialog loadingDialog) {
        return (JsonRequestBuilder) super.showLoadingDialog(loadingDialog);
    }

    @Override
    public JsonRequestBuilder setCacheControl(boolean shouldReadCache, boolean shouldCacheResponse, long cacheTimeInSeconds) {
        return (JsonRequestBuilder) super.setCacheControl(shouldReadCache, shouldCacheResponse, cacheTimeInSeconds);
    }

    @Override
    public JsonRequestBuilder setRetryCount(int retryCount) {
        return (JsonRequestBuilder) super.setRetryCount(retryCount);
    }

    @Override
    public JsonRequestBuilder setTimeout(int timeoutInMills) {
        return (JsonRequestBuilder) super.setTimeout(timeoutInMills);
    }

    @Override
    public JsonRequestBuilder setIgnoreCer() {
        return (JsonRequestBuilder) super.setIgnoreCer();
    }

    @Override
    public JsonRequestBuilder setIsAppendToken(boolean isAppendToken, boolean isInHeaderOrParam) {
        return (JsonRequestBuilder) super.setIsAppendToken(isAppendToken, isInHeaderOrParam);
    }

    //todo 复写string的

    @Override
    public JsonRequestBuilder setParamsAsJson() {
        return (JsonRequestBuilder) super.setParamsAsJson();
    }


}

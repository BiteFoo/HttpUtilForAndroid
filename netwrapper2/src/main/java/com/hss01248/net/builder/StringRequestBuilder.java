package com.hss01248.net.builder;

import android.app.Activity;
import android.app.Dialog;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.wrapper.MyNetListener;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class StringRequestBuilder <T> extends BaseNetBuilder{

    public StringRequestBuilder(){
        this.type = ConfigInfo.TYPE_STRING;
    }



    //todo 请求以什么形式,key=value&key=value还是json形式
    public boolean paramsAsJson = false;
    public StringRequestBuilder<T> setParamsAsJson() {
        this.paramsAsJson = true;

        return this;
    }

    @Override
    protected ConfigInfo execute() {
        //做一些参数合理性校验

        return new ConfigInfo(this);
    }

//todo 以下的都是复写基类的方法,强转成子类

    @Override
    public StringRequestBuilder url(String url) {
        return (StringRequestBuilder) super.url(url);
    }

    @Override
    public StringRequestBuilder addHeader(String key, String value) {
        return (StringRequestBuilder) super.addHeader(key, value);
    }

    @Override
    public StringRequestBuilder addParams(String key, String value) {
        return (StringRequestBuilder) super.addParams(key, value);
    }

    @Override
    public StringRequestBuilder callback(MyNetListener listener) {
        return (StringRequestBuilder) super.callback(listener);
    }



    @Override
    public StringRequestBuilder showLoadingDialog(Activity activity, String loadingMsg) {
        return (StringRequestBuilder) super.showLoadingDialog(activity, loadingMsg);
    }

    @Override
    public StringRequestBuilder showLoadingDialog(Dialog loadingDialog) {
        return (StringRequestBuilder) super.showLoadingDialog(loadingDialog);
    }

    @Override
    public StringRequestBuilder setCacheControl(boolean shouldReadCache, boolean shouldCacheResponse, long cacheTimeInSeconds) {
        return (StringRequestBuilder) super.setCacheControl(shouldReadCache, shouldCacheResponse, cacheTimeInSeconds);
    }

    @Override
    public StringRequestBuilder setRetryCount(int retryCount) {
        return (StringRequestBuilder) super.setRetryCount(retryCount);
    }

    @Override
    public StringRequestBuilder setTimeout(int timeoutInMills) {
        return (StringRequestBuilder) super.setTimeout(timeoutInMills);
    }

    @Override
    public StringRequestBuilder setIgnoreCer() {
        return (StringRequestBuilder) super.setIgnoreCer();
    }

    @Override
    public StringRequestBuilder setIsAppendToken(boolean isAppendToken, boolean isInHeaderOrParam) {
        return (StringRequestBuilder) super.setIsAppendToken(isAppendToken, isInHeaderOrParam);
    }

}

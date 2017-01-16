package com.hss01248.net.builder;

import android.app.Activity;
import android.app.Dialog;

import com.hss01248.net.wrapper.MyNetListener;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class DownloadBuilder <T> extends BaseNetBuilder{



    private String savedPath;

    public DownloadBuilder<T> savedPath(String path ){
        this.savedPath = path;
        return this;
    }




    //todo 以下的都是复写基类的方法,强转成子类

    @Override
    public DownloadBuilder url(String url) {
        return (DownloadBuilder) super.url(url);
    }

    @Override
    public DownloadBuilder addHeader(String key, String value) {
        return (DownloadBuilder) super.addHeader(key, value);
    }

    @Override
    public DownloadBuilder addParams(String key, String value) {
        return (DownloadBuilder) super.addParams(key, value);
    }

    @Override
    public DownloadBuilder callback(MyNetListener listener) {
        return (DownloadBuilder) super.callback(listener);
    }



    @Override
    public DownloadBuilder showLoadingDialog(Activity activity, String loadingMsg) {
        return (DownloadBuilder) super.showLoadingDialog(activity, loadingMsg);
    }

    @Override
    public DownloadBuilder showLoadingDialog(Dialog loadingDialog) {
        return (DownloadBuilder) super.showLoadingDialog(loadingDialog);
    }

    @Override
    public DownloadBuilder setCacheControl(boolean shouldReadCache, boolean shouldCacheResponse, long cacheTimeInSeconds) {
        return (DownloadBuilder) super.setCacheControl(shouldReadCache, shouldCacheResponse, cacheTimeInSeconds);
    }

    @Override
    public DownloadBuilder setRetryCount(int retryCount) {
        return (DownloadBuilder) super.setRetryCount(retryCount);
    }

    @Override
    public DownloadBuilder setTimeout(int timeoutInMills) {
        return (DownloadBuilder) super.setTimeout(timeoutInMills);
    }

    @Override
    public DownloadBuilder setIgnoreCer() {
        return (DownloadBuilder) super.setIgnoreCer();
    }

    @Override
    public DownloadBuilder setIsAppendToken(boolean isAppendToken, boolean isInHeaderOrParam) {
        return (DownloadBuilder) super.setIsAppendToken(isAppendToken, isInHeaderOrParam);
    }

}

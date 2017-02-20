package com.hss01248.http.builder;


import com.hss01248.http.config.ConfigInfo;
import com.hss01248.http.config.NetDefaultConfig;
import com.hss01248.http.util.UrlUtil;
import com.hss01248.http.util.TextUtils;
import com.hss01248.http.wrapper.MyNetListener;

import java.io.File;
import java.util.UUID;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class DownloadBuilder <T> extends ProgressBaseBuilder{

    public DownloadBuilder(){
        super();
        this.type = ConfigInfo.TYPE_DOWNLOAD;

    }

    public String savedPath;





    //是否打開,是否讓媒体库扫描,是否隐藏文件夹
    public boolean isOpenAfterSuccess = false;//默认不扫描
    public boolean isHideFolder = false;
    public boolean isNotifyMediaCenter = true;//媒体文件下载后,默认:通知mediacenter扫描


    //文件校验相关设置(默认不校验)MD5,SHA1,CRC32三种校验方法,任选一种即可
    public boolean isVerify = false;//是否校驗文件
    public String verifyStr;
    public boolean verfyByMd5OrShar1 = false;


    public DownloadBuilder<T> setNotifyMediaCenter(boolean notifyMediaCenter) {
        isNotifyMediaCenter = notifyMediaCenter;
        return this;
    }
    public DownloadBuilder<T> setOpenAfterSuccess() {
        isOpenAfterSuccess = true;
        return this;
    }
    public DownloadBuilder<T> setHideFile() {
        isHideFolder = true;
        return this;
    }


    public DownloadBuilder<T> savedPath(String path ){
        this.savedPath = path;
        return this;
    }

    public DownloadBuilder<T> verifyMd5(String md5Str ){
        this.isVerify = true;
        verfyByMd5OrShar1 = true;
        this.verifyStr = md5Str;
        return this;
    }

    public DownloadBuilder<T> verifyShar1(String shar1Str ){
        this.isVerify = true;
        verfyByMd5OrShar1 = false;
        this.verifyStr = shar1Str;
        return this;
    }



    @Override
    protected boolean validate() {
        if(!super.validate()){
            return false;
        }

            method = NetDefaultConfig.Method.GET;
            this.type = ConfigInfo.TYPE_DOWNLOAD;
            if(savedPath ==null || savedPath =="" ){
                savedPath = getDefaultSavedPath();
            }
            if(!new File(savedPath).exists()){
                listener.onError("保存路径不存在");
                return false;
            }else {
                return true;
            }

    }

    /**
     * 默认保存到download/retrofit文件夹下
     * @return
     */
    private String getDefaultSavedPath() {
        String fileName =  UrlUtil.guessFileName(url);
        if(TextUtils.isEmpty(fileName)){
            fileName = UUID.randomUUID().toString();
        }
       File dir = new File("c:\\download\\","retrofit");
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir,fileName);
       /* if(file.exists()){
            file.delete();
        }else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        return file.getAbsolutePath();
    }

    //todo 以下的都是复写基类的方法,强转成子类


    @Override
    public DownloadBuilder paramsStr(String paramsStr) {
        return (DownloadBuilder) super.paramsStr(paramsStr);
    }

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

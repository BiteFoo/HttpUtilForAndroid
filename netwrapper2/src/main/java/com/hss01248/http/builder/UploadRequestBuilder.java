package com.hss01248.http.builder;


import com.hss01248.http.config.ConfigInfo;
import com.hss01248.http.config.NetDefaultConfig;
import com.hss01248.http.util.CollectionUtil;
import com.hss01248.http.wrapper.MyNetListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class UploadRequestBuilder <T> extends ProgressBaseBuilder{
    public Map<String,String> files;



    public UploadRequestBuilder(){
        type = ConfigInfo.TYPE_UPLOAD_WITH_PROGRESS;
        files = new HashMap<String,String>();
    }


    public UploadRequestBuilder<T> addFile(String desc,String filePath){
        files.put(desc,filePath);
        return this;
    }



    @Override
    protected boolean validate() {
        if(!super.validate()){
            return false;
        }
        method = NetDefaultConfig.Method.POST;
        headers.put("Content-Type","multipart/form-data");

        /*CollectionUtil.filter(files.entrySet(), new CollectionUtil.Filter<Map.Entry<String,String>>() {
            @Override
            public boolean remain(Map.Entry<String,String> item) {
                return false;
            }
        });*/

        //不应该过滤,而应该直接onerror
        CollectionUtil.filterMap(files, new CollectionUtil.MapFilter<String, String>() {
            public boolean isRemain(Map.Entry<String, String> entry) {
                return new File(entry.getValue()).exists();
            }
        });

        if(files.size()==0){
            listener.onError("没有添加上传文件");
            return false;
        }
        return true;
    }

    //todo 以下的都是复写基类的方法,强转成子类

    @Override
    public UploadRequestBuilder url(String url) {
        return (UploadRequestBuilder) super.url(url);
    }

    @Override
    public UploadRequestBuilder addHeader(String key, String value) {
        return (UploadRequestBuilder) super.addHeader(key, value);
    }

    @Override
    public UploadRequestBuilder addParams(String key, String value) {
        return (UploadRequestBuilder) super.addParams(key, value);
    }

    @Override
    public UploadRequestBuilder callback(MyNetListener listener) {
        return (UploadRequestBuilder) super.callback(listener);
    }



    @Override
    public UploadRequestBuilder setCacheControl(boolean shouldReadCache, boolean shouldCacheResponse, long cacheTimeInSeconds) {
        return (UploadRequestBuilder) super.setCacheControl(shouldReadCache, shouldCacheResponse, cacheTimeInSeconds);
    }

    @Override
    public UploadRequestBuilder setRetryCount(int retryCount) {
        return (UploadRequestBuilder) super.setRetryCount(retryCount);
    }

    @Override
    public UploadRequestBuilder setTimeout(int timeoutInMills) {
        return (UploadRequestBuilder) super.setTimeout(timeoutInMills);
    }

    @Override
    public UploadRequestBuilder setIgnoreCer() {
        return (UploadRequestBuilder) super.setIgnoreCer();
    }

    @Override
    public UploadRequestBuilder setIsAppendToken(boolean isAppendToken, boolean isInHeaderOrParam) {
        return (UploadRequestBuilder) super.setIsAppendToken(isAppendToken, isInHeaderOrParam);
    }

}

package com.hss01248.net.builder;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.builder.IBuilder;
import com.hss01248.net.wrapper.MyNetListener;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class ApiBuilder implements IBuilder {

    @Override
    public <E> StringRequestBuilder<E> buildStringRequest(String url, MyNetListener<E> listener) {
        return new StringRequestBuilder<>().url(url).callback(listener);
    }

    @Override
    public <E> JsonRequestBuilder<E> buildJsonRequest(String url, Class<E> clazz, MyNetListener<E> listener) {
        JsonRequestBuilder builder = new JsonRequestBuilder();
        builder.url(url)
                .setJsonClazz(clazz)
                .callback(listener);

               // .setJson
        return builder;
    }

    @Override
    public <E> StandardJsonRequestBuilder<E> buildStandardJsonRequest(String url, Class<E> clazz, MyNetListener<E> listener) {
        return new StandardJsonRequestBuilder<>().url(url).setJsonClazz(clazz).callback(listener);
    }

    @Override
    public <E> DownloadBuilder<E> buildDownloadRequest(String url, MyNetListener<E> callback) {
        return new DownloadBuilder<>().url(url).callback(callback);
    }

    @Override
    public <E> UploadRequestBuilder<E> buildUpLoadRequest(String url, String fileDesc, String filePath, MyNetListener<E> callback) {
        return new UploadRequestBuilder<>().url(url).addFile(fileDesc,filePath).callback(callback);
    }
}

package com.hss01248.net.builder;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.wrapper.MyNetListener;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public interface IBuilder {

    //TODO 新api,最简化,其他个性化设置由
    public <E> StringRequestBuilder<E> buildStringRequest(String url, final MyNetListener<E> listener);

    public <E> JsonRequestBuilder<E> buildJsonRequest(String url, Class<E> clazz, final MyNetListener<E> listener);

    public <E> StandardJsonRequestBuilder<E> buildStandardJsonRequest(String url, Class<E> clazz, final MyNetListener<E> listener);

    public<E> DownloadBuilder<E> buildDownloadRequest(String url,  MyNetListener<E> callback);

    <E> UploadRequestBuilder<E> buildUpLoadRequest(String url,  String fileDesc,String filePath, MyNetListener<E> callback);
}

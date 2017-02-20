package com.hss01248.http.builder;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public interface IBuilder {

    //TODO 新api,最简化,其他个性化设置由
    public <E> StringRequestBuilder<E> requestString(String url);

    public <E> JsonRequestBuilder<E> requestJson(String url, Class<E> clazz);

    public <E> StandardJsonRequestBuilder<E> reqeustStandardJson(String url, Class<E> clazz);

    public<E> DownloadBuilder<E> download(String url);

    <E> UploadRequestBuilder<E> upload(String url, String fileDesc, String filePath);
}

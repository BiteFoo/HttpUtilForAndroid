package com.hss01248.net.builder;

/**
 * Created by Administrator on 2017/1/17 0017.
 */

public interface IClient {

    public <E> NetConfig<E> buildStringRequest(NetConfig<E> config);

    public <E> NetConfig<E> buildJsonRequest(NetConfig<E> config);

    public <E> NetConfig<E> buildStandardJsonRequest(NetConfig<E> config);

    public<E> NetConfig<E> buildDownloadRequest(NetConfig<E> config);

    <E> NetConfig<E> buildUpLoadRequest(NetConfig<E> config);

    <E> NetConfig<E> start(NetConfig<E> config);

    <E> NetConfig<E> cancel(NetConfig<E> config);

}

package com.hss01248.http.interfaces;

import com.hss01248.http.config.RequestConfig;

/**
 * Created by hss on 2017/12/28.
 *
 * T : 真正执行网络请求的client
 */

public interface IClient<T> {

    void cancelByTag(Object tag);

    void requestString(RequestConfig config);

    void download(RequestConfig config);

    void upload(RequestConfig config);
}

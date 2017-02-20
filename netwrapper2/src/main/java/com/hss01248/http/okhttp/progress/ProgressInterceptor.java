package com.hss01248.http.okhttp.progress;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class ProgressInterceptor implements Interceptor {




    public ProgressInterceptor(){
    }


    public Response intercept(Chain chain) throws IOException{
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(),chain.request().url().toString())).build();
    }

}

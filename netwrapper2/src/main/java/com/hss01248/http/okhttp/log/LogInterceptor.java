package com.hss01248.http.okhttp.log;

import com.hss01248.http.util.MyLog;
import okhttp3.Interceptor;
import okhttp3.Request;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class LogInterceptor implements Interceptor {

    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        MyLog.i( "request:\n" + request.toString());
        MyLog.i( "request headers:\n" + request.headers().toString());
        MyLog.i( "request body:\n" + request.body());
        long t1 = System.nanoTime();
        okhttp3.Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();
        MyLog.i(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        okhttp3.MediaType mediaType = response.body().contentType();


        String type = response.header("Content-Encoding");

        MyLog.e("Content-Encoding:"+type);


        byte[] responseBytes=response.body().bytes();

        String content = new String(responseBytes,"GBK");

        //String content = response.body().string();
        //String content =  response.body().source().readString(Charset.forName("gb2312"));
       // GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(content.getBytes("utf-8")));

        MyLog.i( "response body:\n" + content);
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}
package com.hss01248.net.okhttp;


import com.hss01248.net.builder.IClient;
import com.hss01248.net.cache.CacheStrategy;
import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.config.GlobalConfig;
import com.hss01248.net.cookie.DiskCookieJar;
import com.hss01248.net.cookie.MemoryCookieJar;
import com.hss01248.net.okhttp.log.LogInterceptor;
import com.hss01248.net.okhttp.progress.UploadFileRequestBody;
import com.hss01248.net.util.HttpsUtil;
import com.hss01248.net.util.TextUtils;
import com.hss01248.net.wrapper.MyJson;
import com.hss01248.net.wrapper.MyLog;
import com.hss01248.net.wrapper.Tool;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/19 0019.
 */
public class OkClient extends IClient {
    private static OkClient okClient;
   private static OkHttpClient client;
    private static OkHttpClient allCerPassClient;


   private OkClient(){

   }

   private OkHttpClient getAllCerPassClient(){
       if(allCerPassClient ==null){
           OkHttpClient.Builder builder = new OkHttpClient.Builder();
           HttpsUtil.setAllCerPass(builder);
           OkClient.allCerPassClient = builder
                   .connectTimeout(6000, TimeUnit.MILLISECONDS)
                   .readTimeout(0, TimeUnit.MILLISECONDS)
                   .writeTimeout(0, TimeUnit.MILLISECONDS)
                   .addNetworkInterceptor(new LogInterceptor())
                   .build();
       }
       return allCerPassClient;
   }

    public static OkClient getInstance(){
        if(okClient ==null){
            okClient = new OkClient();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            setGloablConfig(builder);




            //HttpsUtil.setHttps(builder);
            OkClient.client = builder
                    .connectTimeout(GlobalConfig.get().getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(0, TimeUnit.MILLISECONDS)
                    .writeTimeout(0, TimeUnit.MILLISECONDS)
                    .build();
           // client.newBuilder().build()

        }
        return okClient;
    }

    private static void setGloablConfig(OkHttpClient.Builder builder) {
        setCookie(builder,GlobalConfig.get().getCookieMode());
        setHttps(builder,GlobalConfig.get().isIgnoreCertificateVerify());
        setCacheStrategy(builder,GlobalConfig.get().getCacheMode());
        setLog(builder,GlobalConfig.get().isOpenLog());
    }

    private static void setLog(OkHttpClient.Builder builder, boolean openLog) {
        if(openLog){
            builder.addNetworkInterceptor(new LogInterceptor());
        }
    }

    private static void setCacheStrategy(OkHttpClient.Builder builder, int cacheMode) {
        switch (cacheMode){
            case CacheStrategy.NO_CACHE:{
                //CacheControl

            }
                break;
            case CacheStrategy.DEFAULT:{

            }
            break;
            case CacheStrategy.REQUEST_FAILED_READ_CACHE:{

            }
            break;
            case CacheStrategy.IF_NONE_CACHE_REQUEST:{

            }
            break;
            case CacheStrategy.FIRST_CACHE_THEN_REQUEST:{

            }
            break;
            default:
            break;
        }
    }

    private static void setHttps(OkHttpClient.Builder builder, boolean ignoreCertificateVerify) {
        if(ignoreCertificateVerify){
            HttpsUtil.setAllCerPass(builder);
        }else {
            HttpsUtil.setHttps(builder);
        }
    }

    private static void setCookie(OkHttpClient.Builder builder, int cookieMode) {
        CookieJar cookieJar = CookieJar.NO_COOKIES;
        if(cookieMode == GlobalConfig.COOKIE_MEMORY){
            cookieJar = new MemoryCookieJar();
        }else if (cookieMode == GlobalConfig.COOKIE_DISK){
            cookieJar = new DiskCookieJar();
        }else {
            cookieJar = CookieJar.NO_COOKIES;
        }
        builder.cookieJar(cookieJar);
    }


    public <E> ConfigInfo<E> getString(final ConfigInfo<E> info) {
         /* 如果需要参数 , 在url后边拼接 :  ?a=aaa&b=bbb..... */
        Request.Builder builder = new Request.Builder();
        addTag(builder,info);

        String url = Tool.generateUrlOfGET(info);
       MyLog.e("url:"+url);
        builder.url(url);
        //builder.
        addHeaders(builder,info.headers);

        handleStringRequest(info, builder);
        return info;
    }

    private <E> void addTag(Request.Builder builder, ConfigInfo<E> info) {
        if(info.tagForCancle!=null){
            builder.tag(info.tagForCancle);
        }
    }

    public <E> ConfigInfo<E> postString(ConfigInfo<E> info) {

        Request.Builder builder = new Request.Builder();
        addTag(builder,info);
        builder.url(info.url);
        addHeaders(builder,info.headers);
        addPostBody(builder,info);

        handleStringRequest(info, builder);
        return info;
    }

    public ConfigInfo download(final ConfigInfo info) {
        Request.Builder builder = new Request.Builder();
        addTag(builder,info);
        String url = Tool.generateUrlOfGET(info);
        builder.url(url);
        addHeaders(builder,info.headers);
        //info.listener.registEventBus();
        requestAndHandleResoponse(info, builder, new ISuccessResponse() {
            public void handleSuccess(Call call, Response response) throws IOException {
                Tool.writeResponseBodyToDisk(response.body(),info);
            }
        });

        return info;
    }

    public ConfigInfo upload(final ConfigInfo info) {
        Request.Builder builder = new Request.Builder();
        addTag(builder,info);
        builder.url(info.url);
        addHeaders(builder,info.headers);
        //info.listener.registEventBus();
        addUploadBody(builder,info);

               requestAndHandleResoponse(info, builder, new ISuccessResponse() {
            public void handleSuccess(Call call, final Response response) throws IOException {
                final String str = response.body().string();

                Tool.callbackOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Tool.dismiss(info.loadingDialog);
                        info.listener.onSuccess(str,str);
                    }
                });
            }
        });
        return info;
    }




    private void addHeaders(Request.Builder builder, Map<String, String> headers) {

        Headers.Builder headBuilder = new Headers.Builder();
        for(Map.Entry<String,String> header   : headers.entrySet()){

            headBuilder.set(header.getKey(),header.getValue());
        }
        builder.headers(headBuilder.build());
    }

    private FormBody getFormBody(Map<String,String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,String> param   : params.entrySet()){
            builder.add(param.getKey(),param.getValue());
        }
        return builder.build();
    }

    private void addPostBody(Request.Builder builder, ConfigInfo info) {
        RequestBody body = null;
        if(TextUtils.isNotEmpty(info.paramsStr)){
            body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), info.paramsStr);
            builder.post(body);
            return;
        }

        if(info.paramsAsJson){
            String jsonStr = MyJson.toJsonStr(info.params);
            body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), jsonStr);
        }else {
            body=   getFormBody(info.params);
        }
        builder.post(body);
    }

    private <E> void addUploadBody(Request.Builder builder0, ConfigInfo<E> configInfo) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (configInfo.params != null && configInfo.params.size() >0){
            Map<String,String> params = configInfo.params;
            int count = params.size();
            if (count>0){
                Set<Map.Entry<String,String>> set = params.entrySet();
                for (Map.Entry<String,String> entry : set){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    builder.addFormDataPart(key,value);
                }
            }
        }

        if (configInfo.files != null && configInfo.files.size() >0){
            Map<String,String> files = configInfo.files;
            int count = files.size();
            int index=0;

            if (count>0){
                Set<Map.Entry<String,String>> set = files.entrySet();
                for (Map.Entry<String,String> entry : set){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    File file = new File(value);
                    UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, Tool.getMimeType(value),configInfo,index);
                    builder.addFormDataPart(key,file.getName(),fileRequestBody);
                    index++;
                }
            }
        }

        //MultipartBody body = builder.build();
        builder0.post(builder.build());
    }

    private <E> void handleStringRequest(final ConfigInfo<E> info, Request.Builder builder) {

        requestAndHandleResoponse(info, builder, new ISuccessResponse() {
            public void handleSuccess(Call call, Response response) throws IOException {
               /* String type = response.header("Content-Encoding");
                byte[] bytes = response.body().bytes();
                String str = new String(bytes,"utf-8");*/
               // String str =  response.body().source().readString(Charset.forName("gb2312"));

               /* if("gzip".equals(type)){
                    str = GZipUtil.uncompress(str);
                }*/
                String str = response.body().string();
                Tool.parseStringByType(str,info);
                Tool.dismiss(info.loadingDialog);
            }
        });
    }

    private <E> void requestAndHandleResoponse(final ConfigInfo<E> info, Request.Builder builder, final ISuccessResponse successResponse) {
        final Request request = builder.build();
        OkHttpClient theClient;
        if(info.ignoreCer){
            if(allCerPassClient==null){
                allCerPassClient = getAllCerPassClient();
            }
            theClient = allCerPassClient;
        }else {
            theClient = client;
        }
        Call call = theClient.newCall(request);
        info.request = call;
        if(info.isSync){//同步请求
            try {
              final Response response =   call.execute();
                if(response.isSuccessful()){
                    successResponse.handleSuccess(call,response);
                }else {

                            info.listener.onCodeError("http错误码:"+response.code(),response.message(),response.code());

                    
                }

            } catch (IOException e) {

                info.listener.onError(e.getMessage());
                e.printStackTrace();
            }
            return;
        }
        //异步请求
        call.enqueue(new Callback() {
            public void onFailure(Call call, final IOException e) {
                Tool.callbackOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Tool.dismiss(info.loadingDialog);
                        info.listener.onError(e.getMessage());
                    }
                });

                e.printStackTrace();
            }
            public void onResponse(Call call, final Response response) throws IOException {
                if(response.isSuccessful()){
                    successResponse.handleSuccess(call,response);
                }else {
                    Tool.callbackOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            Tool.dismiss(info.loadingDialog);
                            info.listener.onCodeError("http错误码:"+response.code(),response.message(),response.code());
                        }
                    });

                }
            }
        });
    }

    interface ISuccessResponse{
        void handleSuccess(Call call, Response response) throws IOException;
    }

    @Override
    public void cancleRequest(Object tag) {
        cancel(client,tag);
        cancel(allCerPassClient,tag);
    }

    private void cancel(OkHttpClient client, Object tag) {
        if(client==null || tag==null){
            return;
        }
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    @Override
    public void cancleAllRequest() {
        if(client!=null){
            client.dispatcher().cancelAll();
        }
        if(allCerPassClient != null){
            client.dispatcher().cancelAll();
        }


    }



}

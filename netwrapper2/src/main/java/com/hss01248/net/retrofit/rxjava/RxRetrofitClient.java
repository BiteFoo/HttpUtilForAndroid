package com.hss01248.net.retrofit.rxjava;

import android.util.Log;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.config.HttpsConfig;
import com.hss01248.net.config.NetDefaultConfig;
import com.hss01248.net.retrofit.ApiService;
import com.hss01248.net.retrofit.NoCacheInterceptor;
import com.hss01248.net.retrofit.UseragentInterceptor;
import com.hss01248.net.retrofit.https.HttpsUtil;
import com.hss01248.net.retrofit.progress.ProgressInterceptor;
import com.hss01248.net.retrofit.progress.UploadFileRequestBody;
import com.hss01248.net.threadpool.ThreadPoolFactory;
import com.hss01248.net.wrapper.BaseNet;
import com.hss01248.net.wrapper.MyJson;
import com.hss01248.net.wrapper.MyNetApi;
import com.hss01248.net.wrapper.Tool;
import com.litesuits.android.async.SimpleTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/21.
 */
public class RxRetrofitClient extends BaseNet<Call> {


    Retrofit retrofit;
    RxApiService service;


    ApiService serviceDownload;
    //需要单独为下载的call设置Retrofit: 主要是超时时间设置为0
    Retrofit retrofitDownload;

    //上传的client
    ApiService serviceUpload;
    Retrofit retrofitUpload;

    //完全忽略证书校验的client,用于某些额外的单次请求中
    ApiService serviceIgnoreSSL;
    Retrofit retrofitIgnoreSSL;





    private void init() {
        //默认情况下，Retrofit只能够反序列化Http体为OkHttp的ResponseBody类型
        //并且只能够接受ResponseBody类型的参数作为@body

        OkHttpClient.Builder httpBuilder=new OkHttpClient.Builder();

        //https:
        setHttps(httpBuilder);


        OkHttpClient client=httpBuilder.readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS) //设置超时

                .retryOnConnectionFailure(true)//重试
                //.addInterceptor(new ProgressInterceptor())//下载时更新进度
                .addNetworkInterceptor(new NoCacheInterceptor())//request和resoponse都加上nocache,
               // .addInterceptor(new UseragentInterceptor())
               /* .sslSocketFactory(new SSLCertificateSocketFactory(), new X509TrustManager() {
                })*/
                .build();

        retrofit = new Retrofit
                .Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//新的配置
                .baseUrl(NetDefaultConfig.baseUrl)
                .client(client)
                // .addConverterFactory(GsonConverterFactory.create()) // 使用Gson作为数据转换器
                .build();

        service = retrofit.create(RxApiService.class);
    }

    private void setHttps(OkHttpClient.Builder httpBuilder) {
        if(HttpsConfig.certificates != null && HttpsConfig.certificates.size()>0){
            httpBuilder.sslSocketFactory(HttpsUtil.getSSLSocketFactory(MyNetApi.context, HttpsConfig.certificates));
        }




    }

    private static RxRetrofitClient instance;

    private RxRetrofitClient(){
        init();
        // initDownload();
    }

    private void initDownload() {
        OkHttpClient.Builder httpBuilder=new OkHttpClient.Builder();
        setHttps(httpBuilder);
        OkHttpClient client=httpBuilder.readTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS).writeTimeout(0, TimeUnit.SECONDS) //设置超时
                .retryOnConnectionFailure(false)//重试
                .addInterceptor(new UseragentInterceptor())
                .addInterceptor(new ProgressInterceptor())//下载时更新进度
                .build();

        retrofitDownload = new Retrofit
                .Builder()
                .baseUrl(NetDefaultConfig.baseUrl)
                .client(client)
                // .addConverterFactory(GsonConverterFactory.create()) // 使用Gson作为数据转换器
                .build();

        serviceDownload = retrofitDownload.create(ApiService.class);
    }

    private void initUpload() {
        OkHttpClient.Builder httpBuilder=new OkHttpClient.Builder();
        setHttps(httpBuilder);
        OkHttpClient client=httpBuilder.readTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(0, TimeUnit.SECONDS).writeTimeout(0, TimeUnit.SECONDS) //设置超时
                .retryOnConnectionFailure(false)//重试
                // .addInterceptor(new ProgressRequestInterceptor())//上传时更新进度
                .addInterceptor(new UseragentInterceptor())
               /* .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        return null;
                    }
                })*/
                .build();

        retrofitDownload = new Retrofit
                .Builder()
                .baseUrl(NetDefaultConfig.baseUrl)
                .client(client)
                .build();

        serviceDownload = retrofitUpload.create(ApiService.class);
    }


    private void initSSLIgnore() {
        OkHttpClient.Builder httpBuilder=new OkHttpClient.Builder();
        HttpsUtil.setAllCerPass(httpBuilder);
        OkHttpClient client=httpBuilder.readTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS).writeTimeout(0, TimeUnit.SECONDS) //设置超时
                .retryOnConnectionFailure(false)//重试
                .build();

        retrofitIgnoreSSL = new Retrofit
                .Builder()
                .baseUrl(NetDefaultConfig.baseUrl)
                .client(client)
                .build();

        serviceIgnoreSSL = retrofitIgnoreSSL.create(ApiService.class);
    }



    public static RxRetrofitClient getInstance(){
        if (instance == null){
            synchronized (RxRetrofitClient.class){
                if (instance ==  null){
                    instance = new RxRetrofitClient();
                }
            }
        }
        return  instance;
    }


    @Override
    protected boolean isAppendUrl() {
        return false;
    }


    @Override
    public <E> void cancleRequest(ConfigInfo<E> configInfo) {
        if (configInfo.tagForCancle instanceof Call){
            Call call = (Call) configInfo.tagForCancle;
            if (!call.isCanceled()){
                call.cancel();
            }
        }
    }

    @Override
    protected <E> Call newUploadRequestWithoutProgress(ConfigInfo<E> configInfo) {
        return null;
    }

    @Override
    protected  Call newUploadRequest(final ConfigInfo configInfo) {
        if(configInfo == null){//用于测试第二种方法
            return  uploadWithProgress2(configInfo);
        }

        ApiService service = null;

        if(configInfo.isIgnoreCer()){
            if(serviceIgnoreSSL == null){
                initSSLIgnore();
            }
            service = serviceIgnoreSSL;
        }else {
            if (serviceUpload == null){
                initUpload();
            }
            service = serviceUpload;
        }



        configInfo.listener.registEventBus();
        Map<String, RequestBody> filesMap = new HashMap<>();

        //将文件放进map中
        if (configInfo.files != null && configInfo.files.size() >0){
            Map<String,String> files = configInfo.files;
            int count = files.size();
            if (count>0){
                Set<Map.Entry<String,String>> set = files.entrySet();
                for (Map.Entry<String,String> entry : set){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    File file = new File(value);
                    String type = Tool.getMimeType(file);
                    Log.e("type","mimetype:"+type);
                    UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, type,configInfo.url);
                    filesMap.put(key+"\"; filename=\"" + file.getName(), fileRequestBody);
                }
            }
        }
        //将key-value放进body中:
        Map<String, RequestBody> paramsMap = new HashMap<>();
        if (configInfo.params != null && configInfo.params.size() >0){
            Map<String,String> params = configInfo.params;
            int count = params.size();
            if (count>0){
                Set<Map.Entry<String,String>> set = params.entrySet();
                for (Map.Entry<String,String> entry : set){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String type = "text/plain";
                    RequestBody fileRequestBody = RequestBody.create(MediaType.parse(type),value);
                    paramsMap.put(key, fileRequestBody);
                }
            }
        }



        Call<ResponseBody> call = service.uploadWithProgress(configInfo.url,paramsMap,filesMap,configInfo.headers);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Tool.dismiss(configInfo.loadingDialog);

                if (response.isSuccessful()){
                    try {
                        String string = response.body().string();
                        configInfo.listener.onSuccess(string,string);
                    } catch (IOException e) {
                        e.printStackTrace();
                        onFailure(call,e);
                    }

                }else {
                    configInfo.listener.onError(response.code()+"");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Tool.dismiss(configInfo.loadingDialog);
                configInfo.listener.onError(t.toString());
            }
        });

        return call;

    }

    public Call uploadWithProgress2(final ConfigInfo configInfo){

        ApiService service = null;

        if(configInfo.isIgnoreCer()){
            if(serviceIgnoreSSL == null){
                initSSLIgnore();
            }
            service = serviceIgnoreSSL;
        }else {
            if (serviceUpload == null){
                initUpload();
            }
            service = serviceUpload;
        }
        configInfo.listener.registEventBus();

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
            if (count>0){
                Set<Map.Entry<String,String>> set = files.entrySet();
                for (Map.Entry<String,String> entry : set){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    File file = new File(value);
                    String type = Tool.getMimeType(file);
                    UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, type,configInfo.url);
                    builder.addFormDataPart(key,file.getName(),fileRequestBody);
                }
            }
        }

        MultipartBody body = builder.build();
        Call<ResponseBody> call = service.uploadWithProgress2(configInfo.url,body,configInfo.headers);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Tool.dismiss(configInfo.loadingDialog);

                if (response.isSuccessful()){
                    try {
                        String string = response.body().string();
                        configInfo.listener.onSuccess(string,string);
                    } catch (IOException e) {
                        e.printStackTrace();
                        onFailure(call,e);
                    }

                }else {
                    configInfo.listener.onError(response.code()+"");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Tool.dismiss(configInfo.loadingDialog);
                configInfo.listener.onError(t.toString());
            }
        });

        return call;

    }

    @Override
    protected  Call newDownloadRequest(final ConfigInfo configInfo) {

        ApiService service = null;

        if(configInfo.isIgnoreCer()){
            if(serviceIgnoreSSL == null){
                initSSLIgnore();
            }
            service = serviceIgnoreSSL;
        }else {
            if (serviceDownload == null){
                initUpload();
            }
            service = serviceDownload;
        }
        Call<ResponseBody> call = service.download(configInfo.url,configInfo.headers);
        configInfo.listener.registEventBus();

        configInfo.tagForCancle = call;

        //todo 改成在子线程中执行

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                            configInfo.listener.onError(response.code()+"");
                    Tool.dismiss(configInfo.loadingDialog);
                    return;
                }
                //开子线程将文件写到指定路径中
                SimpleTask<Boolean> simple = new SimpleTask<Boolean>() {
                    @Override
                    protected Boolean doInBackground() {
                        return writeResponseBodyToDisk(response.body(),configInfo.filePath);
                    }
                    @Override
                    protected void onPostExecute(Boolean result) {
                        Tool.dismiss(configInfo.loadingDialog);
                        if (result){
                            configInfo.listener.onSuccess(configInfo.filePath,configInfo.filePath);
                        }else {
                            configInfo.listener.onError("文件下载失败");
                        }
                    }
                };
                simple.execute();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, final Throwable t) {

                        configInfo.listener.onError(t.toString());
                Tool.dismiss(configInfo.loadingDialog);

            }
        });
        return call;
    }
    

    @Override
    protected <E> Call newCommonStringRequest(final ConfigInfo<E> configInfo) {


        RxApiService service2 = service;

       /* if(configInfo.isIgnoreCer()){
            if(serviceIgnoreSSL == null){
                initSSLIgnore();
            }
            service2 = serviceIgnoreSSL;
        }else {
            if (service == null){
                init();
            }
            service2 = service;
        }*/


        if(configInfo.isResponseJsonArray()){//当解析出来后是json数组时
            service2.executGet(configInfo.url,configInfo.params,configInfo.headers)               //获取Observable对象
                    .subscribeOn(Schedulers.from(ThreadPoolFactory.getDownLoadPool().getExecutor()))//传入一个用于网络请求的线程池
                    .observeOn(Schedulers.computation())//json转换,计算密集型
                    .map(new Func1<ResponseBody, List<E>>() {
                        @Override
                        public List<E> call(ResponseBody body) {//在这里实现对json的解析

                            //解析代码示例:
                            try {
                                String str =  body.string();
                                return MyJson.parseArray(str,configInfo.clazz);

                            } catch (IOException e) {
                                throw Exceptions.propagate(e);//将错误抛出,统一到最后的onerror中处理
                            }
                        }
                    })
                    .observeOn(Schedulers.io())         //请求完成后在io线程中执行:缓存某些数据等等.
                    .doOnNext(new Action1<List<E>>() {
                        @Override
                        public void call(List<E> beans) {

                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                    .subscribe(new Subscriber<List<E>>() {
                        @Override
                        public void onCompleted() {
                            Tool.dismiss(configInfo.loadingDialog);
                        }

                        @Override
                        public void onError(Throwable e) {
                            //统一用Throwable的detailMessage来传递说明信息,包括标准json中的msg字段
                            //请求失败
                            Log.e("rxjava",e.getMessage());
                        }

                        @Override
                        public void onNext(List<E> bean) {
                            //请求成功,订阅了的可以收到
                        }
                    });



            return null;
        }

        //jsonobj时,使用下面的解析
        service2.executGet(configInfo.url,configInfo.params,configInfo.headers)               //获取Observable对象
                .subscribeOn(Schedulers.from(ThreadPoolFactory.getDownLoadPool().getExecutor()))//请求在新的线程中执行
                .observeOn(Schedulers.computation())
                .map(new Func1<ResponseBody, E>() {
                    @Override
                    public E call(ResponseBody body) {//在这里实现对json的解析

                        //解析代码示例:(还需要一些处理为空,以及标准json等情况,都是细节...)
                        try {
                            String str =  body.string();
                            if(configInfo.type == ConfigInfo.TYPE_STRING){
                                return (E) str;
                            }else {//还有几种情况...
                                return MyJson.parse(str,configInfo.clazz);
                            }



                        } catch (IOException e) {
                            throw Exceptions.propagate(e);//将错误抛出,统一到最后的onerror中处理
                        }
                    }
                })
                .observeOn(Schedulers.io()) //请求完成后在io线程中执行:缓存某些键值等.
                .doOnNext(new Action1<E>() {
                    @Override
                    public void call(E bean) {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<E>() {//这个Subscriber应该从外部作为参数传入,类似callback
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //统一用Throwable的detailMessage来传递说明信息,包括标准json中的msg字段
                        //请求失败
                        Tool.dismiss(configInfo.loadingDialog);
                        Log.e("rxjava",e.toString());
                    }

                    @Override
                    public void onNext(E bean) {
                        //请求成功
                        Tool.dismiss(configInfo.loadingDialog);
                        Log.e("rxjava",bean.toString());
                        Log.e("rxjava",MyJson.toJsonStr(bean));
                    }
                });
        return null;

    }



    private boolean writeResponseBodyToDisk(ResponseBody body,String path) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(path);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("io", "file download: " + fileSizeDownloaded + " of " + fileSize);//  这里也可以实现进度监听
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

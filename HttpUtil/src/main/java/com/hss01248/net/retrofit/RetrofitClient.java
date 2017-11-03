package com.hss01248.net.retrofit;

/**
 * Created by Administrator on 2016/9/21.
 */
public class RetrofitClient  {


    /*Retrofit retrofit;
    ApiCallService service;


    ApiCallService serviceDownload;
    //需要单独为下载的call设置Retrofit: 主要是超时时间设置为0
    Retrofit retrofitDownload;

    //上传的client
    ApiCallService serviceUpload;
    Retrofit retrofitUpload;

    //完全忽略证书校验的client,用于某些额外的单次请求中
    ApiCallService serviceIgnoreSSL;
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
               *//* .sslSocketFactory(new SSLCertificateSocketFactory(), new X509TrustManager() {
                })*//*
                .build();

        retrofit = new Retrofit
                .Builder()
                .baseUrl(NetDefaultConfig.baseUrl)
                .client(client)
                // .addConverterFactory(GsonConverterFactory.create()) // 使用Gson作为数据转换器
                .build();

        service = retrofit.create(ApiCallService.class);
    }

    private void setHttps(OkHttpClient.Builder httpBuilder) {
        if(HttpsConfig.certificates != null && HttpsConfig.certificates.size()>0){
            httpBuilder.sslSocketFactory(HttpsUtil.getSSLSocketFactory(MyNetApi.context, HttpsConfig.certificates));
        }
    }

    private static RetrofitClient instance;

    private RetrofitClient(){
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

        serviceDownload = retrofitDownload.create(ApiCallService.class);
    }

    private void initUpload() {
        OkHttpClient.Builder httpBuilder=new OkHttpClient.Builder();
        setHttps(httpBuilder);
        OkHttpClient client=httpBuilder.readTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(0, TimeUnit.SECONDS).writeTimeout(0, TimeUnit.SECONDS) //设置超时
                .retryOnConnectionFailure(false)//重试
                // .addInterceptor(new ProgressRequestInterceptor())//上传时更新进度
                .addInterceptor(new UseragentInterceptor())
               *//* .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        return null;
                    }
                })*//*
                .build();

        retrofitUpload = new Retrofit
                .Builder()
                .baseUrl(NetDefaultConfig.baseUrl)
                .client(client)
                .build();

        serviceUpload = retrofitUpload.create(ApiCallService.class);
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

        serviceIgnoreSSL = retrofitIgnoreSSL.create(ApiCallService.class);
    }



    public static RetrofitClient getInstance(){
        if (instance == null){
            synchronized (RetrofitClient.class){
                if (instance ==  null){
                    instance = new RetrofitClient();
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

        ApiCallService service = null;

        if(configInfo.isIgnoreCer() && configInfo.url.startsWith("https")){
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
                    configInfo.listener.onCodeError("http错误码为:"+response.code(),response.message(),response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


               Tool.handleError(t,configInfo);

            }
        });

        return call;

    }

    public Call uploadWithProgress2(final ConfigInfo configInfo){

        ApiCallService service = null;

        if(configInfo.isIgnoreCer() && configInfo.url.startsWith("https")){
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
                    configInfo.listener.onCodeError("http错误码为:"+response.code(),response.message(),response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Tool.handleError(t,configInfo);
            }
        });

        return call;

    }

    @Override
    protected  Call newDownloadRequest(final ConfigInfo configInfo) {

        ApiCallService service = null;

        if(configInfo.isIgnoreCer() && configInfo.url.startsWith("https")){
            if(serviceIgnoreSSL == null){
                initSSLIgnore();
            }
            service = serviceIgnoreSSL;
        }else {
            if (serviceDownload == null){
                initDownload();
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
                    configInfo.listener.onCodeError("http错误码为:"+response.code(),response.message(),response.code());
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
                        if (!result){
                            configInfo.listener.onError("文件下载失败");
                            return;
                        }

                        //文件校验
                        if(configInfo.isVerify){
                            String str = "";
                            if(configInfo.verfyByMd5OrShar1){//md5
                                str = EncryptUtils.encryptMD5File2String(configInfo.filePath);

                            }else {//sha1
                                str = EncryptUtils.encryptSHA1ToString(configInfo.filePath);//todo 缺少shar1文件的算法
                            }
                            if(str.equalsIgnoreCase(configInfo.verifyStr)){//校验通过
                                configInfo.listener.onSuccess(configInfo.filePath,configInfo.filePath);
                                handleMedia(configInfo);
                            }else {
                                configInfo.listener.onError("文件下载失败:校验不一致");
                            }
                        }else {
                            configInfo.listener.onSuccess(configInfo.filePath,configInfo.filePath);
                            handleMedia(configInfo);
                        }

                    }
                };
                simple.execute();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, final Throwable t) {
                Tool.handleError(t,configInfo);
            }
        });
        return call;
    }

    private void handleMedia(ConfigInfo configInfo) {
        if(configInfo.isNotifyMediaCenter){
            FileUtils.refreshMediaCenter(MyNetApi.context,configInfo.filePath);
        }else {
            if(configInfo.isHideFolder){
                FileUtils.hideFile(new File(configInfo.filePath));
            }
        }

        if(configInfo.isOpenAfterSuccess){
            FileUtils.openFile(MyNetApi.context,new File(configInfo.filePath));
        }

    }


    @Override
    protected <E> Call newCommonStringRequest(final ConfigInfo<E> configInfo) {


        ApiCallService service2 = null;

        if(configInfo.isIgnoreCer() && configInfo.url.startsWith("https")){
            if(serviceIgnoreSSL == null){
                initSSLIgnore();
            }
            service2 = serviceIgnoreSSL;
        }else {
            if (service == null){
                init();
            }
            service2 = service;
        }

        Call<ResponseBody> call;
        if (configInfo.method == HttpMethod.GET){
            call = service2.executGet(configInfo.url,configInfo.params,configInfo.headers);
        }else if (configInfo.method == HttpMethod.POST){
            if(configInfo.paramsAsJson){//参数在请求体以json的形式发出
                String jsonStr = MyJson.toJsonStr(configInfo.params);
                Log.e("dd","jsonstr request:"+jsonStr);
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), jsonStr);
                call = service2.executeJsonPost(configInfo.url,body,configInfo.headers);
            }else {
                call = service2.executePost(configInfo.url,configInfo.params,configInfo.headers);
            }
        }else {
            configInfo.listener.onError("不是get或post方法");//暂时不考虑其他方法
            call = null;
            return call;
        }
        configInfo.tagForCancle = call;

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
             *//* String  string =  response.body().string();

                Gson gson = new Gson();
                Type objectType = new TypeToken<E>() {}.getType();
                final E bean = gson.fromJson(string,objectType);
                configInfo.listener.onSuccess(bean,string);*//*

                if (!response.isSuccessful()){
                            configInfo.listener.onCodeError("http错误码为:"+response.code(),response.message(),response.code());
                    Tool.dismiss(configInfo.loadingDialog);
                    return;
                }
                String string = "";

                try {
                    string =  response.body().string();
                    Log.e("string",string);
                    Tool.parseStringByType(string,configInfo);
                    Tool.dismiss(configInfo.loadingDialog);

                } catch (final IOException e) {
                    e.printStackTrace();

                            configInfo.listener.onError(e.toString());
                    Tool.dismiss(configInfo.loadingDialog);

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, final Throwable t) {

                Tool.handleError(t,configInfo);
            }
        });
        return call;
    }



    private boolean writeResponseBodyToDisk(ResponseBody body, String path) {
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

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    protected <E> ConfigInfo<E> getString(ConfigInfo<E> info) {
        return null;
    }

    @Override
    protected <E> ConfigInfo<E> postString(ConfigInfo<E> info) {
        return null;
    }

    @Override
    protected ConfigInfo download(ConfigInfo info) {
        return null;
    }

    @Override
    protected ConfigInfo upload(ConfigInfo info) {
        return null;
    }

    @Override
    public void cancleRequest(Object tag) {

    }

    @Override
    public void cancleAllRequest() {

    }*/
}

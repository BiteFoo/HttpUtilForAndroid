package com.hss01248.http.config;


import com.hss01248.http.builder.*;
import com.hss01248.http.interfaces.IClient;
import com.hss01248.http.util.MyLog;
import com.hss01248.http.util.Tool;
import com.hss01248.http.util.HttpUtil;
import com.hss01248.http.wrapper.MyNetListener;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/3.
 */
public class ConfigInfo<T> {

    private ConfigInfo(){

    }

    public Object request;//跟具体client有关的请求对象


    //核心参数
    public int method ;
    public String url;
    public Map params ;
    public String paramsStr;


    public boolean paramsAsJson ;
    public int type = TYPE_STRING;//请求的类型,6类中的一种

    //回调
    public MyNetListener<T> listener;


    public Class<T> clazz;

    //设置标准格式json本次响应的不同字段
    public String key_data = "";
    public String key_code = "";
    public String key_msg = "";
   // public String key_isSuccess = "";

    public int code_success;
    public int code_unlogin;
    public int code_unFound;

    public boolean isCustomCodeSet;


    public boolean isResponseJsonArray() {
        return isResponseJsonArray;
    }

    private boolean isResponseJsonArray ;

    public boolean isIgnoreCer() {
        return ignoreCer;
    }

    //本次请求是否忽略证书校验--也就是通过所有证书.
// 这个属性没有全局配置,也不建议全局配置. 如果是自签名,放置证书到raw下,并在初始化前addCer方法,即可全局使用https
    private boolean ignoreCer ;

    public boolean isSync;
    public String responseCharset;



    //请求的客户端对象
    public IClient client;

    public ConfigInfo<T> start(){
        client = HttpUtil.getClient();

        validata();
        client.start(this);



        return this;
    }

    /**
     * 参数逻辑校验
     */
    private void validata() {
        String url = Tool.appendUrl(this.url, true);//todo 自动拼接url功能
        this.url = url;
        this.listener.url = url;
        this.listener.configInfo = this;

        //todo 自己实现缓存或者利用okhttp的缓存功能

        //打印调试
        MyLog.json(url);
        MyLog.e("headers-----------------------------------");
        MyLog.json(headers);
        MyLog.e("params-----------------------------------");
        MyLog.json(params);
        MyLog.json(paramsStr);

        //todo 看上传文件在不在


        //todo 看下载路径在不在
    }


    //是否拼接token
    public boolean isAppendToken ;

    //状态为成功时,data对应的字段是否为空
    public boolean isSuccessDataEmpty ;




    //请求头  http://tools.jb51.net/table/http_header
    public Map<String,String> headers ;

    public Map<String,String> cookies ;

   /* public ConfigInfo<T> setHeaders(Map<String,String> headers){
        this.headers = headers;
        return this;
    }*/

    public ConfigInfo<T> addHeaderOfContentType(String contentType){
        this.headers.put("Content-Type",contentType);
        return this;
    }

    public ConfigInfo<T> addHeaderOfAcceptType(String acceptType){
        this.headers.put("Accept",acceptType);
        return this;
    }

    public ConfigInfo<T> addHeaderOfAuthorization(String authorizationMsg){
        this.headers.put("Authorization",authorizationMsg);
        return this;
    }

    public ConfigInfo<T> addHeaderOfContentLength(long contentLength){
        this.headers.put("Content-Length",contentLength+"");
        return this;
    }
    public ConfigInfo<T> addHeaderOfContentLength(String contentStr){
        this.headers.put("Content-Length",contentStr.getBytes().length+"");
        return this;
    }

    /**
     * 文件上传时调用
     * @param file
     * @return
     */
    public ConfigInfo<T> addHeaderOfContentLength(File file){
        this.headers.put("Content-Length",file.length()+"");
        return this;
    }


    public ConfigInfo<T> addHeaderOfRange(long from,long to){
        this.headers.put("Range","bytes="+from+"-"+to);
        return this;
    }
    public ConfigInfo<T> addHeaderOfUserAgent(String agent){
        this.headers.put("User-Agent",agent);
        return this;
    }













    //重試次數
    public int retryCount ;




    //超時設置,ms
    public int timeout ;






    //用于取消请求用的
    public Object tagForCancle = "";



    //緩存控制
   // public boolean forceGetNet = true;
    public boolean shouldReadCache ;
    public boolean shouldCacheResponse;
    public long cacheTime; //单位秒




    public boolean isFromCache ;//内部控制,不让外部设置

    //優先級,备用 volley使用
    public int priority ;






    /**
     * 下载的一些通用策略:  downloadStratege

     * 1. 是否用url中的文件名作为最终的文件名,或者指定文件名
     * 2.如果是图片,音频,视频等多媒体文件,是否在下载完成后让mediacenter扫描一下?
     * 3. 如果是apk文件,是否在下载完成后打开?或者弹窗提示用户?
     * 4. md5校验 : 是否预先提供md5 ,下载完后与文件md5比较,以确定所下载的文件的完整性?
     * 5.断点续传的实现
     * 6.下载队列和指定同时下载文件的个数
     *
     * */
    //下載文件的保存路徑
    public String filePath;
    //是否打開,是否讓媒体库扫描,是否隐藏文件夹
    public boolean isOpenAfterSuccess ;//默认不扫描
    public boolean isHideFolder ;
    public boolean isNotifyMediaCenter ;//媒体文件下载后,默认:通知mediacenter扫描


    //文件校验相关设置(默认不校验)
    public boolean isVerify ;//是否校驗文件
    public String verifyStr;
    public boolean verfyByMd5OrShar1 ;



    //上传的文件路径
    public Map<String, String> files;

    /*//最終的數據類型:普通string,普通json,規範的jsonobj

    public int resonseType = TYPE_STRING;*/





    public static final int TYPE_STRING = 1;//純文本,比如html
    public static final int TYPE_JSON = 2;
    public static final int TYPE_JSON_FORMATTED = 3;//jsonObject包含data,code,msg,數據全在data中,可能是obj,頁可能是array,也可能為空

    public static final int TYPE_DOWNLOAD = 4;
    public static final int TYPE_UPLOAD_WITH_PROGRESS = 5;
    public static final int TYPE_UPLOAD_NONE_PROGRESS = 6;//测试用的

//优先级

    public static final int Priority_LOW = 1;
    public static final int Priority_NORMAL = 2;
    public static final int Priority_IMMEDIATE = 3;
    public static final int Priority_HIGH = 4;

    //http方法



    /* public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }*/






    private void assginValues(BaseNetBuilder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.params = builder.params;
        this.headers = builder.headers;

        this.cacheTime = builder.cacheTime;
        this.isFromCache = builder.isFromCache;
        this.shouldCacheResponse = builder.shouldCacheResponse;
        this.shouldReadCache = builder.shouldReadCache;
        this.ignoreCer = builder.ignoreCer;
        this.listener = builder.listener;
        this.retryCount = builder.retryCount;
        this.timeout = builder.timeout;
        this.type = builder.type;
        this.paramsStr = builder.paramsStr;
        this.isSync = builder.isSync;
        this.responseCharset = builder.responseCharset;


    }

    public ConfigInfo (BaseNetBuilder builder){
        assginValues(builder);
        start();
    }

    public ConfigInfo (StringRequestBuilder builder){
        assginValues(builder);
        this.paramsAsJson = builder.paramsAsJson;
        start();
    }
    public ConfigInfo (JsonRequestBuilder builder){
        assginValues(builder);
        this.paramsAsJson = builder.paramsAsJson;
        this.clazz = builder.clazz;
        this.isResponseJsonArray = builder.isResponseJsonArray;


        start();

    }
    public ConfigInfo (StandardJsonRequestBuilder builder){
        assginValues(builder);
        this.paramsAsJson = builder.paramsAsJson;
        this.clazz = builder.clazz;
        this.isResponseJsonArray = builder.isResponseJsonArray;

        this.code_success = builder.code_success;
        this.code_unlogin = builder.code_unlogin;
        this.code_unFound = builder.code_unFound;
        this.isCustomCodeSet = builder.isCustomCodeSet;
        this.isSuccessDataEmpty = builder.isSuccessDataEmpty;

        this.key_code = builder.key_code;
        this.key_data = builder.key_data;
        this.key_msg = builder.key_msg;


        start();

    }


    public ConfigInfo (DownloadBuilder builder){
        assginValues(builder);
        this.filePath = builder.savedPath;
        this.isOpenAfterSuccess = builder.isOpenAfterSuccess;
        this.isNotifyMediaCenter = builder.isNotifyMediaCenter;
        this.isHideFolder = builder.isHideFolder;

        this.verfyByMd5OrShar1 = builder.verfyByMd5OrShar1;
        this.isVerify = builder.isVerify;
        this.verifyStr = builder.verifyStr;



        start();

    }
    public ConfigInfo (UploadRequestBuilder builder){
        assginValues(builder);
        this.files = builder.files;
        start();
    }





}

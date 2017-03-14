package com.hss01248.net.config;

import android.app.Dialog;
import android.content.DialogInterface;

import com.hss01248.net.builder.BaseNetBuilder;
import com.hss01248.net.builder.DownloadBuilder;
import com.hss01248.net.builder.IClient;
import com.hss01248.net.builder.JsonRequestBuilder;
import com.hss01248.net.builder.StandardJsonRequestBuilder;
import com.hss01248.net.builder.StringRequestBuilder;
import com.hss01248.net.builder.UploadRequestBuilder;
import com.hss01248.net.interfaces.HttpMethod;
import com.hss01248.net.wrapper.HttpUtil;
import com.hss01248.net.wrapper.MyLog;
import com.hss01248.net.wrapper.MyNetListener;
import com.hss01248.net.wrapper.Tool;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.internal.http.HttpDate;

/**
 * Created by Administrator on 2016/9/3.
 */
public class ConfigInfo<T> {

    public ConfigInfo(){

    }
    public Object request;//跟具体client有关的请求对象

    //核心参数
    public int method = HttpMethod.GET;
    public String url;
    public Map params ;
    public String paramsStr;



    public boolean paramsAsJson = false;
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






    public boolean isResponseJsonArray = false;




    public    int cacheMode = GlobalConfig.get().getCacheMode();
    public int cookieMode;


//本次请求是否忽略证书校验--也就是通过所有证书.
// 这个属性没有全局配置,也不建议全局配置. 如果是自签名,放置证书到raw下,并在初始化前addCer方法,即可全局使用https
    public boolean ignoreCer = false;







    //请求的客户端对象
    public IClient client;

    public ConfigInfo<T> start(){
        if(client == null){
            client = HttpUtil.getClient();
        }
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
       /* MyLog.json(url);
        MyLog.e("headers-----------------------------------");
        MyLog.json(headers);
        MyLog.e("params-----------------------------------");
        MyLog.json(params);
        MyLog.json(paramsStr);*/

        //todo 看上传文件在不在


        //todo 看下载路径在不在


        //todo dialog的取消网络请求
        //Tool.(loadingDialog)
        if(loadingDialog!=null ){

            if(tagForCancle ==null){
                tagForCancle = UUID.randomUUID().toString();
            }



            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MyLog.i("取消请求中.......");
                    HttpUtil.cancelRquest(tagForCancle);
                   // listener.onCancel();//这里不需要回调,因为在执行过程中相应地方都会有回调

                }
            });
        }
    }


    //是否拼接token
    public boolean isAppendToken = true;

    //状态为成功时,data对应的字段是否为空
    public boolean isSuccessDataEmpty = true;
    /*public ConfigInfo<T> setFailWhenDataIsEmpty(){
        this.isSuccessDataEmpty = false;
        return this;
    }*/









    //请求头  http://tools.jb51.net/table/http_header
    public Map<String,String> headers = new HashMap<>();


    //重試次數
    public int retryCount ;


    public int timeout ;




    /**
     *
     * @param loadingMsg 提示语
     * @param activity  Context ,最好传入activity,当然context也可以
     * @return
     *//*
    public ConfigInfo<T> setShowLoadingDialog( Activity activity,String loadingMsg){
        return setShowLoadingDialog(null,loadingMsg,activity,0);
    }

    *//**
     *
     * @return
     *//*
    public ConfigInfo<T> setShowLoadingDialog(Dialog loadingDialog){

        return  setShowLoadingDialog(loadingDialog,"",null,0);
    }



    private ConfigInfo<T> setShowLoadingDialog(Dialog loadingDialog, String msg, Activity activity, int minTime){
        if (loadingDialog == null){
            if (TextUtils.isEmpty(msg)){
                msg = "加载中...";
            }
            if (activity == null){
                this.loadingDialog = null;//todo 生成dialog,先不显示
            }else {
                try {
                    this.loadingDialog = ProgressDialog.show(activity, "", msg,false, true);
                }catch (Exception e){
                    e.printStackTrace();

                }
            }

        }else {
            this.loadingDialog = loadingDialog;
        }

      *//*  this.isForceMinTime = true;

        if (minTime >NetDefaultConfig.TIME_MINI){
            this.minTime = minTime;
        }else {
            this.minTime = NetDefaultConfig.TIME_MINI;
        }*//*

        return this;
    }*/



   // public boolean isForceMinTime = false;

    public Dialog loadingDialog;
    public boolean isLoadingDialogHorizontal;
    public boolean updateProgress ;



    //用于取消请求用的
    public Object tagForCancle ;




    //緩存控制
   // public boolean forceGetNet = true;
    public boolean shouldReadCache = false;
    public boolean shouldCacheResponse = false;
    public long cacheTime = HttpDate.MAX_DATE; //单位秒




    public boolean isFromCache = false;//内部控制,不让外部设置

    //優先級,备用 volley使用
    public int priority = Priority_NORMAL;







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

    public boolean isSync;
    public String responseCharset;

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
        this.ignoreCer = builder.ignoreCertificateVerify;
        this.listener = builder.listener;
        this.retryCount = builder.retryCount;
        this.timeout = builder.timeout;
        this.type = builder.type;
        this.loadingDialog = builder.loadingDialog;
        this.paramsStr = builder.paramsStr;
        this.isSync = builder.isSync;
        this.responseCharset = builder.responseCharset;
        this.cookieMode = builder.cookieMode;
        this.cacheMode = builder.cacheMode;
        this.tagForCancle = builder.tagForCancle;


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

        this.updateProgress = builder.updateProgress;
        this.isLoadingDialogHorizontal = builder.isLoadingDialogHorizontal;

        start();

    }
    public ConfigInfo (UploadRequestBuilder builder){
        assginValues(builder);
        this.files = builder.files;

        this.updateProgress = builder.updateProgress;
        this.isLoadingDialogHorizontal = builder.isLoadingDialogHorizontal;


        start();
    }





}

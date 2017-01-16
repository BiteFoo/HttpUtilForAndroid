package com.hss01248.net.builder;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.text.TextUtils;

import com.hss01248.net.config.HttpMethod;
import com.hss01248.net.config.NetDefaultConfig;
import com.hss01248.net.interfaces.INet;
import com.hss01248.net.wrapper.MyNetListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/3.
 */
public class NetConfig<T> {


    //核心参数
    public int method ;
    public String url;
    public Map params ;



    public boolean paramsAsJson ;
    public int type = TYPE_STRING;//请求的类型,6类中的一种

    //回调
    public MyNetListener<T> listener;


    public Class<T> clazz;

    //设置标准格式json本次响应的不同字段
    public String key_data ;
    public String key_code ;
    public String key_msg ;
   // public String key_isSuccess = "";

    public int code_success;
    public int code_unlogin;
    public int code_unFound;

    public boolean isCustomCodeSet;
    private String savedPath;


    //TODO builder的字段拷贝.其中默认值应该在各builder中初始化好,而不是这里初始化
    public NetConfig (StringRequestBuilder stringRequestBuilder){

        start();

    }
    public NetConfig (JsonRequestBuilder jsonRequestBuilder){

        start();

    }
    public NetConfig (StandardJsonRequestBuilder standardJsonRequestBuilder){

        start();

    }
    public NetConfig (DownloadBuilder stringRequestBuilder){

        start();

    }
    public NetConfig (UploadRequestBuilder stringRequestBuilder){

        start();
    }
    public NetConfig (BaseNetBuilder stringRequestBuilder){


        start();
    }



    public boolean isResponseJsonArray() {
        return isResponseJsonArray;
    }



    private boolean isResponseJsonArray = false;





//本次请求是否忽略证书校验--也就是通过所有证书.
// 这个属性没有全局配置,也不建议全局配置. 如果是自签名,放置证书到raw下,并在初始化前addCer方法,即可全局使用https
    private boolean ignoreCer = false;



    public boolean isIgnoreCer() {
        return ignoreCer;
    }






    //请求的客户端对象
    public INet client;

    public NetConfig<T> start(){
       // client.start(this);
        return this;
    }


    //是否拼接token
    public boolean isAppendToken = true;

    //状态为成功时,data对应的字段是否为空
    public boolean isSuccessDataEmpty = true;




    //请求头  http://tools.jb51.net/table/http_header
    public Map<String,String> headers ;

    //重試次數
    public int retryCount ;


    //超時設置,ms
    public int timeout ;

    public Dialog loadingDialog;

    //用于取消请求用的
    public Object tagForCancle = "";



    //緩存控制
   // public boolean forceGetNet = true;
    public boolean shouldReadCache = false;
    public boolean shouldCacheResponse = false;
    public long cacheTime = NetDefaultConfig.CACHE_TIME; //单位秒




    public boolean isFromCache = false;//内部控制,不让外部设置

    //優先級,备用 volley使用
    public int priority = Priority_NORMAL;



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











}

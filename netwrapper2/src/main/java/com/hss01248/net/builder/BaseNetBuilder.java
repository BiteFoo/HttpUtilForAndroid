package com.hss01248.net.builder;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.text.TextUtils;

import com.hss01248.net.config.NetDefaultConfig;
import com.hss01248.net.wrapper.MyNetListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class BaseNetBuilder<T> {

    public Map<String,String> params;
    public Map<String,String> headers;
    public MyNetListener<T> listener;
    public String url;
    public int method;


    //todo 以下是http请求基本组成
    public BaseNetBuilder<T> addHeader(String key,String value){
        if(headers == null){
            headers = new HashMap<>();
        }
        headers.put(key,value);
        return this;
    }

    public BaseNetBuilder<T> addParams(String key,String value){
        if(params == null){
            params = new HashMap<>();
        }
        params.put(key,value);
        return this;
    }

    public BaseNetBuilder<T> callback(MyNetListener<T> listener){
        this.listener = listener;
        return this;
    }

    public BaseNetBuilder<T> url(String url ){
        this.url = url;
        return this;
    }

    public NetConfig<T> get(){
        method = NetDefaultConfig.Method.GET;
        //client.start(this);
      return   execute();

    }

    public NetConfig<T> post(){
        method = NetDefaultConfig.Method.POST;
        // client.start(this);
        return  execute();

    }

    public NetConfig<T> execute(){
        return new NetConfig(this);
    }



    //TODO 以下是UI显示控制
    public Dialog loadingDialog;
    public BaseNetBuilder<T> showLoadingDialog(Activity activity, String loadingMsg){
        return setShowLoadingDialog(null,loadingMsg,activity);
    }
    /**
     *
     * @return
     */
    public BaseNetBuilder<T> showLoadingDialog(Dialog loadingDialog){

        return  setShowLoadingDialog(loadingDialog,"",null);
    }

    private BaseNetBuilder<T> setShowLoadingDialog(Dialog loadingDialog, String msg, Activity activity){
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

      /*  this.isForceMinTime = true;

        if (minTime >NetDefaultConfig.TIME_MINI){
            this.minTime = minTime;
        }else {
            this.minTime = NetDefaultConfig.TIME_MINI;
        }*/

        return this;
    }

    //todo 以下是缓存控制策略
    public boolean shouldReadCache = false;
    public boolean shouldCacheResponse = false;
    public long cacheTime = NetDefaultConfig.CACHE_TIME; //单位秒
    public boolean isFromCache = false;//内部控制,不让外部设置
    /**
     * 只支持String和json类型的请求,不支持文件下载的缓存.
     * @param shouldReadCache 是否先去读缓存
     * @param shouldCacheResponse 是否缓存response  内部已做判断,只会缓存状态是成功的那些请求
     * @param cacheTimeInSeconds 缓存的时间,单位是秒
     * @return
     *

     */
    public BaseNetBuilder<T> setCacheControl(boolean shouldReadCache,boolean shouldCacheResponse,long cacheTimeInSeconds){
        this.shouldReadCache = shouldReadCache;
        this.shouldCacheResponse = shouldCacheResponse;
        this.cacheTime = cacheTimeInSeconds;
        return this;

    }




    //todo 以下是超时以及重试策略
    public int retryCount = NetDefaultConfig.RETRY_TIME;
    public int timeout = NetDefaultConfig.TIME_OUT;

    public BaseNetBuilder<T> setRetryCount(int retryCount){
        this.retryCount = retryCount;
        return this;
    }

    public BaseNetBuilder<T> setTimeout(int timeoutInMills){
        this.timeout = timeoutInMills;
        return this;
    }

    //TODO https自签名证书的处理策略:单个请求是否忽略
    public boolean ignoreCer = false;

    public BaseNetBuilder<T> setIgnoreCer() {
        this.ignoreCer = true;
        return this;
    }

    public boolean isIgnoreCer() {
        return ignoreCer;
    }



    //TODO token身份验证字段的拼接
    public boolean isAppendToken = false;//默认没有token验证
    public boolean isInHeaderOrParam = false;//默认在参数体中传递
    public BaseNetBuilder<T> setIsAppendToken(boolean isAppendToken,boolean isInHeaderOrParam){
        this.isAppendToken = isAppendToken;
        this.isInHeaderOrParam = isInHeaderOrParam;
        return this;
    }

    public BaseNetBuilder<T> setTag(Object object){
        return this;
    }




}

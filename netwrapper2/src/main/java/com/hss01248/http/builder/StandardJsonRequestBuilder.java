package com.hss01248.http.builder;


import com.hss01248.http.config.BaseNetBean;
import com.hss01248.http.config.ConfigInfo;
import com.hss01248.http.config.NetDefaultConfig;
import com.hss01248.http.wrapper.MyNetListener;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class StandardJsonRequestBuilder <T> extends JsonRequestBuilder{


    public StandardJsonRequestBuilder(){
        this.type = ConfigInfo.TYPE_JSON_FORMATTED;
        this.key_code = NetDefaultConfig.KEY_CODE;
        this.key_data = NetDefaultConfig.KEY_DATA;
        this.key_msg = NetDefaultConfig.KEY_MSG;
        this.code_success = BaseNetBean.CODE_SUCCESS;
        this.code_unlogin = BaseNetBean.CODE_UNLOGIN;
        this.code_unFound = BaseNetBean.CODE_UN_FOUND;
        isCustomCodeSet = false;
        isSuccessDataEmpty = true;
    }







    //todo 设置标准格式json本次响应的不同字段
    public String key_data = "";
    public String key_code = "";
    public String key_msg = "";
    // public String key_isSuccess = "";

    public int code_success;
    public int code_unlogin;
    public int code_unFound;

    public boolean isCustomCodeSet ;

    /**
     * 单个请求的
     * @param keyData
     * @param keyCode
     * @param keyMsg

     * @return
     */
    public StandardJsonRequestBuilder<T> setStandardJsonKey(String keyData,String keyCode,String keyMsg){
        this.key_data = keyData;
        this.key_code = keyCode;
        this.key_msg = keyMsg;
        return this;
    }

    /**
     * 单个请求的code的key可能会不一样
     * @param keyCode
     * @return
     */
    public StandardJsonRequestBuilder<T> setStandardJsonKeyCode(String keyCode){
        this.key_code = keyCode;
        return this;

    }

    /**
     * 单个请求用到的code的具体值
     * @param code_success
     * @param code_unlogin
     * @param code_unFound
     * @return
     */
    public StandardJsonRequestBuilder<T> setCustomCodeValue(int code_success,int code_unlogin,int code_unFound){
        this.code_success = code_success;
        this.code_unlogin = code_unlogin;
        this.code_unFound = code_unFound;
        isCustomCodeSet = true;
        return this;
    }


    //todo 状态为成功时,data对应的字段是否为空
    public boolean isSuccessDataEmpty ;
    public StandardJsonRequestBuilder<T> setFailWhenDataIsEmpty(){
        this.isSuccessDataEmpty = false;
        return this;
    }





    //todo 以下的都是复写基类的方法,强转成子类


    @Override
    public StandardJsonRequestBuilder paramsStr(String paramsStr) {
        return (StandardJsonRequestBuilder) super.paramsStr(paramsStr);
    }

    @Override
    public StandardJsonRequestBuilder url(String url) {
        return (StandardJsonRequestBuilder) super.url(url);
    }

    @Override
    public StandardJsonRequestBuilder addHeader(String key, String value) {
        return (StandardJsonRequestBuilder) super.addHeader(key, value);
    }

    @Override
    public StandardJsonRequestBuilder addParams(String key, String value) {
        return (StandardJsonRequestBuilder) super.addParams(key, value);
    }

    @Override
    public StandardJsonRequestBuilder callback(MyNetListener listener) {
        return (StandardJsonRequestBuilder) super.callback(listener);
    }





    @Override
    public StandardJsonRequestBuilder setCacheControl(boolean shouldReadCache, boolean shouldCacheResponse, long cacheTimeInSeconds) {
        return (StandardJsonRequestBuilder) super.setCacheControl(shouldReadCache, shouldCacheResponse, cacheTimeInSeconds);
    }

    @Override
    public StandardJsonRequestBuilder setRetryCount(int retryCount) {
        return (StandardJsonRequestBuilder) super.setRetryCount(retryCount);
    }

    @Override
    public StandardJsonRequestBuilder setTimeout(int timeoutInMills) {
        return (StandardJsonRequestBuilder) super.setTimeout(timeoutInMills);
    }

    @Override
    public StandardJsonRequestBuilder setIgnoreCer() {
        return (StandardJsonRequestBuilder) super.setIgnoreCer();
    }

    @Override
    public StandardJsonRequestBuilder setIsAppendToken(boolean isAppendToken, boolean isInHeaderOrParam) {
        return (StandardJsonRequestBuilder) super.setIsAppendToken(isAppendToken, isInHeaderOrParam);
    }

    //复写string


    @Override
    public StandardJsonRequestBuilder setJsonClazz(Class clazz) {
        return (StandardJsonRequestBuilder) super.setJsonClazz(clazz);
    }

    @Override
    public StandardJsonRequestBuilder setParamsAsJson() {
        return (StandardJsonRequestBuilder) super.setParamsAsJson();
    }

    @Override
    public StandardJsonRequestBuilder setResponseJsonArray() {
        return (StandardJsonRequestBuilder) super.setResponseJsonArray();
    }


}

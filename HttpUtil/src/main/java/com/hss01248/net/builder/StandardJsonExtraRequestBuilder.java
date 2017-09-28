package com.hss01248.net.builder;

import android.app.Activity;
import android.app.Dialog;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.config.GlobalConfig;
import com.hss01248.net.wrapper.MyNetListener;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class StandardJsonExtraRequestBuilder<T> extends JsonRequestBuilder{


    public StandardJsonExtraRequestBuilder(){
        this.type = ConfigInfo.TYPE_JSON_FORMATTED_EXTRA;
        this.key_code = GlobalConfig.get().getStandardJsonKeyCode();
        this.key_data = GlobalConfig.get().getStandardJsonKeyData();
        this.key_msg = GlobalConfig.get().getStandardJsonKeyMsg();
        this.code_success = GlobalConfig.get().getCodeSuccess();
        this.code_unlogin = GlobalConfig.get().getCodeUnlogin();
        this.code_unFound = GlobalConfig.get().getCodeUnfound();
        isCustomCodeSet = false;
        isTreatEmptyDataAsSuccess  = true;//todo quanju
    }







    //todo 设置标准格式json本次响应的不同字段
    public String key_data = "";
    public String key_code = "";
    public String key_msg = "";
    // public String key_isSuccess = "";

    //不那么标准的json
    public String key_isSuccess = GlobalConfig.get().key_isSuccess;
   // public boolean isKeyCodeInt = GlobalConfig.get().isKeyCodeInt;//code对应的字段是int还是String
    public String key_extra1 = GlobalConfig.get().key_extra1;//json外层额外的字段,如果为空就说明没有
    public String key_extra2 = GlobalConfig.get().key_extra2;
    public String key_extra3 = GlobalConfig.get().key_extra3;

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
    public StandardJsonExtraRequestBuilder<T> setStandardJsonKey(String keyData, String keyCode, String keyMsg){
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
    public StandardJsonExtraRequestBuilder<T> setStandardJsonKeyCode(String keyCode){
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
    public StandardJsonExtraRequestBuilder<T> setCustomCodeValue(int code_success, int code_unlogin, int code_unFound){
        this.code_success = code_success;
        this.code_unlogin = code_unlogin;
        this.code_unFound = code_unFound;
        isCustomCodeSet = true;
        return this;
    }


    //todo 状态为成功时,data对应的字段是否为空
    public boolean isTreatEmptyDataAsSuccess ;

    /**
     * 设置 当data对应字段为空时,回调是成功还是失败
     * 默认是成功.
     * 只有当data预期为jsonobject时,这个设置才生效
     */
    public StandardJsonExtraRequestBuilder<T> setTreatEmptyDataStrAsSuccess(boolean treatEmptyDataAsSuccess){
        this.isTreatEmptyDataAsSuccess = treatEmptyDataAsSuccess;
        return this;
    }


    @Override
    protected ConfigInfo execute() {
        //做一些参数合理性校验
        if(clazz ==null){
            throw new RuntimeException("没有设置clazz参数");
        }


        return new ConfigInfo(this);
    }


    //todo 以下的都是复写基类的方法,强转成子类

    @Override
    public StandardJsonExtraRequestBuilder url(String url) {
        return (StandardJsonExtraRequestBuilder) super.url(url);
    }

    @Override
    public StandardJsonExtraRequestBuilder addHeader(String key, String value) {
        return (StandardJsonExtraRequestBuilder) super.addHeader(key, value);
    }

    @Override
    public StandardJsonExtraRequestBuilder addParam(String key, String value) {
        return (StandardJsonExtraRequestBuilder) super.addParam(key, value);
    }

    @Override
    public StandardJsonExtraRequestBuilder addHeaders(Map headers) {
        return (StandardJsonExtraRequestBuilder) super.addHeaders(headers);
    }

    @Override
    public StandardJsonExtraRequestBuilder addParams(Map params) {
        return (StandardJsonExtraRequestBuilder) super.addParams(params);
    }

    @Override
    public StandardJsonExtraRequestBuilder addParamsInString(String paramsStr) {
        return (StandardJsonExtraRequestBuilder) super.addParamsInString(paramsStr);
    }

    @Override
    public StandardJsonExtraRequestBuilder callback(MyNetListener listener) {
        return (StandardJsonExtraRequestBuilder) super.callback(listener);
    }


    @Override
    public StandardJsonExtraRequestBuilder showLoadingDialog() {
        return (StandardJsonExtraRequestBuilder) super.showLoadingDialog();
    }

    @Override
    public StandardJsonExtraRequestBuilder showLoadingDialog(Activity activity) {
        return (StandardJsonExtraRequestBuilder) super.showLoadingDialog(activity);
    }

    @Override
    public StandardJsonExtraRequestBuilder showLoadingDialog(String loadingMsg) {
        return (StandardJsonExtraRequestBuilder) super.showLoadingDialog(loadingMsg);
    }

    @Override
    public StandardJsonExtraRequestBuilder showLoadingDialog(Activity activity, String loadingMsg) {
        return (StandardJsonExtraRequestBuilder) super.showLoadingDialog(activity, loadingMsg);
    }

    @Override
    public StandardJsonExtraRequestBuilder showLoadingDialog(Dialog loadingDialog) {
        return (StandardJsonExtraRequestBuilder) super.showLoadingDialog(loadingDialog);
    }
    @Override
    public StandardJsonExtraRequestBuilder setCacheMode(int cacheMode) {
        return (StandardJsonExtraRequestBuilder) super.setCacheMode(cacheMode);
    }

    /*@Override
    public StandardJsonRequestBuilder setCacheControl(boolean shouldReadCache, boolean shouldCacheResponse, long cacheTimeInSeconds) {
        return (StandardJsonRequestBuilder) super.setCacheControl(shouldReadCache, shouldCacheResponse, cacheTimeInSeconds);
    }*/

    @Override
    public StandardJsonExtraRequestBuilder setRetryCount(int retryCount) {
        return (StandardJsonExtraRequestBuilder) super.setRetryCount(retryCount);
    }

    @Override
    public StandardJsonExtraRequestBuilder setTimeout(int timeoutInMills) {
        return (StandardJsonExtraRequestBuilder) super.setTimeout(timeoutInMills);
    }

    @Override
    public StandardJsonExtraRequestBuilder setIgnoreCertificateVerify() {
        return (StandardJsonExtraRequestBuilder) super.setIgnoreCertificateVerify();
    }



    //复写string


    @Override
    public StandardJsonExtraRequestBuilder setJsonClazz(Class clazz) {
        return (StandardJsonExtraRequestBuilder) super.setJsonClazz(clazz);
    }

    @Override
    public StandardJsonExtraRequestBuilder setParamsAsJson() {
        return (StandardJsonExtraRequestBuilder) super.setParamsAsJson();
    }

    @Override
    public StandardJsonExtraRequestBuilder setResponseJsonArray() {
        return (StandardJsonExtraRequestBuilder) super.setResponseJsonArray();
    }

    @Override
    public StandardJsonExtraRequestBuilder setExtraTag(Object extraTag) {
        return (StandardJsonExtraRequestBuilder) super.setExtraTag(extraTag);
    }

    @Override
    public StandardJsonExtraRequestBuilder setCacheMaxAge(int cacheMaxAge) {
        return (StandardJsonExtraRequestBuilder) super.setCacheMaxAge(cacheMaxAge);
    }

    @Override
    public StandardJsonExtraRequestBuilder setAppendCommonHeaders(boolean appendCommonHeaders) {
        return (StandardJsonExtraRequestBuilder) super.setAppendCommonHeaders(appendCommonHeaders);
    }

    @Override
    public StandardJsonExtraRequestBuilder setAppendCommonParams(boolean appendCommonParams) {
        return (StandardJsonExtraRequestBuilder) super.setAppendCommonParams(appendCommonParams);
    }
}

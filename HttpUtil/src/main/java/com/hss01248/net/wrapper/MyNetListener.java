package com.hss01248.net.wrapper;


import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.interfaces.HttpCallback;
import com.hss01248.net.interfaces.NetState;

import java.util.List;

/**
 * Created by Administrator on 2016/4/15 0015.
 */
//@MainThread
public abstract class MyNetListener<T>  implements HttpCallback<T>{

    public String url;
    /**
     * 用于回调时判断是否是特定的url和参数发出的请求.
     */
    public ConfigInfo configInfo;


    public void setNetState(int netState) {
        this.netState = netState;
    }

    public int netState;

    public int getNetState(){
        return netState;
    }

    public boolean isNeting(){
        return netState == NetState.LOADING;
    }

    /**
     * called when the request is success bug data is empty
     */
    public  void onEmpty(){}

    /**
     *提供给开发者的校验通过后,执行前最后一步的调用
     * @return
     */
    public void onPreExecute() {
    }

    /**
     * 提供给开发者的额外 的校验接口
     * @param config
     * @return
     */
    public boolean onPreValidate(ConfigInfo config) {
        return true;
    }

    public void onNoNetwork(){
        onError("no network connection");
    }

    public void onTimeout(){
        onError("connect time out,please check your network");
    }
    public boolean isResponseFromCache(){
        return configInfo.isFromCache;
    }

    /** Called when response success. */
    public abstract void onSuccess(T response,String responseStr,boolean isFromCache);

    public  void onSuccessArr(List<T> response, String resonseStr,boolean isFromCache){

    }

    public  void onSuccessObj(T response, String responseStr, String data, int code, String msg,boolean isFromCache){
            onSuccess(response,responseStr,isFromCache);
    }

    public  void onSuccessArr(List<T> response, String responseStr, String data, int code, String msg,boolean isFromCache){
        onSuccessArr(response,responseStr,isFromCache);
    }


    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     */
    public void onError(String msgCanShow) {

    }


    /**
     * 有错误码的error

     * @param code
     */
    public void onCodeError(String msgCanShow, String hiddenMsg, int code) {
        if (msgCanShow==null || msgCanShow.equals("")){
            onError("错误码为:"+code);
        }else {
            onError(msgCanShow);
        }
    }

    @Override
    public void onError(String code, String serverMsg, String exceptionMsg) {
        onError(serverMsg);
    }

    /**
     * 取消的请求走空,不要再回调到onError
     */
    public void onCancel() {
        //onError("请求已取消");
    }

    public void onUnFound() {
        onError("没有找到该内容");
    }
    public    void onUnlogin(){
        onError("您还没有登录");
    }



    /**
     * 都是B作为单位
     */
    public void onProgressChange(long transPortedBytes, long totalBytes) {
        //MyLog.e("transPortedBytes:"+transPortedBytes+"--totalBytes:"+totalBytes);
    }

    /**
     *
     * @param transPortedBytes
     * @param totalBytes
     * @param fileIndex
     * @param filesCount 总的上传文件数量
     */
    public void onFilesUploadProgress(long transPortedBytes, long totalBytes,int fileIndex,int filesCount) {
        MyLog.e("FilesUploadprogress:"+transPortedBytes+"--totalBytes:"+totalBytes+"--fileIndex:"+fileIndex+"-----filecount:"+filesCount);
        onProgressChange(transPortedBytes,totalBytes);
    }


}

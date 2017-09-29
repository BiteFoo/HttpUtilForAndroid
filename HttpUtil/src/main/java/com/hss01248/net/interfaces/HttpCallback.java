package com.hss01248.net.interfaces;

import java.util.List;

/**
 * Created by huangshuisheng on 2017/9/28.
 */

public interface HttpCallback<T> {




    void onNoNetwork();

    void onTimeout();

    void onCancel();

    void onUnlogin();




    void onEmpty();




    void onProgressChange(long transPortedBytes, long totalBytes);
    void onFilesUploadProgress(long transPortedBytes, long totalBytes,int fileIndex,int filesCount);



    void onSuccess(T obj,String responseStr,boolean isFromCache);
    void onSuccessArr(List<T> arr,String responseStr,boolean isFromCache);


    void onError(String msgCanShow);
    void onError(String code,String serverMsg,String exceptionMsg);
    void onCodeError(String msgCanShow, String hiddenMsg, int code);

}

package com.hss01248.net.wrapper;


import android.app.ProgressDialog;
import android.text.TextUtils;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.interfaces.IListener;
import com.hss01248.net.retrofit.progress.ProgressEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Administrator on 2016/4/15 0015.
 */
public abstract class MyNetListener<T> implements IListener<T> {

    public String url;
    public ConfigInfo configInfo;




    /**
     * called when the request is success bug data is empty
     */
    public  void onEmpty(){}

    public void onPreExecute() {}

    public void onNoNetwork(){
        onError("当前没有网络");
    }

    public void onTimeout(){
        onError("连接超时,请检查网络");
    }



    /** Called when response success. */
    public abstract void onSuccess(T response,String resonseStr);

    public  void onSuccessArr(List<T> response,String resonseStr){

    }

    public  void onSuccessObj(T response,String responseStr,String data,int code,String msg){
            onSuccess(response,responseStr);
    }

    public  void onSuccessArr(List<T> response, String responseStr, String data, int code, String msg){
        onSuccessArr(response,responseStr);
    }





    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     */
    public void onError(String msgCanShow) {}


    /**
     * 有错误码的error

     * @param code
     */
    public void onCodeError(String msgCanShow,String hiddenMsg,int code) {
        if (TextUtils.isEmpty(msgCanShow)){
            onError("错误码为:"+code);
        }else {
            onError(msgCanShow);
        }
    }


    public void onCancel() {}

    public void onUnFound() {
        onError("没有找到该内容");
    }
    public    void onUnlogin(){
        onError("您还没有登录");
    }



    /**
     * 都是B作为单位
     */
    public void onProgressChange(long fileSize, long downloadedSize) {}

    public void onFinish(){}


    public void onNetworking() {}

    /** Inform when the cache already use,
     * it means http networking won't execute. */
    public void onUsedCache() {}


    public void onRetry() {}


    /**
     * 只需要在retrofit下载时调用
     */
    public void registEventBus(){
        EventBus.getDefault().register(this);
    }

    public void unRegistEventBus(){
        EventBus.getDefault().unregister(this);
    }

    private long lastProgress;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onMessage(ProgressEvent event){
        if (event.url.equals(url)){
             if(lastProgress >= event.totalBytesRead){
                return;
            }

            lastProgress = event.totalBytesRead;
            onProgressChange(event.totalLength,event.totalBytesRead);


            //进度更新: 数据太会出现
            if(configInfo != null && configInfo.loadingDialog instanceof ProgressDialog){
                ProgressDialog dialog = (ProgressDialog) configInfo.loadingDialog;
                if(configInfo.updateProgress){
                    if(configInfo.isLoadingDialogHorizontal){

                        if(event.totalLength > Integer.MAX_VALUE){
                            dialog.setProgress((int) (event.totalBytesRead*0.00001));
                            dialog.setMax((int) (event.totalLength*0.00001));
                        }else {
                            dialog.setProgress((int) event.totalBytesRead);
                            dialog.setMax((int) event.totalLength);
                        }




                    }else {
                        String str = "加载中...";
                        if(configInfo.type == ConfigInfo.TYPE_DOWNLOAD){
                            str = "下载载中...";
                        }else  if(configInfo.type == ConfigInfo.TYPE_UPLOAD_WITH_PROGRESS) {
                            str = "文件上传...";
                        }
                        dialog.setMessage(str+(event.totalBytesRead*100/event.totalLength)+"%");
                    }
                }
            }

            if (event.done){
                unRegistEventBus();
                onFinish();
            }
        }
    }


}

package com.hss01248.net.wrapper;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.interfaces.NetState;

import java.util.List;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class ListenerDector<T> extends MyNetListener<T> {
    MyNetListener listener;

    public ListenerDector(MyNetListener<T> listener){
        this.listener = listener;
        this.configInfo = listener.configInfo;
        this.url = listener.url;
    }


    @Override
    public boolean isNeting() {
        return listener.isNeting();
    }

    @Override
    public int getNetState() {
        return listener.getNetState();
    }

    @Override
    public void setNetState(int netState) {
        listener.setNetState(netState);
    }

    @Override
    public void onSuccess(final T response, final String responseStr, final boolean isFromCache) {
        postDelay(new Runnable() {
            @Override
            public void run() {
                if(!isFromCache)
                listener.setNetState(NetState.FINISHED);
                listener.onSuccess(response, responseStr,isFromCache);
            }
        });

    }

    private void postDelay(final Runnable runnable) {
        int delaytime = 0;

        if(configInfo.forceMinTime && configInfo.netStartTime>0){
            long timePassed = System.currentTimeMillis() - configInfo.netStartTime;
            int timeLeft = (int) (500 - timePassed);
            if(timeLeft <= 0){
                delaytime = 0;
            }else {
                delaytime = timeLeft;
            }
            MyLog.e("starttime:"+ configInfo.netStartTime + "--end time:"+ System.currentTimeMillis());
            MyLog.e("timeLeft:" + timeLeft);
        }else {
            delaytime = 0;
        }


        Tool.callbackOnMainThread(new Runnable() {
            @Override
            public void run() {
                Tool.dismiss(configInfo.loadingDialog);
                runnable.run();
            }
        }, delaytime);
    }

    @Override
    public void onSuccessArr(final List<T> response, final String responseStr, final boolean isFromCache) {
        postDelay(new Runnable() {
            @Override
            public void run() {
                if(!isFromCache)
                    listener.setNetState(NetState.FINISHED);
                listener.onSuccessArr(response,responseStr,isFromCache);
            }
        });
    }

    @Override
    public void onSuccessArr(final List<T> response, final String responseStr, final String data, final int code, final String msg, final boolean isFromCache) {
       // super.onSuccessArr(response, responseStr, data, code, msg, isFromCache);
        postDelay(new Runnable() {
            @Override
            public void run() {
                if(!isFromCache)
                    listener.setNetState(NetState.FINISHED);
                listener.onSuccessArr(response,responseStr,data,code,msg,isFromCache);
            }
        });
    }

    @Override
    public void onSuccessObj(final T response, final String responseStr, final String data, final int code, final String msg, final boolean isFromCache) {
        //super.onSuccessObj(response, responseStr, data, code, msg, isFromCache);
        postDelay(new Runnable() {
            @Override
            public void run() {
                if(!isFromCache)
                    listener.setNetState(NetState.FINISHED);
                listener.onSuccessObj(response,responseStr,data,code,msg,isFromCache);
            }
        });
    }

    @Override
    public void onEmpty() {
        postDelay(new Runnable() {
            @Override
            public void run() {
                listener.setNetState(NetState.FINISHED);
                listener.onEmpty();
            }
        });
    }

    @Override
    public void onError(final String msgCanShow) {
        postDelay(new Runnable() {
            @Override
            public void run() {
                listener.setNetState(NetState.FINISHED);
                listener.onError(msgCanShow);
            }
        });
    }

    @Override
    public void onCodeError(final String msgCanShow, final String hiddenMsg, final int code) {
        postDelay(new Runnable() {
            @Override
            public void run() {
                listener.setNetState(NetState.FINISHED);
                listener.onCodeError(msgCanShow,hiddenMsg,code);
            }
        });
    }

    @Override
    public void onCancel() {
        postDelay(new Runnable() {
            @Override
            public void run() {
                listener.setNetState(NetState.FINISHED);
                listener.onCancel();
            }
        });
    }

    @Override
    public void onTimeout() {
        postDelay(new Runnable() {
            @Override
            public void run() {
                listener.setNetState(NetState.FINISHED);
                listener.onCancel();

            }
        });
    }

    @Override
    public void onUnlogin() {
        postDelay(new Runnable() {
            @Override
            public void run() {
                listener.onUnlogin();
                listener.setNetState(NetState.FINISHED);
            }
        });
    }

    @Override
    public void onUnFound() {
        postDelay(new Runnable() {
            @Override
            public void run() {
                listener.onUnFound();
                listener.setNetState(NetState.FINISHED);
            }
        });
    }

    @Override
    public void onNoNetwork() {
        postDelay(new Runnable() {
            @Override
            public void run() {
                listener.onNoNetwork();
                listener.setNetState(NetState.FINISHED);
            }
        });
    }

    @Override
    public boolean onPreValidate(ConfigInfo config) {
       return listener.onPreValidate(config);
    }

    @Override
    public void onFilesUploadProgress(long transPortedBytes, long totalBytes, int fileIndex, int filesCount) {
        listener.onFilesUploadProgress(transPortedBytes, totalBytes, fileIndex, filesCount);
    }

    @Override
    public void onPreExecute() {
        listener.onPreExecute();
        listener.setNetState(NetState.LOADING);
    }

    @Override
    public void onProgressChange(long transPortedBytes, long totalBytes) {
        listener.onProgressChange(transPortedBytes, totalBytes);
    }

    @Override
    public boolean isResponseFromCache() {
        return listener.isResponseFromCache();
    }

}

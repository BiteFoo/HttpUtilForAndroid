package com.hss01248.netdemo.akulaku;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.interfaces.StringParseStrategy;
import com.hss01248.net.wrapper.MyJson;
import com.hss01248.net.wrapper.MyLog;
import com.hss01248.net.wrapper.MyNetListener;
import com.hss01248.net.wrapper.Tool;

import java.util.List;

/**
 * Created by huangshuisheng on 2017/9/28.
 */

public class AkulakuParser implements StringParseStrategy {
    @Override
    public <T> void parseCommonJson(String response, ConfigInfo<T> configInfo, boolean isFromCache) throws Exception{
        AkulakuRootBean rootBean = MyJson.parseObject(response,AkulakuRootBean.class);
        //todo 拿到systime,同步时间?
        configInfo.responseExtra1 = rootBean.getSysTime();
        if (rootBean.isSuccess()){
            MyNetListener<T> callback = configInfo.listener;
            //如果不是字符串
            if(!(rootBean.getData() instanceof String)){
                if(rootBean.getData() ==null){
                    if(configInfo.isTreatEmptyDataAsSuccess){
                        callback.onSuccess(null,response,isFromCache);
                    }else {
                        callback.onEmpty();
                    }
                }else {
                    callback.onSuccess((T) rootBean.getData(),response,isFromCache);
                }
                return;
            }
            //如果是字符串
            if(isCallOnEmpty((String) rootBean.getData(),configInfo)){
                callback.onEmpty();
            }else {
                String dataStr = (String) rootBean.getData();
                MyLog.e("data str:"+dataStr);
                //如果不是来自缓存,则缓存结果
                if(configInfo.isResponseJsonArray){
                    List<T> datas = MyJson.parseArray(dataStr,configInfo.clazz);
                    if(datas==null || datas.size()==0){
                        callback.onEmpty();
                    }else {
                        callback.onSuccessArr(datas,dataStr,isFromCache);
                        Tool.cacheResponseIfFromNet(response,configInfo);
                        Tool.setReadCacheSuccessIfIsCache(configInfo);
                    }
                }else {
                    T data = MyJson.parseObject(dataStr,configInfo.clazz);
                    if(data==null){
                        callback.onEmpty();
                    }
                    callback.onSuccess(data,dataStr,isFromCache);
                    Tool.cacheResponseIfFromNet(response,configInfo);
                    Tool.setReadCacheSuccessIfIsCache(configInfo);
                }
            }
        }else {
            //todo 处理unlogin的情况
            configInfo.listener.onError(rootBean.getErrCode(),rootBean.getErrMsg(),"");
        }
    }

    @Override
    public boolean isCallOnEmpty(String dataStr, ConfigInfo configInfo) throws Exception{
        return Tool.isJsonEmpty(dataStr);
    }


}

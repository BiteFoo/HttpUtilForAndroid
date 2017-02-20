package com.hss01248.http.interfaces;

import com.hss01248.http.config.ConfigInfo;
import com.hss01248.http.config.NetDefaultConfig;

/**
 * Created by Administrator on 2017/1/19 0019.
 */
public abstract class IClient {
    protected abstract <E> ConfigInfo<E> getString(ConfigInfo<E> info);

    protected abstract  <E> ConfigInfo<E> postString(ConfigInfo<E> info);

    protected abstract   ConfigInfo download(ConfigInfo info);

    protected abstract   ConfigInfo upload(ConfigInfo info);

    public <E> ConfigInfo<E> start(ConfigInfo<E> info){
        switch (info.type){

            case ConfigInfo.TYPE_STRING:
            case ConfigInfo.TYPE_JSON:
            case ConfigInfo.TYPE_JSON_FORMATTED:{
                if(info.method == NetDefaultConfig.Method.GET){
                    return getString(info);
                }else if(info.method == NetDefaultConfig.Method.POST){
                    return postString(info);
                }
            }
            case ConfigInfo.TYPE_DOWNLOAD:
                return download(info);
            case ConfigInfo.TYPE_UPLOAD_WITH_PROGRESS:
                return upload(info);
            default:
                return info;
        }
    }




}

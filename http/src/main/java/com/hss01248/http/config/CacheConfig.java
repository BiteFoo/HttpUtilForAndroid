package com.hss01248.http.config;

/**
 * Created by hss on 2017/12/28.
 */

public class CacheConfig {

    //緩存控制
    // public boolean forceGetNet = true;
    public boolean shouldReadCache = false;
    public boolean shouldCacheResponse = false;
    public int cacheMaxAge = Integer.MAX_VALUE/2; //单位秒




    public boolean isFromCache = false;//内部控制,不让外部设置
    public boolean isFromCacheSuccess = false;//内部控制,不让外部设置

    public    int cacheMode = GlobalConfig.get().getCacheMode();
}

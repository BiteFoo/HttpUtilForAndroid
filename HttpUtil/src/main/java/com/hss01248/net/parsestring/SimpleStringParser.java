package com.hss01248.net.parsestring;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.interfaces.StringParseStrategy;
import com.hss01248.net.wrapper.Tool;


import static com.hss01248.net.wrapper.Tool.setReadCacheSuccessIfIsCache;

/**
 * Created by huangshuisheng on 2017/9/28.
 */

public class SimpleStringParser implements StringParseStrategy {
    @Override
    public void parseCommonJson(String response, ConfigInfo configInfo, boolean isFromCache) throws Exception{
        //处理结果
        configInfo.listener.onSuccess(response, response,isFromCache);
        //缓存
        Tool.cacheResponseIfFromNet(response, configInfo);
        Tool.setReadCacheSuccessIfIsCache(configInfo);

    }

    @Override
    public boolean isCallOnEmpty(String response, ConfigInfo configInfo) throws Exception {
        return false;
    }
}

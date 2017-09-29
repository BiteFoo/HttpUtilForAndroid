package com.hss01248.net.interfaces;

import com.hss01248.net.config.ConfigInfo;

/**
 * Created by huangshuisheng on 2017/9/28.
 */

public interface StringParseStrategy {

    <T> void parseCommonJson(String response, ConfigInfo<T> configInfo, boolean isFromCache) throws Exception;


    /**
     * 判定一个json string什么时候为空
     * @param response
     * @param configInfo
     * @return
     */
    boolean isCallOnEmpty(String response,ConfigInfo configInfo) throws Exception;

}

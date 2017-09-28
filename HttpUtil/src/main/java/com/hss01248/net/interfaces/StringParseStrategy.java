package com.hss01248.net.interfaces;

import com.hss01248.net.config.ConfigInfo;

/**
 * Created by huangshuisheng on 2017/9/28.
 */

public interface StringParseStrategy {

    void parseCommonJson(String response, ConfigInfo configInfo, boolean isFromCache);

    boolean isCallOnEmpty(String response,ConfigInfo configInfo);

}

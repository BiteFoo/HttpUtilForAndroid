package com.hss01248.net.parsestring;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.interfaces.StringParseStrategy;
import com.hss01248.net.wrapper.Tool;

/**
 * Created by huangshuisheng on 2017/9/29.
 */

public class CommonJsonStrategy implements StringParseStrategy {
    @Override
    public <T> void parseCommonJson(String response, ConfigInfo<T> configInfo, boolean isFromCache) throws Exception {
        Tool.parseCommonJson(response,configInfo,isFromCache);

    }

    @Override
    public boolean isCallOnEmpty(String response, ConfigInfo configInfo) throws Exception {
        return false;
    }
}

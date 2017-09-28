package com.hss01248.net.akulaku;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.interfaces.StringParseStrategy;
import com.hss01248.net.wrapper.MyJson;

/**
 * Created by huangshuisheng on 2017/9/28.
 */

public class AkulakuParser implements StringParseStrategy {
    @Override
    public void parseCommonJson(String response, ConfigInfo configInfo, boolean isFromCache) {
        AkulakuRootBean rootBean = MyJson.parseObject(response,AkulakuRootBean.class);
        if (rootBean.isSuccess()){

        }else {
            configInfo.callback.onError(rootBean.getErrCode(),rootBean.getErrMsg(),"");
        }

    }

    @Override
    public boolean isCallOnEmpty(String response, ConfigInfo configInfo) {
        return false;
    }


}

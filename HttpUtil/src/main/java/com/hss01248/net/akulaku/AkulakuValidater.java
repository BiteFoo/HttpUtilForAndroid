package com.hss01248.net.akulaku;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.interfaces.ValidateStrategy;

import java.util.Map;

import okhttp3.CookieJar;

/**
 * Created by huangshuisheng on 2017/9/28.
 */

public class AkulakuValidater implements ValidateStrategy {
    @Override
    public void addToken(String token, Map<String, String> headers, Map<String, String> params, CookieJar cookieJar) {
        params.put("tokenss",token);
    }

    @Override
    public void reLogin(ConfigInfo info) {

    }
}

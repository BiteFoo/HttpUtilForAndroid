package com.hss01248.net.interfaces;

import com.hss01248.net.config.ConfigInfo;

import java.util.Map;

import okhttp3.CookieJar;

/**
 * Created by huangshuisheng on 2017/9/28.
 */

public interface ValidateStrategy {

    void addToken(String token, Map<String,String> headers, Map<String,String> params, CookieJar cookieJar);

    void reLogin(ConfigInfo info);
}

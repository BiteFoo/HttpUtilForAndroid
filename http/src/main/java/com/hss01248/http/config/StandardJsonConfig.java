package com.hss01248.http.config;

/**
 * Created by hss on 2017/12/28.
 */

public class StandardJsonConfig {

    //设置标准格式json本次响应的不同字段
    public String key_data = "";
    public String key_code = "";
    public String key_msg = "";

    //不那么标准的json
    public String key_isSuccess = GlobalConfig.get().key_isSuccess;
    // public boolean isKeyCodeInt = GlobalConfig.get().isKeyCodeInt;//code对应的字段是int还是String
    public String key_extra1 = GlobalConfig.get().key_extra1;//json外层额外的字段,如果为空就说明没有
    public String key_extra2 = GlobalConfig.get().key_extra2;
    public String key_extra3 = GlobalConfig.get().key_extra3;


    public int code_success;
    public int code_unlogin;
    public int code_unFound;

    public boolean isCustomCodeSet;

    public boolean isResponseJsonArray = false;
}

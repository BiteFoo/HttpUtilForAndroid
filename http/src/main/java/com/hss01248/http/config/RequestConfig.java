package com.hss01248.http.config;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.hss01248.http.callback.NetCallback;
import com.hss01248.http.interfaces.IClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hss on 2017/12/28.
 */

public class RequestConfig<T> {



    /**
     * Created by Administrator on 2016/9/3.
     */
    public class ConfigInfo<T> {

        public ConfigInfo(){

        }
        public Object request;//跟具体client有关的请求对象
        public Object extraTag;
        public Object responseExtra1;
        public Object responseExtra2;
        public Object responseExtra3;
        //核心参数
        public int method = HttpMethod.GET;
        public String url;
        public Map<String,String> params ;
        public String paramsStr;






        public boolean paramsAsJson = false;
        public int type = TYPE_STRING;//请求的类型,6类中的一种

        //回调
        public NetCallback callback;


        public Class<T> clazz;







        public boolean isResponseJsonArray = false;





        public int cookieMode;


        //本次请求是否忽略证书校验--也就是通过所有证书.
// 这个属性没有全局配置,也不建议全局配置. 如果是自签名,放置证书到raw下,并在初始化前addCer方法,即可全局使用https
        public boolean ignoreCer = false;







        //请求的客户端对象
        public IClient client;




        public boolean forceMinTime  = false;//强制最短时间,防止网络太快,请求结束了,dialog才弹出来.
        public long netStartTime;





        //是否拼接token
        public boolean isAppendToken = true;

        //状态为成功时,data对应的字段是否为空
        public boolean isTreatEmptyJsonAsSuccess = false;
        public boolean isTreatEmptyResponBodyAsSuccess = false;
    /*public ConfigInfo<T> setFailWhenDataIsEmpty(){
        this.isTreatEmptyDataAsSuccess = false;
        return this;
    }*/









        //请求头  http://tools.jb51.net/table/http_header
        public Map<String,String> headers = new HashMap<>();


        //重試次數
        public int retryCount ;


        public int timeout ;






        //用于取消请求用的
        public Object tagForCancle ;






        //優先級,备用 volley使用
        public int priority = Priority_NORMAL;











        //上传的文件路径
        public Map<String, String> files;

    /*//最終的數據類型:普通string,普通json,規範的jsonobj

    public int resonseType = TYPE_STRING;*/


        public boolean isAppendCommonHeaders ;
        public boolean isAppendCommonParams ;

        public static final int TYPE_DOWNLOAD = 1;
        public static final int TYPE_UPLOAD_WITH_PROGRESS = 2;
        public static final int TYPE_UPLOAD_NONE_PROGRESS = 3;//测试用的

        public static final int TYPE_STRING = 4;//純文本,比如html
        public static final int TYPE_JSON = 5;
        public static final int TYPE_JSON_FORMATTED = 6;//jsonObject包含data,code,msg,數據全在data中,可能是obj,頁可能是array,也可能為空
        public static final int TYPE_JSON_FORMATTED_EXTRA = 7;

//优先级

        public static final int Priority_LOW = 1;
        public static final int Priority_NORMAL = 2;
        public static final int Priority_IMMEDIATE = 3;
        public static final int Priority_HIGH = 4;

        public boolean isSync;
        public String responseCharset;

        //http方法



    /* public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }*/












    }

}

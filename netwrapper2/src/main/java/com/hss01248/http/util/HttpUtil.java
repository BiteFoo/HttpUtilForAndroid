package com.hss01248.http.util;


import android.content.Context;
import android.os.Handler;

import com.hss01248.http.builder.DownloadBuilder;
import com.hss01248.http.builder.JsonRequestBuilder;
import com.hss01248.http.builder.StandardJsonRequestBuilder;
import com.hss01248.http.builder.StringRequestBuilder;
import com.hss01248.http.builder.UploadRequestBuilder;
import com.hss01248.http.config.BaseNetBean;
import com.hss01248.http.config.HttpsConfig;
import com.hss01248.http.config.NetDefaultConfig;
import com.hss01248.http.interfaces.IClient;
import com.hss01248.http.okhttp.OkClient;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/21.
 */
public class HttpUtil {
    private static Context context;
    private static Handler mainHandler;
    protected static boolean isDebug;


    public static IClient getClient() {
        return OkClient.getInstance();
    }




    /**
     * 添加证书.如果有,一定要在init方法前面调用:在init方法后面调用无效
     * @param cerFileInRaw  证书要放在raw目录下
     */
    public static void addCer(int cerFileInRaw){
        if(HttpsConfig.certificates == null ){
            HttpsConfig.certificates = new ArrayList<Integer>();
        }
        HttpsConfig.certificates.add(cerFileInRaw);
    }

    /**
     * 注意:如果要添加https的自签名证书,一定要在此方法之前调用addcer方法

     */
    public static void init(String baseUrl,Context context,boolean isDebug){
        HttpUtil.context = context;
        mainHandler = new Handler(context.getMainLooper());
        HttpUtil.isDebug = isDebug;

        NetDefaultConfig.baseUrl = baseUrl;
        //HttpUtil.client =  OkClient.getInstance();//如果要使用rxjava,将RetrofitClient改成RxRetrofitClient即可.

        NetDefaultConfig.USER_AGENT = System.getProperty("http.agent");
        MyLog.e("user-agent:"+ NetDefaultConfig.USER_AGENT);

    }


    /**
     * 指定标准格式json的三个字段.比如聚合api的三个字段分别是error_code(但有的又是resultcode),reason,result,error_code
     * 如果几个code没有,可以设为负值
     * @param tokenName
     * @param data
     * @param code
     * @param msg
     * @param codeSuccess
     * @param codeUnlogin
     * @param codeUnfound
     */
    public static void initAppDefault(String tokenName,String data,String code,String msg,int codeSuccess,int codeUnlogin,int codeUnfound){
        NetDefaultConfig.TOKEN = tokenName;
        NetDefaultConfig.KEY_DATA = data;
        NetDefaultConfig.KEY_CODE = code;
        NetDefaultConfig.KEY_MSG = msg;
        BaseNetBean.CODE_SUCCESS = codeSuccess;
        BaseNetBean.CODE_UNLOGIN = codeUnlogin;
        BaseNetBean.CODE_UN_FOUND = codeUnfound;
    }



    public  static <E> StringRequestBuilder<E> requestString(String url) {
        return new StringRequestBuilder<E>().url(url);
    }


    public static <E> JsonRequestBuilder<E> requestJson(String url, Class clazz) {
        JsonRequestBuilder builder = new JsonRequestBuilder();
        builder.url(url).setJsonClazz(clazz);
        return builder;
    }


    public static <E> StandardJsonRequestBuilder<E> reqeustStandardJson(String url, Class<E> clazz) {
        return new StandardJsonRequestBuilder<E>().url(url).setJsonClazz(clazz);
    }


    public static <E> DownloadBuilder<E> download(String url) {
        return new DownloadBuilder<E>().url(url);
    }


    public static <E> UploadRequestBuilder<E> upload(String url, String fileDesc, String filePath) {
        return new UploadRequestBuilder<E>().url(url).addFile(fileDesc,filePath);
    }
}

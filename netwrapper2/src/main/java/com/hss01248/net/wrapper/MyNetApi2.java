package com.hss01248.net.wrapper;

import android.content.Context;
import android.util.Log;

import com.hss01248.net.builder.DownloadBuilder;
import com.hss01248.net.builder.JsonRequestBuilder;
import com.hss01248.net.builder.StandardJsonRequestBuilder;
import com.hss01248.net.builder.StringRequestBuilder;
import com.hss01248.net.builder.UploadRequestBuilder;
import com.hss01248.net.config.BaseNetBean;
import com.hss01248.net.config.HttpsConfig;
import com.hss01248.net.config.NetDefaultConfig;
import com.hss01248.net.interfaces.ILoginManager;
import com.hss01248.net.retrofit.RetrofitClient;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/21.
 */
public class MyNetApi2 {

    public static Context context;
    public static BaseNet adapter;


    /**
     * 添加证书.如果有,一定要在init方法前面调用:在init方法后面调用无效
     * @param cerFileInRaw  证书要放在raw目录下
     */
    public static void addCer(int cerFileInRaw){
        if(HttpsConfig.certificates == null ){
            HttpsConfig.certificates = new ArrayList<>();
        }
        HttpsConfig.certificates.add(cerFileInRaw);
    }

    /**
     * 注意:如果要添加https的自签名证书,一定要在此方法之前调用addcer方法
     * @param context
     * @param baseUrl
     * @param loginManager
     */
    public static void init(Context context,String baseUrl,ILoginManager loginManager){
        MyNetApi2.context = context;
        NetDefaultConfig.baseUrl = baseUrl;
        MyNetApi2.adapter = RetrofitClient.getInstance();//如果要使用rxjava,将RetrofitClient改成RxRetrofitClient即可.
        if (loginManager instanceof  BaseNet){
            throw  new RuntimeException("please implement ILoginManager independently");
            //避免可能的无限循环调用
        }
        MyNetApi2.adapter.setLoginManager(loginManager);
        NetDefaultConfig.USER_AGENT = System.getProperty("http.agent");
        Log.e("e","user-agent:"+ NetDefaultConfig.USER_AGENT);

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



    public  static <E> StringRequestBuilder<E> buildStringRequest(String url) {
        return new StringRequestBuilder<>().url(url);
    }


    public static <E> JsonRequestBuilder<E> buildJsonRequest(String url, Class<E> clazz) {
        JsonRequestBuilder builder = new JsonRequestBuilder();
        builder.url(url).setJsonClazz(clazz);
        return builder;
    }


    public static <E> StandardJsonRequestBuilder<E> buildStandardJsonRequest(String url, Class<E> clazz) {
        return new StandardJsonRequestBuilder<>().url(url).setJsonClazz(clazz);
    }


    public static <E> DownloadBuilder<E> buildDownloadRequest(String url) {
        return new DownloadBuilder<>().url(url);
    }


    public static <E> UploadRequestBuilder<E> buildUpLoadRequest(String url, String fileDesc, String filePath) {
        return new UploadRequestBuilder<>().url(url).addFile(fileDesc,filePath);
    }
}

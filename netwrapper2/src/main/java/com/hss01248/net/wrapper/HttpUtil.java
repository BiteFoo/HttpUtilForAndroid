package com.hss01248.net.wrapper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.hss01248.net.builder.DownloadBuilder;
import com.hss01248.net.builder.IClient;
import com.hss01248.net.builder.JsonRequestBuilder;
import com.hss01248.net.builder.StandardJsonRequestBuilder;
import com.hss01248.net.builder.StringRequestBuilder;
import com.hss01248.net.builder.UploadRequestBuilder;
import com.hss01248.net.config.GlobalConfig;
import com.hss01248.net.okhttp.OkClient;
import com.hss01248.net.oldapi.BaseNet;

/**
 * Created by Administrator on 2016/9/21.
 */
public class HttpUtil {

    public static Context context;
    public static BaseNet adapter;

    public static Handler getMainHandler() {
        if(mainHandler==null){
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }
    private static Handler mainHandler;
    private static GlobalConfig globalConfig;

    public static GlobalConfig  init(Context context,String baseUrl ){
        HttpUtil.context = context;
        globalConfig = GlobalConfig.get();
        globalConfig.setBaseUrl(baseUrl);
        return globalConfig;
    }

    public static IClient getClient() {
        return OkClient.getInstance();
    }










    /**
     * 添加证书.如果有,一定要在init方法前面调用:在init方法后面调用无效
     * @param cerFileInRaw  证书要放在raw目录下
     */
   /* public static void addCer(int cerFileInRaw){
        if(HttpsConfig.certificates == null ){
            HttpsConfig.certificates = new ArrayList<>();
        }
        HttpsConfig.certificates.add(cerFileInRaw);
    }*/

    /**
     * 注意:如果要添加https的自签名证书,一定要在此方法之前调用addcer方法
     * @param context
     * @param baseUrl
     * @param loginManager
     */
    /*public static void init(Context context,String baseUrl,ILoginManager loginManager){
        HttpUtil.context = context;
        NetDefaultConfig.baseUrl = baseUrl;
        HttpUtil.adapter = RetrofitClient.getInstance();//如果要使用rxjava,将RetrofitClient改成RxRetrofitClient即可.
        if (loginManager instanceof  BaseNet){
            throw  new RuntimeException("please implement ILoginManager independently");
            //避免可能的无限循环调用
        }
        HttpUtil.adapter.setLoginManager(loginManager);
        NetDefaultConfig.USER_AGENT = System.getProperty("http.agent");
        Log.e("e","user-agent:"+ NetDefaultConfig.USER_AGENT);

    }*/


    /**
     * 指定标准格式json的三个字段.比如聚合api的三个字段分别是error_code(但有的又是resultcode),reason,result,error_code
     * 如果几个code没有,可以设为负值

     */
   /* public static void initAppDefault(String tokenName,String data,String code,String msg,int codeSuccess,int codeUnlogin,int codeUnfound){
        NetDefaultConfig.TOKEN = tokenName;
        NetDefaultConfig.KEY_DATA = data;
        NetDefaultConfig.KEY_CODE = code;
        NetDefaultConfig.KEY_MSG = msg;
        BaseNetBean.CODE_SUCCESS = codeSuccess;
        BaseNetBean.CODE_UNLOGIN = codeUnlogin;
        BaseNetBean.CODE_UN_FOUND = codeUnfound;
    }*/



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

    public static void cancelRquest(Object tag){
        getClient().cancleRequest(tag);
    }
    public static void cancelAll(){
        getClient().cancleAllRequest();
    }


}

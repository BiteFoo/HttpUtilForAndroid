package com.hss01248.net.wrapper;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.hss01248.net.cache.ACache;
import com.hss01248.net.config.BaseNetBean;
import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.config.NetDefaultConfig;
import com.hss01248.net.old.CommonHelper;
import com.hss01248.net.old.MyNetUtil;
import com.litesuits.android.async.SimpleTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Tool {

    public static void addToken(Map map) {
        if (map != null){
            map.put(NetDefaultConfig.TOKEN, NetDefaultConfig.getToken());//每一个请求都传递sessionid
        }else {
            map = new HashMap();
            map.put(NetDefaultConfig.TOKEN, NetDefaultConfig.getToken());//每一个请求都传递sessionid
        }

    }


    public static String appendUrl(String urlTail,boolean isToAppend) {
        String url ;
        if (!isToAppend || urlTail.contains("http:")|| urlTail.contains("https:")){
            url =  urlTail;
        }else {
            url = NetDefaultConfig.baseUrl+  urlTail;
        }

        return url;
    }


    public static String getCacheKey(ConfigInfo configInfo){
        String url = configInfo.url;
        Map<String,String> map = configInfo.params;
        StringBuilder stringBuilder = new StringBuilder(100);
        stringBuilder.append(url);
        int size = map.size();
        Set<Map.Entry<String,String>> set = map.entrySet();
        if (size>0){
            for (Map.Entry<String,String> entry: set){
                stringBuilder.append(entry.getKey()).append(entry.getValue());
            }

        }

        return stringBuilder.toString();

    }




    public static <T> void parseInTime(long time, ConfigInfo<T> configInfo, final Runnable runnable
    ) {
        long time2 = System.currentTimeMillis();
        long gap = time2 - time;

        if (configInfo.isForceMinTime && (gap < NetDefaultConfig.TIME_MINI)){
            TimerUtil.doAfter(new TimerTask() {
                @Override
                public void run() {
                    runnable.run();
                }
            },(NetDefaultConfig.TIME_MINI - gap));

        }else {
            runnable.run();
        }
    }



    public static boolean isJsonEmpty(String data){
        if (TextUtils.isEmpty(data) || "[]".equals(data)
                || "{}".equals(data) || "null".equals(data)) {
            return true;
        }

        return false;

    }



    public  static  <E> void parseStandJsonStr(String string, long time, final ConfigInfo<E> configInfo)  {
        if (isJsonEmpty(string)){//先看是否为空
            parseInTime(time,configInfo, new Runnable() {
                @Override
                public void run() {
                    configInfo.listener.onEmpty();
                }
            });
        }else {

            // final BaseNetBean<E> bean = MyJson.parseObject(string,BaseNetBean.class);//如何解析内部的字段?
          /*  Gson gson = new Gson();z这样也不行
            Type objectType = new TypeToken<BaseNetBean<E>>() {}.getType();
            final BaseNetBean<E> bean = gson.fromJson(string,objectType);*/

            JSONObject object = null;
            try {
                object = new JSONObject(string);
            } catch (JSONException e) {
                e.printStackTrace();
                parseInTime(time,configInfo, new Runnable() {
                    @Override
                    public void run() {
                        configInfo.listener.onError("json 格式异常");
                    }
                });
                return;
            }

            final String dataStr = object.optString(NetDefaultConfig.KEY_DATA);
            final int code = object.optInt(NetDefaultConfig.KEY_CODE);
            final String msg = object.optString(NetDefaultConfig.KEY_MSG);

            final String finalString1 = string;

            parseInTime(time,configInfo, new Runnable() {
                @Override
                public void run() {

                    parseStandardJsonObj(finalString1,dataStr,code,msg,configInfo);

                }
            });

        }
    }




    /**
     * 解析标准json的方法

     * @param configInfo
     * @param adapter
     * @param <E>
     */
    private static <E> void parseStandardJsonObj(String response,String data,int code,String msg, final ConfigInfo<E> configInfo
                                                 ){

        switch (code){
            case BaseNetBean.CODE_SUCCESS://请求成功

                if (isJsonEmpty(data)){
                    configInfo.listener.onEmpty();
                }else {
                    try{

                        if (data.startsWith("{")){
                            E bean =  MyJson.parseObject(data,configInfo.clazz);
                            configInfo.listener.onSuccessObj(bean ,response,data,code,msg);
                            cacheResponse(response, configInfo);
                        }else if (data.startsWith("[")){
                            List<E> beans =  MyJson.parseArray(data,configInfo.clazz);
                            configInfo.listener.onSuccessArr(beans,response,data,code,msg);
                            cacheResponse(response, configInfo);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        configInfo.listener.onError(e.toString());
                        return;
                    }
                }

                break;
            case BaseNetBean.CODE_UN_FOUND://没有找到内容
                configInfo.listener.onUnFound();
                break;
            case BaseNetBean.CODE_UNLOGIN://未登录
                configInfo.client.autoLogin(new MyNetListener() {
                    @Override
                    public void onSuccess(Object response, String resonseStr) {
                        configInfo.client.resend(configInfo);
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        configInfo.listener.onUnlogin();
                    }
                });
                break;

            default:
                configInfo.listener.onCodeError("",msg,code);
                break;
        }
    }


    public static boolean writeFile(final InputStream is, final String path, final MyNetListener callback){
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(path);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];


                long fileSizeDownloaded = 0;

                inputStream = is;
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("00", "file download: " + fileSizeDownloaded + " of " );
                }

                outputStream.flush();
                callback.onSuccess(path,path);

                return true;
            } catch (IOException e) {
                callback.onError(e.toString());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            callback.onError(e.toString());
            return false;
        }






















          /*  SimpleTask task = new SimpleTask<String>() {
                @Override
                protected String doInBackground() {
                    try {
                        File file = new File(path);
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            fos.flush();
                        }
                        fos.close();
                        bis.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return e.toString();
                    }

                    return path;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (!path.equals(s)){
                        callback.onError(s);
                    }else {
                        callback.onSuccess(path,path);
                    }
                }

            };

            task.execute();*/





    }


    public static  void parseStringByType(final long time, final String string, final ConfigInfo configInfo) {
        switch (configInfo.type){
            case ConfigInfo.TYPE_STRING:

                //缓存
                cacheResponse(string, configInfo);

                //处理结果
                CommonHelper.parseInTime(time, configInfo, new Runnable() {
                    @Override
                    public void run() {
                        configInfo.listener.onSuccess(string, string);
                    }
                });
                break;
            case ConfigInfo.TYPE_JSON:
                CommonHelper.parseInTime(time, configInfo, new Runnable() {
                    @Override
                    public void run() {
                        parseCommonJson(time,string,configInfo);
                    }
                });

                break;
            case ConfigInfo.TYPE_JSON_FORMATTED:
                parseStandJsonStr(string, time, configInfo);
                break;

        }
    }

    private static void cacheResponse(final String string, final ConfigInfo configInfo) {
        if (configInfo.shouldCacheResponse && !configInfo.isFromCache && configInfo.cacheTime >0){
            SimpleTask<Void> simple = new SimpleTask<Void>() {

                @Override
                protected Void doInBackground() {
                    ACache.get(MyNetUtil.context).put(getCacheKey(configInfo),string, (int) (configInfo.cacheTime));
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                }
            };
            simple.execute();
        }
    }

    private static <E> void parseCommonJson(long time, String string, ConfigInfo<E> configInfo) {

        if (isJsonEmpty(string)){
            configInfo.listener.onEmpty();
        }else {
            try{

                if (string.startsWith("{")){
                    E bean =  MyJson.parseObject(string,configInfo.clazz);
                    configInfo.listener.onSuccessObj(bean ,string,string,0,"");
                    cacheResponse(string, configInfo);
                }else if (string.startsWith("[")){
                    List<E> beans =  MyJson.parseArray(string,configInfo.clazz);
                    configInfo.listener.onSuccessArr(beans,string,string,0,"");
                    cacheResponse(string, configInfo);
                }else {
                    configInfo.listener.onError("不是标准json格式");
                }

            }catch (Exception e){
                e.printStackTrace();
                configInfo.listener.onError(e.toString());
            }
        }
    }


    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    public static String getMimeType(File file){
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }
}

package com.hss01248.http.util;


import android.webkit.MimeTypeMap;

import com.alibaba.fastjson.JSONException;
import com.hss01248.http.config.BaseNetBean;
import com.hss01248.http.config.ConfigInfo;
import com.hss01248.http.config.NetDefaultConfig;
import com.hss01248.http.wrapper.MyNetListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Tool {












    public static boolean writeResponseBodyToDisk(ResponseBody body, ConfigInfo info) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(info.filePath);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                long oldTime = 0L;
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    //MyLog.d( "file download: " + fileSizeDownloaded + " of " + fileSize);//todo 控制频率

                    long currentTime = System.currentTimeMillis();
                    if (currentTime - oldTime > NetDefaultConfig.PROGRESS_INTERMEDIATE || fileSizeDownloaded == fileSizeDownloaded) {//每300ms更新一次进度
                        oldTime = currentTime;
                        info.listener.onProgressChange(fileSizeDownloaded,fileSize);

                        if(fileSizeDownloaded == fileSize){
                            info.listener.onSuccess(info.filePath,info.filePath);
                        }
                    }


                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
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
            e.printStackTrace();
            return false;
        }
    }


    public static String urlEncode(String string)  {
        String str = "";
        try {
            str=  URLEncoder.encode(string,"UTF-8");
            return str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string;
        }
    }

    public static String generateUrlOfGET(ConfigInfo info){
        StringBuilder stringBuilder= new StringBuilder();
        if(!info.url.startsWith("http")){
            stringBuilder.append(NetDefaultConfig.baseUrl)
                    .append(info.url);
        }
        if(!info.url.contains("?")){
            stringBuilder.append("?");
        }else if(!info.url.endsWith("&")){
            stringBuilder.append("&");
        }
        if(TextUtils.isNotEmpty(info.paramsStr)){
            stringBuilder.append(info.paramsStr);
            if(!info.paramsStr.endsWith("&")){
                stringBuilder.append("&");
            }
        }
        String parms = Tool.getKeyValueStr(info.params);
        if(TextUtils.isNotEmpty(parms)){
            stringBuilder.append(parms);
        }
        return stringBuilder.toString();
    }

    public static String getKeyValueStr(Map<String,String> params) {
        StringBuilder stringBuilder = new StringBuilder();

        for(Map.Entry<String,String> param   : params.entrySet()){
            stringBuilder.append(param.getKey())
                    .append("=")
                    .append(param.getValue())
                    .append("&");
        }
        return stringBuilder.toString();
    }





    public static void addToken(Map map) {
        if (map != null){
            map.put(NetDefaultConfig.TOKEN, NetDefaultConfig.getToken());//每一个请求都传递sessionid
        }else {
            map = new HashMap();
            map.put(NetDefaultConfig.TOKEN, NetDefaultConfig.getToken());//每一个请求都传递sessionid
        }

    }

    public static void handleError(Throwable t,ConfigInfo configInfo){
        if(t != null){
            t.printStackTrace();
        }

        String str = t.toString();
        if(str.contains("timeout")){
            configInfo.listener.onTimeout();
        }else {
            configInfo.listener.onError(str);
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





    public static boolean isJsonEmpty(String data){
        if (data== null || "".equals(data) || "[]".equals(data)
                || "{}".equals(data) || "null".equals(data)) {
            return true;
        }
        return false;
    }







    public  static  <E> void parseStandJsonStr(String string,  final ConfigInfo<E> configInfo)  {
        if (isJsonEmpty(string)){//先看是否为空
        configInfo.listener.onEmpty();

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
                configInfo.listener.onError("json 格式异常");
                return;
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
            String key_data = TextUtils.isEmpty(configInfo.key_data) ? NetDefaultConfig.KEY_DATA : configInfo.key_data;
            String key_code = TextUtils.isEmpty(configInfo.key_code) ? NetDefaultConfig.KEY_CODE : configInfo.key_code;
            String key_msg = TextUtils.isEmpty(configInfo.key_msg) ? NetDefaultConfig.KEY_MSG : configInfo.key_msg;

            final String dataStr = object.optString(key_data);
            final int code = object.optInt(key_code);
            final String msg = object.optString(key_msg);

            final String finalString1 = string;

            parseStandardJsonObj(finalString1,dataStr,code,msg,configInfo);
            //todo 将时间解析放到后面去

        }
    }






    /**
     * 解析标准json的方法

     * @param configInfo

     * @param <E>
     */
    private static <E> void parseStandardJsonObj(final String response, final String data, final int code,
                                                 final String msg, final ConfigInfo<E> configInfo){

        int codeSuccess = configInfo.isCustomCodeSet ? configInfo.code_success : BaseNetBean.CODE_SUCCESS;
        int codeUnFound = configInfo.isCustomCodeSet ? configInfo.code_unFound : BaseNetBean.CODE_UN_FOUND;
        int codeUnlogin = configInfo.isCustomCodeSet ? configInfo.code_unlogin : BaseNetBean.CODE_UNLOGIN;

        if (code == codeSuccess){
            if (isJsonEmpty(data)){
                if(configInfo.isResponseJsonArray()){
                    configInfo.listener.onEmpty();
                }else {
                    if(configInfo.isSuccessDataEmpty){
                        configInfo.listener.onSuccess(null,TextUtils.isEmpty(msg)? "请求成功!" :msg);
                    }else {
                        configInfo.listener.onError("数据为空");
                    }
                }
            }else {
                try{
                    if (data.startsWith("{")){
                        final E bean =  MyJson.parseObject(data,configInfo.clazz);
                         configInfo.listener.onSuccessObj(bean ,response,data,code,msg);

                        //cacheResponse(response, configInfo);
                    }else if (data.startsWith("[")){
                        final List<E> beans =  MyJson.parseArray(data,configInfo.clazz);
                         configInfo.listener.onSuccessArr(beans,response,data,code,msg);


                        //cacheResponse(response, configInfo);
                    }else {//如果data的值是一个字符串,而不是标准json,那么直接返回
                        if (String.class.equals(configInfo.clazz) ){//此时,E也是String类型.如果有误,会抛出到下面catch里
                           configInfo.listener.onSuccess((E) data,data);


                        }else {
                            configInfo.listener.onError("不是标准的json数据");
                        }
                    }
                }catch (final Exception e){
                    e.printStackTrace();
                    configInfo.listener.onError(e.toString());
                    return;
                }
            }
        }else if (code == codeUnFound){
           configInfo.listener.onUnFound();
        }else if (code == codeUnlogin){
            /*configInfo.client.autoLogin(new MyNetListener() {
                @Override
                public void onSuccess(Object response, String resonseStr) {
                    configInfo.client.start(configInfo);
                }
                @Override
                public void onError(String error) {
                    super.onError(error);
                     configInfo.listener.onUnlogin();
                }
            });*/
        }else {
           configInfo.listener.onCodeError(msg,"",code);
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

                    com.hss01248.http.util.MyLog.d( "file download: " + fileSizeDownloaded + " of " );
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


    public static  void parseStringByType(final String string, final ConfigInfo configInfo) {
        switch (configInfo.type){
            case ConfigInfo.TYPE_STRING:
                //缓存
                //cacheResponse(string, configInfo);
                //处理结果
                 configInfo.listener.onSuccess(string, string);
                break;
            case ConfigInfo.TYPE_JSON:
                 parseCommonJson(string,configInfo);
                break;
            case ConfigInfo.TYPE_JSON_FORMATTED:
                parseStandJsonStr(string, configInfo);
                break;
        }
    }

    /*private static void cacheResponse(final String string, final ConfigInfo configInfo) {
        if (configInfo.shouldCacheResponse && !configInfo.isFromCache && configInfo.cacheTime >0){
            SimpleTask<Void> simple = new SimpleTask<Void>() {

                @Override
                protected Void doInBackground() {
                    ACache.getAsync(HttpUtil.context).put(getCacheKey(configInfo),string, (int) (configInfo.cacheTime));
                    MyLog.d("caching resonse:"+string);
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                }
            };
            simple.execute();
        }
    }*/

    private static <E> void parseCommonJson( String string, ConfigInfo<E> configInfo) {
        if (isJsonEmpty(string)){
            configInfo.listener.onEmpty();
        }else {
            try{
                if (string.startsWith("{")){
                    E bean =  MyJson.parseObject(string,configInfo.clazz);
                    configInfo.listener.onSuccessObj(bean ,string,string,0,"");
                    //cacheResponse(string, configInfo);
                }else if (string.startsWith("[")){
                    List<E> beans =  MyJson.parseArray(string,configInfo.clazz);
                    configInfo.listener.onSuccessArr(beans,string,string,0,"");
                    //cacheResponse(string, configInfo);
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

    public static String getMimeType(String fileUrl){

       /* FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileUrl);
        return type;*/

      /* String type = "text*//*";

        try {
            *//*Magic parser = new Magic() ;
            // getMagicMatch accepts Files or byte[],
            // which is nice if you want to test streams
            MagicMatch match = null;
            match = parser.getMagicMatch(new File(fileUrl),true);
            System.out.println(match.getMimeType()) ;
            String str = match.getMimeType();*//*

            MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
            File f = new File (fileUrl);
            Collection<?> mimeTypes = MimeUtil.getMimeTypes(f);
            System.out.println("mimeTypes:"+mimeTypes);
            if(!mimeTypes.isEmpty()){
                type = mimeTypes.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return type;*/




        String suffix = getSuffix(new File(fileUrl));
        if (suffix == null) {
            return "file";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file";
    }
}

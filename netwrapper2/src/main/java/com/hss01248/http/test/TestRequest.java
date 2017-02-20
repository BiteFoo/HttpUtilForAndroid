package com.hss01248.http.test;

import com.hss01248.http.test.bean.*;
import com.hss01248.http.util.MyJson;
import com.hss01248.http.util.MyLog;
import com.hss01248.http.util.HttpUtil;
import com.hss01248.http.wrapper.MyNetListener;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/19 0019.
 */
public class TestRequest {

    public static void main(String[] args){
        HttpUtil.init("http://www.qxinli.com:9001/api/");
        //HttpsUtil.addCrtificateFile("F:\\srca.cer");
        //testUpload();

        //testGetString();

        testCaoliu();
        //testPostString();
       // getAsync();

    }

    public static void get(){
        HttpClient client = new DefaultHttpClient();
        // 设置代理服务器地址和端口
        //client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
        // 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
        HttpGet method=new HttpGet("http://c6.h0j.org/thread0806.php?fid=16&search=&page=2");
        method.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) ");
        //使用POST方法
        //HttpMethod method = new PostMethod("http://java.sun.com");

        try {
          HttpResponse response= client.execute(method);
            //打印服务器返回的状态
            System.out.println(response.getStatusLine());
            //打印返回的信息
           // System.out.println(response.getEntity().);
            response.getEntity().writeTo(new FileOutputStream(new File("E:\\1.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //释放连接
        method.releaseConnection();
    }



    private static void testCaoliu(){
        HttpUtil.requestString("http://c6.h0j.org/thread0806.php?fid=16&search=&page=2")
               // .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) ")
               //.addHeader("Accept-Encoding","gzip, deflate, sdch")
               // .addHeader("Accept-Language","en")
                .setTimeout(200000)

                //.addHeader("Accept-Encoding","gzip")
                .callback(new MyNetListener() {
                    @Override
                    public void onSuccess(Object response, String resonseStr) {
                        MyLog.d(resonseStr);
                    }

                    @Override
                    public void onError(String msgCanShow) {
                        super.onError(msgCanShow);
                        MyLog.d(msgCanShow);
                    }
                })
                .getAsync();
    }

    private static void testparamsStr() {
        HttpUtil.requestString("http://ugc.moji001.com/sns/json/liveview/timeline/city")
                //.paramsStr("adapter=CARSMobileServiceAdapterV2P1&procedure=login&compressResponse=true&parameters=[{\"para\":\"A31LhyXjbi8ZPLgToZdlZG5lqo5mjHvgWuofKLaMHaPaYokHXqGXrsG/yftadoUGs2//4vctFq4tlFxOPHxsvhHRF61o9jW3DfxWrFK4ZCpHZOXZ1p2mJT6Wjq0+8ngrll9eP4RcKTRot+ElV7DSKM/TWla57XgQe6VPxahjGNSOWGsJrqH+OMpvIVhiDUew6Ctnb9Kd/Uza3c93nR5IlJCVMdw5brmo+ShQtEPTqNFTPtLyfhY9Q68XvzeIIq410Dd8G+lScmmBPBukYh/TYMToXRD/PWEnYx09E9DulPCRQxPo2Hgsc6S+gGfrzOpLzUGx+XhfOcRlkmWUS82MvY7NXa18ohCzjuAKse6orncYchEPeMF5yDCoB2DnoxoL6UdL/F9sOY6xpfDR1ghaISFZmm0nLGtEAuu52r/rwBOS/rpy9NrIKZ7y3UthlfD/a1kV3mm8ETSmsf1XEwLa0Y3GvEWOMq6RWbk6rdR0J2XewZjRYVXyQIAehus53xc2TikyrCuTl46LzSRPZcEroYv74x/kYdS7c7Kt/s+aLuaTpFWU8g6IzwDc/FmNhiwPmf1tCSrZZxaoPAnZhYJJ/OpjNPTWIt3TmQd+pQePR23MBD1bMgmaRy4YafwtGxkzN+zAvF0Kmy9DBpmb3k5q9S6vAjtALnUGsZ0xCIjKmTBWRt8OyZt0qnwu2UhSGuJuhtdDC+Mqf1s6qV60h4Hh7e9hBMHBzWUk+d+m46kAorZbzQn6xzu/s7d/RmdAUwdNPaleVXpGJNE1aCLMVWDk5osPVrw5kQUbvdf3UikG8mWS4Q9RuPHeRRN0+8/MFqvCq2fwFaMSa/Q+6QE1fJ20wm582bfnjd4Hyh7m3jrSXnW8G8AwYM5Hc4IbpspPLoJipsreTP0UR/mSa8QiSTUhuedbeWsNhoUy/plz2r1/66Xg3NMCBYF9gbhJSPzsWTnGmo92A3iX6sYGlTaOPAoF1SR001e63QtQ+nikQKkomsrDH8zEyLfFs4Bqba9QYPqOvijF8JqQPOyWP9H9R0yy3LWUOZvCPc4gV0K4lcJq6dwfgWo4yFw5nWc9ZJngHvIcZ7IpjieefXwh7SNVtijchyw==\"}]&__wl_deviceCtxVersion=-1&__wl_deviceCtxSession=78959051484789237814&isAjaxRequest=true&x=0.3938058527566308")
               .paramsStr("{\n" +
                       "    \"common\": {\n" +
                       "        \"app_version\": \"1006010802\",\n" +
                       "        \"device\": \"MI MAX\",\n" +
                       "        \"height\": 1920,\n" +
                       "        \"identifier\": \"861379031753821\",\n" +
                       "        \"language\": \"CN\",\n" +
                       "        \"os_version\": \"23\",\n" +
                       "        \"pid\": \"5599\",\n" +
                       "        \"platform\": \"Android\",\n" +
                       "        \"uid\": \"325461297\",\n" +
                       "        \"width\": 1080\n" +
                       "    },\n" +
                       "    \"params\": {\n" +
                       "        \"city_id\": \"5528\",\n" +
                       "        \"is_webp\": 1,\n" +
                       "        \"is_wifi\": 1,\n" +
                       "        \"page_length\": 40,\n" +
                       "        \"page_past\": 0\n" +
                       "    }\n" +
                       "}\n")
                .callback(new MyNetListener() {
                    @Override
                    public void onSuccess(Object response, String resonseStr) {
                        MyLog.d(resonseStr);
                    }

                    @Override
                    public void onError(String msgCanShow) {
                        super.onError(msgCanShow);
                        MyLog.d(msgCanShow);
                    }
                }).postAsync();
    }

    public static void testGetString(){
        HttpUtil.requestString("https://kyfw.12306.cn/otn/regist/init")
                .callback(new MyNetListener<String>() {
                    @Override
                    public void onSuccess(String response, String resonseStr) {
                        MyLog.e(response);
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        MyLog.e(error);
                    }
                })
               .setIgnoreCer()
                .getAsync();
    }
    public static void testPostString(){
        HttpUtil.requestString("article/getArticleCommentList/v1.json")
                .addParams("pageSize","30")
                .addParams("articleId","1738")
                .addParams("pageIndex","1")
                .callback(new MyNetListener<String>() {
                    @Override
                    public void onSuccess(String response, String resonseStr) {
                        MyLog.e(response);
                    }
                }).postAsync();
    }
    public static void testGetJson(){
        HttpUtil.requestJson("version/latestVersion/v1.json",GetCommonJsonBean.class)
                               .callback(new MyNetListener<GetCommonJsonBean>() {
                    @Override
                    public void onSuccess(GetCommonJsonBean response, String resonseStr) {
                        MyLog.json(MyJson.toJsonStr(response));
                    }
                })
                .getAsync();

    }
    public static void testPostJson(){
        HttpUtil.requestJson("article/getArticleCommentList/v1.json",PostCommonJsonBean.class)
                .addParams("pageSize","30")
                .addParams("articleId","1738")
                .addParams("pageIndex","1")
                .callback(new MyNetListener<PostCommonJsonBean>() {
                    @Override
                    public void onSuccess(PostCommonJsonBean response, String resonseStr) {
                        MyLog.json(MyJson.toJsonStr(response));
                    }
                }).postAsync();
    }

    public static void testGetStandardJson(){
/*	聚合api:笑话大全
                    sort	string	是	类型，desc:指定时间之前发布的，asc:指定时间之后发布的
                    page	int	否	当前页数,默认1
                    pagesize	int	否	每次返回条数,默认1,最大20
                    time	string	是	时间戳（10位），如：1418816972
                    key 	string  您申请的key*/
        Map<String,String> map4 = new HashMap<String,String>();
        map4.put("sort","desc");
        map4.put("page","1");
        map4.put("pagesize","4");
        map4.put("time",System.currentTimeMillis()/1000+"");
        map4.put("key","fuck you");


        HttpUtil.reqeustStandardJson("http://japi.juhe.cn/joke/content/list.from",GetStandardJsonBean.class)
                .addParams("sort","desc")
                .addParams("page","1")
                .addParams("pagesize","4")
                .addParams("time",System.currentTimeMillis()/1000+"")
                .addParams("key","fuck you")
                .setStandardJsonKey("result","error_code","reason")
                .setCustomCodeValue(0,2,-1)

                .callback(new MyNetListener<GetStandardJsonBean>() {
                    @Override
                    public void onSuccess(GetStandardJsonBean response, String resonseStr) {
                        MyLog.json(MyJson.toJsonStr(response));
                    }
                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        MyLog.e(error);
                    }
                })
                .getAsync();
    }
    public static void testPostStandardJson(){
        HttpUtil.reqeustStandardJson("article/getArticleCommentList/v1.json",PostStandardJsonArray.class)
                .addParams("pageSize","30")
                .addParams("articleId","1738")
                .addParams("pageIndex","1")
                .setResponseJsonArray()
                .callback(new MyNetListener<PostStandardJsonArray>() {
                    @Override
                    public void onSuccess(PostStandardJsonArray response, String resonseStr) {
                        //MyLog.json(MyJson.toJsonStr(response));
                    }

                    @Override
                    public void onSuccessArr(List<PostStandardJsonArray> response, String responseStr, String data, int code, String msg) {
                        super.onSuccessArr(response, responseStr, data, code, msg);
                        MyLog.json(MyJson.toJsonStr(response));
                    }
                })
                .postAsync();
    }
    public static void testParamsAsJson(){
        HttpUtil.reqeustStandardJson("http://app.cimc.com:9090/app/appVersion/getLatestVersion",VersionInfo.class)
                .addParams("versionName","1.0.0")
                .addParams("appType","0")
                .setParamsAsJson()
                .setCustomCodeValue(1,2,3)
                .callback(new MyNetListener<VersionInfo>() {


                    @Override
                    public void onSuccess(VersionInfo response, String resonseStr) {
                        MyLog.e(resonseStr);
                    }

                    @Override
                    public void onEmpty() {
                        super.onEmpty();
                    }

                    @Override
                    public void onError(String msgCanShow) {
                        super.onError(msgCanShow);
                        MyLog.e(msgCanShow);
                    }
                })
                .postAsync();
    }
    public static void testDownload(){
        String url = "https://travel.12306.cn/imgs/resources/uploadfiles/images/fed7d5b4-37d3-4f32-bacc-e9b942cb721d_product_W572_H370.jpg";
        String url2 = "http://www.qxinli.com/download/qxinli.apk";
        HttpUtil.download(url2)
                .setIgnoreCer()
                .callback(new MyNetListener() {
                    @Override
                    public void onSuccess(Object response, String onSuccess) {
                        MyLog.e("onSuccess:"+onSuccess);
                    }

                    @Override
                    public void onProgressChange(long fileSize, long downloadedSize) {
                        super.onProgressChange(fileSize, downloadedSize);
                        //MyLog.e("progress:"+downloadedSize+"--filesize:"+fileSize);
                    }

                    @Override
                    public void onError(String msgCanShow) {
                        super.onError(msgCanShow);
                        MyLog.e(msgCanShow);
                    }
                })
                .getAsync();
    }
    public static void testUpload(){
        HttpUtil.upload("http://localhost:8008/spring3/test/upload.action",
                "picFile","C:\\Users\\Administrator\\Pictures\\wall\\new-zealand-lake-wakatipu-new-zealand-mountain-lake-nature5555.jpg")
                .addFile("picFile2","F:\\迅雷下载\\[阳光电影www.ygdy8.com].追凶者也.HD.720p.国语中字.rmvb")
                .addParams("uploadFile555","1474363536041.jpg")
                .addParams("api_secret777","898767hjk")
                .callback(new MyNetListener<String>() {
                    @Override
                    public void onSuccess(String response, String resonseStr) {
                        MyLog.e(resonseStr);
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        MyLog.e("error:"+error);
                    }

                    @Override
                    public void onProgressChange(long downloadedSize, long fileSize) {
                       // super.onProgressChange(fileSize, downloadedSize);
                        MyLog.e("upload onProgressChange:"+downloadedSize + "  total:"+ fileSize +"  progress:"+downloadedSize*100/fileSize);
                    }
                }).postAsync();
    }
}

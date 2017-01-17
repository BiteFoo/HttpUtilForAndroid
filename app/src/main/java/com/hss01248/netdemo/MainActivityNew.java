package com.hss01248.netdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.utils.EncodeUtils;
import com.blankj.utilcode.utils.EncryptUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.interfaces.ILoginManager;
import com.hss01248.net.wrapper.MyJson;
import com.hss01248.net.wrapper.MyNetApi;
import com.hss01248.net.wrapper.MyNetApi2;
import com.hss01248.net.wrapper.MyNetListener;
import com.hss01248.netdemo.bean.AskPhoneInfo;
import com.hss01248.netdemo.bean.GetCommonJsonBean;
import com.hss01248.netdemo.bean.GetStandardJsonBean;
import com.hss01248.netdemo.bean.PostCommonJsonBean;
import com.hss01248.netdemo.bean.PostStandardJsonArray;
import com.hss01248.netdemo.bean.VersionInfo;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivityNew extends Activity {


    @Bind(R.id.get_string)
    Button getString;
    @Bind(R.id.post_string)
    Button postString;
    @Bind(R.id.get_json)
    Button getJson;
    @Bind(R.id.post_json)
    Button postJson;
    @Bind(R.id.get_standard_json)
    Button getStandardJson;
    @Bind(R.id.post_standard_json)
    Button postStandardJson;
    @Bind(R.id.download)
    Button download;
    @Bind(R.id.upload)
    Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
        Logger.init("netapi");
        //MyNetApi2.initAddHttps(R.raw.srca);//添加12306的证书
        MyNetApi2.init(getApplicationContext(),"http://www.qxinli.com:9001/api/", new ILoginManager() {
            @Override
            public boolean isLogin() {
                return false;
            }

            @Override
            public <T> ConfigInfo<T> autoLogin() {
                return null;
            }

            @Override
            public <T> ConfigInfo<T> autoLogin(MyNetListener<T> listener) {
                return null;
            }
        });

        MyNetApi2.initAppDefault("session_id","data","code","msg",0,5,2);










    }

    @OnClick({R.id.get_string, R.id.post_string, R.id.get_json, R.id.post_json, R.id.get_standard_json,
            R.id.post_standard_json, R.id.download, R.id.upload,R.id.postbyjson,R.id.testvoice,R.id.testvoice2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_string:

                MyNetApi2.buildStringRequest("https://kyfw.12306.cn/otn/regist/init")
                        .callback(new MyNetListener<String>() {
                                    @Override
                                    public void onSuccess(String response, String resonseStr) {
                                        Logger.e(response);

                                    }

                                    @Override
                                    public void onError(String error) {
                                        super.onError(error);
                                        Logger.e(error);
                                    }
                            })
                        .setIgnoreCer().get();
                break;
            case R.id.post_string:

                MyNetApi2.buildStringRequest("article/getArticleCommentList/v1.json")
                        .addParams("pageSize","30")
                        .addParams("articleId","1738")
                        .addParams("pageIndex","1")
                        .callback(new MyNetListener<String>() {
                            @Override
                            public void onSuccess(String response, String resonseStr) {
                                Logger.e(response);
                            }
                        }).post();
                break;
            case R.id.get_json:


                MyNetApi2.buildJsonRequest("version/latestVersion/v1.json",GetCommonJsonBean.class)
                        .showLoadingDialog(MainActivityNew.this,"加载中...")
                        .callback(new MyNetListener<GetCommonJsonBean>() {
                            @Override
                            public void onSuccess(GetCommonJsonBean response, String resonseStr) {
                                Logger.json(MyJson.toJsonStr(response));
                            }
                        })
                        .get();
                break;
            case R.id.post_json:

                MyNetApi2.buildJsonRequest("article/getArticleCommentList/v1.json",PostCommonJsonBean.class)
                        .addParams("pageSize","30")
                        .addParams("articleId","1738")
                        .addParams("pageIndex","1")
                        .callback(new MyNetListener<PostCommonJsonBean>() {
                            @Override
                            public void onSuccess(PostCommonJsonBean response, String resonseStr) {
                                Logger.json(MyJson.toJsonStr(response));
                            }
                        }).post();

                break;
            case R.id.get_standard_json:

                /*	聚合api:笑话大全
                    sort	string	是	类型，desc:指定时间之前发布的，asc:指定时间之后发布的
                    page	int	否	当前页数,默认1
                    pagesize	int	否	每次返回条数,默认1,最大20
                    time	string	是	时间戳（10位），如：1418816972
                    key 	string  您申请的key*/
                Map<String,String> map4 = new HashMap<>();
                map4.put("sort","desc");
                map4.put("page","1");
                map4.put("pagesize","4");
                map4.put("time",System.currentTimeMillis()/1000+"");
                map4.put("key","fuck you");


                MyNetApi2.buildStandardJsonRequest("http://japi.juhe.cn/joke/content/list.from",GetStandardJsonBean.class)
                        .addParams("sort","desc")
                        .addParams("page","1")
                        .addParams("pagesize","4")
                        .addParams("time",System.currentTimeMillis()/1000+"")
                        .addParams("key","fuck you")
                        .setStandardJsonKey("result","error_code","reason")
                        .setCustomCodeValue(0,2,-1)
                        .showLoadingDialog(MainActivityNew.this,"加载中...")
                        .callback(new MyNetListener<GetStandardJsonBean>() {
                            @Override
                            public void onSuccess(GetStandardJsonBean response, String resonseStr) {
                                Logger.json(MyJson.toJsonStr(response));
                            }
                            @Override
                            public void onError(String error) {
                                super.onError(error);
                                Logger.e(error);
                            }
                        })
                        .get();
                break;
            case R.id.post_standard_json:

                MyNetApi2.buildStandardJsonRequest("article/getArticleCommentList/v1.json",PostStandardJsonArray.class)
                        .addParams("pageSize","30")
                        .addParams("articleId","1738")
                        .addParams("pageIndex","1")
                        .setResponseJsonArray()
                        .callback(new MyNetListener<PostStandardJsonArray>() {
                            @Override
                            public void onSuccess(PostStandardJsonArray response, String resonseStr) {
                                //Logger.json(MyJson.toJsonStr(response));
                            }

                            @Override
                            public void onSuccessArr(List<PostStandardJsonArray> response, String responseStr, String data, int code, String msg) {
                                super.onSuccessArr(response, responseStr, data, code, msg);
                                Logger.json(MyJson.toJsonStr(response));
                            }
                        })
                        .post();
                break;
            case R.id.download:
                /*File dir = Environment.getExternalStorageDirectory();
                final File file = new File(dir,"2.jpg");
                if (file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
                String url = "https://travel.12306.cn/imgs/resources/uploadfiles/images/fed7d5b4-37d3-4f32-bacc-e9b942cb721d_product_W572_H370.jpg";
                String url2 = "http://www.qxinli.com/download/qxinli.apk";
                MyNetApi2.buildDownloadRequest(url2)
                        .showLoadingDialog(MainActivityNew.this,"下载中...")
                        .setIgnoreCer()
                        .setOpenAfterSuccess(true)
                        .callback(new MyNetListener() {
                            @Override
                            public void onSuccess(Object response, String onSuccess) {
                                Logger.e("onSuccess:"+onSuccess);
                            }

                            @Override
                            public void onProgressChange(long fileSize, long downloadedSize) {
                                super.onProgressChange(fileSize, downloadedSize);
                                Logger.e("progress:"+downloadedSize+"--filesize:"+fileSize);
                            }

                            @Override
                            public void onError(String msgCanShow) {
                                super.onError(msgCanShow);
                                Logger.e(msgCanShow);
                            }
                        })
                        .get();
                break;
            case R.id.upload:


                MyNetApi2.buildUpLoadRequest("http://192.168.1.100:8080/gm/file/q_uploadAndroidApk.do","uploadFile","/storage/emulated/0/qxinli.apk")
                        .addFile("uploadFile","/storage/emulated/0/Download/retrofit/qxinli.apk")
                        .addParams("uploadFile555","1474363536041.jpg")
                        .addParams("api_secret777","898767hjk")
                        .showLoadingDialog(this,"")
                        .callback(new MyNetListener<String>() {
                            @Override
                            public void onSuccess(String response, String resonseStr) {
                                Logger.e(resonseStr);
                            }

                            @Override
                            public void onError(String error) {
                                super.onError(error);
                                Logger.e("error:"+error);
                            }

                            @Override
                            public void onProgressChange(long fileSize, long downloadedSize) {
                                super.onProgressChange(fileSize, downloadedSize);
                                Logger.e("upload onProgressChange:"+downloadedSize + "  total:"+ fileSize +"  progress:"+downloadedSize*100/fileSize);
                            }
                        }).post();
                break;

            case R.id.postbyjson:

                MyNetApi2.buildStandardJsonRequest("http://app.cimc.com:9090/app/appVersion/getLatestVersion",VersionInfo.class)
                        .addParams("versionName","1.0.0")
                        .addParams("appType","0")
                        .setParamsAsJson()
                        .setCustomCodeValue(1,2,3)
                        .callback(new MyNetListener<VersionInfo>() {


                            @Override
                            public void onSuccess(VersionInfo response, String resonseStr) {
                                Logger.e(resonseStr);
                            }

                            @Override
                            public void onEmpty() {
                                super.onEmpty();
                            }

                            @Override
                            public void onError(String msgCanShow) {
                                super.onError(msgCanShow);
                                Logger.e(msgCanShow);
                            }
                        })
                        .post();
                break;
            case R.id.testvoice:
                /*

                SigParameter是REST API 验证参数
                URL后必须带有sig参数，sig= MD5（账户Id + 账户授权令牌 + 时间戳），共32位(注:转成大写)
                使用MD5加密（账户Id + 账户授权令牌 + 时间戳），共32位
                时间戳是当前系统时间（24小时制），格式“yyyyMMddHHmmss”。时间戳有效时间为50分钟。
                Authorization是包头验证信息
                使用Base64编码（账户Id + 冒号 + 时间戳）
                冒号为英文冒号
                时间戳是当前系统时间（24小时制），格式“yyyyMMddHHmmss”，需与SigParameter中时间戳相同


                * https://api.ucpaas.com/{version}/Accounts/{accountSid}/{function}/{operation}?sig={SigParameter}
                *
                * {
                    "hangupUrl": "",
                    "caller": "008618612341234",
                    "appId": "7df01234567841ed816564bb12345678",
                    "callerRingName": "1124",
                    "bindId": “7df0123456ipoopujk7841ed816564bb12345678”,
                    "requestId": "sel-define follow your heart and rules",
                    "calleeDisplayNum": "00861054890813",
                    “maxAge”: “600”,
                    “record”: “0”,
                    “maxAllowTime”: “10”,
                    “dstVirtualNum”: “00861054890813”,
                    “calleeRingName”: “1142”,
                    “statusUrl”: “”,
                    “callee”: “008613611133396”,
                    “recordUrl”: “”
                    “cityId”:”008610”
                    }
                * */

                String timeStamp = TimeUtils.getCurTimeString(new SimpleDateFormat("yyyyMMddHHmmss"));
                String appId = "0f40082bc33d4314b3fabb054b3862f0";

                String countId = "b6f1205ad17a54e502d725210e1b852a";
                String token = "02fb287b1552664e97507a778f84b7d6";

                String sig = EncryptUtils.encryptMD5ToString(countId+token+timeStamp);
                String author = EncodeUtils.base64Encode2String((countId+":"+timeStamp).getBytes());

                String caller = "13698455645";
                String callee = "18138232137";

                Map map9 = new HashMap<>();
                map9.put("appId",appId);

                map9.put("caller","0086"+ caller);//必选	主叫号码(必须为11位手机号，号码前加0086，如008613631686024)
                map9.put("callee","0086"+ callee);
                map9.put("dstVirtualNum","008610"+"54890831");//必选	分配的直呼虚拟中间保护号码 规则为0086+区号首位0+号码

                map9.put("bindId",appId+"9874");//必选	绑定id，客户方平台保证唯一
                map9.put("maxAge","600");//必选	主被叫+虚拟保护号码允许合作方最大cache存储时间(单位秒)


                map9.put("cityId","008610");//必选	城市id

               /* map9.put("requestId","");
                map9.put("calleeDisplayNum","00861054890813");

                map9.put("record","");
                map9.put("maxAllowTime","10");
                map9.put("hangupUrl","");
               map9.put("calleeRingName","1142");
                map9.put("callerRingName","1124");
                map9.put("statusUrl","");

                map9.put("recordUrl","");*/



                MyNetApi.postCommonJson("https://api.ucpaas.com/2014-06-30/Accounts/"+countId+"/safetyCalls/applyNumber?sig={"+sig+"}",
                        map9, AskPhoneInfo.class, new MyNetListener<AskPhoneInfo>() {


                            @Override
                            public void onSuccess(AskPhoneInfo response, String resonseStr) {
                                Logger.e(resonseStr);
                                ToastUtils.showShortToast(MainActivityNew.this,"可以打电话了....");
                            }

                            @Override
                            public void onEmpty() {
                                super.onEmpty();
                            }

                            @Override
                            public void onError(String msgCanShow) {
                                super.onError(msgCanShow);
                                Logger.e(msgCanShow);
                            }
                        })
                        .setParamsAsJson()
                        .addHeaderOfAuthorization(author)
                        .setIsAppendToken(false)
                        .start();
                break;
            case R.id.testvoice2:{


                 /*
                $sign = get_sign($appkey, $params, $secret, $time);
                签名算法：使用MD5加密 MD5（appkey + interfaces +cti + act+ params+appSecret+time） 注”+”不包含.

                http://api.mixcom.cn/v2/?m=interfaces&c=virt&a=index&act=bindnumber&appkey=d6906c470a7886edaa99802cb87fd465&sign=e4842570f261ff1571ae541371f1e809&time=1480327411
                   &virtualnumber=86170****0673&aparty=86153****2774&bparty=86183****9530&recording=0&endDate=2016-01-01 00:00:00*/

                String smallNum = "8617092580665";
                String aparty = "8615989369965";
                String bparty = "8617722810218";
                String appkey = "8b575f9208f4181d974b72a71ca3ad24";
                String appSecret = "ebvbBE";
                String timeStamp2 = System.currentTimeMillis()/1000+"";
                Logger.e("time:"+timeStamp2);

                String act = "bindnumber";
                String c = "virt";
                String m = "interfaces";

                String params = "";
                params=smallNum + aparty + bparty;

                String str = appkey + m +c + act + params + appSecret + timeStamp2;
                Logger.e("str:"+str);
                String sign = EncryptUtils.encryptMD5ToString(str).toLowerCase();
                Logger.e("签名后的:"+sign);

                Map map11 = new HashMap<>();
                map11.put("m",m);
                map11.put("c",c);
                map11.put("a","index");
                map11.put("act",act);
                map11.put("appkey",appkey);
                map11.put("sign",sign);
                map11.put("time",timeStamp2);

                map11.put("virtualnumber",smallNum);
                map11.put("aparty",aparty);
                map11.put("bparty",bparty);

                MyNetApi.getStandardJson("http://api.mixcom.cn/v2/",
                        map11, VersionInfo.class, new MyNetListener<VersionInfo>() {
                            @Override
                            public void onSuccess(VersionInfo response, String resonseStr) {
                                Logger.e(resonseStr);
                            }

                            @Override
                            public void onEmpty() {
                                super.onEmpty();
                            }

                            @Override
                            public void onError(String msgCanShow) {
                                super.onError(msgCanShow);
                                Logger.e(msgCanShow);
                            }
                        })
                        .setIsAppendToken(false)
                        .setCustomCodeValue(200,-1,-1)
                        .start();
            }


                break;


        }
    }

    String app_key = "4d3d1f40e7a841316084b64c0c4575b1";
    String app_secert = "VQ8bciAjkUl4fiTTvafdvTLnBNGlSS";

  /*  @OnClick({R.id.button, R.id.button2, R.id.button3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
               *//* RetrofitAdapter.getInstance().getString("api/msg/unreadMsg/count/v1.json", new HashMap(), "dd", new MyNetCallback() {
                    @Override
                    public void onSuccess(Object response, String resonseStr) {
                        Log.e("baidu",response.toString());
                    }
                });*//*

                String obj1 = "{\"data\":null,\"code\":0}";
                String obj2 = "{\"data\":{},\"code\":0}";
                String obj3 = "{\"data\":[],\"code\":0}";

                BaseNetBean<TestBean> netBean1 = MyJson.parseObject(obj1,BaseNetBean.class);
                BaseNetBean<TestBean> netBean2 = MyJson.parseObject(obj2,BaseNetBean.class);
                BaseNetBean<List<TestBean>> netBean3 = MyJson.parseObject(obj3,BaseNetBean.class);

                break;
            case R.id.button2:
                Map<String, String> map = new HashMap<String, String>();
               // map.put("id", "145");
                RetrofitAdapter.getInstance().postStandardJson("api/voice/categoryList/v1.json",
                        map, "kk", new MyNetListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response, String resonseStr) {
                        Log.e("postStandardJson","onSuccess");
                    }

                    @Override
                    public void onSuccess(JSONObject response, String responseStr, String data, int code, String msg) {
                       // super.onSuccess(response, responseStr, data, code, msg);
                        Log.e("postStandardJson","onSuccess long "+ "code:"+code + "--msg:"+ msg + "--data:"+data);
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        Log.e("postStandardJson","onError:"+error);
                    }
                });
                break;
            case R.id.button3:
                File file = new File(Environment.getExternalStorageDirectory(),"qxinli.apk");
                if (!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String path = file.getAbsolutePath();
                RetrofitAdapter.getInstance().download("http://www.qxinli.com/download/qxinli.apk", path, new MyNetListener<String>() {
                    @Override
                    public void onSuccess(String response, String resonseStr) {
                        Log.e("download","onSuccess:"+ response);
                    }

                    @Override
                    public void onProgressChange(long fileSize, long downloadedSize) {
                        super.onProgressChange(fileSize, downloadedSize);
                        Log.e("download","onProgressChange:"+downloadedSize+ "--totalsize:"+ fileSize);
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        Log.e("download","error:"+error);
                    }
                });
                break;
        }
    }*/
}

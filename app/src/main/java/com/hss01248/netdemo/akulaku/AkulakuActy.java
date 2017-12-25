package com.hss01248.netdemo.akulaku;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.hss01248.net.config.GlobalConfig;
import com.hss01248.net.wrapper.HttpUtil;
import com.hss01248.net.wrapper.MyJson;
import com.hss01248.net.wrapper.MyLog;
import com.hss01248.net.wrapper.MyNetListener;
import com.hss01248.netdemo.R;
import com.hss01248.netdemo.akulaku.bean.CommonConfig;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangshuisheng on 2017/9/29.
 */

public class AkulakuActy extends Activity {

    @BindView(R.id.get_string)
    Button getString;
    @BindView(R.id.post_string)
    Button postString;
    @BindView(R.id.get_json)
    Button getJson;
    @BindView(R.id.post_json)
    Button postJson;
    @BindView(R.id.get_standard_json)
    Button getStandardJson;
    @BindView(R.id.post_standard_json)
    Button postStandardJson;
    @BindView(R.id.download)
    Button download;
    @BindView(R.id.upload)
    Button upload;
    @BindView(R.id.postbyjson)
    Button postbyjson;
    @BindView(R.id.testvoice)
    Button testvoice;
    @BindView(R.id.testvoice2)
    Button testvoice2;
    @BindView(R.id.unbind)
    Button unbind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akulaku);
        ButterKnife.bind(this);

        GlobalConfig.get().setBaseUrl("http://test.app.akulaku.com/api/");
    }

    @OnClick({R.id.get_string, R.id.post_string, R.id.get_json, R.id.post_json, R.id.get_standard_json,
        R.id.post_standard_json, R.id.download, R.id.upload, R.id.postbyjson, R.id.testvoice, R.id.testvoice2, R.id.unbind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_string:{
                getByRetrofit();
            }
                break;
            case R.id.post_string:
                break;
            case R.id.get_json:
                break;
            case R.id.post_json:
                break;
            case R.id.get_standard_json:
                HttpUtil.buildJsonRequest("common/config.json", CommonConfig.class)
                    .setIsCustomJsonType()
                    .addHeader("dt","4")
                    .addHeader("vc","112")
                    .addParam("countryId",2+"")
                    .showLoadingDialog(this,"正在获取个人信息")
                    .getAsync(new MyNetListener<CommonConfig>() {
                        @Override
                        public void onSuccess(CommonConfig response, String responseStr, boolean isFromCache) {
                            Logger.json(MyJson.toJsonStr(response));
                            Logger.e("systime:"+configInfo.responseExtra1);
                        }

                        @Override
                        public void onError(String code, String serverMsg, String exceptionMsg) {
                            super.onError(code, serverMsg, exceptionMsg);
                        }
                    });
                break;
            case R.id.post_standard_json:
                HttpUtil.buildJsonRequest("json/user/address/add.do",String.class)
                    .addParam("password","password")
                    .addParam("name","gfdgfd")
                    .addParam("countryId","66")
                    .addParam("phoneNumber","65765765")
                    .addParam("postcode","67657")
                    .addParam("province","676")
                    .addParam("city","67")
                    .addParam("street","78")
                    .addParam("roomNumber","6")
                    .postAsync(new AkulakuCallBack<String>() {
                        @Override
                        public void onError(String code, String serverMsg, String exceptionMsg) {
                            Logger.e("code:"+code+"--servermsg:"+serverMsg+"---exceptionmsg:"+exceptionMsg);
                        }

                        @Override
                        public void onSuccess(String response, String responseStr, boolean isFromCache) {
                            MyLog.json(response);

                        }

                        /*@Override
                        public void onError(String code, String serverMsg, String exceptionMsg) {
                            super.onError(code, serverMsg, exceptionMsg);
                            Logger.e("code:"+code+"--servermsg:"+serverMsg+"---exceptionmsg:"+exceptionMsg);
                        }*/

                        /*@Override
                        public void onError(String msgCanShow) {
                            super.onError(msgCanShow);
                            Logger.e(msgCanShow);
                        }*/
                    });
                    //.addParam("roomNumber","6")

                break;
            case R.id.download:
                break;
            case R.id.upload:
                break;
            case R.id.postbyjson:
                break;
            case R.id.testvoice:
                break;
            case R.id.testvoice2:
                break;
            case R.id.unbind:
                break;
            default:break;
        }
    }

    private void getByRetrofit() {

        /*try {
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.qxinli.com:9005/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

            ApiRxService rxService = retrofit.create(ApiRxService.class);
            rxService.executGet("version/latestVersion/v1.json",new HashMap<String, String>(),new HashMap<String, String>())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<BaseNetBean>() {
                    @Override
                    public void accept(BaseNetBean baseNetBean) throws Exception {
                        MyLog.e("doOnNext  accept");
                        MyLog.json(baseNetBean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseNetBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        MyLog.e("onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull BaseNetBean baseNetBean) {
                        MyLog.e("onnext");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        MyLog.e("onError");
                    }

                    @Override
                    public void onComplete() {
                        MyLog.e("onComplete");
                    }
                });
        }catch (Exception e){
            e.printStackTrace();
        }*/


    }
}

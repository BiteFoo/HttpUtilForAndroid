package com.hss01248.http.config;

/**
 * Created by hss on 2017/12/28.
 */

public class DownloadConfig {

    //下載文件的保存路徑
    public String filePath;
    //是否打開,是否讓媒体库扫描,是否隐藏文件夹
    public boolean isOpenAfterSuccess ;//默认不扫描
    public boolean isHideFolder ;
    public boolean isNotifyMediaCenter ;//媒体文件下载后,默认:通知mediacenter扫描


    //文件校验相关设置(默认不校验)
    public boolean isVerify ;//是否校驗文件
    public String verifyStr;
    public boolean verfyByMd5OrShar1 ;
}

package com.hss01248.net.akulaku;

/**
 * Created by huangshuisheng on 2017/9/28.
 */

public class AkulakuRootBean {


    /**
     * data : {"activity":{"id":429,"type":8,"name":"Seckill","countryId":4,"areaId":1,"bannerImg":"https://s3-ap-northeast-1.amazonaws.com/com-silvrr-installment/activity/banner/ClalkYgee_iMwLpCc1TJMj7u21SmdANAgEjke9OGuK0.jpg","bannerImg2":"https://s3-ap-northeast-1.amazonaws.com/com-silvrr-installment/activity/banner/mH_oGBiP0BuQ2akvQy28vOv7EzvmVdCCv-JA285RYV8.jpg","link":"","beginTime":1506495720000,"endTime":1506508500000,"priceBeginTime":1506495720000,"priceEndTime":1506508500000},"list":[]}
     * success : true
     * errCode :
     * errMsg :
     * sysTime : 1506594461539
     */

    private String data;
    private boolean success;
    private String errCode;
    private String errMsg;
    private long sysTime;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public long getSysTime() {
        return sysTime;
    }

    public void setSysTime(long sysTime) {
        this.sysTime = sysTime;
    }
}
